package iglabs.warehouse.converter;

public interface AppContext {
	CsvWriter getCsvWriter();
	ExcelReader getExcelReader();
}
