package lotia.av.dirclone;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import lotia.av.metadata.ffmpeg.FFMPEGMetadataCache;
import lotia.av.metadata.ffmpeg.InvalidFFProbeOutput;

public class DirCloner {

	private enum ProgressReportMode {
		PROGRESS_REPORT_VERBOSE,
		PROGRESS_REPORT_SUMMARY,
		PROGRESS_REPORT_NONE
	}
	
	private void clone(Path srcRoot, Path destRoot, ProgressReportMode reportMode) throws IOException {	

		CountingProgressReporter progSummary = null;
		CloneProgressReporter progress = null;
		
		switch (reportMode) {
			case PROGRESS_REPORT_VERBOSE: {
				TextProgressReporter progress1 = new TextProgressReporter(System.out, false);
				progSummary = new CountingProgressReporter();
				progress = new MultiProgressReporter(progress1, progSummary);
				break;
			}
			
			case PROGRESS_REPORT_SUMMARY: {
				progSummary = new CountingProgressReporter();
				progress = progSummary;
				break;
			}
			
			case PROGRESS_REPORT_NONE: {
				progress = new SilentProgressReporter();
				break;
			}
		}
		
		ClonerFileVisitor cloner = new ClonerFileVisitor(RunType.THE_REAL_DEAL, srcRoot, destRoot, progress);
		
		cloner.addIgnoreSrcPattern(Pattern.compile(".*/Podcasts", Pattern.CASE_INSENSITIVE));
		cloner.addIgnoreSrcPattern(Pattern.compile(".*/Ringtones", Pattern.CASE_INSENSITIVE));
		cloner.addIgnoreSrcPattern(Pattern.compile(".*/Movies", Pattern.CASE_INSENSITIVE));
		cloner.addIgnoreSrcPattern(Pattern.compile(".*/TV Shows", Pattern.CASE_INSENSITIVE));
		cloner.addIgnoreSrcPattern(Pattern.compile(".*/Books", Pattern.CASE_INSENSITIVE));
		cloner.addIgnoreSrcPattern(Pattern.compile(".*/Mobile Applications", Pattern.CASE_INSENSITIVE));
		cloner.addIgnoreSrcPattern(Pattern.compile("Mobile Applications", Pattern.CASE_INSENSITIVE));
		cloner.addIgnoreSrcPattern(Pattern.compile("iPod Games", Pattern.CASE_INSENSITIVE));
		cloner.addIgnoreSrcPattern(Pattern.compile("Previous iTunes Libraries", Pattern.CASE_INSENSITIVE));
		
		String[] extensions = { "m4a", "wav" };
		String[] codecs = { "alac" };
		long[] codecTags = { 1 }; // WAV
		MP3ConvertingHandler convHandler = new MP3ConvertingHandler(extensions, codecs, codecTags, progress);
		
		cloner.pushFileHandler(convHandler);
		cloner.pushFileHandler(new LinkOrCopyHandler(progress));
		
		Files.walkFileTree(srcRoot, cloner);
		
		if (progSummary != null)
			progSummary.printStatistics(System.out);
		
		FFMPEGMetadataCache.getInstance().printStatistics(System.out);
	}
	
	private static void usage()
	{
		System.out.println("[gmclone] Usage: gmclone [-v|--verbose] [-q|--quiet] [--src <source directory>] [--dest <destination directory>]");
	}

	
	public static void main(String[] args) throws IOException, InvalidFFProbeOutput, InterruptedException {
		
		String srcPath = null;
		String destPath = null;
		String cacheDir = null;
		
		ProgressReportMode progMode = ProgressReportMode.PROGRESS_REPORT_SUMMARY;
		
		for (int i = 0; i < args.length; ++i) {
			if (args[i].equalsIgnoreCase("-h") || args[i].equalsIgnoreCase("--help")) {
				usage();
				return;
			}
			else if (args[i].equalsIgnoreCase("--src")) {
				++i;
				if (i >= args.length) {
					usage();
					return;
				}
				srcPath = args[i];
			}
			else if (args[i].equalsIgnoreCase("--dest")) {
				++i;
				if (i >= args.length) {
					usage();
					return;
				}
				destPath = args[i];	
			}
			else if (args[i].equalsIgnoreCase("--cache-dir")) {
				++i;
				if (i >= args.length) {
					usage();
					return;
				}
				cacheDir = args[i]; 
			}
			else if (args[i].equalsIgnoreCase("-q") || args[i].equalsIgnoreCase("--quiet")) {
				progMode = ProgressReportMode.PROGRESS_REPORT_NONE;
			}
			else if (args[i].equalsIgnoreCase("-v") || args[i].equalsIgnoreCase("--verbose")) {
				progMode = ProgressReportMode.PROGRESS_REPORT_VERBOSE;
			}
			else {
				usage();
				return;
			}
		}

		Path srcRoot = null;
		Path destRoot = null;
		Path cacheRoot = null;
		
		if (srcPath == null) {
			String userHome = System.getProperty("user.home");
			srcRoot = Paths.get(userHome, "Music", "iTunes");
		} else {
			srcRoot = Paths.get(srcPath);
		}

		if (destPath == null) {
			String userHome = System.getProperty("user.home");
			destRoot = Paths.get(userHome, "Music", "GoogleMusicClone");
		} else {
			destRoot = Paths.get(destPath);
		}
		
		if (cacheDir == null) {
			String userHome = System.getProperty("user.home");
			cacheRoot = Paths.get(userHome, "Library", "Application Support", "GoogleMusicClone", "MetadataCache");
		} else {
			cacheRoot = Paths.get(cacheDir);
		}
		
		FFMPEGMetadataCache.getInstance().setCacheDirectory(cacheRoot);
		
		(new DirCloner()).clone(srcRoot, destRoot, progMode);
	}

}
