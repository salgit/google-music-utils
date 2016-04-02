package lotia.av.metadata.ffmpeg.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class ExtractionUtils {
	
	public static String readNextLine(LineNumberReader reader) throws IOException {
		
		while(true) {
			String s = reader.readLine();
			if (s == null)
				return s;
			String sTrimmed = s.trim();
			if (sTrimmed.isEmpty())
				continue;
			return sTrimmed;
		}
		
	}

	public static String errorString(InputStream errIn) throws IOException {
		LineNumberReader reader = new LineNumberReader(new InputStreamReader(new BufferedInputStream(errIn)));
		
		StringBuilder strBuf = new StringBuilder();
		
		boolean bInProlog = true;
		
		while (true) {
			String sLine = readNextLine(reader);
			if (sLine == null)
				break;
			if (bInProlog) {
				if (sLine.startsWith("ffmpeg") || sLine.startsWith("ffprobe") || sLine.startsWith("built on") || sLine.startsWith("configuration:") || sLine.startsWith("lib")) {
					continue;
				}
				bInProlog = false;
			}
			strBuf.append(sLine);
		}
		
		return strBuf.toString();
	}

	public static String escapeFilePathForShell(String sIn) {
		byte[] s = sIn.getBytes(FFMPEGMetadataExtractorImpl.s_UTF8);
		StringBuilder buff = new StringBuilder();
		for (int i = 0; i < s.length; ++i) {
			byte b = s[i];
			if ((b >> 0x7f) != 0) {
				buff.append("\\x");
				buff.append(Integer.toHexString(b & 0xff));
			} else {
				buff.append((char)b);
			}
		}
		return buff.toString();
	}
}
