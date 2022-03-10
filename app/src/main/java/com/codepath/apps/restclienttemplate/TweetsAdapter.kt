package com.codepath.apps.restclienttemplate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.models.Tweet


/* turn tweets objects to something that the Recycler view can display */
class TweetsAdapter (private val tweets: List<Tweet>) : RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {

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
        holder.tweetText.text = tweet.tweetText
        Glide.with(holder.itemView).load(tweet.user?.publicImageURL).into(holder.ivProfileImage)
    }

    override fun getItemCount(): Int {  // tells adapter how many views will be in the RecyclerView
        return tweets.size
    }


    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfileImage: ImageView = itemView.findViewById(R.id.iv_profile_image)
        val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
        val tvTimestamp: TextView = itemView.findViewById(R.id.tv_timestamp)
        val tweetText: TextView = itemView.findViewById(R.id.tv_tweet_body)
    }
}
