package com.frienemy.adapters;

import java.util.ArrayList;

import com.frienemy.activities.R;
import com.frienemy.layouts.StalkerListItem;
import com.frienemy.models.StalkerRelationship;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class StalkerAdapter extends ArrayAdapter<StalkerRelationship> {

	private Context context;
	private ArrayList<StalkerRelationship> data;
	private static LayoutInflater inflater=null;
	public static String[] stalkers = new String[10];

	public StalkerAdapter(Context context, ArrayList<StalkerRelationship> stalkers) {
		super(context, android.R.layout.simple_list_item_1, stalkers);
		this.context = context;
		this.data = stalkers;
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
		StalkerListItem vi=(StalkerListItem)convertView;
		if(vi==null)
		{
			vi = (StalkerListItem)inflater.inflate(R.layout.stalker_row, null);
			if(position<10)
			{
				stalkers[position]=data.get(position).fromFriend.firstName + " " + data.get(position).fromFriend.lastName;
			}
			vi.setFriend(data.get(position).fromFriend);
		}
		return vi;
	}
	
}
