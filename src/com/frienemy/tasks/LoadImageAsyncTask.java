package com.frienemy.tasks;

import java.io.IOException;
import java.net.URL;

import com.frienemy.FrienemyApplication;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

public class LoadImageAsyncTask extends AsyncTask<URL, Void, Drawable> {

  public interface LoadImageAsyncTaskResponder {
    public void imageLoading();
    public void imageLoadCancelled();
    public void imageLoaded(Drawable drawable);
  }

  private LoadImageAsyncTaskResponder responder;

  public LoadImageAsyncTask(LoadImageAsyncTaskResponder responder) {
    this.responder = responder;
  }

  @Override
  protected Drawable doInBackground(URL... args) {
    try {
      return Drawable.createFromStream(args[0].openStream(), args[0].toString());
    } catch (IOException e) {
      Log.e(getClass().getName(), "Could not load image.", e);
      return null;
    }
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    if(FrienemyApplication.asyncTaskQueue<20)
    {
        FrienemyApplication.asyncTaskQueue+=1;
    	responder.imageLoading();
    }
    else
    {
    	responder.imageLoadCancelled();
    }
  }

  @Override
  protected void onCancelled() {
    super.onCancelled();
    responder.imageLoadCancelled();
  }

  @Override
  protected void onPostExecute(Drawable result) {
    super.onPostExecute(result);
    FrienemyApplication.asyncTaskQueue-=1;

    responder.imageLoaded(result);
  }

}
