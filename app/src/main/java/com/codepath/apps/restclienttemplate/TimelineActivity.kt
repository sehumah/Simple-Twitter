package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.github.scribejava.apis.TwitterApi
import okhttp3.Headers
import org.json.JSONException


private const val TAG = "TimelineActivity"
class TimelineActivity : AppCompatActivity() {

    private lateinit var client: TwitterClient  // declare client variable

    private lateinit var rvTweets: RecyclerView
    private lateinit var adapter: TweetsAdapter
    private val tweets = ArrayList<Tweet>() // hold list of tweets to hold all the tweets we get from the API call

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        client = TwitterApplication.getRestClient(this)  // initialize client variable

        rvTweets = findViewById(R.id.rv_tweets)  // get the RecyclerView
        adapter = TweetsAdapter(tweets)  // initialize adapter
        rvTweets.layoutManager = LinearLayoutManager(this)  // give RecyclerView a LayoutManager
        rvTweets.adapter = adapter  // set the adapter for the RecyclerView

        populateHomeTimeline()
    }

    private fun populateHomeTimeline () {
        client.getHomeTimeline(object : JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                val jsonArray = json.jsonArray  // get json array from response
                try {
                    val listOfTweetsRetrieved = Tweet.fromJSONArray(jsonArray)  // parse jsonArray and make list of tweets
                    tweets.addAll(listOfTweetsRetrieved)  // add it to our list of original tweets
                    adapter.notifyDataSetChanged()  // notify the adapter that things have changed
                } catch (e: JSONException) {
                    Log.e(TAG, "JSON Exception: $e")
                }

                Log.i(TAG, "Success populating home timeline. JSON object: $json")
            }

            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.i(TAG, "Failed to populate home timeline. Status code: $statusCode")
            }
        })
    }
}

// NOTE: we can now see tweets based on the people we're following
