package lotia.av.test.metadata.ffmpeg;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Iterator;

import lotia.av.metadata.ffmpeg.FFMPEGMetadata;
import lotia.av.metadata.ffmpeg.FFMPEGMetadataExtractor;
import lotia.av.metadata.ffmpeg.Stream;

public class FileDetailsPrinter {

	public static void printFileDetails(PrintStream oo, Path f) {
		
		oo.println("----------------------------------------------------------------------------");
		oo.println("File: " + f.toString());
		
		FFMPEGMetadata metadata;
		
		try {
			metadata = FFMPEGMetadataExtractor.extractMetadataForFile(f);
		} catch (Exception x) {
			String sMsg = x.getMessage();
			if (sMsg == null) {
				oo.println("Unable to process file. An unknown error occured.");
			} else {
				oo.print("Unable to process file: ");
				oo.println(sMsg);
			}
			return;
		}
		
		oo.println("Format (long): " + metadata.getContainerFormat().getFormatLongName());
		oo.println("Format: " + csvMe(metadata.getContainerFormat().getFormatNameIterator()));
		String lyrics = metadata.getContainerFormat().getTag("lyrics");
		oo.println("Lyrics: " + (lyrics == null ? "" : lyrics));
		oo.println("Num Streams: " + metadata.getNumStreams());
		Iterator<Stream> iStream = metadata.getStreamIterator();
		while (iStream.hasNext()) {
			Stream stream = iStream.next();
			oo.println("Codec Type: " + stream.getCodecType().toString());
			oo.println("Codec Name (Tag String): " + stream.getCodecName() + " (" + stream.getCodecTagString() + ")");
		}
	}

	static private String csvMe(Iterator<String> it) {
		StringBuilder b = new StringBuilder();
		boolean bFirst = true;
		while (it.hasNext()) {
			if (!bFirst)
				b.append(", ");
			b.append(it.next());
			bFirst = false;
		}
		return b.toString();
	}

}
