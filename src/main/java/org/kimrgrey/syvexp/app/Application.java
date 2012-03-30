package org.kimrgrey.syvexp.app;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);


	public static void main (String... args) {
		Application application = new Application(args);
		application.execute();
	}

	private CommandLine commandLine = null;

	public Application (String... args) {
		Options options = new Options();
        options.addOption("h", "help", false, "Display message that describes parameters for the application");
        options.addOption("c", "config", true, "Path to configuration file for the application");
        CommandLineParser parser = new PosixParser();
        try {
            this.commandLine = parser.parse(options, args);
        } catch (ParseException expression) {
            printUsage(options);
            return;
        }
        if (commandLine.hasOption("help")) {
            printUsage(options);
            this.commandLine = null;
        }
	}

	private void printUsage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "exporession-server", options);
    }

	public void execute() {
		if (commandLine == null) {
            return;
        }
        try {
            Configuration configuration = Configuration.load(commandLine.getOptionValue("config"));
        } catch (InvalidConfigException exception) {
            logger.error("Failed to export data because of configuration error", exception);
        }
	}
}