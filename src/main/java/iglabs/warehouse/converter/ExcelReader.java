package iglabs.warehouse.converter;

public interface ExcelReader {
	DataTable read(String path) throws Exception;
}
