package com.codepath.apps.restclienttemplate.fragments1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.Application.TwitterApp;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.net.TwitterClient;
import com.codepath.apps.restclienttemplate.utils.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

//import static com.codepath.apps.restclienttemplate.R.id.swipeContainer;

/**
 * Created by GANESH on 10/3/17.
 */

public class TweetsListFragment extends Fragment implements TweetAdapter.TweetAdapterListener {
    public interface TweetSelectedListener {
        public void onTweetSelected(Tweet tweet);
    }


    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    TwitterClient client;
    long maxTweetID;
    ViewPager vpPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("debug", "onCreateView");
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);

        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweet);
        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(tweets, this);

        rvTweets.setAdapter(tweetAdapter);

        linearLayoutManager = new LinearLayoutManager(getContext());
        rvTweets.setLayoutManager(linearLayoutManager);

       /* vpPager = (ViewPager) v.findViewById(R.id.viewpager);
        int i = vpPager.getCurrentItem();*/

        client = TwitterApp.getRestClient();

       /* RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvTweets.addItemDecoration(itemDecoration);*/


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d("debug", "onLoadMore");
                loadNextDataFromApi(page);
            }
        };
        rvTweets.addOnScrollListener(scrollListener);
        return v;
    }

    public void addItems(JSONArray response) {
        Log.d("debug", response.toString());
        long minTweetID = 0;
        for (int i = 0; i < response.length(); i++) {
            try {
                Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));

                if (i == 0) {
                    minTweetID = tweet.uid;
                }
                if (tweet.uid < minTweetID) {
                    minTweetID = tweet.uid;
                }
                tweets.add(tweet);
                tweetAdapter.notifyItemInserted(tweets.size() - 1);
                maxTweetID = minTweetID;

                //Log.d("debug", "maxTweetID");
                //Log.d("debug", Long.toString(maxTweetID));
                //swipeContainer.setRefreshing(false);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onItemSelected(View view, int position) {
        Tweet tweet = tweets.get(position);
        ((TweetSelectedListener) getActivity()).onTweetSelected(tweet);
        Toast.makeText(getContext(), tweet.body, Toast.LENGTH_SHORT);
    }

    public void loadNextDataFromApi(int offset) {

        /*int i = vpPager.getCurrentItem();
        Log.d("debug", "currentpage");
        Log.d("debug", Integer.toString(i));*/

       // Fragment f = getActivity().getFragmentManager().findFragmentById(R.id.fragment_container);


        populateTimeline(maxTweetID-1);

    }

    public void insertTweet(Tweet tweetObj) {
        tweets.add(0, tweetObj);
        tweetAdapter.notifyDataSetChanged();
        linearLayoutManager.scrollToPositionWithOffset(0, 0);

    }
    public void populateTimeline(long max_id) {
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", 1);
        Log.d("debug", "populateTimeLine Home");
        //client = TwitterApp.getRestClient();
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
                Log.d("debug", "hometimeline success 2");
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

    public void populateMentionsTimeline(long max_id) {
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", 1);
        Log.d("debug", "populateTimeLine Mentions");

        if (max_id != 0) {
            params.put("max_id", max_id);
        }
        client.getMentionsTimeline(params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("debug", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("debug", "mentionstimeline success 2");
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

    public void populateUsersTimeline(long max_id) {
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
