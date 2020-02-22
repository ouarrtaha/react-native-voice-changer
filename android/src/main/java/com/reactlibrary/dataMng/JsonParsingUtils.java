package com.reactlibrary.dataMng;


import com.reactlibrary.constants.IVoiceChangerConstants;
import com.reactlibrary.object.EffectObject;
import com.reactlibrary.utils.DBLog;
import com.reactlibrary.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParsingUtils implements IVoiceChangerConstants {

	public static final String TAG = JsonParsingUtils.class.getSimpleName();

	public static EffectObject jsonToEffectObject(String data){
		if (!StringUtils.isEmptyString(data)) {
			try {
				JSONObject mJs = new JSONObject(data);
				String id = mJs.getString("id");
				String name = mJs.getString("name");
				int pitch =mJs.getInt("pitch") ;
				int rate =mJs.getInt("rate");
				boolean isReverse =false;
				if(mJs.opt("reverse")!=null){
					isReverse=mJs.getBoolean("reverse");
				}
				EffectObject mEffectObject = new EffectObject(id, name, pitch, rate);
				mEffectObject.setReverse(isReverse);
				if(mJs.opt("amplify")!=null){
					float amplify= (float) mJs.getDouble("amplify");
					mEffectObject.setAmplify(amplify);
				}
				if(mJs.opt("isMix")!=null){
					boolean isMix=mJs.getBoolean("isMix");
					mEffectObject.setMix(isMix);
					String path=mJs.getString("path");
					mEffectObject.setPathMix(path);
				}
				if(mJs.opt("rotate")!=null){
					float amplify= (float) mJs.getDouble("rotate");
					mEffectObject.setRotate(amplify);
				}

				if(mJs.opt("reverb")!=null){
					JSONArray mJArray = mJs.getJSONArray("reverb");
					int len = mJArray.length();
					if(len>0){
						float[] reverb=new float[len];
						for(int j=0;j<len;j++){
							reverb[j]=(float) mJArray.getDouble(j);
						}
						mEffectObject.setReverb(reverb);
					}
				}
				if(mJs.opt("distort")!=null){
					JSONArray mJArray = mJs.getJSONArray("distort");
					int len = mJArray.length();
					if(len>0){
						float[] distort=new float[len];
						for(int j=0;j<len;j++){
							distort[j]=(float) mJArray.getDouble(j);
						}
						mEffectObject.setDistort(distort);
					}
				}
				if(mJs.opt("chorus")!=null){
					JSONArray mJArray = mJs.getJSONArray("chorus");
					int len = mJArray.length();
					if(len>0){
						float[] chorus=new float[len];
						for(int j=0;j<len;j++){
							chorus[j]=(float) mJArray.getDouble(j);
						}
						mEffectObject.setChorus(chorus);
					}
				}
				if(mJs.opt("flanger")!=null){
					JSONArray mJArray = mJs.getJSONArray("flanger");
					int len = mJArray.length();
					if(len>0){
						float[] flange=new float[len];
						for(int j=0;j<len;j++){
							flange[j]=(float) mJArray.getDouble(j);
						}
						mEffectObject.setFlange(flange);
					}
				}
				if(mJs.opt("filter")!=null){
					JSONArray mJArray1 = mJs.getJSONArray("filter");
					int len1 = mJArray1.length();
					if(len1>0){
						float[] eq=new float[len1];
						for(int j=0;j<len1;j++){
							eq[j]=(float) mJArray1.getDouble(j);
						}
						mEffectObject.setFilter(eq);
					}
				}
				if(mJs.opt("echo")!=null){
					JSONArray mJArray1 = mJs.getJSONArray("echo");
					int len1 = mJArray1.length();
					if(len1>0){
						float[] eq=new float[len1];
						for(int j=0;j<len1;j++){
							eq[j]=(float) mJArray1.getDouble(j);
						}
						mEffectObject.setEcho(eq);
					}
				}
				if(mJs.opt("echo4")!=null){
					JSONArray mJArray1 = mJs.getJSONArray("echo4");
					int len1 = mJArray1.length();
					if(len1>0){
						float[] eq=new float[len1];
						for(int j=0;j<len1;j++){
							eq[j]=(float) mJArray1.getDouble(j);
						}
						mEffectObject.setEcho4(eq);
					}
				}
				if(mJs.opt("eq1")!=null){
					JSONArray mJArray1 = mJs.getJSONArray("eq1");
					int len1 = mJArray1.length();
					if(len1>0){
						float[] eq=new float[len1];
						for(int j=0;j<len1;j++){
							eq[j]=(float) mJArray1.getDouble(j);
						}
						mEffectObject.setEq1(eq);
					}
				}
				if(mJs.opt("eq2")!=null){
					JSONArray mJArray1 = mJs.getJSONArray("eq2");
					int len1 = mJArray1.length();
					if(len1>0){
						float[] eq=new float[len1];
						for(int j=0;j<len1;j++){
							eq[j]=(float) mJArray1.getDouble(j);
						}
						mEffectObject.setEq2(eq);
					}
				}
				if(mJs.opt("eq3")!=null){
					JSONArray mJArray1 = mJs.getJSONArray("eq3");
					int len1 = mJArray1.length();
					if(len1>0){
						float[] eq=new float[len1];
						for(int j=0;j<len1;j++){
							eq[j]=(float) mJArray1.getDouble(j);
						}
						mEffectObject.setEq3(eq);
					}
				}
				if(mJs.opt("phaser")!=null){
					JSONArray mJArray1 = mJs.getJSONArray("phaser");
					int len1 = mJArray1.length();
					if(len1>0){
						float[] eq=new float[len1];
						for(int j=0;j<len1;j++){
							eq[j]=(float) mJArray1.getDouble(j);
						}
						mEffectObject.setPhaser(eq);
					}
				}
				if(mJs.opt("autowah")!=null){
					JSONArray mJArray1 = mJs.getJSONArray("autowah");
					int len1 = mJArray1.length();
					if(len1>0){
						float[] eq=new float[len1];
						for(int j=0;j<len1;j++){
							eq[j]=(float) mJArray1.getDouble(j);
						}
						mEffectObject.setAutoWah(eq);
					}
				}
				if(mJs.opt("compressor")!=null){
					JSONArray mJArray1 = mJs.getJSONArray("compressor");
					int len1 = mJArray1.length();
					if(len1>0){
						float[] com=new float[len1];
						for(int j=0;j<len1;j++){
							com[j]=(float) mJArray1.getDouble(j);
						}
						mEffectObject.setCompressor(com);
					}
				}

				return mEffectObject;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static ArrayList<EffectObject> parsingListEffectObject(String data) {
		if (!StringUtils.isEmptyString(data)) {
			try {
				JSONArray mJsonArray =new JSONArray(data);
				int size = mJsonArray.length();
				if (size > 0) {
					ArrayList<EffectObject> mListEffectObjects = new ArrayList<EffectObject>();
					for (int i = 0; i < size; i++) {
						JSONObject mJs = mJsonArray.getJSONObject(i);
						String id = mJs.getString("id");
						String name = mJs.getString("name");
						int pitch =mJs.getInt("pitch") ;
						int rate =mJs.getInt("rate");
						boolean isReverse =false;
						if(mJs.opt("reverse")!=null){
							isReverse=mJs.getBoolean("reverse");
						}
						EffectObject mEffectObject = new EffectObject(id, name, pitch, rate);
						mEffectObject.setReverse(isReverse);
						if(mJs.opt("amplify")!=null){
							float amplify= (float) mJs.getDouble("amplify");
							mEffectObject.setAmplify(amplify);
						}
						if(mJs.opt("isMix")!=null){
							boolean isMix=mJs.getBoolean("isMix");
							mEffectObject.setMix(isMix);
							String path=mJs.getString("path");
							mEffectObject.setPathMix(path);
						}
						if(mJs.opt("rotate")!=null){
							float amplify= (float) mJs.getDouble("rotate");
							mEffectObject.setRotate(amplify);
						}
						mListEffectObjects.add(mEffectObject);

						if(mJs.opt("reverb")!=null){
							JSONArray mJArray = mJs.getJSONArray("reverb");
							int len = mJArray.length();
							if(len>0){
								float[] reverb=new float[len];
								for(int j=0;j<len;j++){
									reverb[j]=(float) mJArray.getDouble(j);
								}
								mEffectObject.setReverb(reverb);
							}
						}
						if(mJs.opt("distort")!=null){
							JSONArray mJArray = mJs.getJSONArray("distort");
							int len = mJArray.length();
							if(len>0){
								float[] distort=new float[len];
								for(int j=0;j<len;j++){
									distort[j]=(float) mJArray.getDouble(j);
								}
								mEffectObject.setDistort(distort);
							}
						}
						if(mJs.opt("chorus")!=null){
							JSONArray mJArray = mJs.getJSONArray("chorus");
							int len = mJArray.length();
							if(len>0){
								float[] chorus=new float[len];
								for(int j=0;j<len;j++){
									chorus[j]=(float) mJArray.getDouble(j);
								}
								mEffectObject.setChorus(chorus);
							}
						}
						if(mJs.opt("flanger")!=null){
							JSONArray mJArray = mJs.getJSONArray("flanger");
							int len = mJArray.length();
							if(len>0){
								float[] flange=new float[len];
								for(int j=0;j<len;j++){
									flange[j]=(float) mJArray.getDouble(j);
								}
								mEffectObject.setFlange(flange);
							}
						}
						if(mJs.opt("filter")!=null){
							JSONArray mJArray1 = mJs.getJSONArray("filter");
							int len1 = mJArray1.length();
							if(len1>0){
								float[] eq=new float[len1];
								for(int j=0;j<len1;j++){
									eq[j]=(float) mJArray1.getDouble(j);
								}
								mEffectObject.setFilter(eq);
							}
						}
						if(mJs.opt("echo")!=null){
							JSONArray mJArray1 = mJs.getJSONArray("echo");
							int len1 = mJArray1.length();
							if(len1>0){
								float[] eq=new float[len1];
								for(int j=0;j<len1;j++){
									eq[j]=(float) mJArray1.getDouble(j);
								}
								mEffectObject.setEcho(eq);
							}
						}
						if(mJs.opt("echo4")!=null){
							JSONArray mJArray1 = mJs.getJSONArray("echo4");
							int len1 = mJArray1.length();
							if(len1>0){
								float[] eq=new float[len1];
								for(int j=0;j<len1;j++){
									eq[j]=(float) mJArray1.getDouble(j);
								}
								mEffectObject.setEcho4(eq);
							}
						}
						if(mJs.opt("eq1")!=null){
							JSONArray mJArray1 = mJs.getJSONArray("eq1");
							int len1 = mJArray1.length();
							if(len1>0){
								float[] eq=new float[len1];
								for(int j=0;j<len1;j++){
									eq[j]=(float) mJArray1.getDouble(j);
								}
								mEffectObject.setEq1(eq);
							}
						}
						if(mJs.opt("eq2")!=null){
							JSONArray mJArray1 = mJs.getJSONArray("eq2");
							int len1 = mJArray1.length();
							if(len1>0){
								float[] eq=new float[len1];
								for(int j=0;j<len1;j++){
									eq[j]=(float) mJArray1.getDouble(j);
								}
								mEffectObject.setEq2(eq);
							}
						}
						if(mJs.opt("eq3")!=null){
							JSONArray mJArray1 = mJs.getJSONArray("eq3");
							int len1 = mJArray1.length();
							if(len1>0){
								float[] eq=new float[len1];
								for(int j=0;j<len1;j++){
									eq[j]=(float) mJArray1.getDouble(j);
								}
								mEffectObject.setEq3(eq);
							}
						}
						if(mJs.opt("phaser")!=null){
							JSONArray mJArray1 = mJs.getJSONArray("phaser");
							int len1 = mJArray1.length();
							if(len1>0){
								float[] eq=new float[len1];
								for(int j=0;j<len1;j++){
									eq[j]=(float) mJArray1.getDouble(j);
								}
								mEffectObject.setPhaser(eq);
							}
						}
						if(mJs.opt("autowah")!=null){
							JSONArray mJArray1 = mJs.getJSONArray("autowah");
							int len1 = mJArray1.length();
							if(len1>0){
								float[] eq=new float[len1];
								for(int j=0;j<len1;j++){
									eq[j]=(float) mJArray1.getDouble(j);
								}
								mEffectObject.setAutoWah(eq);
							}
						}
						if(mJs.opt("compressor")!=null){
							JSONArray mJArray1 = mJs.getJSONArray("compressor");
							int len1 = mJArray1.length();
							if(len1>0){
								float[] com=new float[len1];
								for(int j=0;j<len1;j++){
									com[j]=(float) mJArray1.getDouble(j);
								}
								mEffectObject.setCompressor(com);
							}
						}

					}
					DBLog.d(TAG, "===================>size effect ="+mListEffectObjects.size());
					return mListEffectObjects;
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
