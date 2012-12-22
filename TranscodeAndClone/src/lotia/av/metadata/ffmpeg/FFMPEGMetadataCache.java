package lotia.av.metadata.ffmpeg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

public class FFMPEGMetadataCache {
	
	private static class CacheEntry {
		public CacheEntry(FFMPEGMetadata m, BasicFileAttributes a) {
			metadata = m;
			attrs = a;
		}
		
		public FFMPEGMetadata metadata;
		public BasicFileAttributes attrs;
	}
	
	private HashMap<Path, CacheEntry> m_cache = new HashMap<Path, CacheEntry>();
	
	private FFMPEGMetadataCache() {
	}
	
    private static class SingletonHolder { 
        public static final FFMPEGMetadataCache instance = new FFMPEGMetadataCache();
    }

    public static FFMPEGMetadataCache getInstance() {
        return SingletonHolder.instance;
    }
	
	public FFMPEGMetadata getMetadataForFile(Path file, BasicFileAttributes attrs) throws IOException, InvalidFFProbeOutput, InterruptedException {
		
		CacheEntry entry;
		
		synchronized(m_cache)
		{
			entry = m_cache.get(file);
		}
		
		if (entry != null) {
			if (attrs == null) {
				attrs = Files.readAttributes(file,  BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
			}
			if (entry.attrs.lastModifiedTime().compareTo(attrs.lastModifiedTime()) >= 0) {
				return entry.metadata;
			}
		} else {
			FFMPEGMetadata metadata = FFMPEGMetadataExtractor.extractMetadataForFile(file);
			
			entry = new CacheEntry(metadata, attrs);
			
			synchronized(m_cache)
			{
				// never mind the race condition here. last one wins :-)
				m_cache.put(file, entry);
			}
		}
		
		return entry.metadata;
	}
}
