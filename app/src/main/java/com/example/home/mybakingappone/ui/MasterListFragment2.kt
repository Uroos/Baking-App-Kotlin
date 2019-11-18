package com.example.home.mybakingappone.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Parcelable
import android.provider.SyncStateContract
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.annotation.VisibleForTesting
import android.support.test.espresso.IdlingResource
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.SimpleIdlingResource
import com.example.home.mybakingappone.model.Recipes
import com.example.home.mybakingappone.onlinecheck.GeneralUrls.BUNDLE_RECYCLER_LAYOUT
import com.example.home.mybakingappone.onlinecheck.GeneralUrls.RECIPES_URL
import com.example.home.mybakingappone.onlinecheck.isOnline
import com.example.home.mybakingappone.utils.OkHttpHandler2
import com.example.home.mybakingappone.utils.parseRecipeJson
import timber.log.Timber
import java.io.Serializable

/**
 * A simple [Fragment] subclass.
 */
class MasterListFragment2 : Fragment(), OkHttpHandler2.OnUpdateListener,MasterListAdapter.RecipeListAdapterOnClickHandler {

    private lateinit var task: OkHttpHandler2
    private lateinit var context2:Context

    private var onLineIntentFilter: IntentFilter? = null
    private lateinit var onLineBroadCastReceiver: BroadcastReceiver

    private var recipes: ArrayList<Recipes>? = ArrayList()
    private var recipeAdapter: MasterListAdapter?=null
    private var layoutManager: GridLayoutManager? = null

    private var savedRecyclerLayoutState: Parcelable? = null
    private var bundleRecyclerViewState: Bundle? = null

    lateinit var recyclerView: RecyclerView
    lateinit var errorMessageDisplay: TextView
    lateinit var loadingIndicator: ProgressBar

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    private lateinit var recipeClickCallback: OnImageClickListener
    // The Idling Resource which will be null in production.
    private var  mIdlingResource: SimpleIdlingResource?=null

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    fun getIdlingResource(): IdlingResource {
        if (mIdlingResource == null) {
            mIdlingResource = SimpleIdlingResource()
        }
        return mIdlingResource!! //as IdlingResource
    }

    override fun onRecipeClick(recipe: Recipes) {
        // after click it will call implemented onImageSelected in MainActivity
        recipeClickCallback.onImageSelected(recipe)
    }

    // OnImageClickListener interface, calls a method in the Main activity named onImageSelected
    interface OnImageClickListener {
        fun onImageSelected(recipe: Recipes?)
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    override fun onAttach(context1: Context) {
        super.onAttach(context1)
        context2 = context1
        if (context1 is OnImageClickListener) {
            //Context of MainActivity
            recipeClickCallback = context1
        } else {
            throw ClassCastException(
                    context1.toString() + " must implement OnDogSelected.")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bundleRecyclerViewState = Bundle()

        var rootView = inflater.inflate(R.layout.fragment_master_list, container, false)
        Timber.plant(Timber.DebugTree())

        // gotten from raywenderlick fragment tutorial with dogs list
         val activity=activity as Context

        recyclerView = rootView.findViewById(R.id.rv_recipes)
        errorMessageDisplay = rootView.findViewById(R.id.tv_error_message_display)
        loadingIndicator = rootView.findViewById(R.id.pb_loading_indicator)

        loadingIndicator.setVisibility(View.VISIBLE)
        getIdlingResource()

        task = OkHttpHandler2()
        task.setUpdateListener(this,mIdlingResource)

        var configuration = resources.configuration
        // Toast.makeText(context, "Smalled screen width is" + configuration.smallestScreenWidthDp, Toast.LENGTH_LONG).show();
        if (configuration.smallestScreenWidthDp > 600) {
            layoutManager = GridLayoutManager(activity, 4)

        } else {
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                layoutManager = GridLayoutManager(activity, 1)
            } else if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutManager = GridLayoutManager(activity, 2)

            }

        }

        recipes = ArrayList()
        recyclerView.setLayoutManager(layoutManager)
        recyclerView.setHasFixedSize(true)
        // gotten from raywenderlick fragment tutorial with dogs list
        recipeAdapter = MasterListAdapter(activity, recipes, this)

//        recipeAdapter = MasterListAdapter(context2, recipes, this)
        recyclerView.setAdapter(recipeAdapter)

        if (savedInstanceState != null) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT) as Parcelable
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState)
        }

        setupData()
        onLineIntentFilter = IntentFilter()
        onLineIntentFilter!!.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        onLineBroadCastReceiver = OnLineBroadCastReceiver
        getIdlingResource()
        return rootView
    }

    override fun onResume() {
        super.onResume()
        //changed from this.context!!
        LocalBroadcastManager.getInstance(this.context2)
                .registerReceiver(onLineBroadCastReceiver, onLineIntentFilter!!)
    }

    override fun onPause() {
        super.onPause()
        //changed from this.context!!
        LocalBroadcastManager.getInstance(this.context2).unregisterReceiver(onLineBroadCastReceiver)
    }

    fun setupData() {
        task = OkHttpHandler2()
        //        task.setUpdateListener(this, mIdlingResource);
        task.setUpdateListener(this,mIdlingResource)
        if (isOnline(context2)) {
            recyclerView.setVisibility(View.INVISIBLE)
            //Toast.makeText(context, "is online", Toast.LENGTH_SHORT).show();
            task!!.execute(RECIPES_URL)
        } else {
            showErrorMessage()
        }

    }

    override fun onUpdate(response: String?) {
        if (response != null) {
            //recipes = parseRecipeJson(getActivity(), response);
            recipes=parseRecipeJson(context2,response)
//            var gson = Gson()
//            var listType = TypeToken<ArrayList<Recipes>>{}.type
//            recipes = gson.fromJson(response, listType);

            recipes!!.get(0).setImage((R.drawable.nutella_pie).toString())
            recipes!!.get(1).setImage((R.drawable.brownies).toString())
            recipes!!.get(2).setImage((R.drawable.yellow_cake).toString())
            recipes!!.get(3).setImage((R.drawable.cheese_cake).toString())

            recipeAdapter!!.setRecipeData((recipes))
            recipeAdapter!!.notifyDataSetChanged()
            recyclerView.setAdapter(recipeAdapter)
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState)

            showRecipeData()

        } else {
            showErrorMessage()
        }
    }

    fun showRecipeData() {
        loadingIndicator.setVisibility(View.INVISIBLE)
        errorMessageDisplay.setVisibility(View.INVISIBLE)
        recyclerView.setVisibility(View.VISIBLE)
    }

    fun showErrorMessage() {
        loadingIndicator.setVisibility(View.INVISIBLE)
        recyclerView.setVisibility(View.GONE)
        errorMessageDisplay.setVisibility(View.VISIBLE)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("recipes", recipes as Serializable)
        if (isOnline(context2)) {
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
        } else {
            showErrorMessage()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isOnline(context2)) {
            if (savedInstanceState != null) {
                savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT)
                recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState)
            }
        } else {
            showErrorMessage()
        }
    }

    val OnLineBroadCastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            var action = intent!!.getAction()
            var connected = (action.equals(ConnectivityManager.CONNECTIVITY_ACTION))
            if (connected) {
                setupData()
            } else {
                showErrorMessage()
            }
        }
    }
}



