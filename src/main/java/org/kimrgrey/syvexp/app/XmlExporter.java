package org.kimrgrey.syvexp.app;


import java.util.Map;
import java.io.IOException;

abstract class XmlExporter implements Exporter {

	public void export(Map<String, String> row)  throws ExportException {
    	String xml = null;
    	try {
    		save(xml);
    	} catch (IOException exception) {
    		throw new ExportException("Failed to export some row because of I/O error");
    	}
    }

    public abstract void save (String xml) throws IOException;
}