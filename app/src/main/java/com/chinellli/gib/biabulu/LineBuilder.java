package com.chinellli.gib.biabulu;

import android.text.SpannableString;

public class LineBuilder {
    private int begin;
    private int end;
    private LineSpanner lineSpanner;

    public LineBuilder(int begin, int end){
        this.begin = begin; this.end = end;
    }

    public void setBegin(int begin){
        this.begin = begin;
    }

    public int getBegin(){
        return this.begin;
    }

    public void setEnd(int end){
        this.end = end;
    }

    public int getEnd(){
        return this.end;
    }

    public LineSpanner getLineSpanner(){
        return this.lineSpanner;
    }

    public void setLineSpanner(LineSpanner lineSpanner){
        this.lineSpanner = lineSpanner;
    }

    public void buildLine(SpannableString spannableString){
        lineSpanner.spanSong(spannableString,begin,end);
    }
}
