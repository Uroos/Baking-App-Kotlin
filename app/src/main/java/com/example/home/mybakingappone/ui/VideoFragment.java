package com.example.home.mybakingappone.ui;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.home.mybakingappone.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class VideoFragment extends Fragment {

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    @BindView(R.id.video_view)
    PlayerView playerView;
    private SimpleExoPlayer player;

    private static boolean playWhenReady = true;
    private static int currentWindow;
    //protected static long playbackPosition = 0;
    public static long playbackPosition = 0; //Changed to this for kotlin

    private static final String urlToTest = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";
    private String urlToDisplay;
    private Unbinder unbinder;

    //final AtomicBoolean playBackState = new AtomicBoolean();

    //Empty constructor is necessary for instatiating the fragment
    public VideoFragment() {

    }

    //Inflates the fragment layout and sets any resources
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Toast.makeText(getActivity(), "onCreateview", Toast.LENGTH_SHORT).show();
        //Timber.v("player is: " + player + " in oncreateview");
        if (savedInstanceState != null) {
            playWhenReady = savedInstanceState.getBoolean(getString(R.string.video_fragment_outstate_playwhenready));
            currentWindow = savedInstanceState.getInt(getString(R.string.video_fragment_outstate_currentWindow));
            playbackPosition = savedInstanceState.getLong(getString(R.string.video_fragment_outstate_playbackposition));

            Timber.v("1 retrieved playback position is: " + playbackPosition);
        }
        View rootView = inflater.inflate(R.layout.fragment_video_player, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        updateStartPosition();
        //outState.putString("url", urlToDisplay);
        outState.putBoolean(getString(R.string.video_fragment_outstate_playwhenready), playWhenReady);
        outState.putInt(getString(R.string.video_fragment_outstate_currentWindow), currentWindow);
        outState.putLong(getString(R.string.video_fragment_outstate_playbackposition), playbackPosition);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(urlToDisplay);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer(urlToDisplay);
        }
    }

    private void initializePlayer(String myurl) {
        //Toast.makeText(getActivity(), "myurl is: " + myurl, Toast.LENGTH_SHORT).show();
        if (myurl == null || TextUtils.isEmpty(myurl)) {
            //Toast.makeText(getActivity(), "myurl is: " + myurl, Toast.LENGTH_SHORT).show();
            playerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getActivity().getResources(), R.drawable.question_mark));
            TrackSelection.Factory adaptiveTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(adaptiveTrackSelectionFactory), new DefaultLoadControl());
            playerView.setPlayer(player);
            playerView.hideController();
            player.setPlayWhenReady(true);
        } else {
            if (player == null) {
                // a factory to create an AdaptiveVideoTrackSelection
                Timber.v("player is: " + player);
                Timber.v("playbackposition is: " + playbackPosition);
                TrackSelection.Factory adaptiveTrackSelectionFactory =
                        new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
                // let the factory create a player instance with default components
                player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity()),
                        new DefaultTrackSelector(adaptiveTrackSelectionFactory), new DefaultLoadControl());
                playerView.setPlayer(player);
                player.setPlayWhenReady(playWhenReady);
                //Timber.v("seeking to playbackposition: " + playbackPosition);
                player.seekTo(currentWindow, playbackPosition);
            }
            MediaSource mediaSource = buildMediaSource(Uri.parse(myurl));
            player.prepare(mediaSource, false, false);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.v("ondestroyview");
        unbinder.unbind();
    }

    private void updateStartPosition() {
        if (player != null) {
            // Timber.v("updating start position");
            //Timber.v("player is: " + player);
            playWhenReady = player.getPlayWhenReady();
            currentWindow = player.getCurrentWindowIndex();
            playbackPosition = player.getCurrentPosition();
            // Timber.v("1 playbackposition retrieved before nulling is: " + playbackPosition);
        }
    }

    @Override
    public void onPause() {
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

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            Timber.v("onStop");
            releasePlayer();

        }

    }

    private void releasePlayer() {
        if (player != null) {
            updateStartPosition();
            player.stop();
            player.release();
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    public void setUrlToDisplay(String urlToDisplay) {
        this.urlToDisplay = urlToDisplay;
    }

    public void setPlaybackPosition ( long i){this.playbackPosition=i;}
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //Hides the app bar
        //      | View.SYSTEM_UI_FLAG_FULLSCREEN

    }
}
