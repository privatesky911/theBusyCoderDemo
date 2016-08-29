package com.dyhdm.huxj.autoreboot;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Created by lisong on 2016/8/24.
 */
public class ConfigUtil {

    private static String CONFIG_PATH = "/mnt/sdcard/config.prop";

    public static Properties loadConfig() {
        Properties prop = openConfig();
        if (prop == null) {
            prop = new Properties();
            prop.setProperty("Counter", "0");
            prop.setProperty("StartEnable", String.valueOf(false));
            prop.setProperty("RebootTime", String.valueOf(10));
            prop.setProperty("RecoveryEnable", String.valueOf(false));
            ConfigUtil.saveConfig(prop);
        }
        return prop;
    }
    private static Properties openConfig() {
        Properties prop = new Properties();
        try {
            FileInputStream mFIS = new FileInputStream(CONFIG_PATH);
            prop.load(mFIS);
            mFIS.close();
        } catch (Exception d) {
            ;
            return null;
        }
        return prop;
    }

    public static boolean saveConfig(Properties prop) {
        try {
            FileOutputStream mFOS = new FileOutputStream(CONFIG_PATH);
            prop.store(mFOS, "");
            mFOS.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
