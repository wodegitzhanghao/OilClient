package com.oil.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oilclient.R;

public class UserSuggestionActivty extends Activity {
	String url = "http://android.oilchem.net/html/suggestion.do?accessToken=dab5b52a907445abb0eff9539b68e31f";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usersuggestion);
		initWeidget();

	}

	private void initWeidget() {
		// TODO Auto-generated method stub
		ImageView iv_back;
		TextView tv_pagetitle;

		iv_back = (ImageView) findViewById(R.id.iv_pageback);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tv_pagetitle = (TextView) findViewById(R.id.tv_page_title);
		tv_pagetitle.setText("�û�����");
	}

}
