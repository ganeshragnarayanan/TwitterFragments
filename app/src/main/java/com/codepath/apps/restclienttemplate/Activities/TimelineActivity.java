package com.codepath.apps.restclienttemplate.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.fragments.EditNameDialogFragment;
import com.codepath.apps.restclienttemplate.fragments1.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments1.MentionsTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments1.SmartFragmentStatePagerAdapter;
import com.codepath.apps.restclienttemplate.fragments1.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments1.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener {


    long maxTweetID = 0;
    private SwipeRefreshLayout swipeContainer;
    Handler handler;
    Runnable runnableCode;
    String userName;
    String screenName;
    String imageURL;
    long userUID;
    ViewPager vpPager;

    TweetsListFragment fragmentTweetsList;

    private SmartFragmentStatePagerAdapter adapterViewPager;


    // Extend from SmartFragmentStatePagerAdapter now instead for more dynamic ViewPager items
    public static class MyPagerAdapter extends SmartFragmentStatePagerAdapter {
        private static int NUM_ITEMS = 2;
        private String tabTitles[] = new String[]{"Home", "Mentions"};

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    //return FirstFragment.newInstance(0, "Page # 1");
                    return new HomeTimelineFragment();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    //return FirstFragment.newInstance(1, "Page # 2");
                    return new MentionsTimelineFragment();
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.rgb(81, 155, 211));
        setSupportActionBar(toolbar);

        setupViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        return true;
    }

    public void onProfileView(MenuItem menu) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("uid", 0);
        i.putExtra("screen_name", "");
        startActivity(i);
    }

    public void makeDelayedTweetRequests() {
        handler = new Handler();
        runnableCode = new Runnable() {
            @Override
            public void run() {
                onPopulateTimeline();
            }
        };
        handler.postDelayed(runnableCode, 2000);
    }

    public void setupViews() {

        vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(), this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

    }

    public void loadNextDataFromApi(int offset) {
        /*populateTimeline(maxTweetID-1);*/
    }

    /* callback for the filters dialog */
    public void getResult(String tweetString, Tweet tweetObj) {

        Log.d("debug", "getResult");
        HomeTimelineFragment fragment = (HomeTimelineFragment) adapterViewPager.getRegisteredFragment(0);
        fragment.fromMainActivity(tweetObj);
        vpPager.setCurrentItem(0);
    }

    public void onPopulateTimeline() {

    }

    public void onComposeAction(MenuItem mi) {
        showEditDialog();
    }

    /* invoke the filters dialog */
    private void showEditDialog() {

        FragmentManager fm = getSupportFragmentManager();
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance("Filters");

        Bundle args = new Bundle();

        args.putString("name", userName);
        args.putString("screen_name", screenName);
        args.putString("profile_url", imageURL);
        editNameDialogFragment.setArguments(args);

        editNameDialogFragment.show(fm, "fragment_edit_name");

    }

    @Override
    public void onTweetSelected(Tweet tweet) {
        Toast.makeText(this, tweet.body, Toast.LENGTH_SHORT);
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("uid", tweet.user.uid);
        i.putExtra("screenName", tweet.user.screenName);
        i.putExtra("followers", tweet.user.followersCount);
        i.putExtra("following", tweet.user.followingCount);
        i.putExtra("description", tweet.user.tagLine);
        i.putExtra("name", tweet.user.name);
        i.putExtra("profileURL", tweet.user.profileImageUrl);

        startActivity(i);
    }
}
