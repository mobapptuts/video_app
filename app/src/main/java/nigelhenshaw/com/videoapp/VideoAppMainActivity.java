package nigelhenshaw.com.videoapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;


public class VideoAppMainActivity extends Activity {

    private Button mRecordView, mPlayView;
    private VideoView mVideoView;
    private int ACTIVITY_START_CAMERA_APP = 0;
    private final static int REQUEST_READ_STORAGE_RESULT = 1;
    private Uri mVideoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_app_main);

        mRecordView = (Button) findViewById(R.id.recordButton);
        mPlayView = (Button) findViewById(R.id.playButton);
        mVideoView = (VideoView) findViewById(R.id.videoView);

        mRecordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callVideoAppIntent = new Intent();
                callVideoAppIntent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);

                startActivityForResult(callVideoAppIntent, ACTIVITY_START_CAMERA_APP);
            }
        });

        mPlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.start();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK) {
            mVideoUri = data.getData();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    mVideoView.setVideoURI(mVideoUri);
                } else {
                    if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(this,
                                "Need access to file to play the video", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_READ_STORAGE_RESULT);
                }
            } else {
                mVideoView.setVideoURI(mVideoUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == REQUEST_READ_STORAGE_RESULT) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mVideoView.setVideoURI(mVideoUri);
            } else {
                Toast.makeText(this,
                        "Cannot play video because we don't have permission to access the file",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video_app_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
