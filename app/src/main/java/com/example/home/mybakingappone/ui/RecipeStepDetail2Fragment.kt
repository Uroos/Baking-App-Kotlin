package com.example.home.mybakingappone.ui


import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.ui.RecipeStepDetail2Fragment.PositionToSave.currentWindow
import com.example.home.mybakingappone.ui.RecipeStepDetail2Fragment.PositionToSave.playWhenReady
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import timber.log.Timber

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val SHORT_DESCRIPTION = "short_description"
private const val DESCRIPTION = "description"
private const val URL = "url"
private const val STEP_NUMBER = "step_number"


/**
 * A simple [Fragment] subclass.
 * Use the [RecipeStepDetail2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecipeStepDetail2Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var shortDescription: String? = null
    private var description: String? = null
    private var stepNumber:Int = 0

    lateinit var tvShortDescription:TextView
    lateinit var tvDescription:TextView

    lateinit var playerView: PlayerView
    var player: SimpleExoPlayer? = null
    var urlToDisplay: String?=null
    var playbackPosition = 0L

    object PositionToSave {
        @JvmStatic
        var playWhenReady = true
        @JvmStatic
        var currentWindow = 0
        val BANDWIDTH_METER = DefaultBandwidthMeter()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            shortDescription = it.getString(SHORT_DESCRIPTION)
            description = it.getString(DESCRIPTION)
            urlToDisplay = it.getString(URL)
            stepNumber=arguments!!.getInt(STEP_NUMBER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.activity_recipe_step_detail, container, false)
        initViews(rootView)

        if(savedInstanceState!=null){
            playWhenReady = savedInstanceState.getBoolean(getString(R.string.video_fragment_outstate_playwhenready));
            currentWindow = savedInstanceState.getInt(getString(R.string.video_fragment_outstate_currentWindow));
            playbackPosition = savedInstanceState.getLong(getString(R.string.video_fragment_outstate_playbackposition))
            urlToDisplay = savedInstanceState.getString(getString(R.string.video_fragment_outstate_url))
            description = savedInstanceState.getString(getString(R.string.recipe_step_description_fragment_outstate_description))
            shortDescription = savedInstanceState.getString(getString(R.string.recipe_step_description_fragment_outstate_short_description))
        }
        tvDescription.setText(description)

        // For making a line under the short description
        makeLine()

        return rootView;
    }

    fun initViews(rootView:View){
        playerView = rootView.findViewById(R.id.video_view)
        tvShortDescription = rootView.findViewById(R.id.tv_recipe_step_short_description)
        tvDescription = rootView.findViewById(R.id.tv_recipe_step_description)
    }

    fun makeLine(){
        if(stepNumber==0){
            var content = SpannableString(shortDescription)
            content.setSpan(UnderlineSpan(), 0, content.length, 0)
            tvShortDescription.setText(content)
        }else{
            var content = SpannableString("Step "+stepNumber)
            content.setSpan(UnderlineSpan(), 0, content.length, 0)
            tvShortDescription.setText(content)
        }
    }

    fun releasePlayer() {
        if (player != null) {
            updateStartPosition()
            player!!.stop()
            player!!.release()
            player = null
        }
    }

    fun initializePlayer(myurl: String?) {
        if (myurl == null || TextUtils.isEmpty(myurl) || myurl == "") {
            playerView.setDefaultArtwork(BitmapFactory.decodeResource
            (getActivity()!!.getResources(), R.drawable.question_mark))
            val adaptiveTrackSelectionFactory = AdaptiveTrackSelection.Factory(VideoFragment2.Position.BANDWIDTH_METER)
            player = ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(getActivity()),
                    DefaultTrackSelector(adaptiveTrackSelectionFactory), DefaultLoadControl());
            playerView.setPlayer(player)
            playerView.hideController()
            player!!.setPlayWhenReady(true)

        } else {
            if (player == null) {
                // a factory to create an AdaptiveVideoTrackSelection
                Timber.v("player is: " + player)
                val adaptiveTrackSelectionFactory = AdaptiveTrackSelection.Factory(VideoFragment2.Position.BANDWIDTH_METER)
                // let the factory create a player instance with default components
                player = ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(getActivity()),
                        DefaultTrackSelector(adaptiveTrackSelectionFactory), DefaultLoadControl())
                playerView.setPlayer(player)
                player!!.setPlayWhenReady(VideoFragment2.Position.playWhenReady)
                player!!.seekTo(VideoFragment2.Position.currentWindow, playbackPosition)

            }else{
            }
            val mediaSource = buildMediaSource(Uri.parse(myurl))
            player!!.prepare(mediaSource, false, false)
        }
    }

    fun updateStartPosition() {
        if (player != null) {
            VideoFragment2.Position.playWhenReady = player!!.getPlayWhenReady()
            VideoFragment2.Position.currentWindow = player!!.getCurrentWindowIndex()
            playbackPosition = player!!.getCurrentPosition()
        }
    }

    fun buildMediaSource(uri: Uri): MediaSource {
        return ExtractorMediaSource.Factory(
                DefaultHttpDataSourceFactory("exoplayer-codelab")).createMediaSource(uri);
    }

    fun setUrlToDisplay1(urlToDisplay: String?) {
        this.urlToDisplay = urlToDisplay
    }

    @SuppressLint("InlinedApi")
    fun hideSystemUi() {
        playerView.systemUiVisibility
                .or(View.SYSTEM_UI_FLAG_LOW_PROFILE)
                .or(View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                .or(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
                .or(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
                .or(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
                .or(View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        updateStartPosition()
        outState.putBoolean(getString(R.string.video_fragment_outstate_playwhenready), playWhenReady)
        outState.putInt(getString(R.string.video_fragment_outstate_currentWindow), currentWindow)
        outState.putLong(getString(R.string.video_fragment_outstate_playbackposition), playbackPosition)
        outState.putString(getString(R.string.video_fragment_outstate_url), urlToDisplay)
        outState.putString(getString(R.string.recipe_step_description_fragment_outstate_description), description)
        outState.putString(getString(R.string.recipe_step_description_fragment_outstate_short_description), shortDescription)
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer(urlToDisplay)
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer(urlToDisplay)
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            Timber.v("onPause");
            releasePlayer();
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            Timber.v("onStop")
            releasePlayer()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param shortDescription Parameter 1.
         * @param description Parameter 2.
         * @param videoUrl Parameter 3.
         * @return A new instance of fragment RecipeStepDetail2Fragment.
         */
        @JvmStatic
        fun newInstance(shortDescription: String, description: String, stepNumber:Int, videoUrl:String?) =
                RecipeStepDetail2Fragment().apply {
                    arguments = Bundle().apply {
                        putString(SHORT_DESCRIPTION, shortDescription)
                        putString(DESCRIPTION, description)
                        putInt(STEP_NUMBER, stepNumber)
                        putString(URL, videoUrl)
                    }
                }
    }
}
