package lotia.av.metadata.ffmpeg;

public class InvalidFFProbeOutput extends Exception {

	private static final long serialVersionUID = 2342040190047945400L;

	public InvalidFFProbeOutput() {
		super("Invalid output format. Unable to parse metadata.");
	}
	
	public InvalidFFProbeOutput(String sErr) {
		super((sErr != null && !sErr.trim().isEmpty()) ? "Invalid output format. Unable to parse metadata. " + sErr : "Invalid output format. Unable to parse metadata.");
	}

}
