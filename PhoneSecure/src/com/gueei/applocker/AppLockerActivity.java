package com.gueei.applocker;

import gueei.binding.Binder;
import android.app.TabActivity;
import android.os.Bundle;

@SuppressWarnings("deprecation")
public class AppLockerActivity extends TabActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binder.setAndBindContentView(this, R.layout.tab, this);
    }
}