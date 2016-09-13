package com.dyhdm.huxj.headerdetaillist;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by huxj-win7 on 2016/9/13.
 */
class ViewHolder {
    ImageView icon=null;
    TextView label=null;
    TextView size=null;

    ViewHolder(View row) {
        this.icon=(ImageView)row.findViewById(R.id.icon);
        this.label=(TextView)row.findViewById(R.id.label);
        this.size=(TextView)row.findViewById(R.id.size);
    }
}
