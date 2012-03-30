package org.kimrgrey.syvexp.app;


import java.util.Map;

public interface Exporter {
    public void export(Map<String, String> row)  throws ExportException;
}