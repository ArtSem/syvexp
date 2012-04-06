package org.kimrgrey.syvexp.app;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class CompositeExporter implements Exporter {
	private List<Exporter> exporters = new ArrayList<Exporter>();

	public CompositeExporter() {
	}

	public void export(Map<String, String> row) throws  ExportException {
    	for (Exporter exporter : exporters) {
    		exporter.export(row);
    	}   
    }

	public void addExporter(Exporter exporter) {
		exporters.add(exporter);
	}

	public void setExporters(List<Exporter> exporters) {
		this.exporters = exporters;
	}

	public List<Exporter> getExporters() {
		return exporters;
	}
}