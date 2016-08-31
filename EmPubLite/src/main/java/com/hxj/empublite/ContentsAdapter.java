package com.hxj.empublite;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by huxj-win7 on 2016/8/10.
 */
public class ContentsAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "ContentsAdapter";
    private BookContents contents = null;

    public ContentsAdapter(Activity ctxt, BookContents contents) {
        super(ctxt.getFragmentManager());
        this.contents = contents;
    }

    @Override
    public Fragment getItem(int position) {
        //String path = contents.getChapterFile(position);
        //Log.d(TAG, "position: "+ position + ", path: " + path);
        //return SimpleContentFragment.newInstance("file:///android_asset/book/"+path);
        return(SimpleContentFragment.newInstance(contents.getChapterPath(position)));
    }

    @Override
    public int getCount() {
        //Log.d(TAG, "getCount()");
        return contents.getChapterCount();
    }
}
