package com.example.home.mybakingappone.ui


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Parcelable
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
import com.example.home.mybakingappone.model.Recipes
import com.example.home.mybakingappone.utils.Constants
import com.example.home.mybakingappone.utils.Constants.*
import com.example.home.mybakingappone.utils.JsonUtils
import com.example.home.mybakingappone.utils.OkHttpHandler
import timber.log.Timber
import java.io.Serializable

/**
 * A simple [Fragment] subclass.
 */
class MasterListFragment2 : Fragment(), OkHttpHandler.OnUpdateListener, MasterListAdapter.RecipeListAdapterOnClickHandler {
    private var task: OkHttpHandler? = null
    private var onLineIntentFilter: IntentFilter? = null
    //private var onLineBroadCastReceiver: BroadCastReceiver()
    private lateinit var onLineBroadCastReceiver: BroadcastReceiver

    private var recipes: List<Recipes> = ArrayList()
    private var recipeAdapter: MasterListAdapter? = null
    private var layoutManager: GridLayoutManager? = null

    private var savedRecyclerLayoutState: Parcelable? = null
    private var bundleRecyclerViewState: Bundle? = null

    lateinit var recyclerView: RecyclerView
    lateinit var errorMessageDisplay: TextView
    lateinit var loadingIndicator: ProgressBar

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    private lateinit var recipeClickCallback: OnImageClickListener

    override fun onRecipeClick(recipe: Recipes?) {
        recipeClickCallback.onImageSelected(recipe)
    }

    // OnImageClickListener interface, calls a method in the Main activity named onImageSelected
    interface OnImageClickListener {
        fun onImageSelected(recipe: Recipes?)
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    override fun onAttach(context1: Context) {
        super.onAttach(context1);
        if (context1 is OnImageClickListener) {
            recipeClickCallback = context1
        } else {
            throw ClassCastException(
                    context.toString() + " must implement OnDogSelected.")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bundleRecyclerViewState = Bundle()

        var rootView = inflater.inflate(R.layout.fragment_master_list, container, false)
        Timber.plant(Timber.DebugTree())

        recyclerView = rootView.findViewById(R.id.rv_recipes)
        errorMessageDisplay = rootView.findViewById(R.id.tv_error_message_display)
        loadingIndicator = rootView.findViewById(R.id.pb_loading_indicator)

        loadingIndicator.setVisibility(View.VISIBLE)

        task = OkHttpHandler()

        var configuration = resources.configuration
        // Toast.makeText(context, "Smalled screen width is" + configuration.smallestScreenWidthDp, Toast.LENGTH_LONG).show();
        if (configuration.smallestScreenWidthDp > 600) {
            layoutManager = GridLayoutManager(context, 4)

        } else {
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                layoutManager = GridLayoutManager(context, 1)
            } else if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutManager = GridLayoutManager(context, 2)

            }

        }

        recipes = ArrayList()
        recyclerView.setLayoutManager(layoutManager)
        recyclerView.setHasFixedSize(true)
        recipeAdapter = MasterListAdapter(context, recipes, this)
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
        return rootView;
    }

    override fun onResume() {
        super.onResume()

        //onLineBroadCastReceiver = this.context!!.registerReceiver(onLineIntentFilter)
        //this.context!!.registerReceiver(onLineBroadCastReceiver, onLineIntentFilter)
        LocalBroadcastManager.getInstance(this.context!!)
                .registerReceiver(onLineBroadCastReceiver, onLineIntentFilter!!)
    }

    override fun onPause() {
        super.onPause()
        this.context!!.unregisterReceiver(onLineBroadCastReceiver)
    }

    fun setupData() {
        task = OkHttpHandler()
        if (isOnline(context)) {
            recyclerView.setVisibility(View.INVISIBLE)
            //Toast.makeText(context, "is online", Toast.LENGTH_SHORT).show();
            task!!.execute(RECIPES_URL)
        } else {
            showErrorMessage()
        }

    }

    override fun onUpdate(response: String?) {
        if (response != null) {
            recipes = JsonUtils.parseRecipeJson(getActivity(), response);
//            var gson = Gson()
//            var listType = TypeToken<ArrayList<Recipes>>{}.type
//            recipes = gson.fromJson(response, listType);

            //recipes.get(0).setImage(String.valueOf(R.drawable.nutella_pie))
            recipes.get(0).setImage((R.drawable.nutella_pie).toString())

            recipes.get(1).setImage((R.drawable.brownies).toString())
            recipes.get(2).setImage((R.drawable.yellow_cake).toString())
            recipes.get(3).setImage((R.drawable.cheese_cake).toString())

            //recipeAdapter = new MasterListAdapter(context, recipes, this)
            recipeAdapter!!.setRecipeData(recipes)
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
        if (isOnline(context)) {
            outState.putParcelable(Constants.BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
        } else {
            showErrorMessage()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isOnline(context)) {
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



