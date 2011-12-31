package com.frienemy.models;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
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

	@Column(name = "infoChanged")
	public boolean infoChanged;

	@Column(name = "changedFields")
	public String changedFields;

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

	public ArrayList<String> changedFieldsArray;
	
	public static ArrayList<Friend> changedFriends(Context context) {
		ArrayList<Friend> changedFriends = Friend.query(context, Friend.class, null, "infoChanged==1", "name ASC");
		return changedFriends;
	}
	
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
		this.infoChanged = false;
		this.changedFields = "";

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
			JSONObject hometownJSON = object.getJSONObject("hometown");
			this.hometown = hometownJSON.getString("name");
		} catch (JSONException e) {

		}
		try {
			JSONArray interestedInArray = object.getJSONArray("interested_in");
			if (interestedInArray.length() > 0)
				this.interestedIn = interestedInArray.getString(0);
		} catch (JSONException e) {

		}
		try {
			JSONObject locationJSON = object.getJSONObject("location");
			this.location = locationJSON.getString("name");
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
			this.relationshipStatus = object.getString("relationship_status");
		} catch (JSONException e) {

		}
		try {
			this.religion = object.getString("religion");
		} catch (JSONException e) {

		}
		try {
			JSONObject significantOtherJSON = object.getJSONObject("significant_other");
			this.significantOther = significantOtherJSON.getString("name"); 
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
			friend.changedFields = "";
			friend.changedFieldsArray = new ArrayList<String>();
			try {
				String newValue = object.getString("name");
				if (true == friend.stalking) {
					if (newValue.equals(friend.name) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Name");
					}
				}
				friend.name = newValue;
			} catch (JSONException e) {

			}
			try {
				String newValue = object.getString("first_name");
				if (true == friend.stalking) {
					if (newValue.equals(friend.firstName) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("First Name");
					}
				}
				friend.firstName = newValue;
			} catch (JSONException e) {

			}
			try {
				String newValue = object.getString("middle_name");
				if (true == friend.stalking) {
					if (newValue.equals(friend.middleName) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Middle Name");
					}
				}
				friend.middleName = newValue;
			} catch (JSONException e) {

			}
			try {
				String newValue = object.getString("last_name");
				if (true == friend.stalking) {
					if (newValue.equals(friend.lastName) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Last Name");
					}
				}
				friend.lastName = newValue;
			} catch (JSONException e) {

			}
			try {
				String newValue = object.getString("gender");
				if (true == friend.stalking) {
					if (newValue.equals(friend.gender) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Gender");
					}
				}
				friend.gender = newValue;
			} catch (JSONException e) {

			}
			try {
				String newValue = object.getString("link");
				if (true == friend.stalking) {
					if (newValue.equals(friend.link) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Link");
					}
				}
				friend.link = newValue;
			} catch (JSONException e) {

			}
			try {
				String newValue = object.getString("username");
				if (true == friend.stalking) {
					if (newValue.equals(friend.username) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Username");
					}
				}
				friend.username = newValue;
			} catch (JSONException e) {

			}
			try {
				String newValue = object.getString("bio");
				if (true == friend.stalking) {
					if (newValue.equals(friend.bio) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Bio");
					}
				}
				friend.bio = newValue;
			} catch (JSONException e) {

			}
			try {
				String newValue = object.getString("birthday");
				if (true == friend.stalking) {
					if (newValue.equals(friend.birthday) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Birthday");
					}
				}
				friend.birthday = newValue;
			} catch (JSONException e) {

			}
			try {
				String newValue = object.getString("education").toString();
				if (true == friend.stalking) {
					if (newValue.equals(friend.education) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Education");
					}
				}
				friend.education = newValue;
			} catch (JSONException e) {

			}
			try {
				String newValue = object.getString("email");
				if (true == friend.stalking) {
					if (newValue.equals(friend.email) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Email");
					}
				}
				friend.email = newValue;
			} catch (JSONException e) {

			}
			try {
				JSONObject newJSON = object.getJSONObject("hometown");
				String newValue = newJSON.getString("name");
				if (true == friend.stalking) {
					if (newValue.equals(friend.hometown) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Hometown");
					}
				}
				friend.hometown = newValue;
			} catch (JSONException e) {

			}
			try {
				JSONArray newArray = object.getJSONArray("interested_in");
				if (newArray.length() > 0) {
					String newValue = newArray.getString(0);
					if (true == friend.stalking) {
						if (newValue.equals(friend.interestedIn) == false) {
							friend.infoChanged = true;
							friend.changedFieldsArray.add("Interested In");
						}
					}
					friend.interestedIn = newValue;
				}
			} catch (JSONException e) {

			}
			try {
				JSONObject newJSON = object.getJSONObject("location");
				String newValue = newJSON.getString("name");
				if (true == friend.stalking) {
					if (newValue.equals(friend.location) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Location");
					}
				}
				friend.location = newValue;
			} catch (JSONException e) {

			}
			try {
				String newValue = object.getString("political");
				if (true == friend.stalking) {
					if (newValue.equals(friend.political) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Political");
					}
				}
				friend.political = newValue;
			} catch (JSONException e) {

			}
			try {
				String newValue = object.getString("quotes");
				if (true == friend.stalking) {
					if (newValue.equals(friend.quotes) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Quotes");
					}
				}
				friend.quotes = newValue;
			} catch (JSONException e) {

			}
			try {
				String newValue = object.getString("relationship_status");
				if (true == friend.stalking) {
					if (newValue.equals(friend.relationshipStatus) == false) {
						friend.relationshipStatusChanged = true;
						RelationshipChangedListString+=friend.name + "-";
					}
				}
				friend.relationshipStatus = newValue;
			} catch (JSONException e) {

			}
			try {
				String newValue = object.getString("religion");
				if (true == friend.stalking) {
					if (newValue.equals(friend.religion) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Religion");
					}
				}
				friend.religion = newValue;
			} catch (JSONException e) {

			}
			try {
				JSONObject newJSON = object.getJSONObject("significant_other");
				String newValue = newJSON.getString("name");
				if (true == friend.stalking) {
					if (newValue.equals(friend.significantOther) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Significant Other");
					}
				}
				friend.significantOther = newValue;
			} catch (JSONException e) {

			}
			try {
				String newValue = object.getString("website");
				if (true == friend.stalking) {
					if (newValue.equals(friend.website) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Website");
					}
				}
				friend.website = newValue;
			} catch (JSONException e) {

			}
			try {
				String newValue = object.getJSONArray("work").toString();
				if (true == friend.stalking) {
					if (newValue.equals(friend.work) == false) {
						friend.infoChanged = true;
						friend.changedFieldsArray.add("Work");
					}
				}
				friend.work = newValue;
			} catch (JSONException e) {

			}
			if (true == friend.stalking) {
				for (int i=0; i < friend.changedFieldsArray.size(); i++) {
					friend.changedFields += friend.changedFieldsArray.get(i);
					if (i != 0 && i < friend.changedFieldsArray.size()) {
						friend.changedFields += ", ";
					}
				}
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
