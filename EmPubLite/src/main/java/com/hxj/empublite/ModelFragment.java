package com.hxj.empublite;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Process;
import android.renderscript.ScriptGroup;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (contents == null) {
            new LoadThread(context.getAssets()).start();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach (Activity), contents: " + ((contents == null) ? "null" : (contents.toString())));
        if (contents == null) {
            new LoadThread(activity.getAssets()).start();
        }
    }

    synchronized public BookContents getBook() {
        Log.d(TAG, "getBook(): " + ((contents == null) ? "null" : (contents.toString())));
        return contents;
    }

    private class LoadThread extends Thread{
        private AssetManager assets = null;
        LoadThread(AssetManager assets) {
            super();
            this.assets = assets;
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            Gson gson = new Gson();
            Log.d("LoadThread", "run ...");
            try {
                InputStream is = assets.open("book/contents.json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                synchronized (this) {
                    contents = gson.fromJson(reader,BookContents.class);
                }
                EventBus.getDefault().post(new BookLoadedEvent(contents));
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), "Exception parsing json", e);
            }
        }
    }
}
