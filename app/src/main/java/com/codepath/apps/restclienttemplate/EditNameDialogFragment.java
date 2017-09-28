package com.codepath.apps.restclienttemplate;


/**
 * Created by GANESH on 9/20/17.
 */

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

// ...

public class EditNameDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener /*implements OnEditorActionListener */ {


    String dateSelected;

    TextView mEditText;
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //This sets a textview to the current length
            mEditText.setText(Integer.toString(140-start));
            Log.d("debug", Integer.toString(start));
            Log.d("debug", Integer.toString(before));
            Log.d("debug", Integer.toString(count));
        }

        public void afterTextChanged(Editable s) {
        }
    };

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }



    public EditNameDialogFragment() {
    }

    public static EditNameDialogFragment newInstance(String title) {
        EditNameDialogFragment frag = new EditNameDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_name, container);
    }

    @Override

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText etComposeTweet = (EditText) view.findViewById(R.id.etComposeTweet);

        mEditText = (TextView) view.findViewById(R.id.mTextView);


        etComposeTweet.addTextChangedListener(mTextEditorWatcher);

        Button btnTweet = (Button) view.findViewById(R.id.btnTweet);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweet = etComposeTweet.getText().toString();
                ((TimelineActivity) getActivity()).getResult(tweet);
                dismiss();
            }
        });
    }


}
