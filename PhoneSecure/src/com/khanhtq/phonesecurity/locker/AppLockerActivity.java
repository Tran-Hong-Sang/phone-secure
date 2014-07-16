package com.khanhtq.phonesecurity.locker;

import com.khanhtq.phonesecurity.R;
import com.khanhtq.phonesecurity.activities.MainActivity;
import com.khanhtq.phonesecurity.utils.T2_Singleton;

import gueei.binding.Binder;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;

@SuppressWarnings("deprecation")
public class AppLockerActivity extends TabActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binder.setAndBindContentView(this, R.layout.tab, this);
    }
	// Khanh Tran Added
	@Override
	public void onBackPressed()
	{
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		T2_Singleton._bool = true;
		this.finish();
	}
	//end
}