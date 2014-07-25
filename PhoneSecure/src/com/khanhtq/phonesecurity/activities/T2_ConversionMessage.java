package com.khanhtq.phonesecurity.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.khanhtq.phonesecurity.R;
import com.khanhtq.phonesecurity.adapters.T2_AdapterForConversationList;
import com.khanhtq.phonesecurity.models.Message;
import com.khanhtq.phonesecurity.utils.T2_ContactUtils;
import com.khanhtq.phonesecurity.utils.T2_SMSUtility;
import com.khanhtq.phonesecurity.utils.T2_SQLiteUtility;
import com.khanhtq.phonesecurity.utils.T2_Singleton;

public class T2_ConversionMessage extends Activity implements OnClickListener {
	ListView list;
	Button send;
	Context context;
	ArrayList<Message> data;
	T2_AdapterForConversationList adapter;
	EditText bodyContent;
	
	public static T2_ConversionMessage _this;
	
	public void doWithReceivedMessages(Message[] msgs) {
		List<Message> array = Arrays.asList(msgs);
		for(Message m: array){
			if(!T2_ContactUtils.getOriginalAddress(m.getAddress())
					.equals(T2_ContactUtils.getOriginalAddress(T2_Singleton.currentMessageForConversation.getAddress()))){
				array.remove(m);
			}
		}
		data.addAll(data.size(), array);
		adapter.notifyDataSetChanged();
		list.setSelection(adapter.getCount() - 1);
	}
	
	
	
	
	
//	T2_SMSListener listener;
	public T2_ConversionMessage(){
//		listener = new T2_SMSListener() {
//			@Override
//			public void doWithReceivedMessages(Message[] msgs) {
//				List<Message> array = Arrays.asList(msgs);
//				for(Message m: array){
//					if(!T2_ContactUtils.getOriginalAddress(m.getAddress())
//							.equals(T2_ContactUtils.getOriginalAddress(T2_Singleton.currentMessageForConversation.getAddress()))){
//						array.remove(m);
//					}
//				}
//				data.addAll(data.size(), array);
//				adapter.notifyDataSetChanged();
//				list.setSelection(adapter.getCount() - 1);
//			}
//		};
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_this = this;
		setContentView(R.layout.t2_conversion);
		setTitle(T2_Singleton.currentMessageForConversation.getFrom() == null || T2_Singleton.currentMessageForConversation.getFrom() .equals("")? T2_Singleton.currentMessageForConversation.getAddress() : T2_Singleton.currentMessageForConversation.getFrom());
		context = getApplicationContext();
		list = (ListView) findViewById(R.id.listView_chat);
		send = (Button) findViewById(R.id.t2_conversation_send);
		send.setOnClickListener(this);
		bodyContent = (EditText) findViewById(R.id.t2_conversation_msgbody);
		data = new ArrayList<Message>();
		T2_SQLiteUtility dbU = new T2_SQLiteUtility(this).open("readDB");
		data = dbU.getMessagesOfConversation(T2_Singleton.currentMessageForConversation.getAddress());
		dbU.close();
		adapter = new T2_AdapterForConversationList(this, data);
		list.setAdapter(adapter);
		list.setSelection(adapter.getCount() - 1);
	}

	@Override
	public void onClick(View v) {
		if (send == v) {
			String address = T2_Singleton.currentMessageForConversation.getAddress();
			String body = bodyContent.getText().toString();
			bodyContent.setText("");
			Message sendingMsg = new Message();
			sendingMsg.setAddress(address);
			sendingMsg.setType(Message.TYPE_SENT);
			sendingMsg.setBody(body);
			T2_SMSUtility.sendMessage(sendingMsg, context);
			sendingMsg.setDate(new Date().getTime());	
			data.add(data.size(),sendingMsg);
			adapter.notifyDataSetChanged();
			Toast.makeText(this, "Sent", Toast.LENGTH_SHORT).show();		
		}
	}
	@Override
	public void onBackPressed() {
		Intent i = new Intent(this, MainActivity.class);
		T2_Singleton._bool = true;
		startActivity(i);
		finish();
	}
}
