package lotia.av.dirclone;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public interface FileHandler {
	/**
	 * Returns a new resolved Path object which describes the target. Returns null
	 * if it does not handle this type of file.
	 * @param file
	 * @param attrs
	 * @param destCandidate
	 * @return
	 */	
	public Path canHandleFile(Path file, BasicFileAttributes attrs, Path destCandidate) throws FileCloningError;
	
	/**
	 * Returns a new resolved Path object which describes the target. Returns null
	 * if it does not handle this type of file. If this method succeeds, the returned
	 * path will have been successfully created. Implementations should call
	 * a progress reporter only if they successfully processed the file. In case of
	 * an error implementations should throw an exception and rely on the caller
	 * to report progress as well as perform any required cleanup.
	 * @param file
	 * @param attrs
	 * @param destCandidate
	 * @return
	 */
	public Path processFile(Path srcFile, BasicFileAttributes srcAttrs, Path destCandidate, RunType runType) throws FileCloningError;

}
