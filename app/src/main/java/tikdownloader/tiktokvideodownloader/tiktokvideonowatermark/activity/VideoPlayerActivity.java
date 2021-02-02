package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.io.File;

import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;

public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private PlayerView playerView;
    private ImageView im_close;
    private ProgressBar progressBar;
    private Intent intent;
    private String fileURL;
    private SimpleExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        playerView = findViewById(R.id.videoPlayer);
        progressBar = findViewById(R.id.progressBar);
        im_close = findViewById(R.id.im_close);
        intent = getIntent();
        if (intent!=null){
            fileURL = intent.getStringExtra("file");
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initializePlayer();
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());
//        String filePath = Environment.getExternalStorageDirectory() + File.separator +
//                "video" + File.separator + "video1.mp4";
        Log.e("filepath", fileURL);
        Uri uri = Uri.parse(fileURL);

        ExtractorMediaSource audioSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(this, "MyExoplayer"), new DefaultExtractorsFactory(), null, null);

        player.prepare(audioSource);
        playerView.setPlayer(player);
        player.setPlayWhenReady(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.im_close:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.setPlayWhenReady(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.setPlayWhenReady(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.setPlayWhenReady(false);
    }
}