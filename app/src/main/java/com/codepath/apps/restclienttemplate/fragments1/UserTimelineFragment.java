package com.codepath.apps.restclienttemplate.fragments1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.restclienttemplate.Application.TwitterApp;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by GANESH on 10/3/17.
 */

public class UserTimelineFragment extends TweetsListFragment {
    //private TwitterClient client;

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();
        populateTimeline(0);
    }

    public void populateTimeline(long max_id) {
        String screenName = getArguments().getString("screen_name");
        long uid = getArguments().getLong("uid");

        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", 1);

        if (max_id != 0) {
            params.put("max_id", max_id);
        }
        client.getUserTimeline(screenName, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("debug", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addItems(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("debug", responseString);
                throwable.printStackTrace();
                //swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("debug", errorResponse.toString());
                throwable.printStackTrace();
                //swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("debug", errorResponse.toString());
                throwable.printStackTrace();
                //swipeContainer.setRefreshing(false);
            }
        });
    }
}
