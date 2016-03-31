package se.evelonn.image.sorter;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OneArgumentOptionHandler;
import org.kohsuke.args4j.spi.Setter;

public class FiltypHandler extends OneArgumentOptionHandler<FilTyp[]> {

	public FiltypHandler(CmdLineParser parser, OptionDef option, Setter<? super FilTyp[]> setter) {
		super(parser, option, setter);
	}

	@Override
	protected FilTyp[] parse(String argument) throws NumberFormatException, CmdLineException {
		try {
			return (FilTyp[]) Stream.of(argument.split(","))
					.map(a -> FilTyp.from(a))
					.collect(Collectors.toList())
					.toArray();
		} catch (IllegalArgumentException e) {
			throw new CmdLineException(owner, e.getMessage());
		}
	}

}
