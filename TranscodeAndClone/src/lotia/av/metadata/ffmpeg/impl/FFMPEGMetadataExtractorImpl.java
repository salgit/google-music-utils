package lotia.av.metadata.ffmpeg.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;

import lotia.av.metadata.ffmpeg.InvalidFFProbeOutput;

public class FFMPEGMetadataExtractorImpl {
	
	static final Charset s_UTF8 = Charset.forName("UTF-8");
	
	static public FFMPEGMetadataImpl extractMetadataForFile(Path f) throws IOException, InvalidFFProbeOutput, InterruptedException {
		String sEncodedFilePath = f.toString(); //escapeFilePathForShell(f.getAbsolutePath());
		ProcessBuilder pb = new ProcessBuilder("ffprobe", "-show_format", "-show_streams", sEncodedFilePath);
		Process proc = pb.start();
		InputStream stdOut = proc.getInputStream();
		try {
			FFMPEGMetadataImpl metadata = FFMPEGMetadataParserImpl.parseMetadata(stdOut);
			if (proc.waitFor() != 0) {
				String sErr = ExtractionUtils.errorString(proc.getErrorStream());
				throw new InvalidFFProbeOutput(sErr);
			}
			return metadata;
		} catch (Exception x) {
			proc.waitFor();
			String sErr = ExtractionUtils.errorString(proc.getErrorStream());
			InvalidFFProbeOutput x2 = new InvalidFFProbeOutput(sErr);
			x2.addSuppressed(x);
			throw x2;
		}
	}
}
