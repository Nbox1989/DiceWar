package com.qiang.testspecialview;

import com.qiang.utils.EnumUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

public class LaunchActivity extends Activity{
	private Button startButton;
	private Button editButton;
	private ProgressBar pb_loading;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launch_layout);
		startButton=(Button)findViewById(R.id.buttonStart);
		editButton=(Button)findViewById(R.id.buttonEdit);
		pb_loading=(ProgressBar)findViewById(R.id.pb_loading);
		
		initStartButton();
		intiEditButton();
	}
	private void initStartButton() {
		// TODO Auto-generated method stub
		startButton=(Button)findViewById(R.id.buttonStart);
		startButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pb_loading.setVisibility(View.VISIBLE);
				
				Intent i=new Intent(LaunchActivity.this, MainActivity.class);
				Bundle b=new Bundle();
				b.putInt("type", EnumUtil.state_play);
				i.putExtras(b);
				startActivityForResult(i,1111);
			}
		});
		
	}
	private void intiEditButton() {
		// TODO Auto-generated method stub
		editButton=(Button)findViewById(R.id.buttonEdit);
		editButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(LaunchActivity.this, MainActivity.class);
				Bundle b=new Bundle();
				b.putInt("type", EnumUtil.state_edit);
				i.putExtras(b);
				startActivityForResult(i,1111);
			}
		});
	}
}
