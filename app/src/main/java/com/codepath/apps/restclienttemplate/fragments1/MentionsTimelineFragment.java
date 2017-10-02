package com.codepath.apps.restclienttemplate.fragments1;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codepath.apps.restclienttemplate.Application.TwitterApp;

/**
 * Created by GANESH on 10/3/17.
 */

public class MentionsTimelineFragment extends TweetsListFragment {
   // private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();
        populateMentionsTimeline(0);
    }

}
