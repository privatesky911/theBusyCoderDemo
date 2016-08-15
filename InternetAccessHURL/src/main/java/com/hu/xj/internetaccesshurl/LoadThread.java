package com.hu.xj.internetaccesshurl;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import de.greenrobot.event.EventBus;

/**
 * Created by xj on 2016/8/14.
 */
public class LoadThread extends Thread {
    static final String SO_URL=
            "https://api.stackexchange.com/2.1/questions?"
                    + "order=desc&sort=creation&site=stackoverflow&tagged=android";

    @Override
    public void run() {
        HttpURLConnection c = null;
        try {
            c = (HttpURLConnection) new URL(SO_URL).openConnection();

            InputStream in = c.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            SOQuestions questions =
                    new Gson().fromJson(reader, SOQuestions.class);

            reader.close();

            EventBus.getDefault().post(new QuestionLoadEvent(questions));

        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
        } finally {
            if (c != null) c.disconnect();
        }
    }
}
