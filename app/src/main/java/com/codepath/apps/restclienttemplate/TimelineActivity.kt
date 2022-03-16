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
import EndlessRecyclerViewScrollListener
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast


private const val TAG = "TimelineActivity"
private const val REQUEST_CODE = 100

class TimelineActivity : AppCompatActivity() {

    private lateinit var client: TwitterClient  // declare client variable

    private lateinit var rvTweets: RecyclerView
    private lateinit var adapter: TweetsAdapter

    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener  // declare variable to listen for scrolls
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var currentMaxId: String

    private val tweets = ArrayList<Tweet>() // hold list of tweets to hold all the tweets we get from the API call

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        client = TwitterApplication.getRestClient(this)  // initialize client variable

        swipeContainer = findViewById(R.id.swipeContainer)  // get reference to swipe container view

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener {
            populateHomeTimeline()
            Log.i(TAG, "Refresh detected...Refreshing timeline!")
        }

        // Configure refreshing colors for SwipeRefreshLayout
        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        );

        rvTweets = findViewById(R.id.rv_tweets)  // get the RecyclerView
        adapter = TweetsAdapter(this, tweets)  // initialize adapter
        rvTweets.layoutManager = LinearLayoutManager(this)  // give RecyclerView a LayoutManager
        rvTweets.adapter = adapter  // set the adapter for the RecyclerView

        /* Below code for infinite pagination feature */
        // Retain an instance so that you can call `resetState()` for fresh searches
        linearLayoutManager = rvTweets.layoutManager as LinearLayoutManager
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.i(TAG, "inside onLoadMore! currentMaxId is: ${currentMaxId.toLong()}") // remove later
                getNextBatchOfTweets()  // send out another network request to get next batch of tweets
                Log.i(TAG, "Inside onLoadMore! Loading more tweets...")
            }
        }
        rvTweets.addOnScrollListener(scrollListener)  // add scroll listener to RecyclerView
        populateHomeTimeline()  // call populate home timeline after everything's been initialized
    }


    /* Override 2 methods to allow tweet composing */

    // inflate the menu resource file we wanna use
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)  // inflate the right resource file
        return true  // inflate and show the menu
    }

    // handles clicks on menu item
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.compose) {
            // navigate to compose screen
            val intent = Intent(this, ComposeActivity::class.java)
            // startActivity(intent)
            startActivityForResult(intent, REQUEST_CODE)  // start the activity, but return a result
        }
        return super.onOptionsItemSelected(item)
    }


    private fun populateHomeTimeline() {
        client.getHomeTimeline(object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                val jsonArray = json.jsonArray  // get json array from response

                try {
                    adapter.clear()  // CLEAR OUT old items before appending the new ones
                    val listOfTweetsRetrieved = Tweet.fromJSONArray(jsonArray)  // parse jsonArray and make list of tweets
                    tweets.addAll(listOfTweetsRetrieved)  // add it to our list of original tweets
                    adapter.notifyDataSetChanged()  // notify the adapter that things have changed
                    swipeContainer.setRefreshing(false)  // Now we call setRefreshing(false) to signal refresh has finished so it stops showing the refreshing icon

                    /*** Start of testing ***/
                    // TODO: store the currentMaxId and somehow get it to getNextBatchOfTweets() inside onLoadMore()
                     currentMaxId = listOfTweetsRetrieved[listOfTweetsRetrieved.size-1].tweetId
                     Log.i(TAG, "inside getHomeTimeline.onSuccess. currentMaxId is: $currentMaxId")
                    /*** End of testing ***/

                } catch (e: JSONException) {
                    Log.e(TAG, "JSON Exception: $e")
                }
                Log.i(TAG, "Success populating home timeline. JSON object: $json")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG, "Failed to populate home timeline. Status code: $statusCode")
            }
        })
    }


    /***/
    private fun getNextBatchOfTweets() {
        client.getNextBatchOfTweets(object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                val jsonArray = json.jsonArray

                try {
                    val newListOfTweetsRetrieved = Tweet.fromJSONArray(jsonArray)
                    tweets.addAll(newListOfTweetsRetrieved)
                    adapter.notifyDataSetChanged()
                    currentMaxId = newListOfTweetsRetrieved[newListOfTweetsRetrieved.size-1].tweetId
                    // adapter.notifyItemRangeInserted(0, tweets.size)
                    scrollListener.resetState()  // reset endless scroll listener when performing a new search
                } catch (e: JSONException) {
                    Log.e(TAG, "JSON Exception: $e")
                }
                Log.i(TAG, "inside getNextBatchOfTweets.onSuccess. JSON object: $json")
                Log.i(TAG, "inside getNextBatchOfTweets.onSuccess. currentMaxId is $currentMaxId")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG, "inside getNextBatchOfTweets.onFailure. Failed to get next batch of tweets. Status code: $statusCode")
            }
        }, currentMaxId.toLong()-1)
    }
}

// NOTE: we can now see tweets based on the people we're following
