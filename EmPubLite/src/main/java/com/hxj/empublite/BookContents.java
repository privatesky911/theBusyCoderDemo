package com.hxj.empublite;

import android.util.Log;

import java.util.List;

/**
 * Created by huxj-win7 on 2016/8/11.
 */
public class BookContents {
    private static final String TAG = "BookContents";
    List<BookContents.Chapter> chapters;

    public int getChapterCount()
    {
        return chapters.size();
    }

    public String getChapterFile(int position) {
        return chapters.get(position).file;
    }

    static class Chapter {
        String file;
    }
}
