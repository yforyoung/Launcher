package com.example.y.launcher;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/*未使用*/
public class LauncherViewPager extends ViewPager {

    public LauncherViewPager(Context context) {
        this(context, null);
    }

    public LauncherViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.setPageMargin(-130);
        this.setOffscreenPageLimit(3);
        this.setPageTransformer(true, new LauncherPageTransformer());
        try {
            Field Scroller = ViewPager.class.getDeclaredField("mScroller");
            Scroller.setAccessible(true);
            Interpolator interpolator = new LinearInterpolator();
            ViewPagerScroller scroller = new ViewPagerScroller(context,
                    interpolator);
            Scroller.set(this, scroller);
        } catch (NoSuchFieldException
                | IllegalArgumentException
                | IllegalAccessException ignored) {
        }

    }

    private class ViewPagerScroller extends Scroller {

        ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy);
        }
    }

    class LauncherPageTransformer implements ViewPager.PageTransformer {
        private float DEFAULT_SCALE = 1.0f;
        private float SCALE_FACTOR = 0.30f;// 缩放因子 0.50f

        @Override
        public void transformPage(View view, float position) {
            if (position <= 1) { // [-1,1]
                if (position < 0) {
                    view.setScaleX(SCALE_FACTOR * position + DEFAULT_SCALE);
                    view.setScaleY(SCALE_FACTOR * position + DEFAULT_SCALE);
                } else {
                    view.setScaleX(SCALE_FACTOR * -position + DEFAULT_SCALE);
                    view.setScaleY(SCALE_FACTOR * -position + DEFAULT_SCALE);
                }
            }
        }


    }
}
