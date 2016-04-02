package lotia.av.metadata.ffmpeg.impl;

import java.util.HashMap;

import lotia.av.metadata.ffmpeg.ContainerFormat;

public class ContainerFormatImpl extends ContainerFormat {

	private static final long serialVersionUID = -5945255397123060490L;

	public void setNumStreams(int numStreams) {
		this.numStreams = numStreams;
	}
	public void addFormatName(String formatName) {
		this.formatNames.add(formatName);
	}
	public void setFormatLongName(String formatLongName) {
		this.formatLongName = formatLongName;
	}
	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public void setBitRate(double bitRate) {
		this.bitRate = bitRate;
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
