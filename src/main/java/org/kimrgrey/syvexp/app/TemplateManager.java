package org.kimrgrey.syvexp.app;


import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateManager {
    private static final Logger logger = LoggerFactory.getLogger(TemplateManager.class);
    private Map<String, Template> templates = new HashMap<String, Template>();

    public TemplateManager(final String dirName, final String extension) {
        File templateDirectory = new File(dirName);
        File[] templateFiles = templateDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(extension);
            }
        });
        for (File templateFile : templateFiles) {
            try {
                Template template = loadTemplate(dirName + File.separator + templateFile.getName());
                if (template != null) {
                    templates.put(templateFile.getName(), template);
                }
            } catch (InvalidConfigException exception) {
                logger.warn("Failed to load some tempalte", exception);
            }
        }
    }

    public Template getTemplate(String templateName) {
        return templates.get(templateName);
    }

    private Template loadTemplate(String templateName) throws InvalidConfigException {
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