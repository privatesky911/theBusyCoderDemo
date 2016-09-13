package com.dyhdm.huxj.headerdetaillist;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class HeaderDetailList extends ListActivity {
    private static final String TAG = "HeaderDetailList";
    private static final String[][] items = {
            {"lorem", "ipsum", "dolor", "sit", "amet"},
            {"consectetuer", "adipiscing", "elit", "morbi", "vel"},
            {"ligula", "vitae", "arcu", "aliquet", "mollis"},
            {"etiam", "vel", "erat", "placerat", "ante"},
            {"porttitor", "sodales", "pellentesque", "augue", "purus"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_header_detail_list_demo);
        setListAdapter(new IconicAdapter());
    }

    private class IconicAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            int count = 0;
            for (String[] batch : items){
                count += batch.length + 1;
                Log.d(TAG, "IconicAdapter, count=" + count);
            }
            return count;
        }

        @Override
        public Object getItem(int position) {
            int offset=position;
            int batchIndex=0;

            for (String[] batch : items) {
                if (offset == 0) {
                    return(Integer.valueOf(batchIndex));
                }

                offset--;

                if (offset < batch.length) {
                    return(batch[offset]);
                }

                offset-=batch.length;
                batchIndex++;
            }

            throw new IllegalArgumentException("Invalid position: "
                    + String.valueOf(position));

        }
        @Override
        public long getItemId(int position) {
            return(position);
        }

        @Override
        public int getViewTypeCount() {
            return(2);
        }

        @Override
        public int getItemViewType(int position) {
            if (getItem(position) instanceof Integer) {
                return(0);
            }

            return(1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (getItemViewType(position) == 0) {
                return (getHeaderView(position, convertView, viewGroup));
            }

            View row = convertView;
            if (row == null) {
                row = getLayoutInflater().inflate(R.layout.row, viewGroup, false);
            }

            ViewHolder holder=(ViewHolder)row.getTag();

            if (holder == null) {
                holder=new ViewHolder(row);
                row.setTag(holder);
            }

            String word=(String)getItem(position);

            if (word.length() > 4) {
                holder.icon.setImageResource(R.drawable.delete);
            }
            else {
                holder.icon.setImageResource(R.drawable.ok);
            }

            holder.label.setText(word);
            holder.size.setText(String.format(getString(R.string.size_template),
                    word.length()));

            return(row);
        }

        private View getHeaderView(int i, View view, ViewGroup viewGroup) {
            View row = view;

            if (row == null) {
                row = getLayoutInflater().inflate(R.layout.header, viewGroup, false);
            }

            Integer batchIndex = (Integer)getItem(i);
            TextView label = (TextView) row.findViewById(R.id.label);
            label.setText(String.format(getString(R.string.batch), 1+batchIndex.intValue()));

            return row;
        }
    }
}
