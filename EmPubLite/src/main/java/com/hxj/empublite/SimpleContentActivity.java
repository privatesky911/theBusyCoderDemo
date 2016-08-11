package com.hxj.empublite;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

public class SimpleContentActivity extends Activity {
    public static final String EXTRA_FILE = "file";
    private static final String TAG = "SimpleContentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            String file = getIntent().getStringExtra(EXTRA_FILE);
            Fragment f = SimpleContentFragment.newInstance(file);
            getFragmentManager().beginTransaction().add(android.R.id.content, f).commit();
        }
    }
}
