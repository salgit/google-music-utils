package lotia.av.metadata.ffmpeg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import lotia.av.metadata.ffmpeg.impl.FFMPEGMetadataParserImpl;

public class FFMPEGMetadataParser {
	
	static FFMPEGMetadata parseMetadata(String file) throws IOException, InvalidFFProbeOutput {
		return parseMetadata(new FileInputStream(file));
	}
	
	static FFMPEGMetadata parseMetadata(File file) throws IOException, InvalidFFProbeOutput {
		return parseMetadata(new FileInputStream(file));
	}
	
	static public FFMPEGMetadata parseMetadata(InputStream in) throws IOException, InvalidFFProbeOutput {
		return FFMPEGMetadataParserImpl.parseMetadata(in);
	}
}
