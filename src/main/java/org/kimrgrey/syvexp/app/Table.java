package org.kimrgrey.syvexp.app;

import java.util.List;

public class Table {
	private String tableName = null;
	private List<String> columns = null;

	public Table() {
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getColumnsList() {
		return columns;
	}

	public String toString() {
		return tableName + " " + columns.toString();
	}
}