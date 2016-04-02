package lotia.av.dirclone.cli;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import lotia.av.metadata.ffmpeg.FFMPEGAudioTranscoder;

public class Options {
    private String userHome = System.getProperty("user.home");

    @Parameter(names={ "-s", "--source" }, converter=FileConverter.class, description="source directory", validateWith=CLIUtils.DirectoryThatExists.class)
    public File src = Paths.get(userHome, "Music", "iTunes", "iTunes Music", "Music").toFile();

    @Parameter(names={ "-d", "--destination" }, converter=FileConverter.class, description="destination directory", validateWith=CLIUtils.DirectoryThatExists.class)
    public File dest = Paths.get(userHome, "Music", "iTunesClone").toFile();

    @Parameter(names={ "-c", "--cache-directory" }, converter=FileConverter.class, description="location of cache directory", validateWith=CLIUtils.DirectoryThatExists.class)
    public File cache = Paths.get(userHome, "Library", "Application Support", "iTunesClone", "MetadataCache").toFile();

    @Parameter(names={ "--codec" }, converter=CLIUtils.CodecConverter.class, description="codec (FLAC, AAC, MP3) used to transcode ALAC files", validateWith=CLIUtils.CodecValidator.class)
    public FFMPEGAudioTranscoder.Codec codec = FFMPEGAudioTranscoder.Codec.CODEC_FLAC;

    @Parameter(names="--dry-run", description="logs progress but does not execute any actions")
    public boolean dryRun = false;

    @Parameter(names={ "-l", "--log-level" }, description="level (0-2) of detailed reported in progress output", validateValueWith=CLIUtils.LogLevelValidator.class
    )
    public int logLevel = 1;

    @Parameter(names={ "-h", "--help" }, help = true)
    public boolean help = false;
}
