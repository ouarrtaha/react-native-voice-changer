package com.reactlibrary.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * IOUtils
 * 
 * @author DOBAO 3/4/2012
 */

public class IOUtils {
	private final static String TAG = IOUtils.class.getSimpleName();

	public static void copyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		}
		catch (Exception ex) {
		}
	}

	public static boolean copyFileAssetsToSdCard(Context mContext, String mAssetsNameFile, String mOutputNameFile, String mDesPath) {
		try {
			AssetManager am = mContext.getAssets();
			OutputStream os = new FileOutputStream(new File(mDesPath, mOutputNameFile));
			byte[] b = new byte[1024];
			int r = 0;
			InputStream is = am.open(mAssetsNameFile);
			while ((r = is.read(b)) != -1) {
				os.write(b, 0, r);
			}
			is.close();
			os.close();
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void writeStream(InputStream is, OutputStream os) {
		final int bufferSize = 1024;
		try {
			byte[] bytes = new byte[bufferSize];
			for (;;) {
				int count = is.read(bytes, 0, bufferSize);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void writeString(String mDirectory, String mNameFile, String mStrData) {
		if (mDirectory == null || mNameFile == null || mStrData == null) {
			new Exception(TAG + ": Some content can not null").printStackTrace();
			return;
		}
		File mFile = new File(mDirectory);
		if ((!mFile.exists())) {
			mFile.mkdirs();
		}
		try {
			File newTextFile = new File(mDirectory, mNameFile);
			FileWriter fw = new FileWriter(newTextFile, false);
			fw.write(mStrData);
			fw.close();
		}
		catch (IOException iox) {
			iox.printStackTrace();
		}
	}

	public static String readString(Context mContext, String mDirectory, String mNameFile) {
		try {
			File mFile = new File(mDirectory, mNameFile);
			if (mFile.exists()) {
				FileInputStream fstream = new FileInputStream(mFile);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				StringBuilder mStrBuilder = new StringBuilder();

				String strLine = null;
				while ((strLine = br.readLine()) != null) {
					mStrBuilder.append(strLine + "\n");
				}
				br.close();
				in.close();
				return mStrBuilder.toString();
			}
		}
		catch (IOException e) {
			Log.e(TAG, "--->error when read string" + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	public static String readStringFromAssets(Context mContext, String mNameFile) {
		try {
			InputStream mInputStream = mContext.getAssets().open(mNameFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(mInputStream));
			StringBuilder contents = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				contents.append(line);
				contents.append("\n");
			}
			return contents.toString();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void writeStrToSdCard(String mData, String mPath, String mNameFile) {
		if (ApplicationUtils.hasSDcard()) {
			DBLog.d("IOUTils", mData);
			String mSdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
			File mDirFile = new File(mSdCardPath + mPath);
			if (!mDirFile.exists()) {
				mDirFile.mkdirs();
			}
			try {
				File mFile = new File(mDirFile.getAbsolutePath() + "/" + mNameFile);
				BufferedWriter buf = new BufferedWriter(new FileWriter(mFile, false));
				buf.write(mData);
				buf.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void cacheString(Context context, String mData, String mPath, String mNameFile) {
		File mDirFile = null;
		DBLog.d("IOUTils", mData);
		if (ApplicationUtils.hasSDcard()) {
			String mSdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
			mDirFile = new File(mSdCardPath + mPath);
			if (!mDirFile.exists()) {
				mDirFile.mkdirs();
			}
		}
		else {
			mDirFile = context.getCacheDir();
		}
		if (mDirFile == null) {
			new Exception(TAG + " readCacheString:dicach is null").printStackTrace();
			return;
		}
		try {
			File mFile = new File(mDirFile.getAbsolutePath(), mNameFile);
			BufferedWriter buf = new BufferedWriter(new FileWriter(mFile, true));
			buf.append(mData);
			buf.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String readCacheString(Context context, String mPath, String mNameFile) {
		File mDirFile = null;
		if (ApplicationUtils.hasSDcard()) {
			String mSdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
			mDirFile = new File(mSdCardPath + mPath);
			if (!mDirFile.exists()) {
				mDirFile.mkdirs();
			}
		}
		else {
			mDirFile = context.getCacheDir();
		}

		if (mDirFile == null) {
			new Exception(TAG + " readCacheString:dicach is null").printStackTrace();
			return null;
		}
		File mFile = new File(mDirFile, mNameFile);
		if (mFile.exists()) {
			FileInputStream fstream;
			try {
				fstream = new FileInputStream(mFile);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				StringBuilder mStrBuilder = new StringBuilder();

				String strLine = null;
				while ((strLine = br.readLine()) != null) {
					mStrBuilder.append(strLine + "\n");
				}
				br.close();
				in.close();
				DBLog.d(TAG, "--------->dataOutReader=" + mStrBuilder.toString());
				return mStrBuilder.toString();
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * clear cache
	 */
	public static void deleteAllFileInDirectory(File mCacheDir) {
		if (!mCacheDir.exists()) {
			return;
		}
		File[] files = mCacheDir.listFiles();
		int size = files.length;
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				File f = files[i];
				f.delete();
			}
		}
	}

	public static void clearCache(Context mContext, String dirCache) {
		File cacheDir;
		if (ApplicationUtils.hasSDcard()) {
			cacheDir = new File(Environment.getExternalStorageDirectory(), dirCache);
		}
		else {
			cacheDir = mContext.getCacheDir();
		}
		if (!cacheDir.exists()) {
			return;
		}
		deleteAllFileInDirectory(cacheDir);
	}

	public static File getDiskCacheDir(Context context, String uniqueName) {
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath()
				: context.getCacheDir().getPath();

		return new File(cachePath + File.separator + uniqueName);
	}

	@TargetApi(9)
	public static boolean isExternalStorageRemovable() {
		if (hasGingerbread()) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

	@TargetApi(8)
	public static File getExternalCacheDir(Context context) {
		if (hasFroyo()) {
			return context.getExternalCacheDir();
		}

		// Before Froyo we need to construct the external cache dir ourselves
		final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
	}
	
	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}
}
