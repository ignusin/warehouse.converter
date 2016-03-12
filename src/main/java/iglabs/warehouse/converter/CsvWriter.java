package iglabs.warehouse.converter;

public interface CsvWriter {	
	void write(String path, DataTable dataTable, int[] columnMap) throws Exception;
}
