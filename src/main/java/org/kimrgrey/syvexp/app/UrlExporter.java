package org.kimrgrey.syvexp.app;

import java.util.Map;

import java.io.StringWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLConnection;

public class UrlExporter implements Exporter {
	private static final Logger logger = LoggerFactory.getLogger(VelocityExporter.class);

    private URL url = null;
	
    public UrlExporter(String url) throws InvalidConfigException {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException exception) { 
            logger.debug("Invalid url {}", url, exception);
            throw new InvalidConfigException("Invalid url " + url);
        }
    }

	public void export(Map<String, String> row) throws  ExportException {
        String json = new Gson().toJson(row);
        logger.debug("Export another yet row {}", json);
        try {
            URLConnection uc = url.openConnection();
            uc.setDoInput(true);
            uc.setDoOutput(true);
            uc.setUseCaches(false);
            uc.setRequestProperty("Content-Type", "application/json");
            uc.setRequestProperty("Content-Length",json.length() + "");
            uc.connect();
            DataOutputStream dos = new DataOutputStream(uc.getOutputStream());
            dos.write(json.getBytes());
        } catch (IOException exception) { 
            logger.debug("Failed open connection to url {}", url, exception);
            throw new ExportException("Failed open connection to url " + url);
        }
        
    }
 
    public void setUrl(URL url){
         this.url = url;
    }

    public URL getUrl(){
        return this.url;
    }
}
