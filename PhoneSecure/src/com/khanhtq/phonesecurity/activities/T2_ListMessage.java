package com.khanhtq.phonesecurity.activities;

import java.util.ArrayList;

import com.khanhtq.phonesecurity.R;
import com.khanhtq.phonesecurity.adapters.T2_AdapterForMessageList;
import com.khanhtq.phonesecurity.models.Message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class T2_ListMessage extends Activity {
	ListView listview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.t2_list_message);
		Intent intent = getIntent();
		String type = intent.getStringExtra("type");
		if(type == null || type.equals("inbox")) setTitle("Inbox");
		else setTitle("Sent");
		listview = (ListView) findViewById(R.id.t2_sent_inbox_listview);
		ArrayList<Message> data = new ArrayList<Message>();
		data.add(new Message(0, "0943396121", "0943396121", System.currentTimeMillis() - 100000,
				Message.TYPE_INBOX, Message.UNREAD, 0, "Toi nay tra da nha"));
		data.add(new Message(1, "0987678765", "0987678765", System.currentTimeMillis(),
				Message.TYPE_INBOX, Message.UNREAD, 0, "Oke con de"));
		data.add(new Message(2, "098893944", "Khuong", System.currentTimeMillis() + 100000,
				Message.TYPE_INBOX, Message.READ, 0, "Hihi"));
		T2_AdapterForMessageList adapter = new T2_AdapterForMessageList(this,data);
		listview.setAdapter(adapter);
	}
	@Override
	public void onBackPressed(){
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		finish();
	}
}
