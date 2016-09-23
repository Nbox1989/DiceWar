package com.qiang.testspecialview;

import java.util.ArrayList;
import java.util.List;

import com.qiang.utils.ConstUtil;
import com.qiang.utils.EnumUtil;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.TextView;

public class PopView extends View{

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	    diceP.setColor(EnumUtil.getColorAt(InitiatorColor));
	    for(int i=0;i<Initiator.size();i++)
	    {	    	
	    	RectF rec=new RectF(dicePadding+(dicePadding+diceWidth)*i, dicePadding, 
	    			dicePadding+(dicePadding+diceWidth)*i+diceWidth, dicePadding+diceHeight);
	    	drawDice(Initiator.get(i), rec, canvas, diceP);
	    }
	    
	    diceP.setColor(EnumUtil.getColorAt(RecieverColor));
	    for(int i=0;i<Reciever.size();i++)
	    {
	    	RectF rec=new RectF(getRight()-dicePadding-(dicePadding+diceWidth)*i-diceWidth, getBottom()-dicePadding-diceHeight, 
	    			getRight()-dicePadding-(dicePadding+diceWidth)*i, getBottom()-dicePadding);
	    	drawDice(Reciever.get(i), rec, canvas, diceP);
	    	
	    }
	        
	    textP.setColor(EnumUtil.getColorAt(InitiatorColor));
	    canvas.drawText(InitiatortotalNum<=9?" "+String.valueOf(InitiatortotalNum):String.valueOf(InitiatortotalNum),
	    		0, (getHeight()+fontHeight)/2, textP);
	    textP.setColor(EnumUtil.getColorAt(RecieverColor));
	    canvas.drawText(RecievertotalNum<=9?" "+String.valueOf(RecievertotalNum):String.valueOf(RecievertotalNum),
	    		200, (getHeight()+fontHeight)/2, textP);
	    textP.setColor(Color.BLACK);
	    textP.setTextSize(60.0f);
	    canvas.drawText("VS", 100, (getHeight()+fontHeight)/2, textP);
	    textP.setTextSize(90.0f);
	    canvas.drawText(isWin==0?"Draw!!!":(isWin>0?"Win!!!":"Lose!!!"), 300, (getHeight()+fontHeight)/2, textP);
	    
