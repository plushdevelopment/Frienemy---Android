package com.frienemy.models;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Friend")
public class Friend extends ActiveRecordBase<Friend> {

	private static final String TAG = Friend.class.getSimpleName();
	private static String RelationshipChangedListString = "";

	@Column(name = "uid")
	public String uid;

	@Column(name = "name")
	public String name;

	@Column(name = "relationshipStatus")
	public String relationshipStatus = "NA";

	@Column(name = "frienemyStatus")
	public int frienemyStatus;

	@Column(name = "relationshipChanged")
	public boolean relationshipStatusChanged;

	@Column(name = "frienemyStatusChanged")
	public boolean frienemyStatusChanged;

	@Column(name = "stalkerRank")
	public int stalkerRank;

	@Column(name = "isCurrentUser")
	public boolean isCurrentUser;

	@Column(name = "isCurrentUsersFriend")
	public boolean isCurrentUsersFriend;

	@Column(name = "stalking")
	public boolean stalking;

	@Column(name = "firstName")
	public String firstName;

	@Column(name = "middleName")
	public String middleName;

	@Column(name = "lastName")
	public String lastName;

	@Column(name = "gender")
	public String gender;

	@Column(name = "link")
	public String link;

	@Column(name = "username")
	public String username;

	@Column(name = "bio")
	public String bio;

	@Column(name = "birthday")
	public String birthday;

	@Column(name = "education")
	public String education;

	@Column(name = "email")
	public String email;

	@Column(name = "hometown")
	public String hometown;

	@Column(name = "interestedIn")
	public String interestedIn;

	@Column(name = "location")
	public String location;

	@Column(name = "political")
	public String political;

	@Column(name = "quotes")
	public String quotes;

	@Column(name = "religion")
	public String religion;

	@Column(name = "significantOther")
	public String significantOther;

	@Column(name = "website")
	public String website;

	@Column(name = "work")
	public String work;

	public static ArrayList<Friend> stalkingFriends(Context context) {
		ArrayList<Friend> stalkingFriends = Friend.query(context, Friend.class, null, "stalking==1", "name ASC");
		return stalkingFriends;
	}

	public static ArrayList<Friend> allFriends(Context context) {
		ArrayList<Friend> allFriends = Friend.query(context, Friend.class);
		return allFriends;
	}

	public ArrayList<StalkerRelationship> stalkers() {
		ArrayList<StalkerRelationship> stalkers = StalkerRelationship.query(getContext(), StalkerRelationship.class, null, "toFriend==" + getId() + " AND fromFriend<>" + getId(), "rank DESC");
		return stalkers;
	}

	public ArrayList<Comment> comments() {
		ArrayList<Comment> comments = Comment.query(getContext(), Comment.class, null, "friend = " + getId(), "createdTime ASC");
		return comments;
	}

	public ArrayList<Post> posts() {
		ArrayList<Post> posts = Post.query(getContext(), Post.class, null, "from = " + getId(), "updatedTime ASC");
		return posts;
	}

	public Friend(Context context) {
		super(context);
	}

	public Friend(Context context, JSONObject object) {
		super(context);
		
		this.stalking = false;
		this.frienemyStatus = 0;
		this.isCurrentUser = false;
		
		try {
			this.uid = object.getString("id");
		} catch (JSONException e) {

		}
		try {
			this.name = object.getString("name");
		} catch (JSONException e) {

		}
		try {
			this.firstName = object.getString("first_name");
		} catch (JSONException e) {

		}
		try {
			this.middleName = object.getString("middle_name");
		} catch (JSONException e) {

		}
		try {
			this.lastName = object.getString("last_name");
		} catch (JSONException e) {

		}
		try {
			this.gender = object.getString("gender");
		} catch (JSONException e) {

		}
		try {
			this.link = object.getString("link");
		} catch (JSONException e) {

		}
		try {
			this.username = object.getString("username");
		} catch (JSONException e) {

		}
		try {
			this.bio = object.getString("bio");
		} catch (JSONException e) {

		}
		try {
			this.birthday = object.getString("birthday");
		} catch (JSONException e) {

		}
		try {
			this.education = object.getJSONArray("education").toString();
		} catch (JSONException e) {

		}
		try {
			this.email = object.getString("email");
		} catch (JSONException e) {

		}
		try {
			this.hometown = object.getString("hometown");
		} catch (JSONException e) {

		}
		try {
			this.interestedIn = object.getString("interested_in");
		} catch (JSONException e) {

		}
		try {
			this.location = object.getString("location");
		} catch (JSONException e) {

		}
		try {
			this.political = object.getString("political");
		} catch (JSONException e) {

		}
		try {
			this.quotes = object.getString("quotes");
		} catch (JSONException e) {

		}
		try {
			String relationshipStatus = object.getString("relationship_status");
			if (relationshipStatus.equals(this.relationshipStatus) == false) {
				this.relationshipStatusChanged = true;
				RelationshipChangedListString+=this.name + "-";
			}
			this.relationshipStatus = relationshipStatus;
		} catch (JSONException e) {

		}
		try {
			this.religion = object.getString("religion");
		} catch (JSONException e) {

		}
		try {
			this.significantOther = object.getString("significant_other");
		} catch (JSONException e) {

		}
		try {
			this.website = object.getString("website");
		} catch (JSONException e) {

		}
		try {
			this.work = object.getJSONArray("work").toString();
		} catch (JSONException e) {

		}
	}

