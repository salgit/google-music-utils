package lotia.av.metadata.ffmpeg.impl;

import java.util.ArrayList;

import lotia.av.metadata.ffmpeg.FFMPEGMetadata;
import lotia.av.metadata.ffmpeg.Stream;

public class FFMPEGMetadataImpl extends FFMPEGMetadata {
	
	private static final long serialVersionUID = -3228137464621533490L;

	public void setContainerFormat(ContainerFormatImpl containerFormat) {
		this.containerFormat = containerFormat;
	}
	public ArrayList<Stream> getStreams() {
		return streams;
	}
	public void setStreams(ArrayList<Stream> streams) {
		this.streams = streams;
	}
	public void addStream(StreamImpl stream) {
		streams.add(stream);
	}

}
