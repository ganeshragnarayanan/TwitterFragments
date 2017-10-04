package com.codepath.apps.restclienttemplate.fragments;


/**
 * Created by GANESH on 9/20/17.
 */

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.Activities.TimelineActivity;
import com.codepath.apps.restclienttemplate.Application.TwitterApp;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.net.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

// ...

public class EditNameDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener /*implements OnEditorActionListener */ {


    String dateSelected;
    private TwitterClient client;

    public interface TweetSelectedListener {
        public void onTweetSelected(Tweet tweet);
    }

    TextView mEditText;
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //This sets a textview to the current length
            mEditText.setText(Integer.toString(140 - start));
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
        getDialog().setTitle("           Compose Tweet");

        return inflater.inflate(R.layout.fragment_edit_name, container);
    }

    @Override

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String userName = getArguments().getString("name");
        String screenName = getArguments().getString("screen_name");
        String imageURL = getArguments().getString("profile_url");

        final EditText etComposeTweet = (EditText) view.findViewById(R.id.etComposeTweet);

        final ImageView ivImage = (ImageView) view.findViewById(R.id.ivImage);
        final TextView tvName = (TextView) view.findViewById(R.id.tvName);

        Glide.with(this).load(imageURL).into(ivImage);
        tvName.setText(userName);

        mEditText = (TextView) view.findViewById(R.id.mTextView);

        etComposeTweet.addTextChangedListener(mTextEditorWatcher);

        Button btnTweet = (Button) view.findViewById(R.id.btnTweet);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweet = etComposeTweet.getText().toString();

                Context context = getActivity();
                postTweet(tweet, context);
                dismiss();
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void postTweet(final String tweet, final Context context) {
        client = TwitterApp.getRestClient();
        client.postTweet(tweet, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("debug", response.toString());

                try {
                    Tweet tweetObj = Tweet.fromJSON(response);
                    ((TimelineActivity) context).getResult(tweet, tweetObj);
                    //((TweetSelectedListener) getActivity()).onTweetSelected(tweetObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("debug", response.toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("debug", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("debug", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("debug", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }
}
