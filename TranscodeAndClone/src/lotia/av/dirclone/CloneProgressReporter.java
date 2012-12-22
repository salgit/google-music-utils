package lotia.av.dirclone;

import java.nio.file.Path;

public interface CloneProgressReporter {
	
	public enum EventType {
		DIRECTORY_CREATE,
		DIRECTORY_SKIP_IGNORE,
		DIRECTORY_DELETE,
		
		FILE_UPDATE_EXISTING_BEGIN,
		FILE_UPDATE_EXISTING_END,
		FILE_CREATE_NEW_BEGIN,
		FILE_CREATE_NEW_END,
		FILE_SKIP_SAME,
		FILE_SKIP_NEWER,
		FILE_SKIP_IGNORE,
		FILE_COPY,
		FILE_HARDLINK,
		FILE_TRANSCODE,
		FILE_DELETE,
		
		ERROR
	}
	
	void reportProgress(EventType evt, Path src, Path dest, Throwable errorReason);

}
