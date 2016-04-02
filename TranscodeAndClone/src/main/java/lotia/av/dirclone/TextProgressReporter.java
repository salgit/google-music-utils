package lotia.av.dirclone;

import java.io.PrintStream;
import java.nio.file.Path;

public class TextProgressReporter implements CloneProgressReporter {
	
	private PrintStream m_out = System.out;
	private boolean m_bVerbose = true;
	
	public TextProgressReporter() {
	}
	
	public TextProgressReporter(PrintStream out) {
		m_out = out;
	}
	
	public TextProgressReporter(boolean bVerbose) {
		m_bVerbose = bVerbose;
	}
	
	public TextProgressReporter(PrintStream out, boolean bVerbose) {
		m_out = out;
		m_bVerbose = bVerbose;
	}

	@Override
	public void reportProgress(EventType evt, Path src, Path dest, Throwable errorReason) {
		switch (evt) {
			case DIRECTORY_CREATE: {
				m_out.println("Created directory " + dest.toString() + " from " + src.toString() + ".");
				break;
			}
			case DIRECTORY_DELETE: {
				m_out.println("Deleted directory " + src.toString() + ".");
				break;
			}
			case DIRECTORY_SKIP_IGNORE: {
				m_out.println("Ignored source directory " + src.toString() + ".");
				break;
			}

			case FILE_UPDATE_EXISTING_BEGIN: {
				m_out.println("Begin updating existing target file " + dest.toString() + " from " + src.toString() + ".");
				break;
			}
			case FILE_UPDATE_EXISTING_END: {
				m_out.println("End updating existing target file " + dest.toString() + " from " + src.toString() + ".");
				break;
			}
			case FILE_CREATE_NEW_BEGIN: {
				m_out.println("Begin creating new target file " + dest.toString() + " from " + src.toString() + ".");
				break;
			}
			case FILE_CREATE_NEW_END: {
				m_out.println("End creating new target file " + dest.toString() + " from " + src.toString() + ".");
				break;
			}
			case FILE_SKIP_SAME: {
				if (m_bVerbose) {
					m_out.println("Skipped target file (same as source) " + dest.toString() + " with source " + src.toString() + ".");
				}
				break;
			}
			case FILE_SKIP_NEWER: {
				if (m_bVerbose) {
					m_out.println("Skipped target file (newer than source) " + dest.toString() + " with source " + src.toString() + ".");
				}
				break;
			}
			case FILE_SKIP_IGNORE: {
				m_out.println("Ignored source file " + src.toString() + ".");
				break;
			}
			case FILE_COPY: {
				m_out.println("Copied target file " + dest.toString() + " from " + src.toString() + ".");
				break;
			}
			case FILE_HARDLINK: {
				m_out.println("Hard linked target file " + dest.toString() + " from " + src.toString() + ".");
				break;
			}
			case FILE_TRANSCODE: {
				m_out.println("Transcoded target file " + dest.toString() + " from " + src.toString() + ".");
				break;
			}
			case FILE_DELETE: {
				m_out.println("Deleted file " + src.toString() + ".");
				break;
			}
			
			case ERROR: {
				m_out.println("Error while processing " + src.toString() + (errorReason != null ? ". " + errorReason.getMessage() : "."));
				break;
			}
		}
	}

}
