package com.example.parstagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.parstagram.Post
import com.example.parstagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment : FeedFragment() {

    override fun queryPosts(){
        // specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        // find all post objects
        query.include(Post.KEY_USER)
        // only return posts from current user
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
        // return posts newest to oldest
        query.addDescendingOrder("createdAt")
        // only return the last 20 posts
        query.limit = 20;

        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null)
                    Log.e(TAG, "Error fetching posts")
                else {
                    if (posts != null){
                        for (post in posts)
                            Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser()?.fetchIfNeeded()?.username)
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}