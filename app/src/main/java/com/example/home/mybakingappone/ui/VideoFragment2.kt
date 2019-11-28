package com.example.home.mybakingappone.ui


import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.ui.VideoFragment2.Position.playbackPosition
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

/**
 * A simple [Fragment] subclass.
 */
const val urlToTest = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";

class VideoFragment2 : Fragment() {

    lateinit var playerView: PlayerView
    var player: SimpleExoPlayer? = null
    var urlToDisplay: String?=null

    object Position {
        @JvmStatic
        var playbackPosition = 0L //Changed to this for kotlin
        @JvmStatic
        var playWhenReady = true
        @JvmStatic
        var currentWindow = 0
        val BANDWIDTH_METER = DefaultBandwidthMeter()

    }
    //Empty constructor is necessary for instantiating the fragment
    fun VideoFragment2() {

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (savedInstanceState != null) {
            Position.playWhenReady = savedInstanceState.getBoolean(getString(R.string.video_fragment_outstate_playwhenready));
            Position.currentWindow = savedInstanceState.getInt(getString(R.string.video_fragment_outstate_currentWindow));
            playbackPosition = savedInstanceState.getLong(getString(R.string.video_fragment_outstate_playbackposition))
            urlToDisplay = savedInstanceState.getString("url_to_display")
            //Timber.v("1 retrieved playback position is: " + Position.playbackPosition);
        }
        val rootView = inflater.inflate(R.layout.fragment_video_player, container, false)
        playerView = rootView.findViewById(R.id.video_view)
        return rootView;
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        updateStartPosition();
        outState.putBoolean(getString(R.string.video_fragment_outstate_playwhenready), Position.playWhenReady)
        outState.putInt(getString(R.string.video_fragment_outstate_currentWindow), Position.currentWindow)
        outState.putLong(getString(R.string.video_fragment_outstate_playbackposition), playbackPosition)
        outState.putString("url_to_display", urlToDisplay)

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
        super.onPause();
        if (Util.SDK_INT <= 23) {
//            if (playerView != null) {
//                currentWindow = playerView.getPlayer().getCurrentWindowIndex();
//                playbackPosition = Math.max(0, playerView.getPlayer().getContentPosition());
//            }
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

    fun releasePlayer() {
        if (player != null) {
            updateStartPosition()
            player!!.stop()
            player!!.release()
            player = null
        }
    }

    fun initializePlayer(myurl: String?) {
        //Toast.makeText(getActivity(), "myurl is: " + myurl, Toast.LENGTH_SHORT).show()
        if (myurl == null || TextUtils.isEmpty(myurl) || myurl == "") {
            //Toast.makeText(getActivity(), "myurl is null " + myurl, Toast.LENGTH_SHORT).show()
            playerView.setDefaultArtwork(BitmapFactory.decodeResource
            (getActivity()!!.getResources(), R.drawable.question_mark))
            val adaptiveTrackSelectionFactory = AdaptiveTrackSelection.Factory(Position.BANDWIDTH_METER)
            player = ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(getActivity()),
                    DefaultTrackSelector(adaptiveTrackSelectionFactory), DefaultLoadControl());
            playerView.setPlayer(player)
            playerView.hideController()
            player!!.setPlayWhenReady(true)
        } else {
            if (player == null) {
                // a factory to create an AdaptiveVideoTrackSelection
                Timber.v("player is: " + player)
                //Timber.v("playbackposition is: " + Position.playbackPosition)
                val adaptiveTrackSelectionFactory = AdaptiveTrackSelection.Factory(Position.BANDWIDTH_METER)
                // let the factory create a player instance with default components
                player = ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(getActivity()),
                        DefaultTrackSelector(adaptiveTrackSelectionFactory), DefaultLoadControl())
                playerView.setPlayer(player)
                player!!.setPlayWhenReady(Position.playWhenReady)
                //Timber.v("seeking to playbackposition: " + playbackPosition);
                //player!!.seekTo(Position.currentWindow, Position.playbackPosition)
                player!!.seekTo(Position.currentWindow, playbackPosition)
            }
            val mediaSource = buildMediaSource(Uri.parse(myurl))
            player!!.prepare(mediaSource, false, false)
        }
    }

    fun updateStartPosition() {
        if (player != null) {
            Position.playWhenReady = player!!.getPlayWhenReady()
            Position.currentWindow = player!!.getCurrentWindowIndex()
            playbackPosition = player!!.getCurrentPosition()

            // Timber.v("1 playbackposition retrieved before nulling is: " + playbackPosition);
        }
    }

    fun buildMediaSource(uri: Uri): MediaSource {
        return ExtractorMediaSource.Factory(
                DefaultHttpDataSourceFactory("exoplayer-codelab")).createMediaSource(uri);
    }

    fun setUrlToDisplay1(urlToDisplay: String?) {
        this.urlToDisplay = urlToDisplay
    }

    fun setPlaybackPosition1(i: Long) {
        playbackPosition = i
    }

    @SuppressLint("InlinedApi")
    fun hideSystemUi() {
        playerView.systemUiVisibility
                .or(SYSTEM_UI_FLAG_LOW_PROFILE)
                .or(SYSTEM_UI_FLAG_LAYOUT_STABLE)
                .or(SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
                .or(SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
                .or(SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        //Hides the app bar
        //      | View.SYSTEM_UI_FLAG_FULLSCREEN
    }

}
