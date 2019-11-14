package com.example.home.mybakingappone.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.home.mybakingappone.R;
import com.example.home.mybakingappone.SimpleIdlingResource;
import com.example.home.mybakingappone.model.Recipes;
import com.example.home.mybakingappone.utils.Constants;
import com.example.home.mybakingappone.utils.OkHttpHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

import static com.example.home.mybakingappone.utils.Constants.BUNDLE_RECYCLER_LAYOUT;
import static com.example.home.mybakingappone.utils.Constants.RECIPES_URL;
import static com.example.home.mybakingappone.utils.Constants.isOnline;

public class MasterListFragment extends Fragment implements OkHttpHandler.OnUpdateListener, MasterListAdapter.RecipeListAdapterOnClickHandler {

    private OkHttpHandler task;
    private Unbinder unbinder;
    private Context context;
    private IntentFilter onLineIntentFilter;
    private OnLineBroadCastReceiver onLineBroadCastReceiver;

    @BindView(R.id.rv_recipes)
    RecyclerView recyclerView;
    @BindView(R.id.tv_error_message_display)
    TextView errorMessageDisplay;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar loadingIndicator;

    private List<Recipes> recipes;
    private MasterListAdapter recipeAdapter;
    private GridLayoutManager layoutManager;

    private Parcelable savedRecyclerLayoutState;
    private Bundle bundleRecyclerViewState;

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnImageClickListener recipeClickCallback;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    public void onRecipeClick(Recipes recipe) {
        recipeClickCallback.onImageSelected(recipe);
    }

    // OnImageClickListener interface, calls a method in the Main activity named onImageSelected
    public interface OnImageClickListener {
        void onImageSelected(Recipes recipe);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            recipeClickCallback = (OnImageClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }


    // Mandatory empty constructor
    public MasterListFragment() {
    }

    // Inflates the GridView of all Recipe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bundleRecyclerViewState = new Bundle();

        View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);
//        Toolbar menuToolbar = rootView.findViewById(R.id.menu_toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(menuToolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.recipe_title));

        unbinder = ButterKnife.bind(this, rootView);
        Timber.plant(new Timber.DebugTree());

        loadingIndicator.setVisibility(View.VISIBLE);
        getIdlingResource();

        task = new OkHttpHandler();
        task.setUpdateListener(this, mIdlingResource);

        Configuration configuration = getResources().getConfiguration();
        // Toast.makeText(context, "Smalled screen width is" + configuration.smallestScreenWidthDp, Toast.LENGTH_LONG).show();
        if (configuration.smallestScreenWidthDp > 600) {
            layoutManager = new GridLayoutManager(context, 4);

        } else {
            if(configuration.orientation==Configuration.ORIENTATION_PORTRAIT) {
                layoutManager = new GridLayoutManager(context, 1);
            }else if(configuration.orientation==Configuration.ORIENTATION_LANDSCAPE){
                layoutManager = new GridLayoutManager(context, 2);

            }

        }

        recipes = new ArrayList<>();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recipeAdapter = new MasterListAdapter(context, recipes, this);
        recyclerView.setAdapter(recipeAdapter);

        if (savedInstanceState != null) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            //recipes = (List) savedInstanceState.getSerializable("recipes");
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }

        setupData();
        onLineIntentFilter = new IntentFilter();
        onLineIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        onLineBroadCastReceiver = new OnLineBroadCastReceiver();
        // Get the IdlingResource instance
        getIdlingResource();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        context.registerReceiver(onLineBroadCastReceiver, onLineIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        context.unregisterReceiver(onLineBroadCastReceiver);
    }

    private void setupData() {
        task = new OkHttpHandler();
        task.setUpdateListener(this, mIdlingResource);
        if (isOnline(context)) {
            recyclerView.setVisibility(View.INVISIBLE);
            //Toast.makeText(context, "is online", Toast.LENGTH_SHORT).show();
            task.execute(RECIPES_URL);
        } else {
            showErrorMessage();
        }

    }

    @Override
    public void onUpdate(String response) {
        if (response != null) {
            //recipes = JsonUtils.parseRecipeJson(getActivity(), response);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Recipes>>() {
            }.getType();
            recipes = gson.fromJson(response, listType);

            recipes.get(0).setImage(String.valueOf(R.drawable.nutella_pie));
            recipes.get(1).setImage(String.valueOf(R.drawable.brownies));
            recipes.get(2).setImage(String.valueOf(R.drawable.yellow_cake));
            recipes.get(3).setImage(String.valueOf(R.drawable.cheese_cake));

            //recipeAdapter = new MasterListAdapter(context, recipes, this);
            recipeAdapter.setRecipeData(recipes);
            recipeAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(recipeAdapter);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);

            showRecipeData();
        } else {
            showErrorMessage();
        }
    }

    private void showRecipeData() {
        loadingIndicator.setVisibility(View.INVISIBLE);
        errorMessageDisplay.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        loadingIndicator.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.GONE);
        errorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("recipes", (Serializable) recipes);
        if (isOnline(context)) {
            outState.putParcelable(Constants.BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isOnline(context)) {
            if (savedInstanceState != null) {
                savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
                recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            }
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class OnLineBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean connected = (action.equals(ConnectivityManager.CONNECTIVITY_ACTION));
            if (connected) {
                setupData();
            } else {
                showErrorMessage();
            }
        }
    }
}
