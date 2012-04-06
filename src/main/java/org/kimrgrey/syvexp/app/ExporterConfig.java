package org.kimrgrey.syvexp.app;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExporterConfig {
	private String className = null;
	private String configFile = null;
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);


	public ExporterConfig() {
		
	}

	public Exporter createInstance() throws InvalidConfigException {
		Exporter exporter = null;
		try {
			exporter = (Exporter) new Gson().fromJson(new InputStreamReader(new FileInputStream(configFile)), Class.forName(className));
		} catch (FileNotFoundException exception) {
			logger.debug("Failed to read configuration because file " + className + " not found", exception);
			throw new InvalidConfigException("Failed to read configuration because file " + className + " not found");
		} catch (SecurityException exception) {
			logger.debug("Failed to read configuration because access to file " + className + " was denied", exception);
			throw new InvalidConfigException("Failed to read configuration because access to file " + className + " was denied");
		} catch (JsonIOException exception) {
			logger.debug("Failed to read configuration from file " + className + " because of I/O error", exception);
			throw new InvalidConfigException("Failed to read configuration from file " + className + " because of I/O error");
		} catch (JsonSyntaxException exception) {
			logger.debug("Failed to read configuration from file " + className + " because of syntax error", exception);
			throw new InvalidConfigException("Failed to read configuration from file " + className + " because of syntax error");
		} catch (Exception exception) {
			logger.debug("Ooops... Something wrong happened", exception);
			throw new InvalidConfigException("Ooops... Something wrong happened");
		}
		return exporter;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public String getConfigFile() {
		return configFile;
	}

}