package com.zjd.timepicker;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zjd.timepicker.util.DensityUtil;
import com.zjd.timepicker.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 左金栋 on 2017/8/31.
 */

public class TimePicker extends View {
    private Context context;
    private Paint paintBackground,paintText,paintAlpha;

    private int width;//设置高
    private int height;//设置高

    private List<Integer> yearList,monthList,dateList,hourList;
    private List<String>minuteList;

    private int year=0,month=0,day=0,hour=0,minute=0,daysThisMonth=0;
    private String strMinute="";

    public TimePicker(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public TimePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    public TimePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawYear(canvas);
        drawMonth(canvas);
        drawDate(canvas);
        drawHour(canvas);
        drawMinute(canvas);
        drawAlpha(canvas);
    }

    /**
     * 背景
     */
    private void drawBackground(Canvas canvas) {
        paintBackground.setColor(Color.GRAY);
        canvas.drawRect(0,0,width,height,paintBackground);
        paintBackground.setColor(Color.DKGRAY);
        for (int i = 0; i < 5; i++) {
            canvas.drawRect(height/20+i*width/5,height/20,width/5-height/20+i*width/5,height*19/20,paintBackground);
            canvas.drawLine(height/20+i*width/5,height/2-height/8,width/5-height/20+i*width/5,height/2-height/8,paintText);
            canvas.drawLine(height/20+i*width/5,height/2+height/8,width/5-height/20+i*width/5,height/2+height/8,paintText);
        }
        canvas.clipRect(height/20,height/20,width-height/20,height*19/20);
    }

    /**
     * 渐变边框
     */
    private void drawAlpha(Canvas canvas) {
        Shader mShader = new LinearGradient(height/20+width/10,height/20,height/20+width/10,height*19/20,new int[]{Color.BLACK,0x00000000,Color.BLACK},null, Shader.TileMode.CLAMP);
        paintAlpha.setShader(mShader);
        for (int i = 0; i < 5; i++) {
            canvas.drawRect(height/20+i*width/5,height/20,width/5-height/20+i*width/5,height*19/20,paintAlpha);
        }
    }

    private float yearX,yearY,yearY2;
    /**
     * 年
     */
    private void drawYear(Canvas canvas) {
        Rect rect = new Rect();
        String year=yearList.get(0)+"";
        paintText.getTextBounds(year, 0, year.length(), rect);
        for (int i = 0; i < yearList.size(); i++) {
            canvas.drawText(yearList.get(i)+"",yearX,yearY2+rect.height()/2+i*height/4,paintText);
        }
    }

    private float monthX,monthY,monthY2;
    /**
     * 月
     */
    private void drawMonth(Canvas canvas) {
        Rect rect = new Rect();
        String month=monthList.get(0)+"";
        paintText.getTextBounds(month, 0, month.length(), rect);
        for (int i = 0; i < monthList.size(); i++) {
            canvas.drawText(monthList.get(i)+"",monthX,monthY2+rect.height()/2+i*height/4,paintText);
        }
    }

    private float dateX,dateY,dateY2;
    /**
     * 日
     */
    private void drawDate(Canvas canvas) {
        Rect rect = new Rect();
        String date=dateList.get(0)+"";
        paintText.getTextBounds(date, 0, date.length(), rect);
        for (int i = 0; i < dateList.size(); i++) {
            canvas.drawText(dateList.get(i)+"",dateX,dateY2+rect.height()/2+i*height/4,paintText);
        }
    }

    private float hourX,hourY,hourY2;
    /**
     * 时
     */
    private void drawHour(Canvas canvas) {
        Rect rect = new Rect();
        String hour=hourList.get(0)+"";
        paintText.getTextBounds(hour, 0, hour.length(), rect);
        for (int i = 0; i < hourList.size(); i++) {
            canvas.drawText(hourList.get(i)+"",hourX,hourY2+rect.height()/2+i*height/4,paintText);
        }
    }

