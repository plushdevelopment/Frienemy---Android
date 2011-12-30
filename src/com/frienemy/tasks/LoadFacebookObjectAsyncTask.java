package com.frienemy.tasks;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.Util;

import android.os.AsyncTask;

public class LoadFacebookObjectAsyncTask extends AsyncTask<String, Void, JSONObject> {

	public interface LoadFacebookObjectAsyncTaskResponder {
		public void objectLoading();
		public void objectLoadCancelled();
		public void objectLoaded(JSONObject object);
	}
	
	private LoadFacebookObjectAsyncTaskResponder responder;
	
	public LoadFacebookObjectAsyncTask(LoadFacebookObjectAsyncTaskResponder responder) {
		this.responder = responder;
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		// TODO Auto-generated method stub
		String ret = "";
		try {
			ret = Util.openUrl(params[0], "GET", null);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject object = null;
		try {
			object = new JSONObject(ret);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}
	
	@Override
	  protected void onPreExecute() {
	    super.onPreExecute();
	    responder.objectLoading();
	  }

	  @Override
	  protected void onCancelled() {
	    super.onCancelled();
	    responder.objectLoadCancelled();
	  }

	  @Override
	  protected void onPostExecute(JSONObject object) {
	    super.onPostExecute(object);
	    responder.objectLoaded(object);
	  }

}
