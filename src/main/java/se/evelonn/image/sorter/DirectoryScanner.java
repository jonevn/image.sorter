package se.evelonn.image.sorter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.stream.Stream;

public class DirectoryScanner {

	private ImageSorterOptions imageSorterOptions;

	public DirectoryScanner(ImageSorterOptions imageSorterOptions) {
		this.imageSorterOptions = imageSorterOptions;
	}

	public void startWatching() throws IOException {

		WatchService watchService = FileSystems.getDefault().newWatchService();

		imageSorterOptions.getDirectory().register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

		while (true) {
			WatchKey key;

			try {
				key = watchService.take();
			} catch (InterruptedException e) {
				return;
			}

			key.pollEvents()
					.stream()
					.filter(e -> e.kind() == StandardWatchEventKinds.ENTRY_CREATE)
					.filter(e -> Stream.of(imageSorterOptions.getFiltyper())
							.anyMatch(f -> ((Path) e.context()).toFile().getName().endsWith(f.name().toLowerCase())
									|| ((Path) e.context()).toFile().getName().endsWith(f.name())))
					.forEach(e -> FileMover.move(imageSorterOptions.getDirectory(), (Path) e.context()));

			boolean valid = key.reset();
			if (!valid) {
				break;
			}
		}
	}
}
