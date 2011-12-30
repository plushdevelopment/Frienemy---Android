package com.frienemy.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.widget.Toast;

import com.android.vending.licensing.AESObfuscator;
import com.android.vending.licensing.LicenseChecker;
import com.android.vending.licensing.LicenseCheckerCallback;
import com.android.vending.licensing.ServerManagedPolicy;
import com.flurry.android.FlurryAgent;

public class LicenseCheckActivity extends Activity {
   private class MyLicenseCheckerCallback implements LicenseCheckerCallback {
      public void allow() {
         if (isFinishing()) {
            // Don't update UI if Activity is finishing.
            return;
         }
         // Should allow user access.
         startMainActivity();

      }

      public void applicationError(ApplicationErrorCode errorCode) {
         if (isFinishing()) {
            // Don't update UI if Activity is finishing.
            return;
         }
         // This is a polite way of saying the developer made a mistake
         // while setting up or calling the license checker library.
         // Please examine the error code and fix the error.
         toast("Error: " + errorCode.name());
         startMainActivity();

      }

      public void dontAllow() {
         if (isFinishing()) {
            // Don't update UI if Activity is finishing.
            return;
         }

         // Should not allow access. In most cases, the app should assume
         // the user has access unless it encounters this. If it does,
         // the app should inform the user of their unlicensed ways
         // and then either shut down the app or limit the user to a
         // restricted set of features.
         // In this example, we show a dialog that takes the user to Market.
         //showDialog(0);
         startMainActivity();
      }
   }
   private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2suz1G622Qu1z5XINL8QB/AeZmBOOByV6GxWQJ53M5aeUHFEvwKQMGNNZ7CQLSkSRIKkvjtWRFQ7bkOvauDAwhw/TBKyK0y+j/diIMc+7W0uAshIay0XIR0+rWCqcje1cxGk6d77uNYUDF7OiPoL9Xm/AZ0/mtyLz4Z3hNRA0D43M9T6z7qxcBPkD35napafRZXtadrQ5siH+FDlyCHyu9I65Dmqtar+9VLE9/nEHZfU65sqblrZaWs4A3WrePSRU/6SkTg0OfIBeIv7YF0qt/SHgmdeWgvcUKNOitFyqlSVrw3BTFD/fjGJWj8x14dxlFS76YrHmIOWazy6sakbEQIDAQAB";

   private static final byte[] SALT = new byte[] {
       -4, 60, 32, -38, -3, -57, 74, -64, 51, 89, -95, -43, 24, -117, -36, -113, -11, 33, -64,
       90
   };
   private LicenseChecker mChecker;

   // A handler on the UI thread.

   private LicenseCheckerCallback mLicenseCheckerCallback;

   private void doCheck() {

      mChecker.checkAccess(mLicenseCheckerCallback);
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      // Try to use more data here. ANDROID_ID is a single point of attack.
      String deviceId = Secure.getString(getContentResolver(),
            Secure.ANDROID_ID);

      // Library calls this when it's done.
      mLicenseCheckerCallback = new MyLicenseCheckerCallback();
      // Construct the LicenseChecker with a policy.
      mChecker = new LicenseChecker(this, new ServerManagedPolicy(this,
            new AESObfuscator(SALT, getPackageName(), deviceId)),
            BASE64_PUBLIC_KEY);
      doCheck();

   }
   
   @Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, "EB7H7EBXI7Z7CM21DJSM");
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

   @Override
   protected Dialog onCreateDialog(int id) {
      // We have only one dialog.
      return new AlertDialog.Builder(this)
            .setTitle("Application Not Licensed")
            .setCancelable(false)
            .setMessage(
                  "This application is not licensed. Please purchase it from Android Market")
            .setPositiveButton("Buy App",
                  new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog,
                           int which) {
                        Intent marketIntent = new Intent(
                              Intent.ACTION_VIEW,
                              Uri.parse("http://market.android.com/details?id="
                                    + getPackageName()));
                        startActivity(marketIntent);
                        finish();
                     }
                  })
            .setNegativeButton("Exit",
                  new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog,
                           int which) {
                        finish();
                     }
                  }).create();
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
      mChecker.onDestroy();
   }

   private void startMainActivity() {
      startActivity(new Intent(this, FriendsActivity.class));
      finish();
   }

   public void toast(String string) {
      Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
   }

}