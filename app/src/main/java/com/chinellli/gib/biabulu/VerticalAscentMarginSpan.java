package com.chinellli.gib.biabulu;

import android.graphics.Paint;
import android.text.style.LineHeightSpan;

public class VerticalAscentMarginSpan implements LineHeightSpan {
    private final int value;

    public VerticalAscentMarginSpan(int value) {
        this.value = value;
    }
    @Override
    public void chooseHeight(CharSequence charSequence, int i, int i1, int i2, int i3, Paint.FontMetricsInt fontMetricsInt) {
        fontMetricsInt.ascent -= (value/2);
    }
}
