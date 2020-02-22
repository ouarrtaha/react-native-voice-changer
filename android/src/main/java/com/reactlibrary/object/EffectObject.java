package com.reactlibrary.object;

public class EffectObject {

	private String id;
	private String name;

	private boolean isPlaying;
	private int pitch;
	private float rate;
	private float[] reverb;
	private float[] filter;
	private float[] flange;
	private float[] chorus;
	private float[] distort;
	private boolean isReverse;
	private float[] echo;
	private float[] echo4;
	private float[] eq2;
	private float[] eq3;
	private float[] eq1;
	private float amplify;
	private float rotate;
	private float[] phaser;
	private float[] autoWah;
	private float[] compressor;
	private boolean isMix;
	private String pathMix;

	public EffectObject(String id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.isPlaying = false;
	}

	public EffectObject(String id, String name, int pitch, float rate) {
		super();
		this.id = id;
		this.name = name;
		this.pitch = pitch;
		this.rate = rate;
		this.isPlaying = false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public int getPitch() {
		return pitch;
	}

	public void setPitch(int pitch) {
		this.pitch = pitch;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public float[] getReverb() {
		return reverb;
	}

	public void setReverb(float[] reverb) {
		this.reverb = reverb;
	}

	public float[] getFlange() {
		return flange;
	}

	public void setFlange(float[] flange) {
		this.flange = flange;
	}

	public boolean isReverse() {
		return isReverse;
	}

	public void setReverse(boolean isReverse) {
		this.isReverse = isReverse;
	}

	public float[] getEcho() {
		return echo;
	}

	public void setEcho(float[] echo) {
		this.echo = echo;
	}

	public float[] getEq1() {
		return eq1;
	}

	public void setEq1(float[] eq1) {
		this.eq1 = eq1;
	}

	public float[] getFilter() {
		return filter;
	}

	public void setFilter(float[] filter) {
		this.filter = filter;
	}

	public float getAmplify() {
		return amplify;
	}

	public void setAmplify(float amplify) {
		this.amplify = amplify;
	}

	public float[] getDistort() {
		return distort;
	}

	public void setDistort(float[] distort) {
		this.distort = distort;
	}

	public float[] getChorus() {
		return chorus;
	}

	public void setChorus(float[] chorus) {
		this.chorus = chorus;
	}

	public float[] getEcho4() {
		return echo4;
	}

	public void setEcho4(float[] echo4) {
		this.echo4 = echo4;
	}

	public float[] getEq2() {
		return eq2;
	}

	public void setEq2(float[] eq2) {
		this.eq2 = eq2;
	}

	public float[] getEq3() {
		return eq3;
	}

	public void setEq3(float[] eq3) {
		this.eq3 = eq3;
	}

	public float getRotate() {
		return rotate;
	}

	public void setRotate(float rotate) {
		this.rotate = rotate;
	}

	public float[] getPhaser() {
		return phaser;
	}

	public void setPhaser(float[] phaser) {
		this.phaser = phaser;
	}

	public float[] getAutoWah() {
		return autoWah;
	}

	public void setAutoWah(float[] autoWah) {
		this.autoWah = autoWah;
	}

	public float[] getCompressor() {
		return compressor;
	}

	public void setCompressor(float[] compressor) {
		this.compressor = compressor;
	}

	public boolean isMix() {
		return isMix;
	}

	public void setMix(boolean mix) {
		isMix = mix;
	}

	public String getPathMix() {
		return pathMix;
	}

	public void setPathMix(String pathMix) {
		this.pathMix = pathMix;
	}
}
