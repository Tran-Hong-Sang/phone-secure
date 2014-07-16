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
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.khanhtq.phonesecurity.R;
import com.khanhtq.phonesecurity.locker.AppLockerActivity;
import com.khanhtq.phonesecurity.locker.DetectorService;
import com.khanhtq.phonesecurity.models.Message;
import com.khanhtq.phonesecurity.utils.T2_SMSUtility;
import com.khanhtq.phonesecurity.utils.T2_SQLiteUtility;
import com.khanhtq.phonesecurity.utils.T2_Singleton;

public class MainActivity extends Activity implements OnClickListener {
	Button btn_main_security_settings, btn_lock,t2_main_import_messages;
	Button btnInbox, btnSent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.khanhtq_main_activity);
//		if(!T2_Singleton.isActive()){
//			showLoginDialog();
//		}
		// start service
		Intent startService = new Intent(this, DetectorService.class);
		this.startService(startService);
		// end
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
		
		btnInbox = (Button) findViewById(R.id.t2_main_inbox);
		btnSent = (Button) findViewById(R.id.t2_main_sent);
		btnInbox.setOnClickListener(this);
		btnSent.setOnClickListener(this);
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
		if(v == btnSent){
			Intent i = new Intent(this, T2_ListMessage.class);
			i.putExtra("type", "sent");
			startActivity(i);
		}
		if(t2_main_import_messages == v){
			new RetrieveSMSAndStoreToDB(this).execute();
		}
	}
	@Override
	public void onResume(){
		if(T2_Singleton._bool != true){
			showLoginDialog();
		}
		T2_Singleton._bool = false;
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
//			((Activity)cont).finish();
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
	private SharedPreferences mPref;
	Activity activity;
	private String firsttime;
	public RetrieveSMSAndStoreToDB(Activity act) {
		this.activity = act;
		mPref = PreferenceManager.getDefaultSharedPreferences(activity);
		dialog = ProgressDialog.show(activity, "Please wait", 
		            "Importing Messages", true);
	}
	ProgressDialog dialog;
	@Override
	protected Void doInBackground(Void... params) {
		firsttime = mPref.getString("is_first_time", "true");
		if(!firsttime .equals("true")){
			activity.runOnUiThread(new Runnable(){
	          @Override
	          public void run(){
	        	  Toast.makeText(activity, "You already imported.", Toast.LENGTH_LONG).show();
	          }
	       });
			return null;
		}
		final ArrayList<Message> listMsg = T2_SMSUtility.getMessageListFromInbox(activity);
		if(listMsg == null || listMsg.size() == 0){
			activity.runOnUiThread(new Runnable(){
		          @Override
		          public void run(){
		        	  Toast.makeText(activity, "Empty Inbox", Toast.LENGTH_LONG).show();
		          }
		       });
		} else {
			T2_SQLiteUtility dbU = new T2_SQLiteUtility(activity).open("writeDB");
			dbU.addMessage(listMsg);
			activity.runOnUiThread(new Runnable(){
		          @Override
		          public void run(){
		        	  Toast.makeText(activity, "Done importing.", Toast.LENGTH_LONG).show();
		          }
		    });
			dbU.close();
		}
		mPref.edit().putString("is_first_time", "true").commit();
		return null;
	}
	@Override
	public void onPostExecute(Void v){
		dialog.dismiss();
	}
}