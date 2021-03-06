package com.codepath.apps.restclienttemplate.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONObject


@Parcelize
class Tweet (
    var tweetId: String = "",
    var createdAt: String = "",
    var tweetText: String = "",
    var retweetCount: Int = 0,
    var favoritesCount: Int = 0,
    var user: User? = null) : Parcelable {

    companion object {
        fun parseJSON (jsonObject: JSONObject) : Tweet {
            val tweet = Tweet()
            tweet.tweetId = jsonObject.getString("id_str")
            tweet.createdAt = jsonObject.getString("created_at")
            tweet.tweetText = jsonObject.getString("text")
            tweet.retweetCount = jsonObject.getInt("retweet_count")
            tweet.favoritesCount = jsonObject.getInt("favorite_count")
            tweet.user = User.parseJSON(jsonObject.getJSONObject("user"))
            return tweet
        }

        /* Convert list of JSON objects into list of tweets */
        fun fromJSONArray (jsonArray: JSONArray) : List<Tweet> {
            val tweets = ArrayList<Tweet>()
            for (i in 0 until jsonArray.length()) {
                tweets.add(parseJSON(jsonArray.getJSONObject(i)))
            }
            return tweets
        }
    }

    fun getFormattedTimestamp () : String {
        // String formattedTime = TimeFormatter.getTimeDifference(String dateString);  // returns date & time
        return TimeFormatter.getTimeDifference(createdAt)  // only returns time since tweet was tweeted
    }

    fun getTimeStamp () : String {
        return TimeFormatter.getTimeStamp(createdAt)
    }
}
