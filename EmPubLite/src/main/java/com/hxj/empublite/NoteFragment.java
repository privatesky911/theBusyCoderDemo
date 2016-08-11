package com.hxj.empublite;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View result = inflater.inflate(R.layout.editor, container, false);
        editor = (EditText) result.findViewById(R.id.editor);
        return result;
    }

    private int getPosition(){
        return getArguments().getInt(KEY_POSITION, -1);
    }

}
