package com.qiang.testspecialview;


import java.util.List;

import com.qiang.testspecialview.SpecailView.onDiceResultListener;
import com.qiang.utils.EnumUtil;

import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class MainActivity extends Activity implements onDiceResultListener,OnClickListener{

	private final Handler mHandler = new Handler();	
    private Runnable runnable=new Runnable(){
	    @Override
	    public void run() {
	    // TODO Auto-generated method stub
	    	pop.dismiss();
	    }
    };
	

	private ViewGroup vg;
	
	private View view;
	private PopupWindow pop;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  
        
        vg =(ViewGroup)findViewById(R.id.background);
        vg.setBackgroundResource(R.drawable.bg);
        
        View fragmentTurn=findViewById(R.id.framentturn);
        View fragmentEdit=findViewById(R.id.framentmapedit);
        
        SpecailView sv=(SpecailView)findViewById(R.id.specail_view);
        sv.setDiceResultListener(this);
        
        Button bt=(Button)findViewById(R.id.bt_endturn);
        bt.setOnClickListener(sv);
        /*bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initPopupWindow();
			}
		});*/
        
        FragmentEditBar fragment =(FragmentEditBar) getFragmentManager().findFragmentById(R.id.framentmapedit);  
        sv.setShowBarListener(fragment);
        fragment.setMenuSelectedListener(sv);
        
        FragmentTurnBar fragTurn =(FragmentTurnBar) getFragmentManager().findFragmentById(R.id.framentturn);
        sv.setAdjacentRegionCountListener(fragTurn);
        
        int type=getIntent().getExtras().getInt("type");
        
        if(type==EnumUtil.state_play)
        {
        	EnumUtil.setCurState(type);
        	fragmentTurn.setVisibility(View.VISIBLE);
        	fragmentEdit.setVisibility(View.INVISIBLE);
        	bt.setVisibility(View.VISIBLE);
        }
        else if(type==EnumUtil.state_edit)
        {
        	EnumUtil.setCurState(type);
        	fragmentTurn.setVisibility(View.INVISIBLE);
        	fragmentEdit.setVisibility(View.INVISIBLE);
        	bt.setVisibility(View.INVISIBLE);
        }
    }
    
    public void setBackGround(boolean enable)
    {
    	vg.setBackgroundResource(enable?R.drawable.bg:R.drawable.bg_disable);
    }
    
    @SuppressWarnings("deprecation")
	private void showPopupWindow(){
        view = this.getLayoutInflater().inflate(R.layout.popup_layout, null);
        view.setOnClickListener(this);
        
        pop = new PopupWindow(view, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        pop.setOutsideTouchable(false);
        pop.setFocusable(true);
        pop.showAtLocation(view, Gravity.CENTER, 0, 0);
        //mHandler.sendEmptyMessageDelayed(0, 5000);  
        mHandler.postDelayed(runnable, 1000);
    }
    
	public interface onMenuSelectedListener
	{
		public void OnSelectResult(int result);
	}
	
	public interface onShowBarListener
    {
    	public void onShowBar();
    }
    
	public interface onAdjacentRegionCountListener
	{
		public void onAdjacentRegionCount(List<Integer> numList);
	}

	@Override
	public void onDiceResult() {
		// TODO Auto-generated method stub
		showPopupWindow();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(pop.isShowing())
		{
			pop.dismiss();
		}
	}
	
	
}
