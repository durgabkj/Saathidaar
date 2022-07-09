package com.ottego.saathidaar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class SwipeDisabledPager  extends ViewPager {

    private  boolean enabled;

    public SwipeDisabledPager(Context context) {
        super(context);
    }

    public SwipeDisabledPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled=true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // stop swipe
        if(this.enabled)
        {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(this.enabled)
        {
            return super.onTouchEvent(event);
        }
        // stop switching pages
        return false;
    }

   public  void setPagingEnable(boolean enabled)
   {
       this.enabled = enabled;
   }


    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyScroller extends Scroller {
        public MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350 /*1 secs*/);
        }
    }
}
