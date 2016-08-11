package com.hxj.empublite;

import android.util.Log;

/**
 * Created by huxj-win7 on 2016/8/11.
 */
public class BookLoadedEvent {
    private static final String TAG = "BookLoadedEvent";
    private BookContents contents = null;

    public BookLoadedEvent(BookContents contents) {
        this.contents = contents;
        //Log.d(TAG, "BookLoadedEvent[ contents:" + ((contents == null) ? "null" : (contents.toString())) + " ]");
    }

    public BookContents getBook(){
        //Log.d(TAG, "getBook()");
        return contents;
    }
}
