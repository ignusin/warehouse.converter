package iglabs.warehouse.converter;

import java.util.ArrayList;

public class DataTable {
	private final String[] columns;
	private final ArrayList<Object[]> rows = new ArrayList<>();
	
	public DataTable(String[] columns) {
		this.columns = columns;
	}
	
	public void addRow(Object[] row) {
		if (row.length != columns.length) {
			throw new RuntimeException("The length of row does not match the length of columns.");
		}
		
		rows.add(row);
	}
	
	public String[] getColumns() {
		return columns;
	}
	
	public ArrayList<Object[]> getRows() {
		return rows;
	}
}
