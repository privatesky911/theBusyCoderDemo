package com.dyhdm.huxj.printdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.print.*;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import java.io.FileNotFoundException;

import java.util.Date;
import java.util.Locale;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

public class MainActivity extends Activity {
    private static final int IMAGE_REQUEST_ID=1337;
    private EditText prose=null;
    private WebView wv=null;
    private PrintManager mgr=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prose=(EditText)findViewById(R.id.prose);
        mgr=(PrintManager)getSystemService(PRINT_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.bitmap:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST_ID);
                return true;

            case R.id.report:
                printReport();

                return(true);


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_REQUEST_ID && resultCode ==  Activity.RESULT_OK) {
            PrintHelper helper = new PrintHelper(this);
            helper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            try {
                helper.printBitmap("MyPhoto", data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void printReport() {
        Template tmpl=
                Mustache.compiler().compile(getString(R.string.report_body));
        WebView print=prepPrintWebView(getString(R.string.tps_report));

        print.loadData(tmpl.execute(new TpsReportContext(prose.getText()
                        .toString())),
                "text/html", "UTF-8");
    }
    private WebView prepPrintWebView(final String name) {
        WebView result=getWebView();

        result.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                print(name, view.createPrintDocumentAdapter(),
                        new PrintAttributes.Builder().build());
            }
        });

        return(result);
    }
    private WebView getWebView() {
        if (wv == null) {
            wv=new WebView(this);
        }

        return(wv);
    }

    private PrintJob print(String name, PrintDocumentAdapter adapter,
                           PrintAttributes attrs) {
        startService(new Intent(this, PrintJobMonitorService.class));

        return(mgr.print(name, adapter, attrs));
    }


    @TargetApi(Build.VERSION_CODES.N)
    private static class TpsReportContext {
        private static final SimpleDateFormat fmt=
                new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String msg;

        TpsReportContext(String msg) {
            this.msg=msg;
        }

        @SuppressWarnings("unused")
        String getReportDate() {
            return(fmt.format(new Date()));
        }

        @SuppressWarnings("unused")
        String getMessage() {
            return(msg);
        }
    }


}
