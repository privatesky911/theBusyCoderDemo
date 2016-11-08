package com.ostar.deviceinfo;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView mNetMAC;
    private ImageView mWifiMAC;
    private ImageView mBtMAC;
    private ImageView mSN;

    private TextView mNetMACText;
    private TextView mWifiMACText;
    private TextView mBtMACText;
    private TextView mSNText;
    private TextView mVersionInfo;

    private Button mExitButton;

    private String mNetMacInfo = "ssssssssssssssssssssss";
    private String mWifiMacInfo = "ssssssssssssssssssssss";
    private String mBtMacInfo = "ssssssssssssssssssssss";
    private String mSNInfo = "ssssssssssssssssssssss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        GetInfo();
        SetInfo();
    }

    private void SetInfo() {
        try {
            if (mNetMacInfo != null && !"".equals(mNetMacInfo)) {
                mNetMAC.setImageBitmap(CreateOneDCode(mNetMacInfo));
                mNetMACText.setText("Eth Mac: " + mNetMacInfo);
            }
            if (mWifiMacInfo != null && !"".equals(mWifiMacInfo)) {
                mWifiMAC.setImageBitmap(CreateOneDCode(mWifiMacInfo));
                mWifiMACText.setText("Wifi Mac: " + mWifiMacInfo);
            }
            if (mBtMacInfo != null && !"".equals(mBtMacInfo)) {
                mBtMAC.setImageBitmap(CreateOneDCode(mBtMacInfo));
                mBtMACText.setText("BT Address: " + mBtMacInfo);
            }
            if (mSNInfo != null && !"".equals(mSNInfo)) {
                mSN.setImageBitmap(CreateOneDCode(mSNInfo));
                mSNText.setText("SN: " + mSNInfo);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }

        mVersionInfo.setText(getAppVersion());
    }

    private void GetInfo() {
        mNetMacInfo = getEthMac().replace(':', '-');
        mWifiMacInfo = getWifiMac().replace(':', '-');
        mBtMacInfo = getBtMac().replace(':', '-');
        mSNInfo = getSNInfo();
    }

    private void initView() {
        mNetMAC = (ImageView) findViewById(R.id.ic_net_mac);
        mWifiMAC = (ImageView) findViewById(R.id.ic_wifi_mac);
        mBtMAC = (ImageView) findViewById(R.id.ic_bt_mac);
        mSN = (ImageView) findViewById(R.id.ic_sn);

        mNetMACText = (TextView) findViewById(R.id.text_net_mac);
        mWifiMACText = (TextView) findViewById(R.id.text_wifi_mac);
        mBtMACText = (TextView) findViewById(R.id.text_bt_mac);
        mSNText = (TextView) findViewById(R.id.text_sn);
        mVersionInfo = (TextView) findViewById(R.id.version_info);

        mExitButton = (Button) findViewById(R.id.bt_exit);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    protected Bitmap CreateOneDCode(String str) throws WriterException {
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 250, 250);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return bitmap;
    }

    public String getEthMac() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("sys/class/net/eth0/address"));
            return reader.readLine();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
            }
        }
    }

    private String getWifiMac() {
        tryOpenWifi();
        for (int i = 0; i < 10; i++) {
            WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            WifiInfo info = wifi.getConnectionInfo();
            if (info.getMacAddress() != null && !"".equals(info.getMacAddress())) {
                return info.getMacAddress();
            }
        }
        return null;
    }

    private void tryOpenWifi() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        int state = wifi.getWifiState();
        if (state != WifiManager.WIFI_STATE_ENABLED && state != WifiManager.WIFI_STATE_ENABLING) {
            wifi.setWifiEnabled(true);
        }
    }

    private String getBtMac() {
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (!bluetooth.isEnabled()) bluetooth.enable();
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (bluetooth != null) {
                String address = bluetooth.isEnabled() ? bluetooth.getAddress() : null;
                if (!TextUtils.isEmpty(address)) {
                    // Convert the address to lowercase for consistency with the wifi MAC address.
                    return address.toLowerCase();
                }
            }
        }
        return null;
    }

    private String getSNInfo() {
        String procSerialNumberStr = "";
        try {

            String sCurrentLine;
            BufferedReader br = null;
            br = new BufferedReader(new FileReader("/sys/sn/sn_num"));

            while ((sCurrentLine = br.readLine()) != null) {
                if (procSerialNumberStr.contains("xxxx") || procSerialNumberStr.contains("XXXX")) {
                    procSerialNumberStr = "";
                } else {
                    procSerialNumberStr = sCurrentLine;
                }
                break;
            }
            br.close();
            return procSerialNumberStr;

        } catch (IOException e) {
            return "";
        }
    }

    private String getAppVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return this.getString(R.string.version_name) + version;
        } catch (Exception e) {
            return this.getString(R.string.unknow_version_name);
        }
    }
}
