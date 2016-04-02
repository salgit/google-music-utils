package lotia.av.metadata.ffmpeg.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lotia.av.metadata.ffmpeg.InvalidFFProbeOutput;

public class FFMPEGMetadataParserImpl {

	static private final Pattern s_reSectionHeaderStart = Pattern.compile("\\s*\\[\\s*([\\S]+)\\s*\\]\\s*");
	
	static private final String HDR_STREAM = "STREAM";
	static private final String HDR_FORMAT = "FORMAT";
	
	static private final StreamExtractor s_streamExtractor = new StreamExtractor();
	static private final FormatExtractor s_formatExtractor = new FormatExtractor();
	
	static public FFMPEGMetadataImpl parseMetadata(InputStream in) throws IOException, InvalidFFProbeOutput {
		
		BufferedInputStream buffIn = new BufferedInputStream(in);
		InputStreamReader inReader = new InputStreamReader(buffIn);
		LineNumberReader reader = new LineNumberReader(inReader);
		
		FFMPEGMetadataImpl metadata = new FFMPEGMetadataImpl();
		
		String sCurrentLine = ExtractionUtils.readNextLine(reader);
		
		boolean bSomeOutput = false;
		
		while (sCurrentLine != null) {
			
			bSomeOutput = true;
			
			if (sCurrentLine.startsWith("[")) {
				
				Matcher matcher = s_reSectionHeaderStart.matcher(sCurrentLine);
				
				if (!matcher.matches()) {
					throw new InvalidFFProbeOutput();
				}
				
				String sHeaderName = matcher.group(1);
				
				if (sHeaderName.contentEquals(HDR_STREAM)) {
					metadata.addStream(s_streamExtractor.extractStream(reader));
				}
				else if (sHeaderName.contentEquals(HDR_FORMAT)) {
					
					if (metadata.getContainerFormat() != null) {
						throw new InvalidFFProbeOutput();
					}
					
					metadata.setContainerFormat(s_formatExtractor.extractFormat(reader));
				}
			}
			else {
				throw new InvalidFFProbeOutput();
			}
			
			sCurrentLine = ExtractionUtils.readNextLine(reader);
		}
		
		if (bSomeOutput)
			return metadata;
		
		throw new InvalidFFProbeOutput();
	}
}
