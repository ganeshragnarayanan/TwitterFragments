package com.codepath.apps.restclienttemplate.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.Application.TwitterApp;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.fragments1.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments1.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.net.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity  implements TweetsListFragment.TweetSelectedListener{

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String screenName = getIntent().getStringExtra("screen_name");
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, userTimelineFragment);
        ft.commit();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        toolbar.setBackgroundColor(Color.rgb(81, 155,211));
        setSupportActionBar(toolbar);

        //toolbar.setTitle("hola");
        //toolbar.setTitle();

        Intent intent = getIntent();
        long uid = intent.getLongExtra("uid", 0);
        String screen_name = intent.getStringExtra("screenName");

        String profileImageUrl;
        String tagLine = intent.getStringExtra("description");
        String name = intent.getStringExtra("name");
        String profileURL = intent.getStringExtra("profileURL");
        int followersCount = intent.getIntExtra("followers", 0);
        int followingCount = intent.getIntExtra("following", 0);

        getSupportActionBar().setTitle(screen_name);

        Bundle args = new Bundle();

        args.putLong("uid", uid);
        args.putString("screen_name", screen_name);


        userTimelineFragment.setArguments(args);


        if (uid == 0) {
            client = TwitterApp.getRestClient();
            client.getUserInfo(null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        User user = User.fromJSON(response);
                        //getSupportActionBar().setTitle(user.screenName);
                        //getSupportActionBar().setTitle("screen_name");
                        populateUserHeadline(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } else {
            TextView tvName = (TextView) findViewById(R.id.tvName);
            TextView tvTagLine = (TextView) findViewById(R.id.tvTagLine);
            TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
            TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

            ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
            tvName.setText(name);
            tvTagLine.setText(tagLine);

            tvFollowers.setText(followersCount + " Followers");
            tvFollowing.setText(followingCount + " Following" );

            //Glide.with(this).load(profileURL).into(ivProfileImage);
        }
    }

    public void populateUserHeadline(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagLine = (TextView) findViewById(R.id.tvTagLine);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(user.name);
        tvTagLine.setText(user.tagLine);

        tvFollowers.setText(user.followersCount + " Followers");
        tvFollowing.setText(user.followingCount + " Following" );

        Glide.with(this).load(user.profileImageUrl).into(ivProfileImage);
    }



    @Override
    public void onTweetSelected(Tweet tweet) {
        Toast.makeText(this, tweet.body, Toast.LENGTH_SHORT);
    }
}
