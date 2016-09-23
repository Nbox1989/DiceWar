package com.qiang.utils;

import com.thoughtworks.xstream.converters.extended.ColorConverter;

import android.graphics.Color;

public class EnumUtil {
	public static final int state_edit=0;
	public static final int state_play=1;
	
	private static int curState;
		
	public static int getCurState()
	{
		return curState;
	}
	
	public static boolean setCurState(int state)
	{
		curState=state;
		return true;
	}
	
	public static final int turn_blue=1;
	public static final int turn_red=2;
	public static final int turn_pink=3;
	public static final int turn_yellow=4;
	public static final int turn_green=5;
	public static final int turn_purple=6;
	public static final int turn_orange=7;
	public static final int turn_cyan=8;
	
	private static int curTurn;
	
	public static int getCurTurn()
	{
		return curTurn;
	}
	
	public static boolean setCurTurn(int turn)
	{
		if(turn>0&&turn<9)
		{
			curTurn=turn;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean nextTurn()
	{
		curTurn=(curTurn)%8+1;
		return true;
	}
	
	public static int getColorAt(int index)
	{
		if(index>=1&&index<=8)
		{
			return mColor[index-1];
		}
		else
		{
			return Color.BLACK;
		}
	}
	
	private static int[] mColor=new int[]{Color.BLUE,Color.RED,0xFFFF69B4,Color.YELLOW,
		Color.GREEN,0xFF800080,0xFFFFA500,Color.CYAN};
}
