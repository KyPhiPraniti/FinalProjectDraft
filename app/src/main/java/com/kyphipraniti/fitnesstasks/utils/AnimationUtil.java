package com.kyphipraniti.fitnesstasks.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;

public class AnimationUtil {
    public static void animate(RecyclerView.ViewHolder holder, Boolean goesDown) {

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView, "translationY", goesDown ? 200 :
            -200, 0);
        animatorTranslateY.setDuration(1000);

        animatorSet.playTogether(animatorTranslateY);
        animatorSet.start();
    }
}