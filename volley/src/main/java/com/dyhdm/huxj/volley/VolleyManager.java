package com.dyhdm.huxj.volley;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by huxj-win7 on 2016/8/15.
 */
public class VolleyManager {

    private static volatile VolleyManager INSTANCE = null;
    private RequestQueue queue;
    private ImageLoader imageLoader;

    public VolleyManager(Context ctxt) {
        queue = Volley.newRequestQueue(ctxt);
        imageLoader = new ImageLoader(queue, new LruBitmapCache(ctxt));
    }

    synchronized static VolleyManager get(Context ctxt) {
        if (INSTANCE == null) {
            INSTANCE = new VolleyManager(ctxt.getApplicationContext());
        }
        return INSTANCE;
    }

    void enqueue(Request<?> request) {
        queue.add(request);
    }

    void loadImage(String url, ImageView iv, int placeholderDrawable, int errorDrawable) {
        imageLoader.get(url,
                ImageLoader.getImageListener(iv, placeholderDrawable, errorDrawable));
    }

}
