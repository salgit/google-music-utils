package lotia.av.dirclone;

import java.nio.file.Path;

public class FileCloningError extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5329612485959575765L;
	
	public FileCloningError(Path srcFile, Path destFile) {
		super("Unable to process file " + srcFile.toString() + " to " + destFile.toString());
	}
	
	public FileCloningError(Path srcFile, Path destFile, Throwable x) {
		this(srcFile, destFile);
		initCause(x);
	}

}
