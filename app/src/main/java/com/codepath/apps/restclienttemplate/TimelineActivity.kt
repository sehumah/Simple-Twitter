package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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

    private lateinit var swipeContainer: SwipeRefreshLayout

    private val tweets = ArrayList<Tweet>() // hold list of tweets to hold all the tweets we get from the API call

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        client = TwitterApplication.getRestClient(this)  // initialize client variable

        swipeContainer = findViewById(R.id.swipeContainer)  // get reference to swipe container view

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener {
            populateHomeTimeline()
            Log.i(TAG, "Refresh listener detected a refresh! Refreshing timeline!")
        }

        // Configure refreshing colors for SwipeRefreshLayout
        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);

        rvTweets = findViewById(R.id.rv_tweets)  // get the RecyclerView
        adapter = TweetsAdapter(tweets)  // initialize adapter
        rvTweets.layoutManager = LinearLayoutManager(this)  // give RecyclerView a LayoutManager
        rvTweets.adapter = adapter  // set the adapter for the RecyclerView
        populateHomeTimeline()

    }

    private fun populateHomeTimeline () {
        client.getHomeTimeline(object : JsonHttpResponseHandler () {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                val jsonArray = json.jsonArray  // get json array from response

                try {
                    adapter.clear()  // CLEAR OUT old items before appending the new ones
                    val listOfTweetsRetrieved = Tweet.fromJSONArray(jsonArray)  // parse jsonArray and make list of tweets
                    tweets.addAll(listOfTweetsRetrieved)  // add it to our list of original tweets
                    adapter.notifyDataSetChanged()  // notify the adapter that things have changed
                    // Now we call setRefreshing(false) to signal refresh has finished. And to stop showing the refreshing icon
                    swipeContainer.setRefreshing(false)
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
