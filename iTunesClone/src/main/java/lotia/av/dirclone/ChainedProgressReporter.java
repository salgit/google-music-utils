package lotia.av.dirclone;

import java.nio.file.Path;
import java.util.Vector;

public class ChainedProgressReporter implements CloneProgressReporter {
	
	private Vector<CloneProgressReporter> m_vReporters = new Vector<CloneProgressReporter>(); 
	
	public ChainedProgressReporter(CloneProgressReporter... reporters) {
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
