package com.dyhdm.huxj.autoreboot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by huxj-win7 on 2016/8/23.
 */
public class OnBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(getClass().getSimpleName(), "Hi, Mom!");

            reboot();
        }
    }

    public static final String CMD_REBOOT = "reboot";
    public static final String CMD_REBOOT_RECOVERY = "reboot recovery";
    public static final String CMD_RECOVERY_PARA = "echo \"--wipe_data\" > /cache/recovery/command";

    private final String strEnter = "\n";
    private final String cmd_su = "su";
    private final String cmd_exit = "exit";

    public void reboot() {
        Properties mProp = ConfigUtil.loadConfig();
        try {
            Thread.sleep(Integer.valueOf(mProp.getProperty("RebootTime")) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mProp = ConfigUtil.loadConfig();
        if (Boolean.valueOf(mProp.getProperty("StartEnable"))) {
            Log.d(getClass().getSimpleName(), "do reboot...");

            String mCounter = mProp.getProperty("Counter");
            Integer counterNum = Integer.valueOf(mCounter);
            counterNum++;
            mProp.setProperty("Counter", String.valueOf(counterNum));
            ConfigUtil.saveConfig(mProp);

            Process localProcess = null;
            try {
                localProcess = Runtime.getRuntime().exec(cmd_su);
                DataOutputStream localDataOutputStream = new DataOutputStream(
                        localProcess.getOutputStream());

                if (Boolean.valueOf(mProp.getProperty("RecoveryEnable"))) {
                    localDataOutputStream.writeBytes(CMD_RECOVERY_PARA + strEnter);
                    localDataOutputStream.writeBytes(CMD_REBOOT_RECOVERY + strEnter);
                }else {
                    localDataOutputStream.writeBytes(CMD_REBOOT + strEnter);
                }

                localDataOutputStream.writeBytes(cmd_exit + strEnter);

                localDataOutputStream.flush();
                localDataOutputStream.close();
                try {
                    localProcess.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                localProcess.destroy();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
