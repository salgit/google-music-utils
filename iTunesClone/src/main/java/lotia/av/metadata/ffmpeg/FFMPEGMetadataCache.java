package lotia.av.metadata.ffmpeg;

import java.io.IOException;
import java.io.PrintStream;
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
	
	private PersistentMetadataCache m_onDiskCache;
	
	private int m_nNumNewMetadataComputed = 0;
	
	private FFMPEGMetadataCache() {
	}
	
	public void setCacheDirectory(Path cacheDir) throws IOException {
		// this should be okay. the old reference will continue to be used
		// until a thread is done with it?
		m_onDiskCache = new PersistentMetadataCache(cacheDir);
	}
	
    private static class SingletonHolder { 
        public static final FFMPEGMetadataCache instance = new FFMPEGMetadataCache();
    }

    public static FFMPEGMetadataCache getInstance() {
        return SingletonHolder.instance;
    }
    
    public int getNumPersistentCacheHits() {
    	return (m_onDiskCache != null) ? m_onDiskCache.getNumReadsFromDisk() : 0;
    }
    
    public int getNumNewMetadataComputed() {
    	return m_nNumNewMetadataComputed;
    }
	
	public FFMPEGMetadata getMetadataForFile(Path file, BasicFileAttributes attrs) throws IOException, InvalidFFProbeOutput, InterruptedException {
		
		if (attrs == null) {
			attrs = Files.readAttributes(file,  BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
		}		

		CacheEntry entry;
		
		synchronized(m_cache)
		{
			entry = m_cache.get(file);
		}
		
		// if in-memory cache entry was found, then check if it is valid and return
		if ((entry != null) && (entry.attrs.lastModifiedTime().compareTo(attrs.lastModifiedTime()) >= 0)) {
			return entry.metadata;
		}
		
		// check on-disk cache
		if (m_onDiskCache != null) {
			FFMPEGMetadata diskMD = m_onDiskCache.getMetadataForFile(file, attrs);
			
			if (diskMD != null) {
				entry = new CacheEntry(diskMD, attrs);
			}
		}
		
		// if still haven't found, then load from utility
		if (entry == null)
		{
			FFMPEGMetadata metadata = FFMPEGMetadataExtractor.extractMetadataForFile(file);
			
			++m_nNumNewMetadataComputed;
			
			entry = new CacheEntry(metadata, attrs);
			
			// save it to disk
			if (m_onDiskCache != null)
				m_onDiskCache.putMetadataForFile(file, attrs, metadata);
		}

		synchronized(m_cache)
		{
			// never mind the race condition here. last one wins :-)
			m_cache.put(file, entry);
		}
		
		return entry.metadata;
	}
	
	public void printStatistics(PrintStream out) {
		out.println("Metadata cache hits:    " + getNumPersistentCacheHits());
		out.println("New metadata computed:  " + getNumNewMetadataComputed());
	}
}
