package com.gueei.applocker.khanhtq.activities;

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

import com.gueei.applocker.AppLockerActivity;
import com.gueei.applocker.R;

public class MainActivity extends Activity implements OnClickListener {
	Button btn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.khanhtq_main_activity);
		btn = (Button) findViewById(R.id.demo_click);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btn) {
			Intent i = new Intent(this, AppLockerActivity.class);
			startActivity(i);
		}
	}
}
