package com.hxj.empublite;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.commonsware.cwac.security.ZipUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import de.greenrobot.event.EventBus;
import okio.BufferedSink;
import okio.Okio;
import retrofit.RestAdapter;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class DownloadCheckService extends IntentService {

    private static final String TAG = "DownloadCheckService";
    private static final String OUR_BOOK_DATE = "20120418";
    private static final String UPDATE_FILENAME = "WarOfTheWorlds-Update.zip";//"book.zip";
    public static final String UPDATE_BASEDIR = "updates";

    public DownloadCheckService() {
        super("DownloadCheckService");
        Log.d(TAG, "DownloadCheckService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String url = getUpdateUrl();
            Log.d(TAG, "onHandleIntent, url = " + url);

            if (url != null) {
                File book = download(url);
                File updateDir = new File(getFilesDir(), UPDATE_BASEDIR);
                Log.d(TAG, "start mkdirs = " + updateDir.toString());
                updateDir.mkdirs();
                Log.d(TAG, "start unzip");
                ZipUtils.unzip(book, updateDir);
                book.delete();
                Log.d(TAG, "event bus post");
                EventBus.getDefault().post(new BookUpdatedEvent());
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(),
                    "Exception downloading update", e);
        }
    }

    private String getUpdateUrl() {
        Log.d(TAG, "getUpdateUrl");
        RestAdapter restAdapter =
                new RestAdapter.Builder().setEndpoint("http://commonsware.com")
                        .build();
        BookUpdateInterface updateInterface =
                restAdapter.create(BookUpdateInterface.class);
        BookUpdateInfo info = updateInterface.update();
        if (info.updatedOn.compareTo(OUR_BOOK_DATE) > 0) {
            return (info.updateUrl);
        }
        return (null);
    }

    private File download(String url) throws IOException {
        Log.d(TAG, "download, url = "+url);
        Log.d(TAG, "download, getFilesDir = "+getFilesDir());
        File output = new File(getFilesDir(), UPDATE_FILENAME);
        if (output.exists()) {
            output.delete();
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        Log.d(TAG, "download, response = "+response.toString());
        BufferedSink sink = Okio.buffer(Okio.sink(output));
        sink.writeAll(response.body().source());
        sink.close();
        return (output);
    }
}
