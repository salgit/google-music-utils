package lotia.av.dirclone;

import java.io.PrintStream;
import java.nio.file.Path;

public class CountingProgressReporter implements CloneProgressReporter {
	
	private class Counts {
		public int transcodes = 0;
		public int hardLinks = 0;
		public int copies = 0;
	}
	
	private Counts m_newFileCounts = new Counts();
	private Counts m_updatedFileCounts = new Counts();
	private int m_nSkippedTargetSame = 0;
	private int m_nSkippedTargetNewer = 0;
	private int m_nSkippedIgnore = 0;
	private int m_nFileDeletes = 0;
	private int m_nDirCreates = 0;
	private int m_nDirDeletes = 0;
	private int m_nDirSkipIgnores = 0;
	private int m_nErrors;
	
	private boolean m_bInUpdate = true;
	
	public CountingProgressReporter() {
	}
	
	public void printStatistics(PrintStream out) {
		out.println("New transcodes:         " + m_newFileCounts.transcodes);
		out.println("New hard links:         " + m_newFileCounts.hardLinks);
		out.println("New copies:             " + m_newFileCounts.copies);
		out.println("Updated transcodes:     " + m_updatedFileCounts.transcodes);
		out.println("Updated hard links:     " + m_updatedFileCounts.hardLinks);
		out.println("Updated copies:         " + m_updatedFileCounts.copies);
		out.println("Skipped (target same):  " + m_nSkippedTargetSame);
		out.println("Skipped (target newer): " + m_nSkippedTargetNewer);
		out.println("Skipped (ignore):       " + m_nSkippedIgnore);
		out.println("Deleted files:          " + m_nFileDeletes);
		out.println("Directories created:    " + m_nDirCreates);
		out.println("Directories deleted:    " + m_nDirDeletes);
		out.println("Directories skipped:    " + m_nDirSkipIgnores);
		out.println("Errors:                 " + m_nErrors);
	}
	
	@Override
	public void reportProgress(EventType evt, Path src, Path dest, Throwable errorReason) {
		switch (evt) {
			case FILE_UPDATE_EXISTING_BEGIN: {
				m_bInUpdate = true;
				break;
			}
			case FILE_UPDATE_EXISTING_END: {
				m_bInUpdate = false;
				break;
			}
			case FILE_CREATE_NEW_BEGIN: {
				m_bInUpdate = false;
				break;
			}
			case FILE_CREATE_NEW_END: {
				m_bInUpdate = false;
				break;
			}
			
			case FILE_SKIP_SAME: {
				++m_nSkippedTargetSame;
				break;
			}
			case FILE_SKIP_NEWER: {
				++m_nSkippedTargetNewer;
				break;
			}
			case FILE_SKIP_IGNORE: {
				++m_nSkippedIgnore;
				break;
			}
			
			case FILE_COPY: {
				if (m_bInUpdate)
					++m_updatedFileCounts.copies;
				else
					++m_newFileCounts.copies;
				break;
			}
			case FILE_HARDLINK: {
				if (m_bInUpdate)
					++m_updatedFileCounts.hardLinks;
				else
					++m_newFileCounts.hardLinks;
				break;
			}
			case FILE_TRANSCODE: {
				if (m_bInUpdate)
					++m_updatedFileCounts.transcodes;
				else
					++m_newFileCounts.transcodes;
				break;
			}
			
			case FILE_DELETE: {
				++m_nFileDeletes;
				break;
			}
			
			case DIRECTORY_CREATE: {
				++m_nDirCreates;
				break;
			}
			case DIRECTORY_DELETE: {
				++m_nDirDeletes;
				break;
			}
			case DIRECTORY_SKIP_IGNORE: {
				++m_nDirSkipIgnores;
				break;
			}
			
			case ERROR: {
				++m_nErrors;
				break;
			}
		}
	}

}
