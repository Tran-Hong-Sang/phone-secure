package com.khanhtq.phonesecurity.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.khanhtq.phonesecurity.R;
import com.khanhtq.phonesecurity.adapters.T2_AdapterForConversationList;
import com.khanhtq.phonesecurity.models.Message;
import com.khanhtq.phonesecurity.utils.T2_SQLiteUtility;
import com.khanhtq.phonesecurity.utils.T2_Singleton;

public class T2_ConversionMessage extends Activity implements OnClickListener {
	ListView list;
	Button send;
	Context context;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.t2_conversion);
		setTitle(T2_Singleton.currentMessageForConversation.getFrom());
		context = getApplicationContext();
		list = (ListView) findViewById(R.id.listView_chat);
		//send = (Button) findViewById(R.id.sendBtn);
		//send.setOnClickListener(this);
		ArrayList<Message> data = new ArrayList<Message>();
		T2_SQLiteUtility dbU = new T2_SQLiteUtility(this).open("readDB");
		data = dbU.getMessagesOfConversation(T2_Singleton.currentMessageForConversation.getAddress());
		dbU.close();
		data.add(new Message(0, "0943396121", "0943396121", System.currentTimeMillis() - 100000,
				Message.TYPE_INBOX, Message.UNREAD, 0, "Toi nay tra da nhaToi nay tra da nhaToi nay tra da nhaToi nay tra da nhaToi nay tra da nhaToi nay tra da nhaToi nay tra da nhaToi nay tra da nhaToi nay tra da nhaToi nay tra da nha"));
		data.add(new Message(1, "0943396121", "123456", System.currentTimeMillis(),
				Message.TYPE_INBOX, Message.UNREAD, 0, "Oke con de"));
		data.add(new Message(2, "098893944", "Khuong", System.currentTimeMillis() + 100000,
				Message.TYPE_INBOX, Message.READ, 0, "Hihi"));
		
		data.add(new Message(3, "0943396121", "0943396121", System.currentTimeMillis() - 100000,
				Message.TYPE_INBOX, Message.UNREAD, 0, "Toi nay tra da nhaToi nay tra da nhaToi nay tra da nhaToi nay tra da nhaToi nay tra da nhaToi nay tra da nhaToi nay tra da nhaToi nay tra da nhaToi nay tra da nhaToi nay tra da nha"));
		data.add(new Message(4, "0943396121", "123456", System.currentTimeMillis(),
				Message.TYPE_INBOX, Message.UNREAD, 0, "Oke con de"));
		data.add(new Message(5, "098893944", "Khuong", System.currentTimeMillis() + 100000,
				Message.TYPE_INBOX, Message.READ, 0, "Hihi"));
		T2_AdapterForConversationList adapter = new T2_AdapterForConversationList(this, data);
		list.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		if (send == v) {
//			//TODO
//		}
	}

}
