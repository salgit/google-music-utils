package lotia.av.dirclone;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClonerFileVisitor implements FileVisitor<Path> {
	
	private RunType m_runType;
	private Path m_srcRoot;
	private Path m_destRoot;
	private CloneProgressReporter m_progress;
	private ArrayList<FileHandler> m_fileHandlers = new ArrayList<FileHandler>();
	private Stack<HashSet<String>> m_filesVisited = new Stack<HashSet<String>>();
	private ArrayList<Pattern> m_ignoreSrc = new ArrayList<Pattern>();
	
	public ClonerFileVisitor(RunType runType, Path srcRoot, Path destRoot, CloneProgressReporter progress) {
		m_runType = runType;
		m_srcRoot = srcRoot;
		m_destRoot = destRoot;
		m_progress = progress;
	}
	
	public void pushFileHandler(FileHandler handler) {
		m_fileHandlers.add(handler);
	}
	
	public void addIgnoreSrcPattern(Pattern patt) {
		m_ignoreSrc.add(patt);
	}
	
	private void recordVisitedFile(Path src, Path dest) {
		m_filesVisited.peek().add(dest.getFileName().toString());
	}
	
	private boolean isSrcIgnored(Path relSrc) {
		String sRelSrc = relSrc.toString();
		for (Pattern x : m_ignoreSrc) {
			Matcher m = x.matcher(sRelSrc);
			if (m.matches())
				return true;
		}
		return false;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		Path relDirPath = m_srcRoot.relativize(dir);
		
		if (isSrcIgnored(relDirPath))
		{
			m_progress.reportProgress(CloneProgressReporter.EventType.DIRECTORY_SKIP_IGNORE, dir, null, null);
			return FileVisitResult.SKIP_SUBTREE;
		}
		
		Path newdir = m_destRoot.resolve(relDirPath);
		
		// if this is not the root, then record the directory in the parent
		if (!m_filesVisited.empty()) {
			recordVisitedFile(dir, newdir);
		}
		
		// add a new set for this directory
		m_filesVisited.push(new HashSet<String>());
		
		// if the target directory does not exist, then create it
		// TODO: do we need to copy some kind of extended file attributes from the parent directory?
		if (!Files.exists(newdir)) {
			if (m_runType == RunType.THE_REAL_DEAL)
				Files.createDirectories(newdir);
			m_progress.reportProgress(CloneProgressReporter.EventType.DIRECTORY_CREATE, dir, newdir, null);
		}
		
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		Path relFilePath = m_srcRoot.relativize(file);
		
		if (isSrcIgnored(relFilePath))
		{
			m_progress.reportProgress(CloneProgressReporter.EventType.FILE_SKIP_IGNORE, file, null, null);
			return FileVisitResult.CONTINUE;
		}
		
		Path newPathCandidate = m_destRoot.resolve(relFilePath);
		Iterator<FileHandler> it = m_fileHandlers.iterator();
		while (it.hasNext()) {
			FileHandler handler = it.next();
			try {
				Path newPath = handler.canHandleFile(file, attrs, newPathCandidate);
				if (newPath != null) {
					// record the visited file even if we don't process it
					recordVisitedFile(file, newPath);
					
					// does the new path exist
					if (Files.exists(newPath, LinkOption.NOFOLLOW_LINKS)) {
						// if it's the same file, then skip
						if (Files.isSameFile(file, newPath)) {
							m_progress.reportProgress(CloneProgressReporter.EventType.FILE_SKIP_SAME, file, newPath, null);
						} else { // process file
							BasicFileAttributes targetAttrs = Files.readAttributes(newPath, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
							if (targetAttrs.lastModifiedTime().compareTo(attrs.lastModifiedTime()) < 0) {
								m_progress.reportProgress(CloneProgressReporter.EventType.FILE_UPDATE_EXISTING_BEGIN, file, newPath, null);
								handler.processFile(file, attrs, newPathCandidate, m_runType);
								m_progress.reportProgress(CloneProgressReporter.EventType.FILE_UPDATE_EXISTING_END, file, newPath, null);
							} else {
								m_progress.reportProgress(CloneProgressReporter.EventType.FILE_SKIP_NEWER, file, newPath, null);
							}
						}
					} else { // otherwise process it
						m_progress.reportProgress(CloneProgressReporter.EventType.FILE_CREATE_NEW_BEGIN, file, newPath, null);
						handler.processFile(file, attrs, newPathCandidate, m_runType);
						m_progress.reportProgress(CloneProgressReporter.EventType.FILE_CREATE_NEW_END, file, newPath, null);
					}
					
					// file has been handled
					break;
				}
			} catch (FileCloningError e) {
				m_progress.reportProgress(CloneProgressReporter.EventType.ERROR, file, null, e);
				// TODO: should we continue and try next handler in list?
			}
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		//recordVisitedFile(file, null); // TODO: What do we record here?
		m_progress.reportProgress(CloneProgressReporter.EventType.ERROR, file, null, exc);
		return FileVisitResult.CONTINUE;
	}
	
	private void deleteDirectory(Path dir) throws IOException {
		Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (m_runType == RunType.THE_REAL_DEAL)
					Files.delete(file);
				m_progress.reportProgress(CloneProgressReporter.EventType.FILE_DELETE, file, null, null);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				if (exc == null) {
					if (m_runType == RunType.THE_REAL_DEAL)
						Files.delete(dir);
					m_progress.reportProgress(CloneProgressReporter.EventType.DIRECTORY_DELETE, dir, null, null);
				} else {
					// iteration completed prematurely so we may not be able to delete dir
					m_progress.reportProgress(CloneProgressReporter.EventType.ERROR, dir, null, exc);
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}
	
	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		if (exc != null) {
			m_progress.reportProgress(CloneProgressReporter.EventType.ERROR, dir, null, exc);
		}
		
		// check if target dir has any additional files/dirs which should be deleted
		Path newDir = m_destRoot.resolve(m_srcRoot.relativize(dir));
		
		// find the log of visited items for this directory
		// we use pop as we need to remove this set regardless of whether we encounter
		// errors during deletion.
		HashSet<String> setVisited = m_filesVisited.pop();
		
		// make sure there was no error and the target dir exists
		if (Files.exists(newDir, LinkOption.NOFOLLOW_LINKS)) {
			// enumerate all entries in the new directory and delete anything which
			// was not sourced from an original in the source directory.
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(newDir)) {
				for (Path p: stream) {
					try {
						String name = p.getFileName().toString();
						if (!setVisited.contains(name)) {
							if (Files.isDirectory(p, LinkOption.NOFOLLOW_LINKS)) {
								deleteDirectory(p); // progress report taken care of inside method
							} else {
								if (m_runType == RunType.THE_REAL_DEAL)
									Files.delete(p);
								m_progress.reportProgress(CloneProgressReporter.EventType.FILE_DELETE, p, null, null);
							}
						}
					} catch (IOException e) {
						m_progress.reportProgress(CloneProgressReporter.EventType.ERROR, p, null, e);
					}
				}
			}
			catch (IOException ex) {
				m_progress.reportProgress(CloneProgressReporter.EventType.ERROR, dir, null, ex);
			}
		}
		
		return FileVisitResult.CONTINUE;
	}

}
