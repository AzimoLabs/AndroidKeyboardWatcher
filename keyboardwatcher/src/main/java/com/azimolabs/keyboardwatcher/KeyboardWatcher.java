package com.azimolabs.keyboardwatcher;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

/**
 * Created by froger_mcs on 21/03/16.
 */
public class KeyboardWatcher {

    private Activity activity;
    private ViewGroup rootView;
    private ViewTreeObserver.OnGlobalLayoutListener viewTreeObserverListener;
    private OnKeyboardToggleListener onKeyboardToggleListener;

    public static KeyboardWatcher initWith(Activity activity) {
        KeyboardWatcher keyboardWatcher = new KeyboardWatcher();
        keyboardWatcher.activity = activity;
        return keyboardWatcher;
    }

    public KeyboardWatcher bindKeyboardWatcher(OnKeyboardToggleListener onKeyboardToggleListener) {
        this.onKeyboardToggleListener = onKeyboardToggleListener;
        final View root = activity.findViewById(android.R.id.content);
        int inputModeFlag = activity.getWindow().getAttributes().softInputMode & WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        if (root instanceof ViewGroup && inputModeFlag != 0) {
            rootView = (ViewGroup) root;
            viewTreeObserverListener = new GlobalLayoutListener();
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(viewTreeObserverListener);
        }

        return this;
    }

    public void unbindKeyboardWatcher() {
        if (rootView != null && viewTreeObserverListener != null) {
            if (Build.VERSION.SDK_INT >= 16) {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(viewTreeObserverListener);
            } else {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(viewTreeObserverListener);
            }

            this.onKeyboardToggleListener = null;
            this.activity = null;
        }
    }

    private class GlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        int initialValue;
        private boolean hasSentInitialAction;
        private boolean isKeyboardShown;

        @Override
        public void onGlobalLayout() {
            if (initialValue == 0) {
                initialValue = rootView.getHeight();
            } else {
                if (initialValue > rootView.getHeight()) {
                    if (onKeyboardToggleListener != null) {
                        if (!hasSentInitialAction || !isKeyboardShown) {
                            isKeyboardShown = true;
                            onKeyboardToggleListener.onKeyboardShown(initialValue - rootView.getHeight());
                        }
                    }
                } else {
                    if (!hasSentInitialAction || isKeyboardShown) {
                        isKeyboardShown = false;
                        rootView.post(new Runnable() {
                            @Override
                            public void run() {
                                if (onKeyboardToggleListener != null) {
                                    onKeyboardToggleListener.onKeyboardClosed();
                                }
                            }
                        });
                    }
                }

                hasSentInitialAction = true;
            }
        }
    }

    public interface OnKeyboardToggleListener {
        void onKeyboardShown(int keyboardSize);

        void onKeyboardClosed();
    }
}