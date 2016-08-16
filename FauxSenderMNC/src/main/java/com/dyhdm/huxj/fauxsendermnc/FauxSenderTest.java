package com.dyhdm.huxj.fauxsendermnc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FauxSenderTest extends Activity implements View.OnClickListener {

    private EditText editor;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faux_sender_test);
        editor = (EditText) findViewById(R.id.editor);
        btnSend = (Button) findViewById(R.id.button);
        btnSend.setOnClickListener(this);

    }

    void sendIt(String theMessage) {
        Intent i = new Intent(Intent.ACTION_SEND);

        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, R.string.share_subject);
        i.putExtra(Intent.EXTRA_TEXT, theMessage);
        startActivity(i);
        //startActivity(Intent.createChooser(i, getString(R.string.share_title)));

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            if (TextUtils.isEmpty(editor.getText().toString())) {
                sendIt(getResources().getString(R.string.no_message_supplied));
            } else {
                sendIt(editor.getText().toString());
            }
        }
    }
}
