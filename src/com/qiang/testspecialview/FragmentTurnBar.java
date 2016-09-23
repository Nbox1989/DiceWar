package com.qiang.testspecialview;

import java.util.List;
import java.util.zip.Inflater;

import com.qiang.testspecialview.MainActivity.onAdjacentRegionCountListener;
import com.qiang.utils.DensityUtil;
import com.qiang.utils.EnumUtil;
import com.qiang.utils.StyleUtil;
import com.thoughtworks.xstream.core.util.SelfStreamingInstanceChecker;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentTurnBar extends Fragment implements android.view.View.OnClickListener,onAdjacentRegionCountListener{
	
	private ImageView IV1;
	private ImageView IV2;
	private ImageView IV3;
	private ImageView IV4;
	private ImageView IV5;
	private ImageView IV6;
	private ImageView IV7;
	private ImageView IV8;
	
	private TextView TV1;
	private TextView TV2;
	private TextView TV3;
	private TextView TV4;
	private TextView TV5;
	private TextView TV6;
	private TextView TV7;
	private TextView TV8;
	
	
	private ImageView SelectedIV;
	
	private View RootView;
	
	private Context mContext;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.turnbar, container,false);
		RootView=v;
		IV1=(ImageView)v.findViewById(R.id.imageView1);
		IV2=(ImageView)v.findViewById(R.id.imageView2);
		IV3=(ImageView)v.findViewById(R.id.imageView3);
		IV4=(ImageView)v.findViewById(R.id.imageView4);
		IV5=(ImageView)v.findViewById(R.id.imageView5);
		IV6=(ImageView)v.findViewById(R.id.imageView6);
		IV7=(ImageView)v.findViewById(R.id.imageView7);
		IV8=(ImageView)v.findViewById(R.id.imageView8);
		
		/*IV1.setOnClickListener((android.view.View.OnClickListener) this);
		IV2.setOnClickListener((android.view.View.OnClickListener) this);
		IV3.setOnClickListener((android.view.View.OnClickListener) this);
		IV4.setOnClickListener((android.view.View.OnClickListener) this);
		IV5.setOnClickListener((android.view.View.OnClickListener) this);
		IV6.setOnClickListener((android.view.View.OnClickListener) this);		
		IV7.setOnClickListener((android.view.View.OnClickListener) this);
		IV8.setOnClickListener((android.view.View.OnClickListener) this);*/
		
		mContext=getActivity().getApplicationContext();
		
		SelectedIV=IV1;
		StyleUtil.setSelectedLayout(IV1);
		StyleUtil.setDeSelectedLayout(IV2);
		StyleUtil.setDeSelectedLayout(IV3);
		StyleUtil.setDeSelectedLayout(IV4);
		StyleUtil.setDeSelectedLayout(IV5);
		StyleUtil.setDeSelectedLayout(IV6);
		StyleUtil.setDeSelectedLayout(IV7);
		StyleUtil.setDeSelectedLayout(IV8);
		
		TV1=(TextView)v.findViewById(R.id.textView1);
		TV2=(TextView)v.findViewById(R.id.textView2);
		TV3=(TextView)v.findViewById(R.id.textView3);		
		TV4=(TextView)v.findViewById(R.id.textView4);
		TV5=(TextView)v.findViewById(R.id.textView5);
		TV6=(TextView)v.findViewById(R.id.textView6);
		TV7=(TextView)v.findViewById(R.id.textView7);
		TV8=(TextView)v.findViewById(R.id.textView8);
		
		
		return v;		
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub		
		switchCheckedState(v);
	}
	
	private void switchCheckedState(View v)
	{
		if(v!=SelectedIV)
		{			
			StyleUtil.setSelectedLayout(v);
			StyleUtil.setDeSelectedLayout(SelectedIV);
			SelectedIV=(ImageView)v;			
		}
	}

	@Override
	public void onAdjacentRegionCount(List<Integer> numList) {
		// TODO Auto-generated method stub
		TV1.setText(String.valueOf(numList.get(0)));
		TV2.setText(String.valueOf(numList.get(1)));
		TV3.setText(String.valueOf(numList.get(2)));
		TV4.setText(String.valueOf(numList.get(3)));
		TV5.setText(String.valueOf(numList.get(4)));		
		TV6.setText(String.valueOf(numList.get(5)));
		TV7.setText(String.valueOf(numList.get(6)));
		TV8.setText(String.valueOf(numList.get(7)));
		
		switch(EnumUtil.getCurTurn())
		{
			case EnumUtil.turn_blue:
				switchCheckedState(IV1);
				break;
			case EnumUtil.turn_red:
				switchCheckedState(IV2);
				break;
			case EnumUtil.turn_pink:
				switchCheckedState(IV3);
				break;
			case EnumUtil.turn_yellow:
				switchCheckedState(IV4);
				break;
			case EnumUtil.turn_green:
				switchCheckedState(IV5);
				break;
			case EnumUtil.turn_purple:
				switchCheckedState(IV6);
				break;
			case EnumUtil.turn_orange:
				switchCheckedState(IV7);
				break;
			case EnumUtil.turn_cyan:
				switchCheckedState(IV8);
				break;
			default:
				break;			
		}	
		
	}
}
