package lotia.av.dirclone;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class LinkOrCopyHandler implements FileHandler {
	
	private CloneProgressReporter m_progress;
	
	public LinkOrCopyHandler(CloneProgressReporter progress) {
		m_progress = progress;
	}

	@Override
	public Path canHandleFile(Path srcFile, BasicFileAttributes srcAttrs, Path destCandidate) throws FileCloningError {
		// can handle any type of file
		return destCandidate;
	}

	@Override
	public Path processFile(Path srcFile, BasicFileAttributes srcAttrs, Path destCandidate, RunType runType) throws FileCloningError {
		// TODO: If src and dest are different filesystems then do a copy instead of a link
		try {
			// attempt to remove any existing file which is present ?? is this the right thing to do ??
			if (Files.exists(destCandidate, LinkOption.NOFOLLOW_LINKS)) {
				if (runType == RunType.THE_REAL_DEAL)
					Files.delete(destCandidate);
				m_progress.reportProgress(CloneProgressReporter.EventType.FILE_DELETE, destCandidate, null, null);
			}
			if (runType == RunType.THE_REAL_DEAL)
				Files.createLink(destCandidate, srcFile);
			m_progress.reportProgress(CloneProgressReporter.EventType.FILE_HARDLINK, srcFile, destCandidate, null);
		} catch (IOException e) {
			throw new FileCloningError(srcFile, destCandidate, e);
		}
		return destCandidate;
	}

}
