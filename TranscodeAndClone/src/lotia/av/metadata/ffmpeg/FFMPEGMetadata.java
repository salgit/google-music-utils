package lotia.av.metadata.ffmpeg;

import java.util.ArrayList;
import java.util.Iterator;

public class FFMPEGMetadata implements java.io.Serializable {
	
	private static final long serialVersionUID = -2485690805133516281L;

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
