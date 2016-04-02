package lotia.av.metadata.ffmpeg;

public class UnableToConvertMedia extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7124393305671533560L;

	private static String baseError(String src, String dest) {
		return "Unable to convert media file " + src + " to " + dest + ".";
	}
	
	public UnableToConvertMedia(String src, String dest) {
		super(baseError(src, dest));
	}
	
	public UnableToConvertMedia(String src, String dest, String error) {
		super(baseError(src, dest) + " " + error);
	}

}
