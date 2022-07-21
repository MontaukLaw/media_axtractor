package com.example.myapplicationextractor;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final String TAG = MainActivity.class.getSimpleName();

    MediaPlayer mp;
    boolean isPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(this);
        mp = new MediaPlayer();
        isPaused = false;

        MyExtractor myExtractor = new MyExtractor(this);


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (permissionCheck != 0) {
            Log.e(TAG, "No Intenet permission ");
        }
        try {
            mp.setDisplay(holder);
            if (!isPaused) {
                mp.setDataSource("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear1/fileSequence0.ts");
                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                mp.prepareAsync();
            } else {
                mp.start();
            }
            isPaused = false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (IllegalStateException ise) {
            ise.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mp.isPlaying() && !isPaused) {
            mp.pause();
            isPaused = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.stop();
            mp.release();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}