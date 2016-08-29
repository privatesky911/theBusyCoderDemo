package com.dyhdm.huxj.autoreboot;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    private TextView mCounterText;
    private CheckBox mStartCheckBox;
    private Spinner mRebootTimeSpinner;
    private CheckBox mRecoveryCheckBox;
    private Button mCleanCounterBtn;
    private Button mSaveConfigBtn;

    private String mCounterNum = "0";
    private boolean mStartEnable = false;
    private int mRebootTimeNum = 10;// s
    private boolean mRecoveryEnable = false;

    private Properties mProp;

    private final String strEnter = "\n";
    private final String cmd_su = "su";
    private final String cmd_exit = "exit";
    public static final String CMD_REBOOT = "reboot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        initConfig();
        initView();
    }

    private void initConfig() {
        mProp = ConfigUtil.loadConfig();

        mCounterNum = mProp.getProperty("Counter");
        mStartEnable = Boolean.valueOf(mProp.getProperty("StartEnable"));
        mRebootTimeNum = Integer.valueOf(mProp.getProperty("RebootTime"));
        mRecoveryEnable = Boolean.valueOf(mProp.getProperty("RecoveryEnable"));

    }

    private void initView() {
        mCounterText = (TextView) findViewById(R.id.text_counter);
        mStartCheckBox = (CheckBox) findViewById(R.id.checkbox_start);
        mRebootTimeSpinner = (Spinner) findViewById(R.id.reboot_time_list);
        mRecoveryCheckBox = (CheckBox) findViewById(R.id.checkbox_recovery);
        mCleanCounterBtn = (Button) findViewById(R.id.bt_clean_counter);
        mSaveConfigBtn = (Button) findViewById(R.id.bt_save_config);

        mStartCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mProp.setProperty("StartEnable", String.valueOf(b));
            }
        });

        List<String> mList = new ArrayList<String>();
        mList.add("10s");
        mList.add("20s");
        mList.add("30s");
        mList.add("40s");
        mList.add("50s");
        mList.add("60s");
        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mList);
        mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRebootTimeSpinner.setAdapter(mArrayAdapter);
        mRebootTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int time = (i + 1) * 10;
                mProp.setProperty("RebootTime", String.valueOf(time));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mRecoveryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mProp.setProperty("RecoveryEnable", String.valueOf(b));
            }
        });

        mCleanCounterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProp.setProperty("Counter", "0");
                mProp.setProperty("StartEnable", String.valueOf(false));
                mCounterText.setText("0");
                if (ConfigUtil.saveConfig(mProp)) {
                    Toast.makeText(mContext, "清除成功！！！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "清除失败！！！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSaveConfigBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConfigUtil.saveConfig(mProp)) {
                    Toast.makeText(mContext, "保存成功！！！", Toast.LENGTH_SHORT).show();

                    if (mStartCheckBox.isChecked()){
                        rebootNow();
                    }
                } else {
                    Toast.makeText(mContext, "保存失败！！！", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mCounterText.setText(mCounterNum);
        mStartCheckBox.setChecked(mStartEnable);
        mRebootTimeSpinner.setSelection(mRebootTimeNum/10-1);
        mRecoveryCheckBox.setChecked(mRecoveryEnable);
    }

    private void rebootNow() {
        Process localProcess = null;
        try {
            localProcess = Runtime.getRuntime().exec(cmd_su);
            DataOutputStream localDataOutputStream = new DataOutputStream(
                    localProcess.getOutputStream());

            localDataOutputStream.writeBytes(CMD_REBOOT + strEnter);
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
