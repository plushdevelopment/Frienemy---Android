package com.frienemy.layouts;

import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.RejectedExecutionException;

import com.frienemy.activities.R;
import com.frienemy.models.Friend;
import com.frienemy.tasks.LoadImageAsyncTask;
import com.frienemy.tasks.LoadImageAsyncTask.LoadImageAsyncTaskResponder;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FriendListItem extends RelativeLayout implements LoadImageAsyncTaskResponder {

	private ImageView     imageView;
	private TextView      nameTextView;
	private TextView      relationshipStatusTextView;
	private TextView      frienemyStatusTextView;
	private TextView      stalkFriendTextView;

    private Queue<AsyncTask<URL, Void, Drawable>> latestLoadTask= new LinkedList<AsyncTask<URL, Void, Drawable>>();
	public FriendListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setFriend(Friend friend) {
		findViews();
		nameTextView.setText(friend.name);
		relationshipStatusTextView.setText(friend.relationshipStatus);
		switch (friend.frienemyStatus) {
		case 0:
			frienemyStatusTextView.setText("Friend");
			break;
		case 1:
			frienemyStatusTextView.setText("Frienemy");
			break;
		case 2:
			frienemyStatusTextView.setText("New Friend");
			break;
		}
		if (true == friend.stalking) {
			stalkFriendTextView.setVisibility(View.VISIBLE);
			RotateAnimation ranim = (RotateAnimation)AnimationUtils.loadAnimation(getContext(), R.drawable.anim);
			ranim.setFillAfter(true);
			stalkFriendTextView.setAnimation(ranim);
		} else {
			stalkFriendTextView.setVisibility(View.GONE);
		}
		
	}
	public void setImage(Friend friend) {
		findViews();
			
		addTask(new LoadImageAsyncTask(this), friend.getProfileImageURL());
	}

	public void imageLoading() {
		imageView.setImageDrawable(null);
	}

	public void imageLoadCancelled() {
		// do nothing
	}

	public void imageLoaded(Drawable drawable) {
		imageView.setImageDrawable(drawable);
	}

	private void findViews() {
		imageView = (ImageView) findViewById(R.id.imageView1);
		nameTextView = (TextView) findViewById(R.id.fullname);
		relationshipStatusTextView = (TextView) findViewById(R.id.relationshipStatus);
		frienemyStatusTextView = (TextView) findViewById(R.id.frienemyStatus);
		stalkFriendTextView= (TextView) findViewById(R.id.textstalk);
	}

	
	public void addTask(AsyncTask<URL, Void, Drawable> task, URL url) {
        try{
                task.execute(url);
                while(!latestLoadTask.isEmpty()){
                    task = latestLoadTask.remove();
                    task.execute(url);
                }
            
        } catch (RejectedExecutionException r){
                latestLoadTask.add(task);
        }
}

}
