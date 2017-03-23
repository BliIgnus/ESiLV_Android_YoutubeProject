package fr.esilv.s8.youtubeproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final String VIDEO = "VIDEO";
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private Video video;

    public static void start(Context context, Video video) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(VIDEO, video);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        YouTubePlayerView youtubePlayer = (YouTubePlayerView) findViewById(R.id.video);
        // Initializing video player with developer key
        youtubePlayer.initialize(Config.YOUTUBE_API_KEY, this);

        TextView textTitle = (TextView) this.findViewById(R.id.title);
        TextView textAuthor = (TextView) this.findViewById(R.id.author);
        TextView textDate = (TextView) this.findViewById(R.id.date);
        TextView textDescription = (TextView) this.findViewById(R.id.description);

        video = (Video) getIntent().getSerializableExtra(VIDEO);

        textTitle.setText(video.getTitle());
        textAuthor.setText(video.getAuthor());
        textDate.setText(video.getDate());
        textDescription.setText(video.getDescription());

        final String VIDEO_ID = video.getId();

        /*youtubePlayer.getSettings().setJavaScriptEnabled(true);
        youtubePlayer.getSettings().setPluginState(WebSettings.PluginState.ON);
        youtubePlayer.loadUrl("http://www.youtube.com/embed/" + video.getId() + "?autoplay=1&vq=small");
        youtubePlayer.setWebChromeClient(new WebChromeClient());*/
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.loadVideo(video.getId());
            player.setPlayerStyle(PlayerStyle.CHROMELESS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.video);
    }
}
