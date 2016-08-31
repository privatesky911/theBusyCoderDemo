package com.hxj.empublite;

import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.List;

/**
 * Created by huxj-win7 on 2016/8/11.
 */
public class BookContents {
    private static final String TAG = "BookContents";
    List<BookContents.Chapter> chapters;
    File baseDir=null;
    void setBaseDir(File baseDir) {
        this.baseDir=baseDir;
    }

    public int getChapterCount()
    {
        return chapters.size();
    }

    String getChapterPath(int position) {
        String file=getChapterFile(position);
        if (baseDir == null) {
            return("file:///android_asset/book/" + file);
        }
        return(Uri.fromFile(new File(baseDir, file)).toString());
    }

    public String getChapterFile(int position) {
        return chapters.get(position).file;
    }

    static class Chapter {
        String file;
    }
}
