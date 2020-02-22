package com.reactlibrary.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * some methods for setting
 * @author DoBao
 *
 */
public class ApplicationUtils  {
	
	/**
	 * Md5 input string
	 * @param input
	 * @return md5 string 
	 */
	public static String getMd5Hash(String input) {
		try	{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1,messageDigest);
			String md5 = number.toString(16);
			while (md5.length() < 32){
				md5 = "0" + md5;
				
			}
			return md5;
		} 
		catch(NoSuchAlgorithmException e) {
			Log.e("MD5", e.getMessage());
			return null;
		}
	}
	
	/**
	 * check connection internet
	 * @param mContext
	 * @return true if connecting
	 */
	public static boolean isOnline(Context mContext) {
	    ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
	
	public static boolean hasSDcard(){
		 return android.os.Environment.getExternalStorageState().equals( 
				 android.os.Environment.MEDIA_MOUNTED);
	}
	
	public static String getDeviceId(Context mContext) {
		TelephonyManager mTelephonyMgr=(TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String mDeviceID = mTelephonyMgr.getDeviceId();
        if(mDeviceID==null || mDeviceID.equals("0")){
        	mDeviceID = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
		}
		return mDeviceID;
	}
	
	
	public static String getNameApp(Context mContext){
		final PackageManager pm = mContext.getPackageManager();
		ApplicationInfo ai;
		try {
		    ai = pm.getApplicationInfo(mContext.getPackageName(), 0);
		} catch (final NameNotFoundException e) {
		    ai = null;
		}
		final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
		return applicationName;
	}
	
	public static int getVersionCode(Context mContext){
		PackageInfo pinfo;
		try {
			pinfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			int versionNumber = pinfo.versionCode;
			return versionNumber;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static String getSignature(Context mContext) {
		try {
			PackageManager manager = mContext.getPackageManager();
			PackageInfo appInfo=manager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_SIGNATURES);
			return appInfo.signatures[0].toString();
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void hiddenVirtualKeyboard(Context mContext, View myEditText) {
		try {
			InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showVirtualKeyboad(Context mContext, EditText myEditText){
		try {
			InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(myEditText, InputMethodManager.SHOW_IMPLICIT);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getHashKey(Context mContext) {
		try {
			PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				return Base64.encodeToString(md.digest(), Base64.DEFAULT);
			}
		} 
		catch (NameNotFoundException e) {
			e.printStackTrace();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getVersionName(Context mContext) {
		PackageInfo pinfo;
		try {
			pinfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			String versionName = pinfo.versionName;
			return versionName;
		}
		catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
