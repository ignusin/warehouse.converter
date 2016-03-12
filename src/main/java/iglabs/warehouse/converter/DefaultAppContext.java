package iglabs.warehouse.converter;

public class DefaultAppContext implements AppContext {
	private static final DefaultAppContext instance = new DefaultAppContext();
	
	public static DefaultAppContext getInstance() {
		return instance;
	}
	
	
	private CsvWriter csvWriter;
	private ExcelReader excelReader;	
	
	private DefaultAppContext() {
	}
	
	public void bootstrap() {
		csvWriter = new DefaultCsvWriter();
		excelReader = new DefaultExcelReader();
	}
	
	public CsvWriter getCsvWriter() {
		return csvWriter;
	}
	
	public ExcelReader getExcelReader() {
		return excelReader;
	}
}
