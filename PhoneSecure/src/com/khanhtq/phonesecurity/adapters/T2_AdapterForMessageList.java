package com.khanhtq.phonesecurity.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.khanhtq.phonesecurity.R;
import com.khanhtq.phonesecurity.models.Message;
import com.khanhtq.phonesecurity.utils.T2_ContactUtils;
import com.khanhtq.phonesecurity.utils.T2_DateTimeUtility;

public class T2_AdapterForMessageList extends BaseAdapter {
	private ArrayList<Message> list;
	private Activity activity;

	public T2_AdapterForMessageList(Activity a, ArrayList<Message> l) {
		list = l;
		activity = a;
		Collections.sort(list, new Comparator<Message>() {

			@Override
			public int compare(Message lhs, Message rhs) {
				return (int) (lhs.getDate()/1000 - rhs.getDate()/1000);
			}
		});
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (vi == null) {
			vi = inflater
					.inflate(R.layout.t2_list_row_for_inbox_and_sent, null);
		}
		Message msg = list.get(position);
		if (msg == null) {
			return null;
		}
		ImageView img = (ImageView) vi.findViewById(R.id.t2_sent_inbox_icon);
		TextView txtAddress = (TextView) vi
				.findViewById(R.id.t2_sent_or_inbox_address);
		TextView txtPreview = (TextView) vi
				.findViewById(R.id.t2_sent_or_inbox_preview);
		TextView txtTime = (TextView) vi
				.findViewById(R.id.t2_sent_or_inbox_time);

		txtTime.setText(T2_DateTimeUtility.getTimeInString(msg.getDate()));
		if (msg.getRead() == Message.READ) {
			img.setImageResource(R.drawable.t2_read_message);
		} else
			img.setImageResource(R.drawable.t2_unread_message);
		int size = msg.getBody().length();
		if (size > 20)
			txtPreview.setText(msg.getBody().substring(0, 20));
		else
			txtPreview.setText(msg.getBody());
		txtAddress
				.setText(T2_ContactUtils.getContactName(activity, msg.getAddress()));
		return vi;
	}
}
