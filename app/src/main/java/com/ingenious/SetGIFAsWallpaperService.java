package com.ingenious;


import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.ingenious.SharedPref.Setting;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class SetGIFAsWallpaperService extends WallpaperService {

    private class GIFWallpaperEngine extends Engine {
        private Runnable drawGIF = new Runnable() {
            public void run() {
                GIFWallpaperEngine.this.draw();
            }
        };
        private Handler handler = new Handler();
        private SurfaceHolder holder;
        private int mMovieHeight, mMovieWidth, mSurfaceHeight, mSurfaceWidth;
        private Movie movie;
        private float scaleRatio, x, y;
        private boolean visible;

        public GIFWallpaperEngine() {
            super();
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.holder = surfaceHolder;
        }

        public void onDestroy() {
            super.onDestroy();
            this.handler.removeCallbacks(this.drawGIF);
        }

        public void onSurfaceCreated(SurfaceHolder surfaceHolder) {
            super.onSurfaceCreated(surfaceHolder);

            BufferedInputStream bufferedInputStream2 = null;
            try {
                bufferedInputStream2 = new BufferedInputStream(new FileInputStream(new File(Setting.gifPath, Setting.giFName)), 16384);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bufferedInputStream2.mark(16384);
            this.movie = Movie.decodeStream(bufferedInputStream2);
        }

        public void onSurfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            super.onSurfaceChanged(surfaceHolder, i, i2, i3);
            super.onSurfaceChanged(surfaceHolder, i, i2, i3);
            this.mSurfaceWidth = i2;
            this.mSurfaceHeight = i3;
            this.mMovieWidth = this.movie.width();
            this.mMovieHeight = this.movie.height();
            if (((float) this.mSurfaceWidth) / ((float) this.mMovieWidth) > ((float) this.mSurfaceHeight) / ((float) this.mMovieHeight)) {
                this.scaleRatio = ((float) this.mSurfaceWidth) / ((float) this.mMovieWidth);
            } else {
                this.scaleRatio = ((float) this.mSurfaceHeight) / ((float) this.mMovieHeight);
            }

            this.x = (((float) this.mSurfaceWidth) - (((float) this.mMovieWidth) * this.scaleRatio)) / 2.0f;
            this.y = (((float) this.mSurfaceHeight) - (((float) this.mMovieHeight) * this.scaleRatio)) / 2.0f;
            this.x /= this.scaleRatio;
            this.y /= this.scaleRatio;
        }

        public void onSurfaceDestroyed(SurfaceHolder surfaceHolder) {
            super.onSurfaceDestroyed(surfaceHolder);
        }

        public void draw() {
            if (this.visible) {
                Canvas lockCanvas = this.holder.lockCanvas();
                lockCanvas.save();
                lockCanvas.scale(this.scaleRatio, this.scaleRatio);
                this.movie.draw(lockCanvas, this.x, this.y);
                lockCanvas.restore();
                this.holder.unlockCanvasAndPost(lockCanvas);
                this.movie.setTime((int) (System.currentTimeMillis() % ((long) this.movie.duration())));
                this.handler.removeCallbacks(this.drawGIF);
                this.handler.postDelayed(this.drawGIF, 0);
            }
        }

        public void onVisibilityChanged(boolean z) {
            this.visible = z;
            if (z) {
                this.handler.post(this.drawGIF);
            } else {
                this.handler.removeCallbacks(this.drawGIF);
            }
        }
    }

    public static void setAsWallPaper(Context context) {
        try {
            WallpaperManager.getInstance(context).clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if (DeviceDetectUtil.isMiUi()) {
//            Intent intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
//            intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", new ComponentName(context, str));
//            intent.putExtra("SET_LOCKSCREEN_WALLPAPER", true);
//            context.startActivity(intent);
//            return;
//        }
        Intent intent2 = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
        intent2.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", new ComponentName(context, SetGIFAsWallpaperService.class));
        intent2.putExtra("SET_LOCKSCREEN_WALLPAPER", true);
        context.startActivity(intent2);
    }

    public Engine onCreateEngine() {
        return new GIFWallpaperEngine();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}