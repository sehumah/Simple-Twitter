package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.models.Tweet


private const val TAG = "TweetDetailActivity"
private const val TWEET_EXTRA = "TWEET_EXTRA"

class TweetDetailActivity : AppCompatActivity() {

    // create variables for tweet detail activity views
    private lateinit var ivProfileImage: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var tvScreenName: TextView
    private lateinit var tvTweetText: TextView
    private lateinit var tvTweetTime: TextView
    private lateinit var tvTweetDate: TextView
    private lateinit var tvRetweetCount: TextView
    private lateinit var tvQuoteTweetCount: TextView
    private lateinit var tvFavoriteCount: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet_detail)

        // get reference to the views declared above
        ivProfileImage = findViewById(R.id.iv_profile_image)
        tvUsername = findViewById(R.id.tv_username)
        tvScreenName = findViewById(R.id.tv_screen_name)
        tvTweetText = findViewById(R.id.tv_tweet_body)
        tvTweetTime = findViewById(R.id.tv_tweet_time)
        tvTweetDate = findViewById(R.id.tv_tweet_date)
        tvRetweetCount = findViewById(R.id.tv_retweet_count)
        tvQuoteTweetCount = findViewById(R.id.tv_quote_tweet_count)
        tvFavoriteCount = findViewById(R.id.tv_favorite_count)

        // get tweet object out of the intent's putExtra
        val tweet = intent.getParcelableExtra<Tweet>(TWEET_EXTRA) as Tweet

        // now take data from the tweet object and put them on the new screen
        Glide.with(this).load(tweet.user?.publicImageURL).into(ivProfileImage)
        tvUsername.text = tweet.user?.name
        tvScreenName.text = "@${tweet.user?.screenName}"
        tvTweetText.text = tweet.tweetText
        tvTweetTime.text = tweet.getTimeStamp()
         tvTweetDate.text = ""
        tvRetweetCount.text = tweet.retweetCount.toString()
        // figure out how to get the quote count
        tvFavoriteCount.text = tweet.favoritesCount.toString()

        // log to check that this code is being reached
        Log.i(TAG, "in TweetDetailActivity's override(). tweet contents: ${tweet::class.qualifiedName}")
    }
}
