package lotia.av.metadata.ffmpeg;

import java.io.IOException;
import java.nio.file.Path;

import lotia.av.metadata.ffmpeg.impl.FFMPEGMetadataExtractorImpl;

public class FFMPEGMetadataExtractor {
	
	static public FFMPEGMetadata extractMetadataForFile(Path f) throws IOException, InvalidFFProbeOutput, InterruptedException {
		return FFMPEGMetadataExtractorImpl.extractMetadataForFile(f);
	}

}
