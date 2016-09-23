package com.test.datastructure;

import com.qiang.utils.ConstUtil;

import android.graphics.Color;

public class HexTile {
	public int index;	//start from 1
	public int X;
	public int Y;
	public int color;
	public int belongRegionIndex;
	
	public float getIndexX()
	{
		if(upLayer())
		{
			return index%69;
		}
		else
		{
			return index%69-35+0.5f;
		}
			
	}
	
	public float getIndexY()
	{
		if(upLayer())
		{
			return (index-1)/69*2;
		}
		else
		{
			return (index-1)/69*2+1;
		}
	}
	
	public boolean upLayer()
	{
		return ((index-1)%69+1)<=ConstUtil.COLUMN_COUNT;
	}
	 
	public boolean isAjacentTo(HexTile ht)
	{
		if(Math.abs(getIndexX()-ht.getIndexX())<=1&&Math.abs(getIndexY()-ht.getIndexY())<=1)
		{
			return true;
		}
		return false;
	}
}
