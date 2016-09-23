package com.qiang.testspecialview;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.test.datastructure.HexMap;
import com.test.datastructure.HexRegion;
import com.test.datastructure.HexTile;

public class Player {
	public Player(int c)
	{
		color=c;
		pickedHR=null;
		ran=new Random();
		isCPU=false;
	}
	
	public boolean isCPU;
	
	protected int color;
	protected static HexMap hexmap;
	
	public static void setMap(HexMap hm)
	{
		hexmap=hm;
	}
	
	protected HexRegion pickedHR;
	
	public HexRegion getCurSelectHR()
	{
		return pickedHR;
	}
	
	public void selectRegion(HexRegion hr)
	{
		PickResult pr;
		if(pickedHR==null)	//第一次选
		{
			if(hr.color!=this.color)	//颜色不对
			{
				pr= PickResult.wrongColor;
			}
			else	//颜色正确，选中该区域
			{
				if(hr.getDiceNum()<=1)	//该region的色子数为1
				{
					pr= PickResult.wrongNum;
				}
				else
				{
					pickedHR=hr;
					pr= PickResult.firstPick;
				}
			}
		}
		else	//第二次选
		{
			if(hr.color==this.color)	//第二次依然选的是自己的颜色
			{
				if(hr!=pickedHR)	//选的是自己的另一块region
				{
					if(hr.getDiceNum()<=1)	//该region的色子数为1
					{
						pr= PickResult.wrongNum;
					}
					else
					{
						pickedHR=hr;
						pr= PickResult.changePick;
					}
				}
				else	//选的就是当前的区域，取消选中
				{
					pickedHR=null;
					pr= PickResult.cancelPick;
				}
			}
			else	//第二次选中对手的颜色
			{
				if(pickedHR.adjacentRegionList.contains(hr))	//两块区域相邻
				{
					Battle(pickedHR,hr);	
					pickedHR=null;
					pr= PickResult.secondPick;
				}
				else
				{
					pr= PickResult.remotePick;
				}
			}
		}
		rprl.onRegionPickResult(pr);
	}
	
	private Random ran;
	
	protected void Battle(HexRegion hr1, HexRegion hr2) {
		// TODO Auto-generated method stub
		int sum1=0;
		List<Integer> Num1=new ArrayList<Integer>();
		for(int i=0;i<hr1.getDiceNum();i++)
		{
			int result=ran.nextInt(6)+1;
			Num1.add(Integer.valueOf(result));
			sum1+=result;
		}
		
		int sum2=0;
		List<Integer> Num2=new ArrayList<Integer>();
		for(int i=0;i<hr2.getDiceNum();i++)
		{
			int result=ran.nextInt(6)+1;
			Num2.add(Integer.valueOf(result));
			sum2+=result;
		}
		
		brl.onBattleResult(Num1, hr1, Num2, hr2);
	}

	public enum PickResult
	{
		wrongColor,	//第一次选，选中了错误的颜色
		wrongNum,	//色子数为1
		firstPick,	//第一次选，选中了自己的颜色
		changePick,	//第二次选，选的也是自己的颜色
		cancelPick,	//第二次选，选的是当前的region，取消region闪烁
		remotePick,	//第二次选，选的是对手的region，但是太远了，选择无效
		secondPick;	//第二次选，选的是当前region附近的对手的region，战斗开始
	}
	
	public void setOnRegionPickListener(onRegionPickResultListener l)
	{
		rprl=l;
	}
	
	public void setonBattleResultListener(onBattleResultListener l)
	{
		brl=l;
	}
	
	protected onRegionPickResultListener rprl;
	protected onBattleResultListener brl;
	
	public interface onRegionPickResultListener
	{
		public void onRegionPickResult(PickResult pr);
	}
	
	public interface onBattleResultListener
	{
		public void onBattleResult(List<Integer> Num1,HexRegion hr1,List<Integer> Num2,HexRegion hr2);
	}
}


