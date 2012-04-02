package org.kimrgrey.syvexp.app;

import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import java.io.StringWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VelocityExporter implements Exporter {
	private static final Logger logger = LoggerFactory.getLogger(VelocityExporter.class);

    private Template template = null;
	private String templateName = null;

	public VelocityExporter(Template template) {
        this.template = template;
	}

	public void export(Map<String, String> row)  throws ExportException {
    	VelocityContext context = new VelocityContext();
    	context.put("data", row);
    	StringWriter writer = new StringWriter();
        template.merge(context, writer);
        save(writer.toString());
	}

    public void save(String xml) {
    	FileWriter writer = null;
    	String fileName = java.util.UUID.randomUUID().toString();
        try {
            writer = new FileWriter(fileName);
            writer.write(xml);
            writer.flush();
        } catch (IOException exception) {
            logger.error("Unable to create output file " + fileName, exception);
        } finally {
            try {
                writer.close();
            } catch (IOException exception) {
                logger.error("Unable to save output file " + fileName, exception);
            }
        }
    }
}
