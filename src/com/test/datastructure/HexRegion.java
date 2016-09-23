package com.test.datastructure;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;

public class HexRegion {
	public int index;
	public List<HexTile> HexTileList=new ArrayList<HexTile>();
	public int color;
	
	private int DiceNum;
			
	public int CenterHexTileNo;
	
	public List<HexRegion> adjacentRegionList=new ArrayList<HexRegion>();
	
	public void setDiceNum(int num)
	{
		DiceNum=num;
	}
	
	public int getDiceNum()
	{
		return DiceNum;
	}
	
	public void getCenterHexTileNo()
	{
		float Xweight=0;
		float Yweight=0;
		float Xsum=0;
		float Ysum=0;
		float sum=0;
		for(HexTile ht:HexTileList)
		{
			Xsum+=ht.getIndexX();
			Ysum+=ht.getIndexY();
			sum++;
		}
		Xweight=Xsum/sum;
		Yweight=Ysum/sum;
		
		float dis=1000;
		int centerIndex=0;
		for(HexTile ht:HexTileList)
		{
			float offX=Math.abs(ht.getIndexX()-Xweight);
			float offY=Math.abs(ht.getIndexY()-Yweight);
			if(offX+offY<dis)
			{
				dis=offX+offY;
				centerIndex=ht.index;
			}
		}
		CenterHexTileNo= centerIndex;
	}
}
