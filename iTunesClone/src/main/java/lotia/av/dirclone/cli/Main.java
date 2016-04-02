package lotia.av.dirclone.cli;

import com.beust.jcommander.JCommander;
import lotia.av.dirclone.*;
import lotia.av.metadata.ffmpeg.FFMPEGMetadataCache;

import java.nio.channels.ClosedChannelException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by slotia on 4/2/16.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Options opts = new Options();

        JCommander jcmd = new JCommander(opts);
        jcmd.setProgramName("itunesclone");

        jcmd.parse(args);

        if (opts.help) {
            jcmd.usage();
            return;
        }

        RunType runType = opts.dryRun ? RunType.DRY_RUN : RunType.THE_REAL_DEAL;

        CloneProgressReporter progressReporter = null;
        CountingProgressReporter counter = null;

        switch (opts.logLevel) {
            case 0:
                progressReporter = new SilentProgressReporter();
                break;

            case 1:
                counter = new CountingProgressReporter();
                progressReporter = counter;
                break;

            case 2:
                TextProgressReporter tpr = new TextProgressReporter(System.out, false);
                counter = new CountingProgressReporter();
                progressReporter = new ChainedProgressReporter(tpr, counter);
                break;
        }

        assert(progressReporter != null);

        FFMPEGMetadataCache.getInstance().setCacheDirectory(opts.cache.toPath());

        (new DirCloner(runType, opts.codec)).clone(opts.src.toPath(), opts.dest.toPath(), progressReporter);

        if (opts.logLevel != 0) {
            counter.printStatistics(System.out);
            FFMPEGMetadataCache.getInstance().printStatistics(System.out);
        }
    }
}