	    Bitmap bmp=isWin==0?BitmapFactory.decodeResource(getResources(), R.drawable.daze):	  
	    		(isWin>0?BitmapFactory.decodeResource(getResources(), R.drawable.laugh):
	    			BitmapFactory.decodeResource(getResources(), R.drawable.cry));
	    canvas.drawBitmap(bmp, new Rect(0, 0, bmp.getWidth(), bmp.getHeight()), 
				new Rect(600,(getHeight()-160)/2,600+160,(getHeight()+160)/2), 
				textP);	
	}

	private Paint textP=new Paint();
	private Paint diceP=new Paint();
	private Paint dicePointP=new Paint();
	private float fontHeight;
	
	private final float diceWidth=50.0f;
	private final float diceHeight=50.0f;	
	private final float dicePadding=10.0f;
	
	private Bitmap bmp;
	
	public PopView(Context context) {
		super(context);
		init();
	}
	
	/*public PopView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }*/

    public PopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }   
   
    private void init() {
		// TODO Auto-generated method stub
		dicePointP.setColor(Color.WHITE);
		textP.setTextSize(72.0f);
		textP.setFakeBoldText(true);
		fontHeight=(float) Math.ceil(textP.getFontMetrics().descent-textP.getFontMetrics().ascent);
		
	}

	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(760, 300);
    }
    
    private void drawDice(int diceNum,RectF rec,Canvas can,Paint p)
    {
    	if(diceNum<1||diceNum>6)
    	{
    		return;
    	}
    	switch (diceNum)
    	{
	    	case 1:
	    		drawOne(rec, can, p);
	    		break;
	    	case 2:
	    		drawTwo(rec, can, p);
	    		break;
	    	case 3:
	    		drawThree(rec, can, p);
	    		break;
	    	case 4:
	    		drawFour(rec, can, p);
	    		break;
	    	case 5:
	    		drawFive(rec, can, p);;
	    		break;
	    	case 6:
	    		drawSix(rec, can, p);
	    		break;
    		default:
    			break;
    	}
    }
    
    private void drawOne(RectF rec, Canvas can,Paint p)
    {
	    can.drawRoundRect(rec, RectRadius, RectRadius, p);
    	can.drawCircle(rec.centerX(), rec.centerY(), rec.width()/5, dicePointP);
    }
    
    private void drawTwo(RectF rec, Canvas can,Paint p)
    {
    	can.drawRoundRect(rec, RectRadius, RectRadius, p);
    	can.drawCircle(rec.right-rec.width()/7*2,rec.top+rec.height()/7*2, rec.width()/8, dicePointP);
    	can.drawCircle(rec.left+rec.width()/7*2, rec.bottom-rec.height()/7*2, rec.width()/8, dicePointP);
    }
    
    private void drawThree(RectF rec, Canvas can,Paint p)
    {
    	can.drawRoundRect(rec, RectRadius, RectRadius, p);
    	can.drawCircle(rec.right-rec.width()/4, rec.top+rec.height()/4, rec.width()/8, dicePointP);
    	can.drawCircle(rec.centerX(), rec.centerY(), rec.width()/8, dicePointP);
    	can.drawCircle(rec.left+rec.width()/4, rec.bottom-rec.height()/4, rec.width()/8, dicePointP);
    }
    
    private void drawFour(RectF rec, Canvas can,Paint p)
    {
    	can.drawRoundRect(rec, RectRadius, RectRadius, p);
    	can.drawCircle(rec.left+rec.width()/4, rec.top+rec.height()/4, rec.width()/8, dicePointP);
    	can.drawCircle(rec.right-rec.width()/4, rec.top+rec.height()/4, rec.width()/8, dicePointP);
    	can.drawCircle(rec.left+rec.width()/4, rec.bottom-rec.height()/4, rec.width()/8, dicePointP);
    	can.drawCircle(rec.right-rec.width()/4, rec.bottom-rec.height()/4, rec.width()/8, dicePointP);
    }
    
    private void drawFive(RectF rec, Canvas can,Paint p)
    {
    	can.drawRoundRect(rec, RectRadius, RectRadius, p);
    	can.drawCircle(rec.left+rec.width()/4, rec.top+rec.height()/4, rec.width()/8, dicePointP);
    	can.drawCircle(rec.right-rec.width()/4, rec.top+rec.height()/4, rec.width()/8, dicePointP);
    	can.drawCircle(rec.centerX(), rec.centerY(), rec.width()/8, dicePointP);
    	can.drawCircle(rec.left+rec.width()/4, rec.bottom-rec.height()/4, rec.width()/8, dicePointP);
    	can.drawCircle(rec.right-rec.width()/4, rec.bottom-rec.height()/4, rec.width()/8, dicePointP);
    }
    
    private void drawSix(RectF rec, Canvas can,Paint p)
    {
    	can.drawRoundRect(rec, RectRadius, RectRadius, p);
    	can.drawCircle(rec.left+rec.width()/3, rec.top+rec.height()/4, rec.width()/8, dicePointP);
    	can.drawCircle(rec.right-rec.width()/3, rec.top+rec.height()/4, rec.width()/8, dicePointP);
    	can.drawCircle(rec.left+rec.width()/3, rec.centerY(), rec.width()/8, dicePointP);
    	can.drawCircle(rec.right-rec.width()/3, rec.centerY(), rec.width()/8, dicePointP);
    	can.drawCircle(rec.left+rec.width()/3, rec.bottom-rec.height()/4, rec.width()/8, dicePointP);
    	can.drawCircle(rec.right-rec.width()/3, rec.bottom-rec.height()/4, rec.width()/8, dicePointP);
    }
    
    private final float RectRadius=15.0f;
    
    private static List<Integer> Initiator=new ArrayList<Integer>();   
    
    private static List<Integer> Reciever=new ArrayList<Integer>();
    
    private static int InitiatorColor;
    
    private static int RecieverColor;
    
    private static int isWin;	//0:draw 1:win -1:lose
    
    private static int InitiatortotalNum=10;
    
    private static int RecievertotalNum=15;
    
    public static void setBattleSides(List<Integer> lfrom,int fromColor,List<Integer>lto,int toColor)
    {
    	Initiator.clear();
    	Reciever.clear();
    	Initiator=lfrom;
    	Reciever=lto;
    	InitiatorColor=fromColor;
    	RecieverColor=toColor;
    	
    	InitiatortotalNum=0;
    	RecievertotalNum=0;
    	for(int i:lfrom)
    	{
    		InitiatortotalNum+=i;
    	}
    	for(int i:lto)
    	{
    		RecievertotalNum+=i;
    	}
    	isWin=InitiatortotalNum==RecievertotalNum?0:(InitiatortotalNum>RecievertotalNum?1:-1);
    }
}

