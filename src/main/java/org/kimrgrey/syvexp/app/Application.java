package org.kimrgrey.syvexp.app;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Application {
    private static final String DEFAULT_CONFIG_FILENAME = "syvexp-conf.json";
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
        formatter.printHelp( "syvexp", options);
    }

	public void execute() {
		if (commandLine == null) {
            return;
        }
        String configFileName = DEFAULT_CONFIG_FILENAME;
        if (commandLine.hasOption("config")) {
            configFileName = commandLine.getOptionValue("config");
        }
        Configuration configuration = null;
        Connection connection = null;
        Exporter exporter = null;
        try {
            configuration = Configuration.load(configFileName);
            exporter = configuration.createExporter();
            connection = configuration.createDatabaseConnection();
            List<Table> tableList = configuration.getTableList();
            for (Table table : tableList) {
                logger.info("Start extraction of table by name {}", table.getTableName());
            }
        } catch (InvalidConfigException exception) {
            logger.error("Failed to export data because of configuration error", exception);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                logger.warn("Failed to close database connection", exception);
            }
        }
	}
}