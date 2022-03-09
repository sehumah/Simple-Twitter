package com.codepath.apps.restclienttemplate.models

import org.json.JSONArray
import org.json.JSONObject

class Tweet {

    var tweetText: String = ""
    var createdAt: String = ""
    var user: User? = null  // user who created the tweet

    companion object {
        fun parseJSON (jsonObject: JSONObject) : Tweet {
            val tweet = Tweet()
            tweet.createdAt = jsonObject.getString("created_at")
            tweet.tweetText = jsonObject.getString("text")
            tweet.user = User.parseJSON(jsonObject.getJSONObject("user"))
            return tweet
        }

        /* convert list of JSON objects into list of tweets */
        fun fromJSONArray (jsonArray: JSONArray) : List<Tweet> {
            val tweets = ArrayList<Tweet>()
            for (i in 0 until jsonArray.length()) {
                tweets.add(parseJSON(jsonArray.getJSONObject(i)))
            }
            return tweets
        }
    }
}
