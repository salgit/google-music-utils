package lotia.av.metadata.ffmpeg.impl;

import java.io.IOException;
import java.io.LineNumberReader;

import lotia.av.metadata.ffmpeg.InvalidFFProbeOutput;

public class FormatExtractor extends SectionExtractor {

	public FormatExtractor() {
		super("FORMAT");
	}

	@Override
	protected void handleField(boolean isTag, String name, String value) {

		if (isTag) {
			m_ffFormat.getTagMap().put(name, value);
		}
		else {
			
			if (name.contentEquals("nb_streams")) {
				m_ffFormat.setNumStreams(parseIntField(value));
			}
			else if (name.contentEquals("format_name")) {
				String[] names = value.split(",");
				for (int i = 0; i != names.length; ++i) {
					m_ffFormat.addFormatName(names[i].trim());
				}
			}
			else if (name.contentEquals("format_long_name")) {
				m_ffFormat.setFormatLongName(value);
			}
			else if (name.contentEquals("start_time")) {
				m_ffFormat.setStartTime(parseDoubleField(value));
			}
			else if (name.contentEquals("duration")) {
				m_ffFormat.setDuration(parseDoubleField(value));
			}
			else if (name.contentEquals("size")) {
				m_ffFormat.setSize(parseLongField(value));
			}
			else if (name.contentEquals("bit_rate")) {
				m_ffFormat.setBitRate(parseDoubleField(value));
			}
		}
	}
	
	ContainerFormatImpl m_ffFormat;
	
	public ContainerFormatImpl extractFormat(LineNumberReader reader) throws IOException, InvalidFFProbeOutput {
		
		m_ffFormat = new ContainerFormatImpl();
		
		startExtraction(reader);
		
		return m_ffFormat;
	}

}
