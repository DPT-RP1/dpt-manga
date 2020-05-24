package com.sony.dpt.controller;

import android.view.GestureDetector;
import android.view.MotionEvent;

public abstract class DptGestureListener extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 != null && e2 != null) return onFling(e1, e2);
        return true;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2) {
        float distanceX = e2.getX() - e1.getX();
        float distanceY = e2.getY() - e1.getY();
        if (Math.abs(distanceX) > Math.abs(distanceY)) {
            if (distanceX > 0) {
                onFlingLeft();
            } else {
                onFlingRight();
            }
            return true;
        }
        return false;
    }

    public abstract void onFlingRight();
    public abstract void onFlingLeft();
    public abstract void onSingleTap();

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        onSingleTap();
        return true;
    }
}
