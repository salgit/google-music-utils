package lotia.av.metadata.ffmpeg.impl;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lotia.av.metadata.ffmpeg.InvalidFFProbeOutput;

public abstract class SectionExtractor {
	
	private Pattern m_headerEndPattern;
	
	static private Pattern s_fieldPattern = Pattern.compile("\\s*(?:(TAG)\\s*\\:)?\\s*(.+?)\\s*=\\s*(.*?)\\s*");
	
	protected SectionExtractor(String headerName) {
		
		m_headerEndPattern = Pattern.compile("\\s*\\[\\s*/\\s*" + headerName + "\\s*\\]\\s*");
	}
	
	protected abstract void handleField(boolean isTag, String name, String value);
	
	protected int parseIntField(String value) {
		if (value == null || value.trim().length() == 0 || value.equalsIgnoreCase("N/A"))
			return Integer.MIN_VALUE;
		
		if (value.startsWith("0x"))
			return Integer.decode(value);
		
		return Double.valueOf(value).intValue();
	}
	
	protected long parseLongField(String value) {
		if (value == null || value.trim().length() == 0 || value.equalsIgnoreCase("N/A"))
			return Long.MIN_VALUE;
		
		if (value.startsWith("0x"))
			return Long.decode(value);
		
		return Double.valueOf(value).longValue();
	}
	
	protected double parseDoubleField(String value) {
		if (value == null || value.trim().length() == 0 || value.equalsIgnoreCase("N/A"))
			return Double.NaN;
		
		return Double.valueOf(value);
	}
	
	protected boolean parseBooleanField(String value) {
		return parseIntField(value) != 0;
	}

	protected void startExtraction(LineNumberReader reader) throws IOException, InvalidFFProbeOutput {
		
		String sCurrentLine = ExtractionUtils.readNextLine(reader);
		
		StringWriter strWriter = new StringWriter();
		PrintWriter pendingFieldValue = new PrintWriter(strWriter);
		
		String pendingFieldName = null;
		
		boolean bLastFieldIsTAG = false;
		
		while (sCurrentLine != null) {
			
			if (sCurrentLine.startsWith("[")) { // startsWith is safe as we know input is trimmed
				
				Matcher matcher = m_headerEndPattern.matcher(sCurrentLine);
				
				if (!matcher.matches()) {
					throw new InvalidFFProbeOutput();
				}
				
				break;
			}
			else {
				
				Matcher matcher = s_fieldPattern.matcher(sCurrentLine);
				
				if (!matcher.matches()) {
					
					if (pendingFieldName == null)
						throw new InvalidFFProbeOutput();
					
					// this is continuation of a multi-line value
					if (strWriter.getBuffer().length() > 0)
						pendingFieldValue.println();
					
					pendingFieldValue.append(sCurrentLine);
				}
				else {
					
					if (pendingFieldName != null) {
						// if there is a pending value to commit, do it now
						handleField(bLastFieldIsTAG, pendingFieldName, strWriter.toString());
						
						// clear and reset state
						pendingFieldName = null;
						bLastFieldIsTAG = false;
						strWriter = new StringWriter();
						pendingFieldValue = new PrintWriter(strWriter);
					}
					
					String TAG = matcher.group(1);
					bLastFieldIsTAG = TAG != null;
					
					pendingFieldName = matcher.group(2);
					
					if (pendingFieldName == null) {
						throw new InvalidFFProbeOutput();
					}
					
					String value = matcher.group(3);
					if (value != null && !value.isEmpty())
						pendingFieldValue.append(value);
				}
			}
			
			sCurrentLine = ExtractionUtils.readNextLine(reader);
		}
		
		// if there is a pending value to commit, do it now
		if (pendingFieldName != null) {
			handleField(bLastFieldIsTAG, pendingFieldName, strWriter.toString());
		}
	}
}
