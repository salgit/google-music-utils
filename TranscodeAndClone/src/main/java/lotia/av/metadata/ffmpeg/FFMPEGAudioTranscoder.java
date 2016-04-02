package lotia.av.metadata.ffmpeg;

import java.io.IOException;
import java.nio.file.Path;

import lotia.av.metadata.ffmpeg.impl.FFMPEGAudioTranscoderImpl;

public class FFMPEGAudioTranscoder {
	public static void transcodeToMP3(Path src, Path dest) throws IOException, UnableToConvertMedia {
		FFMPEGAudioTranscoderImpl.transcodeToMP3(src, dest);
	}

	public static void transcodeToAAC(Path src, Path dest) throws IOException, UnableToConvertMedia {
		FFMPEGAudioTranscoderImpl.transcodeToAAC(src, dest);
	}

	public static void transcodeToFLAC(Path src, Path dest) throws IOException, UnableToConvertMedia {
		FFMPEGAudioTranscoderImpl.transcodeToFLAC(src, dest);
	}

	public static void transcodeUsingCodec(Codec codec, Path src, Path dest) throws IOException, UnableToConvertMedia {
		switch (codec) {
			case CODEC_FLAC:
				FFMPEGAudioTranscoderImpl.transcodeToFLAC(src, dest);
				break;

			case CODEC_AAC:
				FFMPEGAudioTranscoderImpl.transcodeToAAC(src, dest);
				break;

			case CODEC_MP3:
				FFMPEGAudioTranscoderImpl.transcodeToMP3(src, dest);
				break;
		}

	}

	public enum Codec {
		CODEC_MP3,
		CODEC_AAC,
		CODEC_FLAC;

		public String getExtension() {
			if (this == CODEC_FLAC)
				return "flac";
			else if (this == CODEC_MP3)
				return "mp3";
			else
				return "m4a";
		}
	}
}
