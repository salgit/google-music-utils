package lotia.av.metadata.ffmpeg.test;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;


public class FileMetadataVisitor implements FileVisitor<Path> {
	
	private PrintStream m_oo;
	
	public FileMetadataVisitor(PrintStream oo) {
		m_oo = oo;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		if (!Files.isHidden(file)) {
			String name = file.getFileName().toString();
			if (!(name.endsWith(".jpg") || name.endsWith(".ini") || name.endsWith(".epub") || name.endsWith(".pdf") || name.equalsIgnoreCase(".DS_Store"))) {
				FileDetailsPrinter.printFileDetails(m_oo, file);
			}
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}

}
