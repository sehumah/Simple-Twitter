package com.codepath.apps.restclienttemplate

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.models.Tweet


private const val TAG = "TweetsAdapter"
private const val TWEET_EXTRA = "TWEET_EXTRA"
/* turn tweets objects to something that the Recycler view can display */
class TweetsAdapter (private val context: Context, private val tweets: ArrayList<Tweet>) : RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val ivProfileImage: ImageView = itemView.findViewById(R.id.iv_profile_image)
        val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
        val tvScreenName: TextView = itemView.findViewById(R.id.tv_screen_name)
        val tvTimestamp: TextView = itemView.findViewById(R.id.tv_timestamp)
        val tvTweetText: TextView = itemView.findViewById(R.id.tv_tweet_body)
        // val tvReplyCount: TextView = itemView.findViewById(R.id.tv_reply_count)
        val tvRetweetCount: TextView = itemView.findViewById(R.id.tv_retweet_count)
        val tvFavoriteCount: TextView = itemView.findViewById(R.id.tv_favorite_count)

        // register a tweet item with an onclick listener
        init {
            itemView.setOnClickListener (this)
        }

        override fun onClick(view: View?) {
            // 1. notify with tweet owner's username that tweet was clicked
            val tweet = tweets[adapterPosition]  // the tweet that was tapped/touched
            Toast.makeText(context, tweet.user?.name, Toast.LENGTH_SHORT).show()  // notify that tweet was touched

            // 2. use intent to navigate to detail activity
            val intent = Intent(context, TweetDetailActivity::class.java)
            intent.putExtra(TWEET_EXTRA, tweet)
            context.startActivity(intent)
            Log.i(TAG, "in TweetAdapter's inner class.onClick. adapterPosition: $adapterPosition, layoutPosition: $layoutPosition, bindingAdapterPosition: $bindingAdapterPosition")
        }
    }

    /* responsible for inflating the layout we want to use for each of the items to display in the RecyclerView */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        // Inflate item layout
        val view = inflater.inflate(R.layout.item_tweet, parent, false)
        return ViewHolder(view)
    }

    /* in charge of populating data into the item through the ViewHolder */
    override fun onBindViewHolder(holder: TweetsAdapter.ViewHolder, position: Int) {
        // 1st. get the tweet that this specific item needs to inflate. Get the data model based on the position
        val tweet: Tweet = tweets.get(position)

        // Set specific view items based on the specific tweet. Set item views based on views and data model
        holder.tvUsername.text = tweet.user?.name  // username might be null
        holder.tvScreenName.text = "@${tweet.user?.screenName}"
        holder.tvTweetText.text = tweet.tweetText
        holder.tvTimestamp.text = tweet.getFormattedTimestamp()
        // holder.tvReplyCount.text = tweet.replyCount.toString()
        holder.tvRetweetCount.text = tweet.retweetCount.toString()
        holder.tvFavoriteCount.text = tweet.favoritesCount.toString()
        Glide.with(holder.itemView).load(tweet.user?.publicImageURL).into(holder.ivProfileImage)
    }

    override fun getItemCount(): Int {  // tells adapter how many views will be in the RecyclerView
        return tweets.size
    }

    /* below code for pull to refresh functionality */

    // Clean all elements of the recycler
    fun clear() {
        tweets.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(tweetList: List<Tweet>) {
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }
}
