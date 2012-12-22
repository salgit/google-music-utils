package lotia.av.dirclone.test;

//import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import lotia.av.dirclone.ClonerFileVisitor;
import lotia.av.dirclone.CountingProgressReporter;
import lotia.av.dirclone.LinkOrCopyHandler;
import lotia.av.dirclone.MP3ConvertingHandler;
import lotia.av.dirclone.RunType;
import lotia.av.dirclone.TextProgressReporter;
import lotia.av.metadata.ffmpeg.InvalidFFProbeOutput;
import org.junit.Test;

public class DirClonerTest {

	@Test
	public void test() throws IOException {
		//Path srcRoot = Paths.get("/Users/ghar/Music/iTunes/iTunes Music/Adele/19");
		Path srcRoot = Paths.get("/Users/ghar/Music/iTunes");
		Path destRoot = Paths.get("/Users/ghar/Music/GoogleMusicClone");
		
		TextProgressReporter progress = new TextProgressReporter(System.out, false);
		//CountingProgressReporter progress = new CountingProgressReporter();
		
		ClonerFileVisitor cloner = new ClonerFileVisitor(RunType.THE_REAL_DEAL, srcRoot, destRoot, progress);
		
		cloner.addIgnoreSrcPattern(Pattern.compile(".*/Podcasts", Pattern.CASE_INSENSITIVE));
		cloner.addIgnoreSrcPattern(Pattern.compile(".*/Ringtones", Pattern.CASE_INSENSITIVE));
		cloner.addIgnoreSrcPattern(Pattern.compile(".*/Movies", Pattern.CASE_INSENSITIVE));
		cloner.addIgnoreSrcPattern(Pattern.compile(".*/TV Shows", Pattern.CASE_INSENSITIVE));
		cloner.addIgnoreSrcPattern(Pattern.compile(".*/Books", Pattern.CASE_INSENSITIVE));
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
		
		//progress.printStatistics(System.out);
	}
	
	public static void main(String[] args) throws IOException, InvalidFFProbeOutput, InterruptedException {
		(new DirClonerTest()).test();
	}

}
