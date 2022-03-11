package com.codepath.apps.restclienttemplate.models

import org.json.JSONArray
import org.json.JSONObject

class Tweet {
    var tweetId: String = ""
    var tweetText: String = ""
    var createdAt: String = ""
    var user: User? = null  // user who created the tweet

    companion object {
        private fun parseJSON (jsonObject: JSONObject) : Tweet {
            val tweet = Tweet()
            tweet.tweetId = jsonObject.getString("id_str")
            tweet.createdAt = jsonObject.getString("created_at")
            tweet.tweetText = jsonObject.getString("text")
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
}