	public static Friend friendInContextForKeyWithStringValue(Context context, String key, String value) {
		return querySingle(context, Friend.class, null, String.format("%s = %s", key, value));
	}

	public static Friend friendInContextForJSONObject(Context context, JSONObject object) {
		Friend friend = null;
		RelationshipChangedListString="";
		try {
			String uid = object.getString("id");
			friend = friendInContextForKeyWithStringValue(context, "uid", uid);
			if (friend == null) {
				friend = new Friend(context);
				friend.uid = uid;
				friend.frienemyStatus = 0;
				friend.isCurrentUser = false;
				friend.stalking = false;
			}
			try {
				friend.name = object.getString("name");
			} catch (JSONException e) {

			}
			try {
				friend.firstName = object.getString("first_name");
			} catch (JSONException e) {

			}
			try {
				friend.middleName = object.getString("middle_name");
			} catch (JSONException e) {

			}
			try {
				friend.lastName = object.getString("last_name");
			} catch (JSONException e) {

			}
			try {
				friend.gender = object.getString("gender");
			} catch (JSONException e) {

			}
			try {
				friend.link = object.getString("link");
			} catch (JSONException e) {

			}
			try {
				friend.username = object.getString("username");
			} catch (JSONException e) {

			}
			try {
				friend.bio = object.getString("bio");
			} catch (JSONException e) {

			}
			try {
				friend.birthday = object.getString("birthday");
			} catch (JSONException e) {

			}
			try {
				friend.education = object.getJSONArray("education").toString();
			} catch (JSONException e) {

			}
			try {
				friend.email = object.getString("email");
			} catch (JSONException e) {

			}
			try {
				friend.hometown = object.getString("hometown");
			} catch (JSONException e) {

			}
			try {
				friend.interestedIn = object.getString("interested_in");
			} catch (JSONException e) {

			}
			try {
				friend.location = object.getString("location");
			} catch (JSONException e) {

			}
			try {
				friend.political = object.getString("political");
			} catch (JSONException e) {

			}
			try {
				friend.quotes = object.getString("quotes");
			} catch (JSONException e) {

			}
			try {
				String relationshipStatus = object.getString("relationship_status");
				if (relationshipStatus.equals(friend.relationshipStatus) == false) {
					friend.relationshipStatusChanged = true;
					RelationshipChangedListString+=friend.name + "-";
				}
				friend.relationshipStatus = relationshipStatus;
			} catch (JSONException e) {

			}
			try {
				friend.religion = object.getString("religion");
			} catch (JSONException e) {

			}
			try {
				friend.significantOther = object.getString("significant_other");
			} catch (JSONException e) {

			}
			try {
				friend.website = object.getString("website");
			} catch (JSONException e) {

			}
			try {
				friend.work = object.getJSONArray("work").toString();
			} catch (JSONException e) {

			}
			try {
				friend.save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (JSONException e) {

		}
		return friend;
	}

	public URL getProfileImageURL() {
		URL url = null;
		try {
			url = new URL(String.format("https://graph.facebook.com/%s/picture", uid));
		} catch (MalformedURLException e) {

		}
		return url;
	}

	public URL getLargeProfileImageURL() {
		URL url = null;
		try {
			url = new URL(String.format("https://graph.facebook.com/%s/picture?type=large", uid));
		} catch (MalformedURLException e) {

		}
		return url;
	}

	public String getObjectURLString() {
		return String.format("https://graph.facebook.com/%s/?fields=id,name,first_name,middle_name,last_name,gender,link,username,bio,birthday,education,email,hometown,interested_in,location,political,quotes,relationship_status,religion,significant_other,website,work", uid);
	}

	public static String[] getList()
	{
		String [] list = RelationshipChangedListString.split("-");
		return list;
	}

}
