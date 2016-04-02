package lotia.av.dirclone;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import lotia.av.metadata.ffmpeg.FFMPEGAudioTranscoder;
import lotia.av.metadata.ffmpeg.FFMPEGMetadataCache;
import lotia.av.metadata.ffmpeg.InvalidFFProbeOutput;

public class DirCloner {

    private RunType m_runType;
    private FFMPEGAudioTranscoder.Codec m_codec;

    public DirCloner(RunType runType, FFMPEGAudioTranscoder.Codec codec) {
        m_runType = runType;
        m_codec = codec;
    }
	
	public void clone(Path srcRoot, Path destRoot, CloneProgressReporter progress) throws IOException {

		ClonerFileVisitor cloner = new ClonerFileVisitor(m_runType, srcRoot, destRoot, progress);
        cloner.addDefaultIgnorePatterns();
		
		String[] extensionsSupportedForTranscode = { "m4a", "wav" };
		String[] codecsToBeTranscoded = { "alac" };
		long[] codecTagsToBeTranscoded = { 1 }; // WAV
		TranscodingHandler convHandler = new TranscodingHandler(extensionsSupportedForTranscode, codecsToBeTranscoded,
                codecTagsToBeTranscoded, m_codec, progress);
		
		cloner.pushFileHandler(convHandler); // first try transcoding handler
		cloner.pushFileHandler(new LinkOrCopyHandler(progress)); // fallback is link/copy
		
		Files.walkFileTree(srcRoot, cloner);
	}
}
