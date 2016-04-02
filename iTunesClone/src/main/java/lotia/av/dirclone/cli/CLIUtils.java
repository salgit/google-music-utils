package lotia.av.dirclone.cli;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;
import lotia.av.metadata.ffmpeg.FFMPEGAudioTranscoder;

import java.io.File;

public class CLIUtils {

	public static class CodecConverter implements IStringConverter<FFMPEGAudioTranscoder.Codec> {
		public FFMPEGAudioTranscoder.Codec convert(String value) {
			switch (value.toLowerCase()) {
			case "mp3":
				return FFMPEGAudioTranscoder.Codec.CODEC_MP3;
			case "aac":
				return FFMPEGAudioTranscoder.Codec.CODEC_AAC;
			case "flac":
				return FFMPEGAudioTranscoder.Codec.CODEC_FLAC;
			default:
				throw new RuntimeException("Invalid codec: " + value);
			}
		}
	}
	
	public static class CodecValidator implements IParameterValidator {
		@Override
		public void validate(String name, String value)
				throws ParameterException {
            String lowerCaseValue = value.toLowerCase();
			if (!lowerCaseValue.equals("mp3") && !lowerCaseValue.equals("aac") && !lowerCaseValue.equals("flac")) {
				throw new ParameterException("Parameter " + name + " should be one of 'MP3', 'AAC' or 'FLAC'.");
			}
		}
	}
	
	public static class DirectoryThatExists implements IParameterValidator {
		@Override
		public void validate(String name, String value)
				throws ParameterException {
			File f = new File(value);
			if (!f.exists() || !f.isDirectory()) {
				throw new ParameterException("The directory '" + value + "' specifid by parameter " + name + " does not exist or is not a directory.");
			}			
		}
	}
	
	public static class FileThatExists implements IParameterValidator {
		@Override
		public void validate(String name, String value)
				throws ParameterException {
			File f = new File(value);
			if (!f.exists() || !f.isFile()) {
				throw new ParameterException("The file '" + value + "' specifid by parameter " + name + " does not exist or is not a file.");
			}			
		}
	}

    public static class LogLevelValidator implements IValueValidator<Integer> {
        @Override
        public void validate(String name, Integer value) throws ParameterException {
            if (value < 0 || value > 2)
                throw new ParameterException("Log level must be 0 through 2 (inclusive).");
        }
    }
}
