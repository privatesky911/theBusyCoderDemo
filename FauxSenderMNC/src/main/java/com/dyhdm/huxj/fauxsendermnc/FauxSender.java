package com.dyhdm.huxj.fauxsendermnc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

public class FauxSender extends Activity {
    public static final String EXTRA_TARGET_ID = "targetId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String epilogue = "";
        super.onCreate(savedInstanceState);

        int targetId = getIntent().getIntExtra(EXTRA_TARGET_ID, -1);
        if (targetId > 0) {
            epilogue = " for target ID #" + targetId;
        }

        String msg = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        if (TextUtils.isEmpty(msg)) {
            msg = getIntent().getStringExtra(Intent.EXTRA_SUBJECT);
        }
        if (TextUtils.isEmpty(msg)) {
            msg = getString(R.string.no_message_supplied);
        }
        Toast.makeText(this, msg + epilogue, Toast.LENGTH_LONG).show();
        finish();
    }
}
