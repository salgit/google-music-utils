package lotia.av.dirclone;

import java.nio.file.Path;
import java.util.Vector;

public class MultiProgressReporter implements CloneProgressReporter {
	
	private Vector<CloneProgressReporter> m_vReporters = new Vector<CloneProgressReporter>(); 
	
	public MultiProgressReporter(CloneProgressReporter... reporters) {
		for (CloneProgressReporter reporter: reporters) {
			m_vReporters.add(reporter);
		}
	}

	@Override
	public void reportProgress(EventType evt, Path src, Path dest, Throwable errorReason) {

		for (CloneProgressReporter reporter: m_vReporters) {
			reporter.reportProgress(evt,  src,  dest, errorReason);
		}
	}

}
