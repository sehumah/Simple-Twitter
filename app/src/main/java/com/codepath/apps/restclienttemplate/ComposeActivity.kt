package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers


private const val TAG = "ComposeActivity"
private const val MAXIMUM_TWEET_CHARACTERS = 280
class ComposeActivity : AppCompatActivity() {

    // grab references to the compose activity views
    private lateinit var btnTweet: Button
    private lateinit var etComposeTweet: EditText
    private lateinit var tvCharacterCount: TextView

    // define variable for the twitter client
    private lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        // grab the views
        btnTweet = findViewById(R.id.btn_tweet)
        etComposeTweet = findViewById(R.id.et_compose_tweet)
        tvCharacterCount = findViewById(R.id.tv_character_count)

        // initialize the client
        client = TwitterApplication.getRestClient(this)

        // handle user's click on tweet button
        btnTweet.setOnClickListener {
            val tweetText = etComposeTweet.text.toString()  // grab the content of the etComposeTweet
            // 1. ensure the tweet isn't empty
            if (tweetText.isEmpty()) {
                Toast.makeText(this, "Tweet is empty!", Toast.LENGTH_SHORT).show()
            }
            // 2. ensure that the tweet meets Twitter's required character count
            else if (tweetText.count() > MAXIMUM_TWEET_CHARACTERS) {
                Toast.makeText(this, "Tweet is too long! Only ${MAXIMUM_TWEET_CHARACTERS} characters allowed!", Toast.LENGTH_SHORT).show()
                // look into displaying a SnackBar message
            }
            else {
                // make an API call to twitter to publish the tweet
                client.publishTweetToTimeline(tweetText, object: JsonHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                        // TODO: "send the tweet back to TimelineActivity to show it on the timeline"
                        val tweet = Tweet.parseJSON(json.jsonObject)
                        // parse the tweet to TimelineActivity
                        val intent = Intent()
                        intent.putExtra("tweet", tweet)  // key="tweet" for when user returns to TimelineActivity, value=tweet
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

        etComposeTweet.addTextChangedListener (object : TextWatcher {
            // Fires right as the text is being changed (even supplies the range of text)
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {

                tvCharacterCount.text = "${etComposeTweet.length().toString()}/280"

                if (etComposeTweet.length() == 0 || etComposeTweet.length() > MAXIMUM_TWEET_CHARACTERS) {
                    tvCharacterCount.setTextColor(Color.RED)
                    // btnTweet.isInvisible = true
                }
                else {
                    tvCharacterCount.setTextColor(Color.GREEN)
                    // btnTweet.isInvisible = false
                }
            }
            // Fires right before text is changing
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {  }

            // Fires right after the text has changed
            override fun afterTextChanged(editable: Editable?) {  }
        })
    }
}
