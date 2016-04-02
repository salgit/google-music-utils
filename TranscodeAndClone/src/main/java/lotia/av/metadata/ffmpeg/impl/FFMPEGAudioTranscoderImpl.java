package lotia.av.metadata.ffmpeg.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import lotia.av.metadata.ffmpeg.UnableToConvertMedia;

public class FFMPEGAudioTranscoderImpl {
	
	private static void runFFMPEG(String... args) throws IOException, FFMPEGError, InterruptedException  {
		
		ProcessBuilder pb = new ProcessBuilder(args);
		pb.redirectErrorStream(true);
		
		Process proc = pb.start();
			
		if (proc.waitFor() != 0) {
			InputStream stdOut = proc.getInputStream();
			String sErr = ExtractionUtils.errorString(stdOut);
			throw new FFMPEGError(sErr);
		}
	}
	
	public static void transcodeToMP3(Path src, Path dest) throws UnableToConvertMedia {
		
		try {
			runFFMPEG("ffmpeg", "-i", src.toString(), "-acodec", "libmp3lame", "-ab", "256k", dest.toString());
		} catch (Exception x) {
			UnableToConvertMedia x2 = new UnableToConvertMedia(src.toString(), dest.toString());
			x2.initCause(x);
			throw x2;
		}
	}

	public static void transcodeToFLAC(Path src, Path dest) throws UnableToConvertMedia {

		try {
			runFFMPEG("ffmpeg", "-i", src.toString(), "-acodec", "flac", "-map_metadata", "0", dest.toString());
		} catch (Exception x) {
			UnableToConvertMedia x2 = new UnableToConvertMedia(src.toString(), dest.toString());
			x2.initCause(x);
			throw x2;
		}
	}

}
