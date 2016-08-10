package com.hxj.empublite;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewFragment;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleContentFragment extends WebViewFragment {
    private static final String KEY_FILE = "file";

    static SimpleContentFragment newInstance(String file) {
        SimpleContentFragment f = new SimpleContentFragment();
        Bundle args = new Bundle();
        args.putString(KEY_FILE, file);
        f.setArguments(args);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result = super.onCreateView(inflater, container, savedInstanceState);
        getWebView().getSettings().setJavaScriptEnabled(true);
        getWebView().getSettings().setBuiltInZoomControls(true);
        getWebView().getSettings().setSupportZoom(true);

        getWebView().loadUrl(getPage());
        
        return result;
    }

    private String getPage() {
        return getArguments().getString(KEY_FILE);
    }

}
