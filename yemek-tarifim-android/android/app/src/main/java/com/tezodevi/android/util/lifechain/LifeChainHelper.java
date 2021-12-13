package com.tezodevi.android.util.lifechain;

import android.content.Context;

public class LifeChainHelper {
    private final Context context;
    private final int score;

    public LifeChainHelper(Context requireContext, int score) {
        this.context = requireContext;
        this.score = score;
    }
}
