package com.example.home.mybakingappone.ui


import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.ui.VideoFragment2.Position.currentWindow
import com.example.home.mybakingappone.ui.VideoFragment2.Position.playWhenReady
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
    var playbackPosition = 0L

    object Position {
        @JvmStatic
        var playWhenReady = true
        @JvmStatic
        var currentWindow = 0
        val BANDWIDTH_METER = DefaultBandwidthMeter()

    }
    companion object {
        fun newInstance(url:String?): VideoFragment2 {
            val f = VideoFragment2()
           val bundle = Bundle(1)
            bundle.putString("url",url)
            f.arguments = bundle
            return f
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(savedInstanceState==null){
            urlToDisplay=arguments!!.getString("url")
        }else {
            playWhenReady = savedInstanceState.getBoolean(getString(R.string.video_fragment_outstate_playwhenready));
            currentWindow = savedInstanceState.getInt(getString(R.string.video_fragment_outstate_currentWindow));
            playbackPosition = savedInstanceState.getLong(getString(R.string.video_fragment_outstate_playbackposition))
            urlToDisplay = savedInstanceState.getString("url_to_display")
        }
        val rootView = inflater.inflate(R.layout.fragment_video_player, container, false)
        playerView = rootView.findViewById(R.id.video_view)
        return rootView;
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
        if (myurl == null || TextUtils.isEmpty(myurl) || myurl == "") {
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
                val adaptiveTrackSelectionFactory = AdaptiveTrackSelection.Factory(Position.BANDWIDTH_METER)
                // let the factory create a player instance with default components
                player = ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(getActivity()),
                        DefaultTrackSelector(adaptiveTrackSelectionFactory), DefaultLoadControl())
                playerView.setPlayer(player)
                player!!.setPlayWhenReady(playWhenReady)
                player!!.seekTo(currentWindow, playbackPosition)

            }else{
            }
            val mediaSource = buildMediaSource(Uri.parse(myurl))
            player!!.prepare(mediaSource, false, false)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        updateStartPosition();
        outState.putBoolean(getString(R.string.video_fragment_outstate_playwhenready), playWhenReady)
        outState.putInt(getString(R.string.video_fragment_outstate_currentWindow), currentWindow)
        outState.putLong(getString(R.string.video_fragment_outstate_playbackposition), playbackPosition)
        outState.putString("url_to_display", urlToDisplay)

    }
    fun updateStartPosition() {
        if (player != null) {
            playWhenReady = player!!.getPlayWhenReady()
            currentWindow = player!!.getCurrentWindowIndex()
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
                .or(SYSTEM_UI_FLAG_LOW_PROFILE)
                .or(SYSTEM_UI_FLAG_LAYOUT_STABLE)
                .or(SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
                .or(SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
                .or(SYSTEM_UI_FLAG_HIDE_NAVIGATION)
                .or(SYSTEM_UI_FLAG_FULLSCREEN)
    }
}
