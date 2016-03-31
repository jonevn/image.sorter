package se.evelonn.image.sorter;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class ImageSorter {

	private static final Logger logger = LogManager.getLogger(ImageSorter.class);

	public static void main(String[] args) {

		ImageSorterOptions imageSorterOptions = new ImageSorterOptions();

		CmdLineParser cmdLineParser = new CmdLineParser(imageSorterOptions);
		try {
			cmdLineParser.parseArgument(args);
		} catch (CmdLineException e) {
			cmdLineParser.printUsage(System.err);
			return;
		}

		logger.debug("Running with parameters: " + imageSorterOptions);
		DirectoryScanner directoryScanner = new DirectoryScanner(imageSorterOptions);
		try {
			directoryScanner.startWatching();
		} catch (IOException e) {
			logger.error("Failed to execute: " + e.getMessage(), e);
			return;
		}
	}
}