package com.dyhdm.huxj.volley;

/**
 * Created by huxj-win7 on 2016/8/15.
 */
public class Item {
    String title;
    Owner owner;
    String answer_count;
    String view_count;
    //String last_activity_date;
    String creation_date;
    String link;

    @Override
    public String toString() {
        return title;
    }
}
