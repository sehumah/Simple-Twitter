package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet

class ComposeActivity : AppCompatActivity() {

    // grab references to the button and edit text
    private lateinit var btnTweet: Button
    private lateinit var etComposeTweet: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        // grab the views
        btnTweet = findViewById(R.id.btn_tweet)
        etComposeTweet = findViewById(R.id.et_compose_tweet)

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
                Toast.makeText(this, tweetText, Toast.LENGTH_SHORT).show()
                // TODO: make an API call to twitter to publish the tweet
            }
        }
    }
}
