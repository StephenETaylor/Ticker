package edu.birzeit.encs539.fall14.ticker;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;



public class FaceView extends View {
    Paint paint = new Paint();
    Time time = new Time();
    long startTime;

    public void setStartTime(long s){
        startTime = s;
    }

    public FaceView(Context c, AttributeSet as){
        super(c,as);
    }
    public FaceView(Context c, AttributeSet as, int style){
        super(c,as, style);
    }

    @Override
    public void onDraw (Canvas canvas){
        int w = getWidth();
        int h = getHeight();
        canvas.drawColor(Color.CYAN);
        double radius = Math.min(w,h)/3.0;
        long now = System.currentTimeMillis();
        time.set(now-startTime);
        double angle = -Math.PI / 2 + ((time.second * Math.PI * 2) / 60);
        float dx = (float)(radius* 0.9 * Math.cos(angle));
        float dy = (float)(radius* 0.9 * Math.sin(angle));
        float cx = w/2;
        float cy = h/2;
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx,cy,(float)radius, paint);
        canvas.drawLine(cx,cy , cx+dx, cy+dy, paint);
    }


}
