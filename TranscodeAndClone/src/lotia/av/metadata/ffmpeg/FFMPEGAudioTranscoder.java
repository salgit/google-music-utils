package lotia.av.metadata.ffmpeg;

import java.io.IOException;
import java.nio.file.Path;

import lotia.av.metadata.ffmpeg.impl.FFMPEGAudioTranscoderImpl;

public class FFMPEGAudioTranscoder {
	public static void transcodeToMP3(Path src, Path dest) throws IOException, UnableToConvertMedia {
		FFMPEGAudioTranscoderImpl.transcodeToMP3(src, dest);
	}
}
