package com.qiang.testspecialview;

import java.util.List;

import android.os.Handler;

import com.test.datastructure.HexMap;
import com.test.datastructure.HexRegion;

public class CPUPlayer extends Player{

	@Override
	public void selectRegion(HexRegion hr) {
		// TODO Auto-generated method stub
		super.selectRegion(hr);
	}

	public CPUPlayer(int c) {
		super(c);
		isCPU=true;
	}
	
	protected HexRegion ownRegion;
	
	protected HexRegion enemyRegion;
	
	public boolean CalPickSides()
	{
		List<HexRegion> mHRList=hexmap.getRegionListByColor(color);
		for(HexRegion mhr:mHRList)
		{
			for(HexRegion hr:mhr.adjacentRegionList)
			{
				if(hr.color!=mhr.color&&hr.getDiceNum()<mhr.getDiceNum())
				{
					ownRegion=mhr;
					enemyRegion=hr;
					return true;
				}
			}
		}
		return false;
	}
	
	public void start()
	{	
		if(b)
		{
			if(!CalPickSides())
			{
				sv.EndTurn();
				return;
			}		
			sv.getChildAt(ownRegion.HexTileList.get(0).index-1).performClick();
		}
		else
		{
			sv.getChildAt(enemyRegion.HexTileList.get(0).index-1).performClick();
		}
		b=!b;
    	mHandler.postDelayed(runnable, 1250);			
	}
	
	private boolean b=true;
	
	private final Handler mHandler = new Handler();	
    private Runnable runnable=new Runnable(){
	    @Override
	    public void run() {
	    // TODO Auto-generated method stub
	    	start();
	    }
    };
    
    public SpecailView sv;
}
