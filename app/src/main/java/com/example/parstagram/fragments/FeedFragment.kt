package com.example.parstagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.parstagram.MainActivity
import com.example.parstagram.Post
import com.example.parstagram.PostAdapter
import com.example.parstagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

open class FeedFragment : Fragment() {
    lateinit var postsRecyclerView: RecyclerView
    lateinit var adapter: PostAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    var allPosts: MutableList<Post> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up recycler view
        postsRecyclerView = view.findViewById(R.id.rv_post)
        // Lookup the swipe container view
        swipeContainer = view.findViewById(R.id.swipeContainer)
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener {
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            Log.i(TAG,"Refreshing timeline")
            queryPosts()
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);

        //1. Create layout for each row in list
        //2. Create data source for each row (this is the Post class)
        //3. Create adapter that will bridge data and row layout (PostAdapter class)
        //4. Set adapter on RecyclerView
        adapter = PostAdapter(requireContext(), allPosts)
        postsRecyclerView.adapter = adapter
        //5. Set layout manager on RecyclerView
        postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }


    // Query for all posts in our server
    open fun queryPosts() {

        // specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        // find all post objects
        query.include(Post.KEY_USER)
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
                        // clear out currently fetched posts
                        adapter.clear()
                        // data has come back add new items to adapter
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                        swipeContainer.setRefreshing(false)
                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "FeedFragment"
    }
}