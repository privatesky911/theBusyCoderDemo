package com.hxj.empublite;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ShareActionProvider;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment implements TextWatcher{

    private ShareActionProvider share = null;
    private Intent shareIntent =
            new Intent(Intent.ACTION_SEND).setType("text/plain");

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        shareIntent.putExtra(Intent.EXTRA_TEXT,s.toString());
    }

    public interface Contract {
        void closeNotes();
    }

    private static final String KEY_POSITION = "position";
    private EditText editor = null;

    static NoteFragment newInstance(int pos) {
        NoteFragment frag = new NoteFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, pos);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notes, menu);
        share = (ShareActionProvider) menu.findItem(R.id.share).getActionProvider();
        share.setShareIntent(shareIntent);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.delete_hint)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                editor.setText(null);
                                getContract().closeNotes();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.setIcon(R.mipmap.ic_warning);
                builder.setTitle(R.string.info);
                AlertDialog alert = builder.create();
                alert.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.editor, container, false);
        editor = (EditText) result.findViewById(R.id.editor);
        editor.addTextChangedListener(this);
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

        if (TextUtils.isEmpty(editor.getText())) {
            DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
            db.loadNote(getPosition());
        }
    }

    @Override
    public void onPause() {
        DatabaseHelper.getInstance(getActivity()).updateNote(getPosition(), editor.getText().toString());
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    private int getPosition() {
        return getArguments().getInt(KEY_POSITION, -1);
    }

    public void onEventMainThread(NoteLoadedEvent event) {
        if (event.getPosition() == getPosition()) {
            editor.setText(event.getProse());
        }
    }

    private Contract getContract() {
        return (Contract)getActivity();
    }
}
