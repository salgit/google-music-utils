package lotia.av.test.metadata.ffmpeg;

//import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lotia.av.metadata.ffmpeg.InvalidFFProbeOutput;
import org.junit.Test;

public class FFMPEGMetadataExtractorTest {

	@Test
	public void test() throws IOException, InvalidFFProbeOutput, InterruptedException {
		
		//Path root = Paths.get("/Users/ghar/Music/iTunes/iTunes Music");
		Path root = Paths.get("/Volumes/mauseeqee/iTunes/iTunes Music/Music/Various/Coke Studio Season 3");
		
		FileMetadataVisitor visitor = new FileMetadataVisitor(System.out);
		
		Files.walkFileTree(root, visitor);
	}
	
	@Test
	public void testSingleFile() throws IOException, InvalidFFProbeOutput, InterruptedException {
		
		System.out.println(System.getenv("PATH"));
		
		Path file = Paths.get("/Volumes/mauseeqee/iTunes/iTunes Music/Music/Louis Armstrong/Unknown Album/We Shall Overcome.m4a");
		
		FileDetailsPrinter.printFileDetails(System.out, file);
	}
	
	public static void main(String[] args) throws IOException, InvalidFFProbeOutput, InterruptedException {
		(new FFMPEGMetadataExtractorTest()).test();
	}

}
