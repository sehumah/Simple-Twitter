package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.github.scribejava.apis.TwitterApi
import okhttp3.Headers


private const val TAG = "TimelineActivity"
class TimelineActivity : AppCompatActivity() {

    private lateinit var client: TwitterClient  // declare client variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        client = TwitterApplication.getRestClient(this)  // initialize client variable
        populateHomeTimeline()

    }

    private fun populateHomeTimeline () {
        client.getHomeTimeline(object : JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                Log.i(TAG, "success populating home timeline. JSON object: $json")
            }

            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.i(TAG, "failed to populate home timeline. Status code: $statusCode")
            }
        })
    }
}
