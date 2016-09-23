package com.qiang.utils;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class StyleUtil {
	private static LinearLayout.LayoutParams mParamSelected;
	private static LinearLayout.LayoutParams mParamDeSelected;
	private static final float SelectedWeigth=3.0f;
	private static final float DeSelectedWeigth=2.0f;
	
	private static final int margin=12;
	
	private static LinearLayout.LayoutParams getSelectedLayoutParams(Context context)
	{		
		mParamSelected=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,SelectedWeigth);
		return mParamSelected;
	}
	
	private static LinearLayout.LayoutParams getDeSelectedLayoutParams(Context context)
	{		
		mParamDeSelected=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,DeSelectedWeigth);
		mParamDeSelected.setMargins(0,DensityUtil.dip2px(context, margin),0, DensityUtil.dip2px(context, margin));
		return mParamDeSelected;
	}
	
	public static boolean setSelectedLayout(View v)
	{
		v.setLayoutParams(getSelectedLayoutParams(v.getContext()));
		return true;
	}
	
	public static boolean setDeSelectedLayout(View v)
	{
		v.setLayoutParams(getDeSelectedLayoutParams(v.getContext()));
		return true;
	}
}
