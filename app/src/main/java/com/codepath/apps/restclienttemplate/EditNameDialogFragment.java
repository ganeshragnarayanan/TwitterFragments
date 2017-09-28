package com.codepath.apps.restclienttemplate;


/**
 * Created by GANESH on 9/20/17.
 */

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

// ...

public class EditNameDialogFragment extends DialogFragment  implements DatePickerDialog.OnDateSetListener /*implements OnEditorActionListener */ {


    String dateSelected;

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
    private EditText mEditText;
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
        /*final EditText etDate = (EditText) view.findViewById(R.id.etDate);


        String filterDateValue = getArguments().getString("date");
        String filterSortValue = getArguments().getString("sort");
        Boolean filterArtsValue = getArguments().getBoolean("arts");
        Boolean filterFashionValue = getArguments().getBoolean("fashion");
        Boolean filterSportsValue = getArguments().getBoolean("sports");

        etDate.setText(filterDateValue);

        if (filterSortValue.equals("oldest")) {
            filterOldest.setSelection(1);
        } else if (filterSortValue.equals("newest")) {
            filterOldest.setSelection(2);
        }
        else {
            filterOldest.setSelection(0);
        }

        if (filterArtsValue) {
            filterArts.setChecked(true);
        }

        if (filterFashionValue) {
            filterFashion.setChecked(true);
        }

        if (filterSportsValue) {
            filterSports.setChecked(true);
        }

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.requestFocus();

        etDate.setOnClickListener (new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }

        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean arts = false, fashion = false, sports = false;

                String date = etDate.getText().toString();


                ((SearchActivity) getActivity()).getResult(date,
                        filterOldest.getSelectedItem().toString(), arts, fashion, sports);

                dismiss();
            }

            DatePickerDialog.OnDateSetListener ondate = (view1, year, monthOfYear, dayOfMonth) -> {
            };
        });*/
    }
}