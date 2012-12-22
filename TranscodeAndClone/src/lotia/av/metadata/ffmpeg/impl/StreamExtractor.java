package lotia.av.metadata.ffmpeg.impl;

import java.io.IOException;
import java.io.LineNumberReader;

import lotia.av.metadata.ffmpeg.InvalidFFProbeOutput;
import lotia.av.metadata.ffmpeg.Stream.Type;

public class StreamExtractor extends SectionExtractor {

	protected StreamExtractor() {
		super("STREAM");
	}

	@Override
	protected void handleField(boolean isTag, String name, String value) {
		
		if (isTag) {
			m_ffStream.getTagMap().put(name,  value);
		}
		else {
			
			if (name.contentEquals("codec_name"))
				m_ffStream.setCodecName(value);
			else if (name.contentEquals("codec_long_name"))
				m_ffStream.setCodecLongName(value);
			else if (name.contentEquals("codec_type"))
				m_ffStream.setCodecType(Type.valueOf("TYPE_" + value.toUpperCase()));
			else if (name.contentEquals("codec_time_base"))
				m_ffStream.setCodecTimeBase(value);
			else if (name.contentEquals("codec_tag_string"))
				m_ffStream.setCodecTagString(value);
			else if (name.contentEquals("codec_tag"))
				m_ffStream.setCodecTag(parseLongField(value));
			else if (name.contentEquals("sample_rate"))
				m_ffStream.setSampleRate(parseLongField(value));
			else if (name.contentEquals("channels"))
				m_ffStream.setChannels(parseIntField(value));
			else if (name.contentEquals("bits_per_sample"))
				m_ffStream.setBitsPerSample(parseIntField(value));
			else if (name.contentEquals("width"))
				m_ffStream.setWidth(parseIntField(value));
			else if (name.contentEquals("height"))
				m_ffStream.setHeight(parseIntField(value));
			else if (name.contentEquals("has_b_frames"))
				m_ffStream.setHasBFrames(parseIntField(value) != 0);
			else if (name.contentEquals("bits_per_sample"))
				m_ffStream.setPixFmt(value);
			else if (name.contentEquals("bits_per_sample"))
				m_ffStream.setLevel(parseIntField(value));			
			else if (name.contentEquals("r_frame_rate"))
				m_ffStream.setrFrameRate(value);
			else if (name.contentEquals("avg_frame_rate"))
				m_ffStream.setAvgFrameRate(value);
			else if (name.contentEquals("time_base"))
				m_ffStream.setTimeBase(value);
			else if (name.contentEquals("start_time"))
				m_ffStream.setStartTime(parseDoubleField(value));
			else if (name.contentEquals("duration"))
				m_ffStream.setDuration(parseDoubleField(value));
			else if (name.contentEquals("nb_frames"))
				m_ffStream.setNumFrames(parseLongField(value));
		}
	}
	
	private StreamImpl m_ffStream;
	
	public StreamImpl extractStream(LineNumberReader reader) throws IOException, InvalidFFProbeOutput {
		
		m_ffStream = new StreamImpl();
		
		startExtraction(reader);
		
		return m_ffStream;
	}
}
