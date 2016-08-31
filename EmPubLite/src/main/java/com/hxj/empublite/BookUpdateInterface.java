package com.hxj.empublite;

import retrofit.http.GET;

/**
 * Created by huxj-win7 on 2016/8/31.
 */
public interface BookUpdateInterface {
    @GET("/misc/empublite-update.json")
    BookUpdateInfo update();
}
