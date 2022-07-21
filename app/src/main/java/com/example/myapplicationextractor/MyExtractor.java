package com.example.myapplicationextractor;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaDataSource;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MyExtractor {

    private final static String TAG = MyExtractor.class.getSimpleName();

    MediaExtractor extractor;
    MediaDataSource mediaDataSource;
    private StringBuilder strBuilder;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public MyExtractor(Context context) {
        extractor = new MediaExtractor();
        strBuilder = new StringBuilder();
        try {
            AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd("movie.mp4");

            extractor.setDataSource(assetFileDescriptor);
            int numTracks = extractor.getTrackCount();

            Log.d(TAG, "numTracks: " + numTracks);

            for (int trackIdx = 0; trackIdx < numTracks; ++trackIdx) {
                MediaFormat format = extractor.getTrackFormat(trackIdx);
                String mime = format.getString(MediaFormat.KEY_MIME);
                int width = format.getInteger(MediaFormat.KEY_WIDTH);
                int height = format.getInteger(MediaFormat.KEY_HEIGHT);
                long duration = format.getLong(MediaFormat.KEY_DURATION, -1);
                int frameRate = format.getInteger(MediaFormat.KEY_FRAME_RATE, -1);
                strBuilder.append("视频尺寸：" + width + "*" + height + "\n帧率：" + frameRate + "fps\n时长：" + duration / 1000 / 1000 + "秒\n");
                Log.i(TAG, "视频尺寸：" + width + "*" + height + "，帧率：" + frameRate + "fps，时长：" + duration / 1000 / 1000 + "秒");
                int profile = format.getInteger(MediaFormat.KEY_PROFILE, -1);
                int level = format.getInteger(MediaFormat.KEY_LEVEL, -1);
                int maxInputSize = format.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE, -1);
                strBuilder.append("视频格式为：" + mime + "\n配置：" + profile + "\n配置等级：" + level + "\n最大单帧缓冲区：" + maxInputSize);
                Log.i(TAG, "视频格式为：" + mime + "，配置：" + profile + "，配置等级：" + level + "，最大单帧缓冲区：" + maxInputSize);
                Log.d(TAG, "mime: " + mime + "width x height: " + width + " x " + height);

                if (mime.startsWith("video")) {
                    extractor.selectTrack(trackIdx);

                    Log.d(TAG, "selectTrack: " + trackIdx);

                    break;
                }
            }
            Log.d(TAG, "Allocate buffer");
            ByteBuffer inputBuffer = ByteBuffer.allocate(1024 * 1024 * 2);

            int trackIndex = extractor.getSampleTrackIndex();
            Log.d(TAG, "trackIndex: " + trackIndex);

            while (extractor.readSampleData(inputBuffer, 0) >= 0) {
                long presentationTimeUs = extractor.getSampleTime();
                extractor.advance();
                Log.d(TAG, "presentationTimeUs: " + presentationTimeUs);
            }

            extractor.release();
            extractor = null;

            // Log.d(TAG, "MyExtractor: " + extractor.readSampleData());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
