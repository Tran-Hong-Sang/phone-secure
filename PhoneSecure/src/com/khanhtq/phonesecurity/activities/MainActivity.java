package com.khanhtq.phonesecurity.activities;

/**
 * 
 * @author Khanh Tran
 *
 */
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.khanhtq.phonesecurity.R;
import com.khanhtq.phonesecurity.locker.AppLockerActivity;
import com.khanhtq.phonesecurity.models.Message;
import com.khanhtq.phonesecurity.utils.T2_SMSUtility;
import com.khanhtq.phonesecurity.utils.T2_Singleton;

public class MainActivity extends Activity implements OnClickListener {
	Button btn_main_security_settings, btn_lock,t2_main_import_messages;
	ImageButton btnInbox,btnConversation, btnSent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.khanhtq_main_activity);
		if(!T2_Singleton.isActive()){
			showLoginDialog();
		}
		initViews();
	}
	/**
	 * Show login dialog.
	 */
	private void showLoginDialog() {
		LoginDialog ld = new LoginDialog(this);
		ld.show();
	}

	private void initViews() {
		t2_main_import_messages = (Button) findViewById(R.id.t2_main_import_messages);
		t2_main_import_messages.setOnClickListener(this);
		btn_lock= (Button) findViewById(R.id.btn_main_lock);
		btn_lock.setOnClickListener(this);
		btn_main_security_settings = (Button) findViewById(R.id.btn_main_security_settings);
		btn_main_security_settings.setOnClickListener(this);
		
		btnInbox = (ImageButton) findViewById(R.id.t2_main_inbox);
		btnSent = (ImageButton) findViewById(R.id.t2_main_sent);
		btnConversation = (ImageButton) findViewById(R.id.t2_main_conversation);
		btnInbox.setOnClickListener(this);
		btnSent.setOnClickListener(this);
		btnConversation.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btn_main_security_settings) {
			Intent i = new Intent(this, AppLockerActivity.class);
			startActivity(i);
		}
		if(v == btn_lock){
			T2_Singleton.deactive();
			LoginDialog ld = new LoginDialog(this);
			ld.setTitle("Locked");
			ld.show();
		}
		if(v == btnInbox){
			Intent i = new Intent(this, T2_ListMessage.class);
			i.putExtra("type", "inbox");
			startActivity(i);
		}
		if(t2_main_import_messages == v){
			new RetrieveSMSAndStoreToDB(this).execute();
		}
	}
	@Override
	public void onResume(){
		
		super.onResume();
	}
}// End MainActivity class

class LoginDialog extends Dialog implements OnClickListener,
		OnSharedPreferenceChangeListener {
	private SharedPreferences mPref;
	private static final String PREF_PASSWORD = "password";
	Context cont;
	Button btnLogin, btnQuit;
	TextView txtPass;
	private String password;

	public LoginDialog(Context context) {
		super(context);
		cont = context;
		mPref = PreferenceManager.getDefaultSharedPreferences(context);
		mPref.registerOnSharedPreferenceChangeListener(this);
		password = mPref.getString(PREF_PASSWORD, "1234");
		setContentView(R.layout.t2_login_dialog);
		btnLogin = (Button) findViewById(R.id.t2_login_btn_login);
		btnQuit = (Button) findViewById(R.id.t2_login_btn_quit);
		btnLogin.setOnClickListener(this);
		btnQuit.setOnClickListener(this);
		txtPass = (EditText) findViewById(R.id.t2_logindialog_txt_psw);
		setTitle("Enter password");
		
	}

	@Override
	public void onClick(View v) {
		if (v == btnLogin) {
			if (checkLogin()) {
				T2_Singleton.active();// Set state is active
				dismiss();
			} else {
				Toast.makeText(cont, "Wrong password", Toast.LENGTH_SHORT)
						.show();
			}

		}
		if (v == btnQuit) {
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			cont.startActivity(startMain);
			((Activity)cont).finish();
		}

	}

	/**
	 * Check if password is correct
	 */
	private boolean checkLogin() {
		String enteredPassword = txtPass.getText().toString();
		if (enteredPassword.equals(password))
			return true;
		return false;
	}
	/**
	 * Prevent press back button
	 */
	@Override
	public void onBackPressed(){
		return;
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

	}
}
//--- AsyncTask for retrieving SMS to save to DB
class RetrieveSMSAndStoreToDB extends AsyncTask<Void, Void, Void>{
	Activity activity;
	public RetrieveSMSAndStoreToDB(Activity act) {
		this.activity = act;
		dialog = ProgressDialog.show(activity, "Please wait", 
		            "Importing Messages", true);
		// TODO Auto-generated constructor stub
	}
	ProgressDialog dialog;
	@Override
	protected Void doInBackground(Void... params) {
		final ArrayList<Message> listMsg = T2_SMSUtility.getMessageListFromInbox(activity);
		if(listMsg == null || listMsg.size() == 0){
			activity.runOnUiThread(new Runnable(){
		          @Override
		          public void run(){
		        	  Toast.makeText(activity, "Empty Inbox", 1).show();
//		        	  new Handler().postDelayed(new Runnable(){
//		        		    public void run() {
//		        		        Toast.makeText(activity, "Empty Inbox", 1).show();
//		        		    }
//		        		}, 5000);
		          }
		       });
		} else {
			activity.runOnUiThread(new Runnable(){
		          @Override
		          public void run(){
		        	  Toast.makeText(activity, "Number of SMS is " + listMsg.size(), 1).show();
		          }
		       });
		}
		
		return null;
	}
	@Override
	public void onPostExecute(Void v){
		dialog.dismiss();
	}
}