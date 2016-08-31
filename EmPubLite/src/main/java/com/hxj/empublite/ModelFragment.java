package com.hxj.empublite;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Process;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.renderscript.ScriptGroup;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import de.greenrobot.event.EventBus;

/**
 * Created by huxj-win7 on 2016/8/11.
 */
public class ModelFragment extends Fragment {
    private static final String TAG = "ModelFragment";
    private BookContents contents = null;
    private SharedPreferences prefs = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setRetainInstance(true);
    }
    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (contents == null) {
            new LoadThread(context.getAssets()).start();
        }
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().register(this);
        //Log.d(TAG, "onAttach (Activity), contents: " + ((contents == null) ? "null" : (contents.toString())));
        if (contents == null) {
            new LoadThread(activity).start();
        }
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @SuppressWarnings("unused")
    public void onEventBackgroundThread(BookUpdatedEvent event) {
        if (getActivity()!=null) {
            new LoadThread(getActivity()).start();
        }
    }

    synchronized public BookContents getBook() {
        //Log.d(TAG, "getBook(): " + ((contents == null) ? "null" : (contents.toString())));
        return contents;
    }

    private class LoadThread extends Thread{
        private Context ctxt  =null;
        LoadThread(Context context) {
            super();
            this.ctxt = context.getApplicationContext();
        }
        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            synchronized(this) {
                prefs=PreferenceManager.getDefaultSharedPreferences(ctxt);
            }
            Gson gson=new Gson();
            File baseDir=
                    new File(ctxt.getFilesDir(),
                            DownloadCheckService.UPDATE_BASEDIR);
            try {
                InputStream is;
                Log.d(TAG, "LoadThread, run baseDir: " + baseDir);
                if (baseDir.exists()) {
                    is=new FileInputStream(new File(baseDir, "contents.json"));
                }
                else {
                    is=ctxt.getAssets().open("book/contents.json");
                }
                BufferedReader reader=
                        new BufferedReader(new InputStreamReader(is));
                synchronized(this) {
                    contents=gson.fromJson(reader, BookContents.class);
                }
                is.close();
                if (baseDir.exists()) {
                    contents.setBaseDir(baseDir);
                }
                EventBus.getDefault().post(new BookLoadedEvent(contents));
            }
            catch (IOException e) {
                Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
            }
        }

    }

    public SharedPreferences getPrefs(){
        return prefs;
    }
}
