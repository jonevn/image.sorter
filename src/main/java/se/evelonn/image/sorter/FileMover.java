package se.evelonn.image.sorter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class FileMover {

	private static final Logger logger = LogManager.getLogger(FileMover.class);

	public static void move(Path directory, Path file) {

		logger.debug("Moving file: " + file);
		try {
			Path absoluteFilePath = directory.resolve(file.toFile().getName());

			String dateAsString = getFileTimestamp(absoluteFilePath);

			logger.debug("Parent directory: " + directory);

			Path expectedFolder = directory.resolve(dateAsString);

			logger.debug("Expected directory: " + expectedFolder);

			if (!Files.isDirectory(expectedFolder)) {
				Files.createDirectory(expectedFolder);
			}

			logger.debug("Moving file: " + absoluteFilePath + " to: " + expectedFolder);
			Files.move(absoluteFilePath, getNextFilename(expectedFolder, file), StandardCopyOption.ATOMIC_MOVE);

		} catch (ImageProcessingException | IOException e) {
			logger.error("Failed to move file: " + file, e);
		}
	}

	private static String getFileTimestamp(Path absoluteFilePath) throws ImageProcessingException, IOException {

		Metadata metadata = ImageMetadataReader.readMetadata(absoluteFilePath.toFile());

		ExifSubIFDDirectory exifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

		Date date = exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);

		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		return localDate.format(DateTimeFormatter.ofPattern("yyyy_MM"));
	}

	private static Path getNextFilename(Path directory, Path file) {

		String fileName = file.toFile().getName();

		String baseName = FilenameUtils.getBaseName(fileName);

		String extension = FilenameUtils.getExtension(fileName);

		Path newFilePath = directory.resolve(String.format("%s.%s", baseName, extension));
		if (!Files.exists(newFilePath)) {
			return newFilePath;
		}

		logger.debug("File " + newFilePath.toFile().getName() + " already exists");

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			newFilePath = directory.resolve(String.format("%s_%d.%s", baseName, i, extension));
			if (!Files.exists(newFilePath)) {
				return newFilePath;
			}
			logger.debug("File " + newFilePath.toFile().getName() + " already exists");
		}
		throw new IllegalStateException("Could not determine next filename");
	}
}