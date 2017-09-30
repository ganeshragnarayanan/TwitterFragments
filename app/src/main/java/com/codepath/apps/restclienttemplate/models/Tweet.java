package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.text.*;

/**
 * Created by GANESH on 9/27/17.
 */

public class Tweet {

    public String body;
    public  long uid;
    public String createdAt;
    public User user;
    public String dateFormatted;

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");

        String dateFormatted = getRelativeTimeAgo(tweet.createdAt);
        tweet.dateFormatted = dateFormatted;

        Log.d("debug", "dateFormatted");
        Log.d("debug", tweet.dateFormatted);

        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        return tweet;
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("debug", "relativeDate");
        Log.d("debug", relativeDate);

        if (relativeDate.indexOf("in ") == 0) {
            relativeDate = relativeDate.substring(3, relativeDate.length());
        }


        int index = relativeDate.indexOf( ' ' );
        String s1= relativeDate.substring(0, index);
        String s2= relativeDate.substring(index+1, index+2);

        return s1+s2;
    }
}
