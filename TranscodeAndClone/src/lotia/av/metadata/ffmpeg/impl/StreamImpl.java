package lotia.av.metadata.ffmpeg.impl;

import java.util.HashMap;

import lotia.av.metadata.ffmpeg.Stream;

public class StreamImpl extends Stream {

	private static final long serialVersionUID = -4446763445082591484L;

	public void setCodecName(String codecName) {
		this.codecName = codecName;
	}
	public void setCodecLongName(String codecLongName) {
		this.codecLongName = codecLongName;
	}
	public void setCodecType(Type codecType) {
		this.codecType = codecType;
	}
	public void setCodecTimeBase(String codecTimeBase) {
		this.codecTimeBase = codecTimeBase;
	}
	public void setCodecTagString(String codecTagString) {
		this.codecTagString = codecTagString;
	}
	public void setCodecTag(long codecTag) {
		this.codecTag = codecTag;
	}
	public void setSampleRate(long sampleRate) {
		this.sampleRate = sampleRate;
	}
	public void setChannels(int channels) {
		this.channels = channels;
	}
	public void setBitsPerSample(int bitsPerSample) {
		this.bitsPerSample = bitsPerSample;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public void setHasBFrames(boolean hasBFrames) {
		this.hasBFrames = hasBFrames;
	}
	public void setPixFmt(String pixFmt) {
		this.pixFmt = pixFmt;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public void setrFrameRate(String rFrameRate) {
		this.rFrameRate = rFrameRate;
	}
	public void setAvgFrameRate(String avgFrameRate) {
		this.avgFrameRate = avgFrameRate;
	}
	public void setTimeBase(String timeBase) {
		this.timeBase = timeBase;
	}
	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
	public void setNumFrames(long numFrames) {
		this.numFrames = numFrames;
	}
	public HashMap<String,String> getTagMap() {
		return tagMap;
	}
	public void setTagMap(HashMap<String,String> tagMap) {
		this.tagMap = tagMap;
	}
	public void setTag(String key, String value) {
		tagMap.put(key, value);
	}
}
