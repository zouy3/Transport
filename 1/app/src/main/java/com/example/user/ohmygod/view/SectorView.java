package com.example.user.ohmygod.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.view.View;

public class SectorView extends View {
    int width,height;
    private RectF oval2;
    private Paint paint;
    int startAngle, sweetAngle;//起始角度，扫描角度
    Timer timer;
    int currentAngle = 0;//当前其实角度
    Handler handler;
    boolean flag = true;
    public SectorView(Context context) {
        super(context);
        setBackgroundColor(Color.BLACK);
        setAlpha((float) 0.5);
        paint = new Paint();
        timer = new Timer();
        handler = new Handler();
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
        paint.setColor(Color.GRAY);
        paint.setAlpha(255);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        //timer.schedule(task, 0, 1000);
        handler.post(new Runnable() {

            @Override
            public void run() {
                currentAngle+=20;
                //postInvalidate();
                invalidate();
                handler.postDelayed(this, 100);
            }
        });
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCirCle(canvas);
        drawSertor(canvas);
    }

    public void drawCirCle(Canvas canvas){
        paint.setStyle(Paint.Style.STROKE);// 设置空心
        for (int r = 20, j=1; r < height/2; ) {
            canvas.drawCircle(width/2, height/2, r, paint);
            r+=80;
        }

    }
    //绘制扇形
    public void drawSertor(Canvas canvas){
        paint.setStyle(Paint.Style.FILL);//设置填满
        oval2 = new RectF(0, (height-width)/2, width, (width+(height-width)/2));
//		Shader mShader = new LinearGradient(0, 0, 100, 100,
//	             new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
//		                      Color.LTGRAY }, null, Shader.TileMode.REPEAT); // 一个材质,打造出一个线性梯度沿著一条线。
        Shader mShader = new LinearGradient(0, 0, 100, 100,
                new int[] { Color.LTGRAY, Color.DKGRAY
                }, null, Shader.TileMode.REPEAT); // 一个材质,打造出一个线性梯度沿著一条线。
        paint.setShader(mShader);
        canvas.drawArc(oval2, currentAngle, 5, true, paint);
    }
}
