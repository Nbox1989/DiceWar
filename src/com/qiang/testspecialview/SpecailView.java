package com.qiang.testspecialview;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.qiang.testspecialview.MainActivity.onAdjacentRegionCountListener;
import com.qiang.testspecialview.MainActivity.onMenuSelectedListener;
import com.qiang.testspecialview.MainActivity.onShowBarListener;
import com.qiang.testspecialview.Player.PickResult;
import com.qiang.testspecialview.Player.onBattleResultListener;
import com.qiang.testspecialview.Player.onRegionPickResultListener;
import com.qiang.utils.ConstUtil;
import com.qiang.utils.EnumUtil;
import com.test.datastructure.HexMap;
import com.test.datastructure.HexRegion;
import com.test.datastructure.HexTile;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class SpecailView extends ViewGroup implements onMenuSelectedListener,View.OnClickListener
		,onRegionPickResultListener,onBattleResultListener{
   
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		init();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.dispatchDraw(canvas);
		if(EnumUtil.getCurState()==EnumUtil.state_play)
		{
			if(hexmap!=null)
			{
				for(HexRegion hr :hexmap.HexRegionList)
				{
					View v=getChildAt(hr.CenterHexTileNo-1);
					int left=v.getLeft();
					int right=(int)(v.getLeft()+v.getWidth()*1.5f);
					int bottom=v.getBottom();
					int top=(int)(bottom-v.getWidth()*1.5f);
					canvas.drawBitmap(bmp, new Rect(0, 0, bmp.getWidth(), bmp.getHeight()), 
								new Rect(left,top,right,bottom), paint);				
					canvas.drawText((hr.getDiceNum()>9?" ":"  ")+String.valueOf(hr.getDiceNum()), left,top+v.getHeight()/4, paint);
				}
			}
		}
	}

	private static final String TAG = "SpecailView";

    private int childRadius;

    private int childWidth;
    private int childHeight;
    private int childHeightDif;//每层child的高度差，是高度的四分之三
    
    private HexMap hexmap;
    
    private int centerX,centerY;
    
    private float lastX;
    private float lastY;
    private float dx;
    private float dy;
    
    private boolean isMoved=false;
    
    private Random ran=new Random();
    
    private onShowBarListener mBarListener;    
    
    private onDiceResultListener mDiceListener;
    
    private Paint paint;
    
    private Bitmap bmp;

    private List<Player> playerList;
    
    private Player curPlayer;
    
    public SpecailView(Context context) {
        super(context);
        Log.i(TAG, "SpecailView()");
        //init();
    }

    public SpecailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Log.i(TAG, "SpecailView( , , )");
        //init();
    }

    public SpecailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "SpecailView( , )");
        //init();
    }

    private void init() {
		// TODO Auto-generated method stub
    	this.setBackgroundColor(Color.TRANSPARENT);
   		
   		if(EnumUtil.getCurState()==EnumUtil.state_edit)
   		{
   			initMapToEdit();
	   		initSpecialButtonsToEdit();
	   		setOnItemClick(new EditClickListener());  		
   		}
   		else if(EnumUtil.getCurState()==EnumUtil.state_play)
   		{
   			initPaints();
	        initMapForPlay();
	   		initSpecialButtonsForPlay();
	   		EnumUtil.setCurTurn(EnumUtil.turn_blue);
	   		setOnItemClick(new PlayClickListener());  
   		}   		
	}
    
    private void initPaints() {
		// TODO Auto-generated method stub
		paint=new Paint();
		paint.setTextSize(48.0f);	
		paint.setFakeBoldText(true);
		paint.setTextSkewX(-0.3f);//斜体 负数表示右斜
   		bmp=BitmapFactory.decodeResource(getResources(), R.drawable.dice11);  
	}

	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);

        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(wSize, hSize);

        centerX = wSize / 2;
        centerY = hSize / 2;
        childWidth = (wSize - ConstUtil.PADDING * 2) / ConstUtil.COLUMN_COUNT;
        childRadius=childWidth/2;
        childHeight = (int)(childRadius * Math.sqrt(3))*4/3;
        childHeightDif=childHeight*3/4;
        
        for (int index = 0; index < ConstUtil.CHILD_COUNT; index++) {
            View child = getChildAt(index);
            // measure
            child.measure(childWidth, childHeight);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //@Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {      	
    	int childLeft,childTop,indexX,indexY;
    	for(int i=0;i<ConstUtil.CHILD_COUNT;i++)
    	{    		
    		//将地图视为每两层一个单元，判断sb在每个单元的上层或者下层
    		boolean topLayer=i%(2*ConstUtil.COLUMN_COUNT-1)<ConstUtil.COLUMN_COUNT?true:false;
    		if(topLayer)
    		{
	    		indexX=i%(2*ConstUtil.COLUMN_COUNT-1);
	    		indexY=i/(2*ConstUtil.COLUMN_COUNT-1)*2;
	    		childLeft=childWidth*indexX;
	    		childTop=childHeightDif*indexY;
    		}
    		else
    		{
    			indexX=i%(2*ConstUtil.COLUMN_COUNT-1)-ConstUtil.COLUMN_COUNT;
    			indexY=i/(2*ConstUtil.COLUMN_COUNT-1)*2+1;
	    		childLeft=(int)(childWidth*(indexX+0.5));
	    		childTop=childHeightDif*indexY;
    		}
            getChildAt(i).layout(ConstUtil.PADDING+childLeft, childTop,ConstUtil.PADDING+ childLeft + childWidth, childTop + childHeight);
    	}
    }
    
    public void setOnItemClick(SpecailButton.OnClickListener l) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            ((SpecailButton)getChildAt(i)).setOnClickListener(l);
        }
    }
   
    private void initSpecialButtonsForPlay()
    {
        for (int i = 0; i < ConstUtil.CHILD_COUNT; i++) {
            SpecailButton sb=new SpecailButton(getContext());
            sb.setviewIndex(i+1);
            sb.setGravity(Gravity.CENTER);
            //sb.setBackgroundResource(R.drawable.logo);
            HexRegion hr=hexmap.findRegionByTileId(i+1);
            if(hr!=null)
            {
            	sb.setcolorIndex(hr.color);
            	sb.setRegionIndex(hr.index);            	
            	sb.setBorder(255);
            	for(HexTile ht :hr.HexTileList)
            	{
	            	if(ht.index==sb.getviewIndex()-35)
					{
						sb.setBoldLeftTop(false);
					}
					if(ht.index==sb.getviewIndex()-34)
					{
						sb.setBoldRightTop(false);
					}
					if(ht.index==sb.getviewIndex()-1)
					{
						sb.setBoldLeft(false);
					}
					if(ht.index==sb.getviewIndex()+1)
					{
						sb.setBoldRight(false);
					}
					if(ht.index==sb.getviewIndex()+34)
					{
						sb.setBoldLeftBottom(false);
					}
					if(ht.index==sb.getviewIndex()+35)
					{
						sb.setBoldRightBottom(false);
					}
            	}
            	
            }         
            addView(sb);
            sb.setOnTouchListener(new PlayTouchListener());
        }
    }
    
    private void initSpecialButtonsToEdit()
    {
    	for (int i = 0; i < ConstUtil.CHILD_COUNT; i++) {
            SpecailButton sb=new SpecailButton(getContext());
            sb.setviewIndex(i+1);
            sb.setGravity(Gravity.CENTER);
            //sb.setBackgroundResource(R.drawable.logo);
            HexRegion hr=hexmap.findRegionByTileId(i+1);
            if(hr!=null)
            {
            	sb.setcolorIndex(hr.color);
            	sb.setRegionIndex(hr.index);
            }         
            sb.setBorderForEdit();
            addView(sb);
            sb.setOnTouchListener(new EditTouchListener());
        }
    }
    
    private void initMapForPlay()
    {
    	try
        {
        	hexmap=HexMap.FileToMap(getContext(), "Map1.xml");
        	

        	for (HexRegion hexRegion : hexmap.HexRegionList) {
        		hexRegion.color=ran.nextInt(8)+1;
        		hexRegion.getCenterHexTileNo();
        		hexRegion.adjacentRegionList=new ArrayList<HexRegion>();
        		hexRegion.setDiceNum(ran.nextInt(4)+1);
    		}
        	hexmap.getAdjacentRegionList();
        	showAdjacentRegionCount();
        	initPlayers();
        	
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    }
    
    private void initPlayers()
    {
    	playerList=new ArrayList<Player>();
    	for(int i=0;i<8;i++)
    	{
    		if(i==0)
    		{
	    		Player p=new Player(i+1);
	    		p.setOnRegionPickListener(this);
	    		p.setonBattleResultListener(this);
	    		playerList.add(p);
    		}
    		else
    		{
    			Player p=new CPUPlayer(i+1);
        		p.setOnRegionPickListener(this);
        		p.setonBattleResultListener(this);
        		((CPUPlayer)p).sv=this;
        		playerList.add(p);
    		}
    	}
    	curPlayer=playerList.get(0);
    	Player.setMap(hexmap);
    }
    
    private void initMapToEdit()
    {   
    	hexmap=new HexMap();
    }

    public void performLongTouch(View v)
    {    	
    	this.setViewEnable(false);    	
    	mBarListener.onShowBar();
    	
    	for(HexRegion hr :hexmap.HexRegionList)
		{
			for(HexTile ht:hr.HexTileList)
			{
				if(ht.index==((SpecailButton)v).getviewIndex())
				{
					selectedRegionIndex=hr.index;
					getSbInHexRegion(hr);
					handler.postDelayed(runnable, 500);
					return;
				}
			}
		}
    }
    
    private void getSbInHexRegion(HexRegion hr)
    {
    	sbListInHexRegion.clear();
    	for(HexTile ht :hr.HexTileList)
    	{
    		sbListInHexRegion.add((SpecailButton)getChildAt(ht.index-1));
    	}
    }

    //for select region issue
	private List<SpecailButton> sbListInHexRegion=new ArrayList<SpecailButton>();
    
    private int selectedRegionIndex=-1;
    
    private boolean shine=false;    
    //end
    
    private HexRegion pickedRegion;
    
	public class PlayClickListener implements SpecailButton.OnClickListener
	{
		@Override
		public void onClick(View v, boolean checked) {
			// TODO Auto-generated method stub
			getPickedRegion(v);
			if(pickedRegion!=null)
			{
				try
				{
					curPlayer.selectRegion(pickedRegion);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	private void getPickedRegion(View v)
	{
		for(HexRegion hr :hexmap.HexRegionList)
		{
			for(HexTile ht:hr.HexTileList)
			{
				if(ht.index==((SpecailButton)v).getviewIndex())
				{
					pickedRegion = hr;
					return;
				}
			}
		}
		pickedRegion=null;
	}		
	
	private boolean regionSelected=false;
		
	public class EditClickListener implements SpecailButton.OnClickListener
	{	
		@Override
		public void onClick(View v, boolean checked) {
			// TODO Auto-generated method stub
			for(HexRegion hr :hexmap.HexRegionList)
			{
				for(HexTile ht:hr.HexTileList)
				{
					if(ht.index==((SpecailButton)v).getviewIndex())
					{
						return;	//该sb已经属于某个region了，单击事件取消
					}
				}
			}
			((SpecailButton)v).SwitchBackGroundForEdit();
			if(!selectedSBList.contains(v))
			{
				selectedSBList.add((SpecailButton)v);
			}
			else
			{
				selectedSBList.remove(v);
			}
		}
	}
	
	public class PlayTouchListener implements OnTouchListener
	{
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			int action = event.getAction();
	        switch (action) {
	        case MotionEvent.ACTION_DOWN:
	            lastX = event.getRawX();
	            lastY = event.getRawY();
	            break;
	        case MotionEvent.ACTION_UP:
	        	if(!isMoved)
	        	{
	        		v.performClick();
	        	}
	        	isMoved=false;
	            break;
	        case MotionEvent.ACTION_MOVE:
	            dx = event.getRawX() -lastX;
	            dy = event.getRawY() -lastY;
	            SpecailView.this.setX(SpecailView.this.getX()+dx);
	            SpecailView.this.setY(SpecailView.this.getY()+dy);
	            lastX = event.getRawX();
	            lastY = event.getRawY();
	            if(dx>3||dy>3)
	            {
	            	isMoved=true;
	            }
	            break;
	        }
	        return true;
		}
	}
	
	public class EditTouchListener implements OnTouchListener
	{
		private long touchDownTime;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			int action = event.getAction();
	        switch (action) {
	        case MotionEvent.ACTION_DOWN:
	            lastX = event.getRawX();
	            lastY = event.getRawY();
	            touchDownTime=System.currentTimeMillis();
	            break;
	        case MotionEvent.ACTION_UP:
	        	if(!isMoved)
	        	{
	        		if(System.currentTimeMillis()-touchDownTime>350)
	        		{
	        			//v.setBackgroundResource(R.drawable.logo_black_1);
	        			performLongTouch(v);
	        		}
	        		else	//如果长按的话就不考虑点击事件
	        		{
	        			v.performClick();
	        		}
	        	}
	        	isMoved=false;
	            break;
	        case MotionEvent.ACTION_MOVE:
	            dx = event.getRawX() -lastX;
	            dy = event.getRawY() -lastY;
	            SpecailView.this.setX(SpecailView.this.getX()+dx);
	            SpecailView.this.setY(SpecailView.this.getY()+dy);
	            lastX = event.getRawX();
	            lastY = event.getRawY();
	            if(dx>3||dy>3)	//只要移动了就表示不考虑点击，不考虑长按
	            {
	            	isMoved=true;
	            }
	            break;	            
	        }
	        return true;
		}
		
	}
	
	
	
	public void setShowBarListener(onShowBarListener sb)
	{
		this.mBarListener=sb;
	}	

	public void setAdjacentRegionCountListener(onAdjacentRegionCountListener al)
	{
		this.arcl=al;
	}
	
	private onAdjacentRegionCountListener arcl;
	
	private void showAdjacentRegionCount()
	{
		List<Integer> numList=new ArrayList<Integer>();
		for(int color=1;color<9;color++)
		{
			numList.add(Integer.valueOf(hexmap.getMaxAdjacentRegions(color)));
		}
		arcl.onAdjacentRegionCount(numList);
	}
	
	@Override
	public void OnSelectResult(int result) {
		// TODO Auto-generated method stub
		switch (result)
		{
			case 0:	//cancel
				operCancel();
				break;
			case 1:	//delete region
				deleteRegion();
				break;
			case 2:	//save a region
				saveRegion();
				break;
			case 3:	//save map
				saveMap();
				break;
			default:
				operCancel();
				break;
		}
		this.setViewEnable(true);
	}
	
	

	private void saveRegion() {
		// TODO Auto-generated method stub
		int regionIndex=1;	//reigon的index
		int insertLocation=0;	//地图中的region按照index有序排列，用户可能删除一些region，所以，序号数是不大于index的
		for(HexRegion hr :hexmap.HexRegionList)
		{
			if(hr.index==regionIndex)
			{
				regionIndex++;
			}
			else if (hr.index>regionIndex)
			{
				break;
			}
			insertLocation++;
		}
		HexRegion hr=new HexRegion();
		hr.index=regionIndex;
		for(SpecailButton sb :selectedSBList)
		{
			HexTile ht=new HexTile();
			ht.index=sb.getviewIndex();
			ht.belongRegionIndex=regionIndex;
			hr.HexTileList.add(ht);
			sb.setRegionIndex(regionIndex);
			sb.setBackgroundResource(R.drawable.logo_black_1);
			sb.setBorder(255);
			for(SpecailButton sb1 :selectedSBList)
			{
				if(sb1.getviewIndex()==sb.getviewIndex()-35)
				{
					sb.setBoldLeftTop(false);
				}
				if(sb1.getviewIndex()==sb.getviewIndex()-34)
				{
					sb.setBoldRightTop(false);
				}
				if(sb1.getviewIndex()==sb.getviewIndex()-1)
				{
					sb.setBoldLeft(false);
				}
				if(sb1.getviewIndex()==sb.getviewIndex()+1)
				{
					sb.setBoldRight(false);
				}
				if(sb1.getviewIndex()==sb.getviewIndex()+34)
				{
					sb.setBoldLeftBottom(false);
				}
				if(sb1.getviewIndex()==sb.getviewIndex()+35)
				{
					sb.setBoldRightBottom(false);
				}
			}
		}
		selectedSBList=new ArrayList<SpecailButton>();		
		hexmap.HexRegionList.add(insertLocation, hr);
		
	}
	
	private void deleteRegion()
	{
		
	}
	
	private void saveMap()
	{
		try
		{
			HexMap.MapToFile(getContext(), hexmap,"tempmap.xml");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void operCancel() {
		// TODO Auto-generated method stub
		handler.removeCallbacks(runnable);
		sbListInHexRegion=new ArrayList<SpecailButton>();	    
		selectedRegionIndex=-1;	    
	    shine=false; 
	}
	
	private List<SpecailButton> selectedSBList=new ArrayList<SpecailButton>();

	private void setViewEnable(boolean enable)
	{
		for(int i =0;i<this.getChildCount();i++)
		{
			getChildAt(i).setEnabled(enable);
		}
	}
			
    private Handler handler=new Handler();
    private Runnable runnable=new Runnable(){
	    @Override
	    public void run() {
	    // TODO Auto-generated method stub
	    	if(EnumUtil.getCurState()==EnumUtil.state_edit)
	    	{
		    	shine=!shine;
		    	for(SpecailButton sb: sbListInHexRegion)
		    	{        		
		    		sb.setBackgroundResource(shine?R.drawable.logo_black:R.drawable.logo_black_1);
		    	}
	    	}
	    	else if(EnumUtil.getCurState()==EnumUtil.state_play)
	    	{
	    		for(SpecailButton sb: sbListInHexRegion)
		    	{   
	    			sb.SwitchShineBackGround();
		    	}
	    	}
		    handler.postDelayed(this, 400);
	    }
    };
    
    private void stopTimer()
    {
    	handler.removeCallbacks(runnable);
		for(SpecailButton sb :sbListInHexRegion)
		{
			sb.setBackgroundResource(sb.getDrawRes());
		}
		sbListInHexRegion.removeAll(sbListInHexRegion);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		EndTurn();
	}

	public void EndTurn() {
		// TODO Auto-generated method stub
		//this.setEnabled(true);
		stopTimer();
		regionSelected=false;
		getBonus();
		EnumUtil.nextTurn();
		
		curPlayer=playerList.get((playerList.indexOf(curPlayer)+1)%8);
		
		showAdjacentRegionCount();
		try
		{
			if(curPlayer.isCPU)
			{
				this.setEnabled(false);
				((CPUPlayer)curPlayer).start();
				//EndTurn();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(hexmap.getRegionListByColor(EnumUtil.getCurTurn()).size()==0)
		{
			EndTurn();
		}
	}

	private void getBonus() {
		// TODO Auto-generated method stub
		try
		{
		int color=EnumUtil.getCurTurn();
		List<HexRegion> hrList=hexmap.getRegionListByColor(color);
		if(hrList==null||hrList.isEmpty())
		{
			return;
		}
		int bonus=hexmap.getMaxAdjacentRegions(color);
		int size=hrList==null?0:hrList.size();
		while (bonus>0&&size>0)
		{
			int index=ran.nextInt(size);
			if(hrList.get(index).getDiceNum()>=6)
			{
				hrList.remove(index);				
				size--;
				continue;
			}
			else
			{
				hrList.get(index).setDiceNum(hrList.get(index).getDiceNum()+1);
				size--;
				bonus--;
			}
		}
		hrList.clear();
		invalidate();
		try
		{
			Thread.sleep(200);
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setDiceResultListener(onDiceResultListener drl)
	{
		mDiceListener=drl;
	}
	
	public interface onDiceResultListener
	{
		public void onDiceResult();
	}

	@Override
	public void onRegionPickResult(PickResult pr) {
		// TODO Auto-generated method stub
		switch (pr)
		{
			case wrongColor: 	//第一次选，选中了错误的颜色或者region色子数为1
				break;
			case wrongNum:		//色子数为1
				break;
			case firstPick:		//第一次选，选中了自己的颜色
				pickARegion();
				break;
			case changePick:	//第二次选，选的也是自己的颜色
				changeRegion();
				break;
			case cancelPick:	//第二次选，选的是当前的region，取消region闪烁
				cancelPick();
				break;
			case remotePick:	//第二次选，选的是对手的region，但是太远了，选择无效
				break;
			case secondPick:	//第二次选，选的是当前region附近的对手的region，战斗开始
				//Battle();
				break;
		}
	}

	private void cancelPick() {
		stopTimer();
		pickedRegion=null;
		sbListInHexRegion.clear();
	}

	private void changeRegion() {
		stopTimer();
		pickARegion();
	}

	private void pickARegion() {
		getSbInHexRegion(pickedRegion);
		handler.postDelayed(runnable, 0);
	}

	@Override
	public void onBattleResult(List<Integer> Num1, HexRegion hr1,
			List<Integer> Num2, HexRegion hr2) {
		// TODO Auto-generated method stub		
		PopView.setBattleSides(Num1, hr1.color, Num2, hr2.color);
		mDiceListener.onDiceResult();
		stopTimer();
		
		int sum1=0;
		int sum2=0;
		for(Integer i:Num1)
		{
			sum1+=i.intValue();
		}
		
		for(Integer i:Num2)
		{
			sum2+=i.intValue();
		}
		
		if(sum1<=sum2)
		{
			loseto(hr1, hr2);
		}
		else if(sum1==sum2)
		{
			draw(hr1, hr2);;
		}
		else
		{
			defeat(hr1, hr2);
		}
		this.invalidate();
	}
	
	private void defeat(HexRegion hr1,HexRegion hr2)
	{
		hr2.setDiceNum(hr1.getDiceNum()-1);			
		hr2.color=hr1.color;
		for(HexTile ht: hr2.HexTileList)
		{
			((SpecailButton)(getChildAt(ht.index-1))).setcolorIndex(hr2.color);
			getChildAt(ht.index-1).invalidate();
		}
		hr1.setDiceNum(1);
		showAdjacentRegionCount();
	}
	
	private void draw(HexRegion hr1,HexRegion hr2)
	{
		if(hr1.getDiceNum()==hr2.getDiceNum())
		{
			hr1.setDiceNum(hr1.getDiceNum()/2);
		}
		else if(hr1.getDiceNum()<hr2.getDiceNum())
		{
			hr1.setDiceNum(hr1.getDiceNum()-1);
		}
		else
		{
			hr1.setDiceNum(1);
		}
	}
	
	private void loseto(HexRegion hr1,HexRegion hr2)
	{
		hr1.setDiceNum(1);
	}
}

	
