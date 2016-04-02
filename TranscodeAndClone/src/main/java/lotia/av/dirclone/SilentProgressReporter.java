package lotia.av.dirclone;

import java.nio.file.Path;

public class SilentProgressReporter implements CloneProgressReporter {

	@Override
	public void reportProgress(EventType evt, Path src, Path dest, Throwable errorReason) {
	}

}
