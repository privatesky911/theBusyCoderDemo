package com.dyhdm.huxj.volley;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import de.greenrobot.event.EventBus;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        if (getFragmentManager().findFragmentById(android.R.id.content) == null){
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new QuestionsFragment())
                    .commit();
        }
    }

  @Override
  public void onResume() {
    super.onResume();
    EventBus.getDefault().register(this);
  }

  @Override
  public void onPause() {
    EventBus.getDefault().unregister(this);
    super.onPause();
  }

    public void onEventMainThread(QuestionClickedEvent event) {
        Log.d(TAG, "onEventMainThread");
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(event.item.link)));
    }
}