    private float minuteX,minuteY,minuteY2;
    /**
     * 分
     */
    private void drawMinute(Canvas canvas) {
        Rect rect = new Rect();
        String minute=minuteList.get(0)+"";
        paintText.getTextBounds(minute, 0, minute.length(), rect);
        for (int i = 0; i < minuteList.size(); i++) {
            canvas.drawText(minuteList.get(i)+"",minuteX,minuteY2+rect.height()/2+i*height/4,paintText);
        }
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = getDefaultSize(getMeasuredWidth(), widthMeasureSpec);// 获得控件的宽度
        setMeasuredDimension(measureWidth , measureWidth/3);//设置宽和高
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initTime();

        width=w;
        height=h;

        paintText.setTextSize(height/8);

        yearX=width/10;
        yearY2=height/2;
        yearLastY=yearY2;

        monthX=width/10+width/5;
        monthY2=height/2-(month+2)*height/4;
        monthLastY=monthY2;

        dateX=width/10+width*2/5;
        dateY2=height/2-(day+4)*height/4;
        dateLastY=dateY2;

        hourX=width/10+width*3/5;
        hourY2=height/2-(hour+3)*height/4;
        hourLastY=hourY2;

        minuteX=width/10+width*4/5;
        minuteY2=height/2-(minute+3)*height/4;
        minuteLastY=minuteY2;
    }

    private void init() {
        initPaint();
    }

    /**
     * 初始化时间
     */
    private void initTime() {
        Date date=new Date();
        //年
        yearList=new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        for (int i = 0; i < 30; i++) {
            yearList.add(cal.get(Calendar.YEAR)+i);
        }

        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH)+1;
        day=cal.get(Calendar.DATE);
        hour=cal.get(Calendar.HOUR_OF_DAY);
        minute=cal.get(Calendar.MINUTE);
        if(minute<10){
            strMinute="0"+minute;
        }else {
            strMinute=""+minute;
        }

