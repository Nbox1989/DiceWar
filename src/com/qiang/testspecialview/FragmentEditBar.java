package com.qiang.testspecialview;

import com.qiang.testspecialview.MainActivity.onMenuSelectedListener;
import com.qiang.testspecialview.MainActivity.onShowBarListener;
import com.qiang.utils.StyleUtil;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentEditBar extends Fragment implements OnClickListener,onShowBarListener{
	private ImageView IV1;
	private ImageView IV2;
	private ImageView IV3;
	private ImageView IV4;
	
	private onMenuSelectedListener mSelectedListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.mapeditbar, container,false);
		
		IV1=(ImageView)v.findViewById(R.id.iv_del);
		IV2=(ImageView)v.findViewById(R.id.iv_done);
		IV3=(ImageView)v.findViewById(R.id.iv_finish);
		IV4=(ImageView)v.findViewById(R.id.iv_cancel);
				
		IV1.setOnClickListener((android.view.View.OnClickListener) this);
		IV2.setOnClickListener((android.view.View.OnClickListener) this);
		IV3.setOnClickListener((android.view.View.OnClickListener) this);
		IV4.setOnClickListener((android.view.View.OnClickListener) this);
		
		return v;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub	
		int clickResult=0;
		switch (v.getId())
		{
			case R.id.iv_del:
				clickResult=1;
				break;
			case R.id.iv_done:
				clickResult=2;
				break;
			case R.id.iv_finish:
				clickResult=3;
				break;
			case R.id.iv_cancel:
				clickResult=0;
				break;
			default:
				break;
		}
		mSelectedListener.OnSelectResult(clickResult);
		this.getView().setVisibility(View.INVISIBLE);
		((MainActivity)getActivity()).setBackGround(true);
	}

	@Override
	public void onShowBar() {
		// TODO Auto-generated method stub
		this.getView().setVisibility(View.VISIBLE);
		((MainActivity)getActivity()).setBackGround(false);
	}
	
	public void setMenuSelectedListener(onMenuSelectedListener l)
	{
		mSelectedListener=l;
	}
}
