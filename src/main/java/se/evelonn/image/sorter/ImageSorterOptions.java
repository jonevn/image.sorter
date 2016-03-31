package se.evelonn.image.sorter;

import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.PathOptionHandler;

public class ImageSorterOptions {

	@Option(name = "-p", required = true, handler = PathOptionHandler.class, usage = "Path to directory to scan")
	private Path directory;

	@Option(name = "-f", handler = FiltypHandler.class, usage = "Filetypes to scan for")
	private FilTyp[] filtyper = { FilTyp.JPG, FilTyp.JPEG, FilTyp.GIF };

	public Path getDirectory() {
		return directory;
	}

	public void setDirectory(Path directory) {
		this.directory = directory;
	}

	public FilTyp[] getFiltyper() {
		return filtyper;
	}

	public void setFiltyper(FilTyp[] filtyper) {
		this.filtyper = filtyper;
	}

	@Override
	public String toString() {
		return "Directory: " + directory.toString() + ", FileTypes: "
				+ Stream.of(filtyper).map(f -> f.name().toLowerCase()).collect(Collectors.joining(","));
	}
}