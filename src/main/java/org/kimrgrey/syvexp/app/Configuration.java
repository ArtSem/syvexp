package org.kimrgrey.syvexp.app;

import java.util.List;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;

import java.lang.SecurityException;
import java.lang.ClassNotFoundException;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

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
		logger.debug("Database driver class is {}", configuration.getJdbcDriver());
		logger.debug("Database url is {}", configuration.getJdbcUrl());
		logger.debug("Database user is {}", configuration.getJdbcUser());
		logger.debug("Database password is {}", configuration.getJdbcPassword());
		logger.debug("Table list is {}", configuration.getTableList());
		return configuration;
	}

	private String templateDirectory = null;
	private String templateExtension = null;
	private String templateName = null;
	private String jdbcDriver = null;
	private String jdbcUrl = null;
	private String jdbcUser = null;
	private String jdbcPassword = null;
	private List<Table> tables = null;
	private List<ExporterConfig> configs = null;

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

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public TemplateManager createTemplateManager() throws InvalidConfigException {
		return new TemplateManager(templateDirectory, templateExtension);
	}

	public Exporter createExporter() throws InvalidConfigException {
		CompositeExporter compositeExporter = new CompositeExporter();
		for (ExporterConfig config : configs) {
			compositeExporter.addExporter(config.createInstance());
		}
		return compositeExporter;
	}

	public Connection createDatabaseConnection() throws InvalidConfigException {
		try { 
			Class.forName(jdbcDriver);
        	return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
		} catch (ClassNotFoundException exception) {
			logger.debug("Failed to load JDBC driver", exception);
			throw new InvalidConfigException("Failed to load JDBC driver");
		} catch (SQLException exception) {
			logger.debug("Failed to open JDBC connection", exception);
			throw new InvalidConfigException("Failed to open JDBC connection");
		}	
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUser(String jdbcUser) {
		this.jdbcUser = jdbcUser;
	}	

	public String getJdbcUser() {
		return jdbcUser;
	}

	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

	public String getJdbcPassword() {
		return jdbcPassword;
	}

	public List<Table> getTableList() {
		return tables;
	}
}