package com.khanhtq.phonesecurity.activities;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.khanhtq.phonesecurity.R;
import com.khanhtq.phonesecurity.adapters.T2_AdapterForMessageList;
import com.khanhtq.phonesecurity.models.Message;
import com.khanhtq.phonesecurity.utils.T2_SMSUtility;
import com.khanhtq.phonesecurity.utils.T2_SQLiteUtility;
import com.khanhtq.phonesecurity.utils.T2_Singleton;

public class T2_ListMessage extends Activity implements OnClickListener,
		OnItemClickListener {
	ListView listview;
	Button t2_bottom_newmessage;
	ArrayList<Message> data;
//	T2_SMSListener listener;
	T2_AdapterForMessageList adapter;
	String type = "inbox";
	public static T2_ListMessage _this;
	
	public void doWithReceivedMessages(Message[] msgs) {
		data.addAll(0, Arrays.asList(msgs));
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_this = this;
		setContentView(R.layout.t2_list_message);
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		listview = (ListView) findViewById(R.id.t2_sent_inbox_listview);
		t2_bottom_newmessage = (Button) findViewById(R.id.t2_bottom_newmessage);
		t2_bottom_newmessage.setOnClickListener(this);
		T2_SQLiteUtility dbU = new T2_SQLiteUtility(this);
		if (type != null) {
			if (type.equals("inbox")) {
				setTitle("Inbox");
				// set receiver
//				IntentFilter iff = new IntentFilter();
//				registerReceiver(listener, iff);
				data = dbU.open("readDB").getMessagesByType(Message.TYPE_INBOX);
			} else if (type.equals("sent")) {
				setTitle("Sent");
				data = dbU.open("readDB").getMessagesByType(Message.TYPE_SENT);
			}
		}
		dbU.close();
		adapter = new T2_AdapterForMessageList(this, data);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
	}

	public T2_ListMessage() {
//		listener = new T2_SMSListener() {
//			@Override
//			public void doWithReceivedMessages(Message[] msgs) {
//				data.addAll(0, Arrays.asList(msgs));
//				adapter.notifyDataSetChanged();
//			}
//		};
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(this, MainActivity.class);
		T2_Singleton._bool = true;
		startActivity(i);
		finish();
	}

	@Override
	public void onClick(View v) {
		if (t2_bottom_newmessage == v) {
			new NewMessageDialog(this).show();
		}
	}

	/**
	 * New Message Dialog
	 * 
	 * @author Khanh Tran
	 * 
	 */
	class NewMessageDialog extends Dialog implements OnClickListener {
		Button btnSend, btnClose;
		EditText edtTo, edtContent;
		AutoCompleteTextView autoComplete;
		Context cont;
		Message msg = null;

		public NewMessageDialog(Context context, Message msg) {
			this(context);
			autoComplete.setText(msg.getAddress());
			this.msg = msg;
		}

		public NewMessageDialog(Context context) {
			super(context);
			cont = context;
			setTitle("New Message");
			setTheme(android.R.style.Theme);
			setContentView(R.layout.t2_new_message);
			autoComplete = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
			String item[] = { "January", "February", "March", "April", "May",
					"June", "July", "August", "September", "October",
					"November", "December" };
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(cont,
					android.R.layout.simple_list_item_1, item);
			autoComplete.setAdapter(adapter);
			autoComplete
					.setDropDownBackgroundResource(R.color.autocompletet_background_color);
			btnSend = (Button) findViewById(R.id.t2_newmsg_send);
			btnClose = (Button) findViewById(R.id.t2_btn_newmsg_close);
			btnClose.setOnClickListener(this);
			btnSend.setOnClickListener(this);
			edtContent = (EditText) findViewById(R.id.t2_newmsg_content);
		}

		@Override
		public void onClick(View v) {
			if (v == btnSend) {
				String to = autoComplete.getText().toString().trim();
				if (to == null || to.length() == 0) {
					Toast.makeText(cont, "Empty Number!", Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (msg != null) {
					to = msg.getAddress();
				}
				Message send = new Message();
				send.setAddress(to);
				send.setBody(edtContent.getText().toString());
				send.setType(Message.TYPE_SENT);
				ProgressDialog pd = new ProgressDialog(cont);
				pd.setTitle("Sending...");
				pd.setMessage("Sending message.");
				pd.show();
				T2_SMSUtility.sendMessage(send, cont);
				pd.dismiss();
				Toast.makeText(cont, "Sent!", Toast.LENGTH_LONG).show();
				if (T2_Singleton.currentMessageForConversation == null) {
					T2_Singleton.currentMessageForConversation = send;
				}
				Intent i = new Intent(cont, T2_ConversionMessage.class);
				cont.startActivity(i);
				this.dismiss();
			}
			if (btnClose == v) {
				this.dismiss();
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Message selectedOne = data.get(position);
		selectedOne.setRead(Message.READ);

		T2_Singleton.currentMessageForConversation = selectedOne; // use this
																	// value to
																	// determine
																	// which one
																	// is chosen
																	// in
																	// conversation.
		if (selectedOne != null) {
			new MessageDetailDialog(this, selectedOne).show();
		}
		// else{
		// Toast.makeText(this, "NULL", Toast.LENGTH_LONG).show();
		// }
	}

	/**
	 * View Message Details
	 */
	class MessageDetailDialog extends Dialog implements
			android.view.View.OnClickListener {
		Context con;
		Message msg;
		TextView t2_viewmsg_from, t2_viewmsg_status, t2_viewmsg_content;
		Button t2_btn_reply, t2_btn_close, t2_btn_viewmsg_conversation;

		public MessageDetailDialog(Context context, Message msg) {
			super(context);
			setContentView(R.layout.t2_view_message);
			con = context;
			this.msg = msg;
			setStatusIsViewedToDatabase(msg);
			adapter.notifyDataSetChanged();
			setTitle(msg.getFrom());
			t2_viewmsg_from = (TextView) findViewById(R.id.t2_viewmsg_from);
			t2_viewmsg_status = (TextView) findViewById(R.id.t2_viewmsg_status);
			t2_viewmsg_content = (TextView) findViewById(R.id.t2_viewmsg_content);
			t2_btn_reply = (Button) findViewById(R.id.t2_btn_reply);
			t2_btn_close = (Button) findViewById(R.id.t2_btn_close);
			t2_btn_viewmsg_conversation = (Button) findViewById(R.id.t2_btn_viewmsg_conversation);
			t2_btn_reply.setOnClickListener(this);
			t2_btn_close.setOnClickListener(this);
			t2_btn_viewmsg_conversation.setOnClickListener(this);
			t2_viewmsg_from.setText(msg.getFrom());
			t2_viewmsg_status
					.setText(msg.getType() == Message.TYPE_SENT ? "Sent"
							: "Received");
			t2_viewmsg_content.setText(msg.getBody());

		}

		private void setStatusIsViewedToDatabase(Message msg2) {
			if (type.equals("sent"))
				return;
			if (msg2.isRead())
				return;
			T2_SQLiteUtility dbU = new T2_SQLiteUtility(con).open("writeDB");
			dbU.setMessageIsRead(msg2.get_id());
			dbU.close();
		}

		@Override
		public void onClick(View v) {
			if (v == t2_btn_close) {
				this.dismiss();
			}
			if (v == t2_btn_reply) {
				this.dismiss();
				new NewMessageDialog(con, msg).show();
			}
			if (v == t2_btn_viewmsg_conversation) {
				Message msg = T2_Singleton.currentMessageForConversation;
				if (msg == null) {
					Toast.makeText(con,
							"No address chosen for viewing conversation!",
							Toast.LENGTH_LONG).show();
				} else {
					Intent i = new Intent(con, T2_ConversionMessage.class);
					con.startActivity(i);
				}

			}
		}

	}

	@Override
	public void onDestroy() {
		String type = getIntent().getStringExtra("type");
		if (type != null) {
//			if (type.equals("inbox")) {
//				unregisterReceiver(listener);
//			}
		}
		super.onDestroy();
	}
}
