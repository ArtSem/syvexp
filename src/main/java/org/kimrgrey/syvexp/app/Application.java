package org.kimrgrey.syvexp.app;

import java.util.HashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import org.apache.velocity.Template;

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
        options.addOption("t", "template", true, "Velocity template that will be used to generate messages");
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
        try {
            Configuration configuration = Configuration.load(commandLine.getOptionValue("config"));
            Template template = configuration.createTemplateManager().getTemplate(commandLine.getOptionValue("template"));
            VelocityExporter exporter = new VelocityExporter(template);
            HashMap<String, String> row = new HashMap<String, String>();
            row.put("column#1", "value#1");
            row.put("column#2", "value#2");
            row.put("column#3", "value#3");
            exporter.export(row);
        } catch (InvalidConfigException exception) {
            logger.error("Failed to export data because of configuration error", exception);
        } catch (ExportException exception) {
            logger.error("Failed to export data because of template error", exception);
        }
	}
}