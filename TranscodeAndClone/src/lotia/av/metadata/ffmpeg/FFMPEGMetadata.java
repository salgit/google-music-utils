package lotia.av.metadata.ffmpeg;

import java.util.ArrayList;
import java.util.Iterator;

public class FFMPEGMetadata {
	
	public ContainerFormat getContainerFormat() {
		return containerFormat;
	}

	public Iterator<Stream> getStreamIterator() {
		return streams.iterator();
	}
	
	public int getNumStreams() {
		return streams.size();
	}
	
	public Stream getStream(int i) {
		return streams.get(i);
	}
	
	protected ContainerFormat containerFormat;
	protected ArrayList<Stream> streams = new ArrayList<Stream>();
}
