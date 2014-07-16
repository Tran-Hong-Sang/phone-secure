package com.khanhtq.phonesecurity.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.khanhtq.phonesecurity.R;
import com.khanhtq.phonesecurity.models.Message;
import com.khanhtq.phonesecurity.utils.T2_DateTimeUtility;

public class T2_AdapterForConversationList extends BaseAdapter {
	
	private Activity activity;
	private ArrayList<Message> list;
	
	public T2_AdapterForConversationList(Activity activity, ArrayList<Message> list) {
		this.activity = activity;
		this.list = list;
		Collections.sort(list, new Comparator<Message>() {

			@Override
			public int compare(Message lhs, Message rhs) {
				return (int) (lhs.getDate()/1000 - rhs.getDate()/1000);
			}
		});
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		RelativeLayout t2_conversation_wrapper;
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (view == null) {
			view = inflater
					.inflate(R.layout.t2_listitem_discuss, null);
		}
		Message sms = (Message) getItem(position);
		t2_conversation_wrapper = (RelativeLayout) view.findViewById(R.id.t2_conversation_wrapper);
		TextView t2_conversation_comment = (TextView) view.findViewById(R.id.t2_conversation_comment);
		TextView t2_conversation_time = (TextView) view.findViewById(R.id.t2_conversation_time);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            
		if (sms != null) {
			//int size = sms.getBody().length();
			// if (size > 20)
			// t2_conversation_comment.setText(sms.getBody());//.substring(0,
			// 20));
			// else
			t2_conversation_time.setText(T2_DateTimeUtility.getTimeInString(sms.getDate()));
			t2_conversation_comment.setText(sms.getBody());
			if (isMine(sms.getAddress())) {
				t2_conversation_wrapper.setBackgroundResource(R.drawable.t2_bubble_green);
				params.gravity = Gravity.RIGHT;
				t2_conversation_wrapper.setLayoutParams(params);
			} else {
				t2_conversation_wrapper.setBackgroundResource(R.drawable.t2_bubble_yellow);
				params.gravity = Gravity.LEFT;
				t2_conversation_wrapper.setLayoutParams(params);
			}
			
		}
		return view;
	}
	
	private boolean isMine(String mynumber) {
		return mynumber.equals("123456");
	}
	
	

}
