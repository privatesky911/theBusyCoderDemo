package com.dyhdm.huxj.volley;


import android.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import java.util.List;
import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionsFragment extends ListFragment implements
        Response.Listener<SOQuestions>, Response.ErrorListener {

    private static final String TAG = "QuestionsFragment";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GsonRequest request = new GsonRequest<SOQuestions>(getString(R.string.url), SOQuestions.class, null, this, this);
        VolleyManager.get(getActivity()).enqueue(request);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Item item = ((ItemsAdapter) getListAdapter()).getItem(position);
        EventBus.getDefault().post(new QuestionClickedEvent(item));
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getActivity(), error.getMessage(),
                Toast.LENGTH_LONG).show();
        Log.d(TAG, "onResponse");
    }

    @Override
    public void onResponse(SOQuestions response) {
        Log.d(TAG, "onResponse");
        setListAdapter(new ItemsAdapter(response.items));
    }

    class ItemsAdapter extends ArrayAdapter<Item> {
        ItemsAdapter(List<Item> items) {
            super(getActivity(), R.layout.row, R.id.title, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);
            Item item = getItem(position);
            ImageView icon = (ImageView) row.findViewById(R.id.icon);
            TextView answer = (TextView) row.findViewById(R.id.answer);
            TextView reviewer = (TextView) row.findViewById(R.id.reviewer);
            TextView creation_date = (TextView) row.findViewById(R.id.create);
            VolleyManager
                    .get(getActivity())
                    .loadImage(item.owner.profileImage, icon,
                            R.drawable.owner_placeholder,
                            R.drawable.owner_error);
            TextView title = (TextView) row.findViewById(R.id.title);
            title.setText(Html.fromHtml(getItem(position).title));
            answer.setText(Html.fromHtml(getItem(position).answer_count));
            reviewer.setText(Html.fromHtml(getItem(position).view_count));

            String date = "-";
            if (getItem(position).creation_date != null) {
                Log.d(TAG, "posix data: "+getItem(position).creation_date);
                long posix_time = (long)Integer.parseInt(getItem(position).creation_date);
                date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(posix_time*1000));
            }
            creation_date.setText(date);

            return (row);
        }
    }
}
