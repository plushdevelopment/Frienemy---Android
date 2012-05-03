package com.frienemy.adapters;

import java.util.ArrayList;

import com.frienemy.activities.R;
import com.frienemy.layouts.FriendListItem;
import com.frienemy.models.Friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class FriendAdapter extends ArrayAdapter<Friend> {

	private Context context;
	private ArrayList<Friend> data;
	private static LayoutInflater inflater=null;

	public FriendAdapter(Context context, ArrayList<Friend> friends) {
		super(context, android.R.layout.simple_list_item_1, friends);
		this.context = context;
		this.data = friends;
		inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public long getItemId(int position) {
		return position;
	}

	//Get information for Each friend and place it into row view
	public View getView(int position, View convertView, ViewGroup parent) {
		FriendListItem vi=(FriendListItem)convertView;
		if(vi==null)
			vi = (FriendListItem)inflater.inflate(R.layout.friend_row, null);
		vi.setFriend(data.get(position));
		vi.setImage(data.get(position));

		return vi;
	}

}