package se.evelonn.image.sorter;

import java.util.Optional;
import java.util.stream.Stream;

public enum FilTyp {
	JPG, GIF, TIFF, JPEG, PNG;

	public static FilTyp from(String namn) {
		Optional<FilTyp> filtyp = Stream.of(FilTyp.values()).filter(f -> namn.equalsIgnoreCase(f.name())).findFirst();
		if (filtyp.isPresent()) {
			return filtyp.get();
		}
		throw new IllegalArgumentException("Filtyp: " + namn + " stöds ej");
	}
}
