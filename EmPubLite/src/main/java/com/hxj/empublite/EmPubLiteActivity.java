package com.hxj.empublite;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewParent;
import android.widget.Adapter;

public class EmPubLiteActivity extends Activity {

    private ViewPager viewPager = null;
    private ContentsAdapter adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        adapter = new ContentsAdapter(this);
        findViewById(R.id.progressBar1).setVisibility(View.GONE);
        viewPager.setAdapter(adapter);
        viewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.help:
                Intent intentHelp = new Intent(EmPubLiteActivity.this, SimpleContentActivity.class);
                startActivity(intentHelp);
                return true;
            case R.id.about:
                Intent intentAbout = new Intent(EmPubLiteActivity.this, SimpleContentActivity.class);
                startActivity(intentAbout);
                return true;
        }

        return (super.onOptionsItemSelected(item));
    }
}
