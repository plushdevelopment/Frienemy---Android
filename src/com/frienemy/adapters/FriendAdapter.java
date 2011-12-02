package com.frienemy.adapters;

import java.util.ArrayList;

import com.frienemy.activities.R;
import com.frienemy.models.Friend;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<Friend> data;
    private static LayoutInflater inflater=null;

    
    public FriendAdapter(Activity a, ArrayList<Friend> friends) {
        activity = a;
        data=friends;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
   
    
    //Get information for Each friend and place it into row view
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
        	vi= inflater.inflate(R.layout.friend_row, null);
         
        	TextView name=(TextView)vi.findViewById(R.id.fullname);;
       // ImageView image=(ImageView)vi.findViewById(R.id.imageView1);
        name.setText("Mike Smith");
        
        return vi;
    }
}