        //月份
        monthList=new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            monthList.add((9+i)%12+1);
        }

        //日期
        dateList=new ArrayList<>();
        initDate();

        //小时
        hourList=new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            hourList.add((21+i)%24);
        }

        //分钟
        minuteList=new ArrayList<>();
        for (int i = 0; i < 66; i++) {
            int minute=(57+i)%60;
            if(minute<10){
                String strMinute="0"+minute;
                minuteList.add(strMinute);
            }else {
                minuteList.add(minute+"");
            }
        }
    }

    private void initDate() {
        Date date=new Date(year,month,0);
        daysThisMonth=date.getDate();
        dateList=new ArrayList<>();
        for (int i = 0; i < daysThisMonth+10; i++) {
            dateList.add((daysThisMonth+i-5)%daysThisMonth+1);
        }
    }


    /**
     * 初始化画笔
     */
    private void initPaint() {
        paintBackground=new Paint();
        paintBackground.setAntiAlias(true);
        paintBackground.setColor(Color.GRAY);
        paintBackground.setStyle(Paint.Style.FILL);
        paintBackground.setStrokeWidth(DensityUtil.dip2px(context,2));

        paintText=new Paint();
        paintText.setAntiAlias(true);
        paintText.setColor(Color.WHITE);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextAlign(Paint.Align.CENTER);

        paintAlpha=new Paint();
        paintAlpha.setAntiAlias(true);
        paintAlpha.setStyle(Paint.Style.FILL);
    }

    private float downX,downY;
    private int moveIndex;
    private float yearLastY,monthLastY,dateLastY,hourLastY,minuteLastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                moveIndex=-1;
                //获取屏幕上点击的坐标
                downX = event.getX();
                downY = event.getY();

                for (int i = 0; i < 5; i++) {
                    if(Util.isInside(downX,downY,height/20+i*width/5,height/20,width/5-height/20+i*width/5,height*19/20)){
                        moveIndex=i;
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(moveIndex==0){
                    yearY=yearLastY+event.getY()-downY;
                    if(yearY>height/2){
                        yearY=height/2;
                    }else if(yearY<height/2-(yearList.size()-1)*height/4){
                        yearY=height/2-(yearList.size()-1)*height/4;
                    }
                    yearY2=yearY;
                    invalidate();
                }else if(moveIndex==1){
                    monthY=monthLastY+event.getY()-downY;
                    if(monthY>height/2){
                        monthY=height/2;
                    }else if(monthY<height/2-(monthList.size()-1)*height/4){
                        monthY=height/2-(monthList.size()-1)*height/4;
                    }

                    if(monthY>0){
                        monthLastY=monthLastY-12*height/4;
                        monthY=monthLastY+event.getY()-downY;
                    }else if(monthY<-13*height/4){
                        monthLastY=monthLastY+12*height/4;
                        monthY=monthLastY+event.getY()-downY;
                    }

                    monthY2=monthY;
                    invalidate();
                }else if(moveIndex==2){
                    dateY=dateLastY+event.getY()-downY;
                    if(dateY>height/2){
                        dateY=height/2;
                    }else if(dateY<height/2-(dateList.size()-1)*height/4){
                        dateY=height/2-(dateList.size()-1)*height/4;
                    }

                    if(dateY>-height/2){
                        dateLastY=dateLastY-daysThisMonth*height/4;
                        dateY=dateLastY+event.getY()-downY;
                    }else if(dateY<-(daysThisMonth+1)*height/4){
                        dateLastY=dateLastY+daysThisMonth*height/4;
                        dateY=dateLastY+event.getY()-downY;
                    }

                    dateY2=dateY;
                    invalidate();
                }else if(moveIndex==3){
                    hourY=hourLastY+event.getY()-downY;
                    if(hourY>height/2){
                        hourY=height/2;
                    }else if(hourY<height/2-(hourList.size()-1)*height/4){
                        hourY=height/2-(hourList.size()-1)*height/4;
                    }

                    if(hourY>0){
                        hourLastY=hourLastY-24*height/4;
                        hourY=hourLastY+event.getY()-downY;
                    }else if(hourY<-25*height/4){
                        hourLastY=hourLastY+24*height/4;
                        hourY=hourLastY+event.getY()-downY;
                    }

                    hourY2=hourY;
                    invalidate();
                }else if(moveIndex==4){
                    minuteY=minuteLastY+event.getY()-downY;
                    if(minuteY>height/2){
                        minuteY=height/2;
                    }else if(minuteY<height/2-(minuteList.size()-1)*height/4){
                        minuteY=height/2-(minuteList.size()-1)*height/4;
                    }

                    if(minuteY>0){
                        minuteLastY=minuteLastY-60*height/4;
                        minuteY=minuteLastY+event.getY()-downY;
                    }else if(minuteY<-61*height/4){
                        minuteLastY=minuteLastY+60*height/4;
                        minuteY=minuteLastY+event.getY()-downY;
                    }

                    minuteY2=minuteY;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(moveIndex==0){
                    yearLastY=yearY;
                    fixYearAnim((height/2-yearY)%(height/4));
                    int position=(int)((height/2-yearLastY+height/8)/(height/4));
                    year=yearList.get(position);
                    initDate();
                }else if(moveIndex==1){
                    monthLastY=monthY;
                    fixMonthAnim((height/2-monthY)%(height/4));
                    int position=(int)((height/2-monthLastY+height/8)/(height/4));
                    month=monthList.get(position);
                    initDate();
                }else if(moveIndex==2){
                    dateLastY=dateY;
                    fixDateAnim((height/2-dateY)%(height/4));
                    int position=(int)((height/2-dateLastY+height/8)/(height/4));
                    day=dateList.get(position);
                }else if(moveIndex==3){
                    hourLastY=hourY;
                    fixHourAnim((height/2-hourY)%(height/4));
                    int position=(int)((height/2-hourLastY+height/8)/(height/4));
                    hour=hourList.get(position);
                }else if(moveIndex==4){
                    minuteLastY=minuteY;
                    fixMinuteAnim((height/2-minuteY)%(height/4));
                    int position=(int)((height/2-minuteLastY+height/8)/(height/4));
                    strMinute=minuteList.get(position);
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private float valueYear=0;
    private void fixYearAnim(final float slipY) {
        ValueAnimator valueAnimatorYear;
        if(slipY<=height/8){
            valueAnimatorYear= ValueAnimator.ofFloat(0f,slipY);
        }else {
            valueAnimatorYear= ValueAnimator.ofFloat(0f,slipY-height/4);
        }
        valueAnimatorYear.setDuration(300);
        valueAnimatorYear.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                valueYear= (float) animation.getAnimatedValue();
                yearY2=yearY+valueYear;
                yearLastY=yearY+valueYear;
                invalidate();
            }
        });
        valueAnimatorYear.start();
    }

    private float valueMonth=0;
    private void fixMonthAnim(final float slipY) {
        ValueAnimator valueAnimatorMonth;
        if(slipY<=height/8){
            valueAnimatorMonth= ValueAnimator.ofFloat(0f,slipY);
        }else {
            valueAnimatorMonth= ValueAnimator.ofFloat(0f,slipY-height/4);
        }
        valueAnimatorMonth.setDuration(300);
        valueAnimatorMonth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                valueMonth= (float) animation.getAnimatedValue();
                monthY2=monthY+valueMonth;
                monthLastY=monthY+valueMonth;
                invalidate();
            }
        });
        valueAnimatorMonth.start();
    }

    private float valueDate=0;
    private void fixDateAnim(final float slipY) {
        ValueAnimator valueAnimatorDate;
        if(slipY<=height/8){
            valueAnimatorDate= ValueAnimator.ofFloat(0f,slipY);
        }else {
            valueAnimatorDate= ValueAnimator.ofFloat(0f,slipY-height/4);
        }
        valueAnimatorDate.setDuration(300);
        valueAnimatorDate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                valueDate= (float) animation.getAnimatedValue();
                dateY2=dateY+valueDate;
                dateLastY=dateY+valueDate;
                invalidate();
            }
        });
        valueAnimatorDate.start();
    }

    private float valueHour=0;
    private void fixHourAnim(final float slipY) {
        ValueAnimator valueAnimatorHour;
        if(slipY<=height/8){
            valueAnimatorHour= ValueAnimator.ofFloat(0f,slipY);
        }else {
            valueAnimatorHour= ValueAnimator.ofFloat(0f,slipY-height/4);
        }
        valueAnimatorHour.setDuration(300);
        valueAnimatorHour.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                valueHour= (float) animation.getAnimatedValue();
                hourY2=hourY+valueHour;
                hourLastY=hourY+valueHour;
                invalidate();
            }
        });
        valueAnimatorHour.start();
    }

    private float valueMinute=0;
    private void fixMinuteAnim(final float slipY) {
        ValueAnimator valueAnimatorMinute;
        if(slipY<=height/8){
            valueAnimatorMinute = ValueAnimator.ofFloat(0f,slipY);
        }else {
            valueAnimatorMinute = ValueAnimator.ofFloat(0f,slipY-height/4);
        }
        valueAnimatorMinute.setDuration(300);
        valueAnimatorMinute.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                valueMinute= (float) animation.getAnimatedValue();
                minuteY2=minuteY+valueMinute;
                minuteLastY=minuteY+valueMinute;
                invalidate();
            }
        });
        valueAnimatorMinute.start();
    }

    public String getDateStr(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String calender=format.format(new Date(getDate()));
        return calender;
    }

    public long getDate(){
        int position=(int)((height/2-dateLastY+height/8)/(height/4));
        day=dateList.get(position);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date=format.parse(year+"-"+month+"-"+day+" "+hour+":"+strMinute);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date().getTime();
        }
    }
}
