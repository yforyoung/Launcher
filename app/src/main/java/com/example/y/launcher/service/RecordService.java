package com.example.y.launcher.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.Environment;
import android.os.HandlerThread;
import android.os.IBinder;
import java.io.File;
import java.io.IOException;

public class RecordService extends Service {
    private VirtualDisplay virtualDisplay;
    private MediaRecorder mediaRecorder;
    private boolean running = false;
    private int width;
    private int height;
    private int dpi;
    private MediaProjection projection;

    public RecordService() {
    }

    public class RecordBinder extends Binder {
        public RecordService getService(){
            return RecordService.this;
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setProjection(MediaProjection projection) {
        this.projection = projection;
    }

    public void startRecord() {
        if (projection == null || running) {
            return;
        }
        initRecorder();
        createVirtualScreen();
        mediaRecorder.start();
        running = true;
    }

    public void stopRecord() {
        if (!running) {
            return;
        }
        running = false;
        mediaRecorder.stop();
        mediaRecorder.reset();
        virtualDisplay.release();
        projection.stop();
    }

    public void setConfig(int width, int height, int dpi) {
        this.width = width;
        this.dpi = dpi;
        this.height = height;
    }

    private void createVirtualScreen() {
        virtualDisplay = projection.createVirtualDisplay(
                "mainScreen",
                width,
                height,
                dpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder.getSurface(),
                null, null
        );
    }

    private void initRecorder() {
        mediaRecorder = new MediaRecorder();
        File file = new File(Environment.getExternalStorageDirectory()+"/ScreenRecord/", System.currentTimeMillis() + ".mp4");
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//音频来源
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);//视频来源
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//输出格式
        mediaRecorder.setOutputFile(file.getAbsolutePath());//输出文件
        mediaRecorder.setVideoSize(width, height);//视频大小
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);//视频编码
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//音频编码
        mediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);//控制视频清晰度和大小
        mediaRecorder.setVideoFrameRate(30);//视频帧数

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSaveDirectory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ScreenRecord/";

            File file = new File(rootDir);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return "保存失败";
                }
            }
            return rootDir;
        } else {
            return "保存失败";
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread serviceThread = new HandlerThread("service_thread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        serviceThread.start();
        running = false;
        mediaRecorder = new MediaRecorder();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return new RecordBinder();
    }
}
