package com.khanhtq.phonesecurity.activities;

/**
 * 
 * @author Khanh Tran
 *
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.khanhtq.phonesecurity.R;
import com.khanhtq.phonesecurity.locker.AppLockerActivity;

public class MainActivity extends Activity implements OnClickListener {
	Button btn_main_security_settings ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.khanhtq_main_activity);
		initViews();
	}
	
	private void initViews() {
		btn_main_security_settings = (Button) findViewById(R.id.btn_main_security_settings);
		btn_main_security_settings.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btn_main_security_settings) {
			Intent i = new Intent(this, AppLockerActivity.class);
			startActivity(i);
		}
	}
}
