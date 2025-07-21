package com.example.prm392_v1.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DraggableFloatingActionButton extends FloatingActionButton implements View.OnTouchListener {

    private float initialX, initialY;
    private float initialTouchX, initialTouchY;
    private static final int CLICK_ACTION_THRESHOLD = 200;

    public DraggableFloatingActionButton(Context context) {
        super(context);
        init();
    }

    public DraggableFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DraggableFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = view.getX();
                initialY = view.getY();
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();
                return true;

            case MotionEvent.ACTION_MOVE:
                view.setX(initialX + (event.getRawX() - initialTouchX));
                view.setY(initialY + (event.getRawY() - initialTouchY));
                return true;

            case MotionEvent.ACTION_UP:
                float endX = event.getRawX();
                float endY = event.getRawY();
                // Nếu khoảng cách di chuyển nhỏ, coi đó là một cú nhấp chuột
                if (Math.abs(endX - initialTouchX) < CLICK_ACTION_THRESHOLD && Math.abs(endY - initialTouchY) < CLICK_ACTION_THRESHOLD) {
                    performClick();
                }
                return true;
        }
        return false;
    }
}