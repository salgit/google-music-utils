package lotia.av.metadata.ffmpeg;

import java.util.HashMap;

public class Stream implements java.io.Serializable {

	private static final long serialVersionUID = 3447600858225517312L;

	public enum Type
	{
		TYPE_UNKNOWN,
		TYPE_AUDIO,
		TYPE_VIDEO,
		TYPE_SUBTITLE,
		TYPE_DATA        /* motion comics use this */
	}

	public String getCodecName() {
		return codecName;
	}
	public String getCodecLongName() {
		return codecLongName;
	}
	public Type getCodecType() {
		return codecType;
	}
	public String getCodecTimeBase() {
		return codecTimeBase;
	}
	public String getCodecTagString() {
		return codecTagString;
	}
	public long getCodecTag() {
		return codecTag;
	}
	public long getSampleRate() {
		return sampleRate;
	}
	public int getChannels() {
		return channels;
	}
	public int getBitsPerSample() {
		return bitsPerSample;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public boolean hasBFrames() {
		return hasBFrames;
	}
	public String getPixFmt() {
		return pixFmt;
	}
	public int getLevel() {
		return level;
	}
	public String getrFrameRate() {
		return rFrameRate;
	}
	public String getAvgFrameRate() {
		return avgFrameRate;
	}
	public String getTimeBase() {
		return timeBase;
	}
	public double getStartTime() {
		return startTime;
	}
	public double getDuration() {
		return duration;
	}
	public long getNumFrames() {
		return numFrames;
	}
	public boolean hasTag(String key) {
		return tagMap.containsKey(key);
	}
	public String getTag(String key) {
		return tagMap.get(key);
	}

	protected String codecName;
	protected String codecLongName;
	protected Type codecType = Type.TYPE_UNKNOWN;
	protected String codecTimeBase;
	protected String codecTagString;
	protected long codecTag = -1;
	protected long sampleRate = -1;
	protected int channels = -1;
	protected int bitsPerSample = -1;
	protected int width = -1;
	protected int height = -1;
	protected boolean hasBFrames = false;
	protected String pixFmt;
	protected int level = -1;
	protected String rFrameRate;
	protected String avgFrameRate;
	protected String timeBase;
	protected double startTime = -1; 
	protected double duration = -1; 
	protected long numFrames = -1;
	protected HashMap<String,String> tagMap = new HashMap<String,String>();
}
