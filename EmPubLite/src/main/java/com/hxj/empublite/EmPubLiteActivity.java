package com.hxj.empublite;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import de.greenrobot.event.EventBus;

public class EmPubLiteActivity extends Activity {
    private static final String MODEL = "model";
    private static final String TAG = "EmPubLiteActivity";

    private ViewPager viewPager = null;
    private ContentsAdapter adapter = null;
    private ModelFragment mfrag = null;

    private static final String PREF_LAST_POSITION="lastPosition";
    private static final String PREF_SAVE_LAST_POSITION="saveLastPosition";
    private static final String PREF_KEEP_SCREEN_ON="keepScreenOn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        Log.d(TAG, "onResume");
        Log.d(TAG, "adapter:" + ((adapter == null) ? "null" : (adapter.toString())));
        if (adapter == null) {
            mfrag = (ModelFragment) getFragmentManager().findFragmentByTag(MODEL);
            Log.d(TAG, "mfrag:" + ((mfrag == null) ? "null" : (mfrag.toString())));
            if (mfrag == null) {
                mfrag = new ModelFragment();
                getFragmentManager().beginTransaction().add(mfrag, MODEL).commit();
            } else if (mfrag.getBook() != null) {
                setupPager(mfrag.getBook());
            }
        }
        if (mfrag.getPrefs() != null){
            viewPager.setKeepScreenOn(mfrag.getPrefs().getBoolean(PREF_KEEP_SCREEN_ON, false));
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        EventBus.getDefault().unregister(this);
        if (mfrag.getPrefs() != null) {
            int pos = viewPager.getCurrentItem();
            mfrag.getPrefs().edit().putInt(PREF_LAST_POSITION, pos).apply();
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.notes:
                Intent intentNotes = new Intent(this, NoteActivity.class);
                intentNotes.putExtra(NoteActivity.EXTRA_POSITION, viewPager.getCurrentItem());
                startActivity(intentNotes);
                return(true);
            case R.id.help:
                Intent intentHelp = new Intent(EmPubLiteActivity.this, SimpleContentActivity.class);
                intentHelp.putExtra(SimpleContentActivity.EXTRA_FILE, "file:///android_asset/misc/help.html");
                startActivity(intentHelp);
                return true;
            case R.id.about:
                Intent intentAbout = new Intent(EmPubLiteActivity.this, SimpleContentActivity.class);
                intentAbout.putExtra(SimpleContentActivity.EXTRA_FILE, "file:///android_asset/misc/about.html");
                startActivity(intentAbout);
                return true;
            case R.id.settings:
                Intent intentSettings = new Intent(EmPubLiteActivity.this, Preferences.class);
                startActivity(intentSettings);
                return true;
            case R.id.update:
                Log.d(TAG, "download button clicked");
                startService(new Intent(this, DownloadCheckService.class));
                return true;
        }

        return (super.onOptionsItemSelected(item));
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(BookLoadedEvent event) {
        Log.d(TAG, "onEventMainThread");
        setupPager(event.getBook());
    }

    public void setupPager(BookContents contents) {
        Log.d(TAG, "setupPager");
        adapter = new ContentsAdapter(this, contents);
        viewPager.setAdapter(adapter);
        findViewById(R.id.progressBar1).setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);

        SharedPreferences prefs = mfrag.getPrefs();
        if (prefs != null) {
            if (prefs.getBoolean(PREF_SAVE_LAST_POSITION, false)) {
                viewPager.setCurrentItem(prefs.getInt(PREF_LAST_POSITION, 0));
            }
            viewPager.setKeepScreenOn(prefs.getBoolean(PREF_KEEP_SCREEN_ON, false));
        }
    }

}
