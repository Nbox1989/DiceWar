package com.qiang.testspecialview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SpecailButton extends TextView implements View.OnClickListener{
    private static final String TAG = "SpecailButton";
    
    public static final int TEXT_ALIGN_LEFT              = 0x00000001;
    public static final int TEXT_ALIGN_RIGHT             = 0x00000010;
    public static final int TEXT_ALIGN_CENTER_VERTICAL   = 0x00000100;
    public static final int TEXT_ALIGN_CENTER_HORIZONTAL = 0x00001000;
    public static final int TEXT_ALIGN_TOP               = 0x00010000;
    public static final int TEXT_ALIGN_BOTTOM            = 0x00100000;
    
    public static final int COLOR_BULE=1;
    public static final int COLOR_RED=2;
    public static final int COLOR_PINK=3;
    public static final int COLOR_YELLOW=4;
    public static final int COLOR_GREEN=5;
    public static final int COLOR_PURPLE=6;
    public static final int COLOR_ORANGE=7;
    public static final int COLOR_CYAN=8;
    
    private int colorIndex;    
    private int viewIndex;	//start from 1
    private int regionIndex;
    private int drawRes=R.drawable.logo;
    private int drawResSelected=R.drawable.logo;
    
    //end map edit use

   
    private int viewWidth;
    private int viewHeight;
    // 计算文字高度 
    private float fontHeight;
     // 计算文字baseline 
    private float textBaseY;
    private Paint textPaint=new Paint();
    
    private Paint borderPaint;
    
    private float paintStrokeWidth;
    

    private String text;

    private FontMetrics fm;

    private Context mContext;
    private boolean checked = false;	
    
    /*用一个八位字节表示边框风格，
     * 从最后一位到倒数第六位置1分别代表：
          左上，右上，右，右下，左下，左六个边加粗*/
    private byte borderStyle=0x00;
    
    public SpecailButton(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public SpecailButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public SpecailButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    /**
     * 变量初始化
     */
    private void init() {
        setOnClickListener(this);
        
        paintStrokeWidth=4;
        borderPaint=new Paint();
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStrokeWidth(paintStrokeWidth);
        
        
        textPaint.setTextSize(28.0f);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Align.CENTER);
        FontMetrics fontMetrics = textPaint.getFontMetrics(); 
         // 计算文字高度 
        float fontHeight = fontMetrics.bottom - fontMetrics.top; 
         // 计算文字baseline 
        float textBaseY = viewHeight - (viewHeight - fontHeight) / 2 - fontMetrics.bottom; 
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);

        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(wSize, hSize);
        Log.i(TAG, "onMeasure()--wMode=" + wMode + ",wSize=" + wSize + ",hMode=" + hMode+ ",hSize=" + hSize);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
            int bottom) {
        Log.i(TAG, "onLayout");
        viewWidth = right - left; 
        viewHeight = bottom - top;   
        
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制控件内容
        //setTextLocation(text);
        //canvas.drawText(text, textCenterX, textBaselineY, paint);
        if((borderStyle&0x01)>0)
        {
        	drawLeftTopLine(canvas);
        }
        if((borderStyle&0x02)>0)
        {
        	drawRightTopLine(canvas);
        }
        if((borderStyle&0x04)>0)
        {
        	drawRightLine(canvas);
        }
        if((borderStyle&0x08)>0)
        {
        	drawRightBottomLine(canvas);
        }
        if((borderStyle&0x10)>0)
        {
        	drawLeftBottomLine(canvas);
        }
        if((borderStyle&0x20)>0)
        {
        	drawLeftLine(canvas);
        }   
       
	    //canvas.drawText("123", viewWidth / 2, textBaseY, p);
    }    

    public interface OnClickListener {
        void onClick(View v, boolean checked);
    }
    private OnClickListener mListener;
    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    //@Override
    public void onClick(View v) {
        //checked = !checked;
        //setBackgroundResource(checked ? R.drawable.logo:drawableResource);
        if (mListener != null) {
            mListener.onClick(v, checked);
        }
    }
    
    public void SwitchBackGroundForPlay()
    {
    	checked = !checked;
        setBackgroundResource(checked ? drawResSelected:drawRes);
    }
    
    public void SwitchBackGroundForEdit()
    {
    	checked = !checked;
        setBackgroundResource(checked ? R.drawable.logo_black:R.drawable.logo);        
    }
    
    public void SwitchShineBackGround()
    {
    	checked = !checked;
    	setBackgroundResource(checked ? drawResSelected:drawRes);
    }

    public String getTextString() {
        return text;
    }

    public void setviewIndex(int index)
    {
    	viewIndex=index;
    }
    
    public int getviewIndex()
    {
    	return viewIndex;
    }
    
    public void setcolorIndex(int index)
    {
    	colorIndex=index;
    	switch (index)
    	{
	    	case COLOR_BULE:
	    		drawRes=R.drawable.hexblue;
	    		drawResSelected=R.drawable.hexblue_1;
	    		break;
	    	case COLOR_RED:
	    		drawRes=R.drawable.hexred;
	    		drawResSelected=R.drawable.hexred_1;
	    		break;
	    	case COLOR_PINK:
	    		drawRes=R.drawable.hexpink;
	    		drawResSelected=R.drawable.hexpink_1;
	    		break;
	    	case COLOR_YELLOW:
	    		drawRes=R.drawable.hexyellow;
	    		drawResSelected=R.drawable.hexyellow_1;
	    		break;
	    	case COLOR_GREEN:
	    		drawRes=R.drawable.hexgreen;
	    		drawResSelected=R.drawable.hexgreen_1;
	    		break;
	    	case COLOR_PURPLE:
	    		drawRes=R.drawable.hexpurple;
	    		drawResSelected=R.drawable.hexpurple_1;
	    		break;
	    	case COLOR_ORANGE:
	    		drawRes=R.drawable.hexorange;
	    		drawResSelected=R.drawable.hexorange_1;
	    		break;
	    	case COLOR_CYAN:
	    		drawRes=R.drawable.hexcyan;
	    		drawResSelected=R.drawable.hexcyan_1;
	    		break;
	    	default:
	    		break;
    	}
    	setBackgroundResource(drawRes);
    }
    
    public void setRegionIndex(int index)
    {
    	regionIndex=index;
    }
    
    public int getRegionIndex()
    {
    	return regionIndex;
    }   
    
    private void drawLeftTopLine(Canvas canvas)
    {
        canvas.drawLine(0.0f, (float)(Math.sqrt(3)*this.viewWidth/6), viewWidth/2,0.0f, borderPaint);
    }
    
    private void drawRightTopLine(Canvas canvas)
    {
        canvas.drawLine(viewWidth/2,0.0f, viewWidth, (float)(Math.sqrt(3)*viewWidth/6), borderPaint);    	
    }
    
    private void drawRightLine(Canvas canvas)
    {
    	//二分之一线宽的调整
        canvas.drawLine(viewWidth-paintStrokeWidth/2, (float)(Math.sqrt(3)*viewWidth/6),viewWidth-paintStrokeWidth/2, (float)(Math.sqrt(3)*viewWidth/2),borderPaint);    	
    }
    
    private void drawRightBottomLine(Canvas canvas)
    {
    	canvas.drawLine(viewWidth, (float)(Math.sqrt(3)*viewWidth/2),viewWidth/2, (float)(2*Math.sqrt(3)*viewWidth/3),borderPaint);
    }
    
    private void drawLeftBottomLine(Canvas canvas)
    {
    	canvas.drawLine(viewWidth/2, (float)(2*Math.sqrt(3)*viewWidth/3),0.0f, (float)(Math.sqrt(3)*viewWidth/2),borderPaint);
    }
    
    private void drawLeftLine(Canvas canvas)
    {
    	//二分之一线宽的调整
    	canvas.drawLine(0.0f+paintStrokeWidth/2, (float)(Math.sqrt(3)*viewWidth/2),0.0f+paintStrokeWidth/2, (float)(Math.sqrt(3)*this.viewWidth/6),borderPaint);
    }
    
    public void setBoldLeftTop(boolean b)
    {
    	borderStyle=(byte) (b?(borderStyle|0x01):(borderStyle&0xFE));
    }
    
    public void setBoldRightTop(boolean b)
    {
    	borderStyle=(byte) (b?(borderStyle|0x02):(borderStyle&0xFD));
    }
    
    public void setBoldRight(boolean b)
    {
    	borderStyle=(byte) (b?(borderStyle|0x04):(borderStyle&0xFB));
    }
    
    public void setBoldRightBottom(boolean b)
    {
    	borderStyle=(byte) (b?(borderStyle|0x08):(borderStyle&0xF7));
    }
    
    public void setBoldLeftBottom(boolean b)
    {
    	borderStyle=(byte) (b?(borderStyle|0x10):(borderStyle&0xEF));
    }
    
    public void setBoldLeft(boolean b)
    {
    	borderStyle=(byte) (b?(borderStyle|0x20):(borderStyle&0xDF));
    }
    
    public void setBorder(int num)
    {
    	borderStyle=(byte)num;
    }
    
    public void setBorderForEdit()
    {
    	if(viewIndex<=35)
    	{
    		setBoldLeftTop(true);
    		setBoldRightTop(true);
    	}
    	if(viewIndex-1>=933)
    	{
    		setBoldLeftBottom(true);
    		setBoldRightBottom(true);
    	}
    	if((viewIndex+68)%69==0)
    	{
    		setBoldLeft(true);
    		setBoldLeftTop(true);
    		setBoldLeftBottom(true);
    	}
    	if((viewIndex+68)%69==35)
    	{
    		setBoldLeft(true);
    	}
    	if((viewIndex+68)%69==68)
    	{
    		setBoldRight(true);
    	}
    	if((viewIndex+68)%69==34)
    	{
    		setBoldRight(true);
    		setBoldRightTop(true);
    		setBoldRightBottom(true);
    	}
    }
    
    public int getDrawRes()
    {
    	return drawRes;
    }
}
