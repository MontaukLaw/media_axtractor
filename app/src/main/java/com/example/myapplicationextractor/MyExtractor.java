package com.example.myapplicationextractor;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaDataSource;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import java.io.IOException;

public class MyExtractor {

    private final static String TAG = MyExtractor.class.getSimpleName();

    MediaExtractor extractor;
    MediaDataSource mediaDataSource;

    public MyExtractor(Context context) {
        extractor = new MediaExtractor();


        try {
            AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd("movie.mp4");
            extractor.setDataSource(assetFileDescriptor);
            int numTracks = extractor.getTrackCount();

            Log.d(TAG, "numTracks: " + numTracks);

            for (int trackIdx = 0; trackIdx < numTracks; ++trackIdx) {
                MediaFormat format = extractor.getTrackFormat(trackIdx);
                String mime = format.getString(MediaFormat.KEY_MIME);
                if (weAreInterestedInThisTrack) {
                    extractor.selectTrack(i);
                }
            }
            Log.d(TAG, "MyExtractor: "+ extractor.readSampleData());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
