package com.example.home.mybakingappone.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.NonNull
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
import com.example.home.mybakingappone.SimpleIdlingResource2
import com.example.home.mybakingappone.model.Recipes2
import com.example.home.mybakingappone.onlinecheck.GeneralUrls.BUNDLE_RECYCLER_LAYOUT
import com.example.home.mybakingappone.onlinecheck.GeneralUrls.RECIPES_URL
import com.example.home.mybakingappone.onlinecheck.isOnline
import com.example.home.mybakingappone.utils.OkHttpHandler2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.io.Serializable

/**
 * A simple [Fragment] subclass.
 */
class MasterListFragment2 : Fragment(), OkHttpHandler2.OnUpdateListener, MasterListAdapter2.RecipeListAdapterOnClickHandler {

    private lateinit var task: OkHttpHandler2
    private lateinit var context2: Context

    private var onLineIntentFilter: IntentFilter? = null
    private lateinit var onLineBroadCastReceiver: BroadcastReceiver

    private var recipes: ArrayList<Recipes2>? = ArrayList()
    private var recipeAdapter: MasterListAdapter2? = null
    private var layoutManager: GridLayoutManager? = null

    private var savedRecyclerLayoutState: Parcelable? = null
    private var bundleRecyclerViewState: Bundle? = null

    lateinit var recyclerView: RecyclerView
    lateinit var errorMessageDisplay: TextView
    lateinit var loadingIndicator: ProgressBar

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    private lateinit var recipeClickCallback: OnImageClickListener
    // The Idling Resource which will be null in production.
    private var mIdlingResource: SimpleIdlingResource2? = null

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    fun getIdlingResource(): IdlingResource {
        if (mIdlingResource == null) {
            mIdlingResource = SimpleIdlingResource2()
        }
        return mIdlingResource!! //as IdlingResource
    }

    override fun onRecipeClick(recipe: Recipes2) {
        recipeClickCallback.onImageSelected(recipe)//Calls the function in Main2Activity
        //Toast.makeText(context,"first ingredient is="+recipe.name, Toast.LENGTH_SHORT).show()

    }

    // OnImageClickListener interface, calls a method in the Main activity named onImageSelected
    interface OnImageClickListener {
        fun onImageSelected(recipe: Recipes2?)
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
        val activity = activity as Context

        recyclerView = rootView.findViewById(R.id.rv_recipes)
        errorMessageDisplay = rootView.findViewById(R.id.tv_error_message_display)
        loadingIndicator = rootView.findViewById(R.id.pb_loading_indicator)

        loadingIndicator.setVisibility(View.VISIBLE)
        getIdlingResource()

        task = OkHttpHandler2()
        task.setUpdateListener(this, mIdlingResource)//this means this fragment is the context

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
        recipeAdapter = MasterListAdapter2(activity, recipes!!, this) // gotten from raywenderlick fragment tutorial with dogs list
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
        task.setUpdateListener(this, mIdlingResource)
        if (isOnline(context2)) {
            recyclerView.setVisibility(View.INVISIBLE)
            //Toast.makeText(context, "is online", Toast.LENGTH_SHORT).show();
            task.execute(RECIPES_URL)
        } else {
            showErrorMessage()
        }

    }

    override fun onUpdate(s: String?) {
        if (s != null) {
            //https://stackoverflow.com/questions/51376954/how-to-use-gson-deserialize-to-arraylist-in-kotlin
            var gson = Gson()
            val itemType = object : TypeToken<ArrayList<Recipes2>>() {}.type
            recipes = gson.fromJson<ArrayList<Recipes2>>(s, itemType)

            recipes!!.get(0).image = R.drawable.nutella_pie.toString()
            recipes!!.get(1).image = R.drawable.brownies.toString()
            recipes!!.get(2).image = R.drawable.yellow_cake.toString()
            recipes!!.get(3).image = R.drawable.cheese_cake.toString()

            recipeAdapter!!.setRecipeData((recipes!!))
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



