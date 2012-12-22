package lotia.av.metadata.ffmpeg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ContainerFormat {
	
	public int getNumStreams() {
		return numStreams;
	}
	public boolean hasFormatName(String name) {
		return formatNames.contains(name);
	}
	public Iterator<String> getFormatNameIterator() {
		return formatNames.iterator();
	}
	public String getFormatLongName() {
		return formatLongName;
	}
	public double getStartTime() {
		return startTime;
	}
	public double getDuration() {
		return duration;
	}
	public long getSize() {
		return size;
	}
	public double getBitRate() {
		return bitRate;
	}
	public boolean hasTag(String key) {
		return tagMap.containsKey(key);
	}
	public String getTag(String key) {
		return tagMap.get(key);
	}

	protected int numStreams = -1;
	protected HashSet<String> formatNames = new HashSet<String>();
	protected String formatLongName;
	protected double startTime = -1;
	protected double duration = -1; 
	protected long size = -1; 
	protected double bitRate = -1;
	protected HashMap<String,String> tagMap = new HashMap<String,String>();
}
