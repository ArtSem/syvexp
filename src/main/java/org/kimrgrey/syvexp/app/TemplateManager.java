package org.kimrgrey.syvexp.app;

import java.util.Properties;
import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import org.apache.velocity.app.Velocity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateManager {
    private static final Logger logger = LoggerFactory.getLogger(TemplateManager.class);
    private Map<String, Template> templates = new HashMap<String, Template>();
    
    public TemplateManager(final String dirName, final String extension) throws InvalidConfigException {
        File templateDirectory = new File(dirName);
        if (!templateDirectory.isDirectory()) {
            throw new InvalidConfigException("Failed to load templates because " + dirName + " is not directory");
        }
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "file");
        properties.setProperty("file.resource.loader.description", "Velocity File Resource Loader");
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        properties.setProperty("file.resource.loader.cache", "true");
        properties.setProperty("file.resource.loader.modificationCheckInterval ", "0");
        properties.setProperty("file.resource.loader.path", dirName);
        Velocity.init(properties);
    }

    public Template getTemplate(String templateName) throws InvalidConfigException {
        logger.debug("Load template from file " + templateName);
        try {
            Template template = Velocity.getTemplate(templateName);
            return template;
        } catch (ResourceNotFoundException exception) {
            throw new InvalidConfigException("Template " + templateName + " not found, please check working directory");
        } catch (ParseErrorException exception) {
            throw new InvalidConfigException("Parsing of template " + templateName + " failed");
        } catch (MethodInvocationException exception) {
            throw new InvalidConfigException("Unable to invoke some method while load template " + templateName);
        }
    }
}