
package com.linkb.jstx.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.linkb.jstx.app.LvxinApplication;


public class AnimationTools {
    private AnimationTools() {
    }

    public static void start(int aid, final View view, final AnimationListener listener) {

        Animation a = android.view.animation.AnimationUtils.loadAnimation(LvxinApplication.getInstance(), aid);
        start(a, view, listener);
    }

    private static void start(Animation a, final View view, final AnimationListener listener) {

        a.setAnimationListener(listener);
        view.startAnimation(a);
    }

    public static void start(View view, int aid) {
        Animation a = android.view.animation.AnimationUtils.loadAnimation(LvxinApplication.getInstance(), aid);
        view.startAnimation(a);
    }
}
