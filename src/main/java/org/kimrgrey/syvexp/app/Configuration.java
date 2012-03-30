package org.kimrgrey.syvexp.app;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;

import java.lang.SecurityException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class Configuration {
	private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

	public static Configuration load(String configFileName) throws InvalidConfigException {
		Configuration configuration = null;
		try {
			configuration = new Gson().fromJson(new InputStreamReader(new FileInputStream(configFileName)) , Configuration.class);
		} catch (FileNotFoundException exception) {
			logger.debug("Failed to read configuration because file " + configFileName + " not found", exception);
			throw new InvalidConfigException("Failed to read configuration because file " + configFileName + " not found");
		} catch (SecurityException exception) {
			logger.debug("Failed to read configuration because access to file " + configFileName + " was denied", exception);
			throw new InvalidConfigException("Failed to read configuration because access to file " + configFileName + " was denied");
		} catch (JsonIOException exception) {
			logger.debug("Failed to read configuration from file " + configFileName + " because of I/O error", exception);
			throw new InvalidConfigException("Failed to read configuration from file " + configFileName + " because of I/O error");
		} catch (JsonSyntaxException exception) {
			logger.debug("Failed to read configuration from file " + configFileName + " because of syntax error", exception);
			throw new InvalidConfigException("Failed to read configuration from file " + configFileName + " because of syntax error");
		}
		logger.debug("Template directory is {}", configuration.getTemplateDirectory());
		logger.debug("Template extension is {}", configuration.getTemplateExtension());
		return configuration;
	}

	private String templateDirectory = null;
	private String templateExtension = null;

	public Configuration() {
	}

	public void setTemplateDirectory(String templateDirectory) {
		this.templateDirectory = templateDirectory;
	}

	public String getTemplateDirectory() {
		return templateDirectory;
	}

	public void setTemplateExtension(String templateExtension) {
		this.templateExtension = templateExtension;
	}

	public String getTemplateExtension() {
		return templateExtension;
	}

	public TemplateManager createTemplateManager() throws InvalidConfigException {
		return new TemplateManager(templateDirectory, templateExtension);
	}
}