package com.codepath.apps.restclienttemplate;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    private EndlessRecyclerViewScrollListener scrollListener;

    long maxTweetID = 0;
    private SwipeRefreshLayout swipeContainer;
    Handler handler;
    Runnable runnableCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViews();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("debug", "refresh");
                tweets.clear();
                tweetAdapter.notifyDataSetChanged();
                scrollListener.resetState();

                populateTimeline(0);
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
               // fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        final SearchView searchView = (SearchView) searchItem.getActionView();

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    public void makeDelayedTweetRequests() {
        handler = new Handler();
        runnableCode = new Runnable() {
            @Override
            public void run() {
                // runs below code on main thread
                onPopulateTimeline();
            }
        };
        handler.postDelayed(runnableCode, 2000);
    }

    public void setupViews() {
        client = TwitterApp.getRestClient();

        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(tweets);
        //rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d("debug", "onLoadMore");
                loadNextDataFromApi(page);
            }
        };
        rvTweets.addOnScrollListener(scrollListener);
        //onPopulateTimeline();
        makeDelayedTweetRequests();

    }

    public void loadNextDataFromApi(int offset) {
        populateTimeline(maxTweetID);
    }

    /* callback for the filters dialog */
    public void getResult(String tweet) {

        postTweet(tweet);
        //onPopulateTimeline();
        makeDelayedTweetRequests();


    }

    public void onPopulateTimeline() {

        tweets.clear();
        tweetAdapter.notifyDataSetChanged();
        scrollListener.resetState();

        populateTimeline(0);
    }

    private void populateTimeline(long max_id) {
        RequestParams params = new RequestParams();
        params.put("count", 25);
        //params.put("since_id", 1);

        if (max_id != 0) {
            params.put("max_id", max_id);
        }
        client.getHomeTimeline(params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("debug", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("debug", response.toString());
                long minTweetID = 0;
                for (int i=0;i<response.length();i++) {
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));

                        if (i==0) {
                            minTweetID = tweet.uid;
                        }
                        if (tweet.uid < minTweetID) {
                            minTweetID = tweet.uid;
                        }
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size()-1);
                        maxTweetID = minTweetID;

                        Log.d("debug", "maxTweetID");
                        Log.d("debug", Long.toString(maxTweetID));
                        swipeContainer.setRefreshing(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
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

    private void postTweet(String tweet) {
        Log.d("debug", "post_tweet");
        client.postTweet(tweet, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("debug", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("debug", response.toString());
                /*for (int i=0;i<response.length();i++) {
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size()-1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }*/
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("debug", responseString);
                throwable.printStackTrace();
                generateToast();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("debug", errorResponse.toString());
                throwable.printStackTrace();
                generateToast();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("debug", errorResponse.toString());
                throwable.printStackTrace();
                generateToast();
            }

            public void generateToast() {
                Context context = getApplicationContext();
                CharSequence text = "Error loading page" +
                        "!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    public void onComposeAction(MenuItem mi) {
        showEditDialog();
    }

    /* invoke the filters dialog */
    private void showEditDialog() {

        FragmentManager fm = getSupportFragmentManager();
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance("Filters");

        Bundle args = new Bundle();
        args.putString("date", "test");


        editNameDialogFragment.setArguments(args);
        editNameDialogFragment.show(fm, "fragment_edit_name");

    }
}
