package com.hu.xj.internetaccesshurl;


import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionsFragment extends ListFragment {

    static final String TAG = "QuestionsFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setRetainInstance(true);
        new LoadThread().start();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Item item = ((ItemsAdapter) getListAdapter()).getItem(position);
        EventBus.getDefault().post(new QuestionClickedEvent(item));
    }

    public void onEventMainThread(QuestionLoadEvent event) {
        Log.d(TAG, "onEventMainThread");
        setListAdapter(new ItemsAdapter(event.questions.items));
    }

    private class ItemsAdapter extends ArrayAdapter<Item> {
        public ItemsAdapter(List<Item> items) {
            super(getActivity(), android.R.layout.simple_list_item_1, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);
            TextView title = (TextView) row.findViewById(android.R.id.text1);
            title.setText(Html.fromHtml(getItem(position).title));
            return row;
        }
    }
}
