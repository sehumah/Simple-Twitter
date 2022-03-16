package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers


private const val TAG = "ComposeActivity"
class ComposeActivity : AppCompatActivity() {

    // grab references to the button & edit text
    private lateinit var btnTweet: Button
    private lateinit var etComposeTweet: EditText

    // define variable for the twitter client
    private lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        // grab the views
        btnTweet = findViewById(R.id.btn_tweet)
        etComposeTweet = findViewById(R.id.et_compose_tweet)

        // initialize the client
        client = TwitterApplication.getRestClient(this)

        // handle user's click on tweet button
        btnTweet.setOnClickListener {
            val tweetText = etComposeTweet.text.toString()  // grab the content of the etComposeTweet

            // 1. ensure the tweet isn't empty
            if (tweetText.isEmpty()) {
                Toast.makeText(this, "Tweet is empty!", Toast.LENGTH_SHORT).show()
                btnTweet.isActivated = false
            }
            // 2. ensure that the tweet meets Twitter's required character count
            else if (tweetText.count() > 140) {
                Toast.makeText(this, "Tweet is too long! Limit is 140 characters!", Toast.LENGTH_SHORT).show()
                btnTweet.isActivated = false
                // look into displaying a SnackBar message
            }
            else {
                // Toast.makeText(this, tweetText, Toast.LENGTH_SHORT).show()
                // make an API call to twitter to publish the tweet
                client.publishTweetToTimeline(tweetText, object: JsonHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                        // TODO: "send the tweet back to TimelineActivity to show it on the timeline"
                        val tweet = Tweet.parseJSON(json.jsonObject)
                        // parse the tweet to TimelineActivity
                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)  // set the result to say whether things are okay or not
                        finish()  // close the ComposeActivity so that we end up back in the TimelineActivity


                        Log.i(TAG, "Successfully published tweet!")
                    }

                    override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                        Log.e(TAG, "Error publishing tweet to timeline! statusCode: $statusCode")
                    }
                })
            }
        }
    }
}
