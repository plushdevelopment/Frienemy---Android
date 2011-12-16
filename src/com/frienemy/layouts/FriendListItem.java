package com.frienemy.layouts;

import java.net.URL;

import com.frienemy.activities.R;
import com.frienemy.models.Friend;
import com.frienemy.tasks.LoadImageAsyncTask;
import com.frienemy.tasks.LoadImageAsyncTask.LoadImageAsyncTaskResponder;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FriendListItem extends RelativeLayout implements LoadImageAsyncTaskResponder {

	private ImageView     imageView;
	private TextView      nameTextView;
	private TextView      relationshipStatusTextView;
	private TextView      frienemyStatusTextView;

	private AsyncTask<URL, Void, Drawable> latestLoadTask;

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

		// cancel old task
		if (null != latestLoadTask) {
			latestLoadTask.cancel(true);
		}
		latestLoadTask = new LoadImageAsyncTask(this).execute(friend.getProfileImageURL());
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
	}

}
