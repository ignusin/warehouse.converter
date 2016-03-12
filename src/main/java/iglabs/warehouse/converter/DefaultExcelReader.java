package iglabs.warehouse.converter;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class DefaultExcelReader implements ExcelReader {
	
	public DataTable read(String path) throws Exception {
        try (InputStream input = new FileInputStream(path)) {
            Workbook wb = WorkbookFactory.create(input);
            
            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) {
            	throw new RuntimeException("No sheet found in provided file.");
            }
            
            DataTable result = null;
            
            int rowCount = 0;
            Iterator<Row> rows = sheet.rowIterator();
            
            while (rows.hasNext()) {
            	Row row = rows.next();
            	
            	// Читаем заголовок.
            	if (rowCount == 0) {
            		String[] columns = readColumnsRow(row);
            		if (columns.length == 0) {
            			break;
            		}
            		
                    result = new DataTable(columns);
            	}
            	// Читаем строку с данными.
            	else {
            		Object[] values = readValuesRow(row);
            		if (values.length == 0) {
            			break;
            		}
            		
            		result.addRow(values);
            	}
            	
            	++rowCount;
            }
            
            if (rowCount <= 1) {
            	throw new RuntimeException("No rows found in provided file.");
            }
            
            return result;
        }
	}
	
	private String[] readColumnsRow(Row row) {
        ArrayList<String> rowData = new ArrayList<>();
        Iterator<Cell> cells = row.cellIterator();
        
        int cellCount = 1;
        while (cells.hasNext()) {
        	Cell cell = cells.next();
        	
            String columnName;
            switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
            	columnName = cell.getStringCellValue();
            	break;
            	
            default:
            	columnName = "Столбец #" + Integer.toString(cellCount);
            	break;
            }
            
            rowData.add(columnName);
            ++cellCount;
        }
        
        String[] result = new String[rowData.size()];
        rowData.toArray(result);
        
        return result;
	}
	
    private Object[] readValuesRow(Row row) {
        ArrayList<Object> rowData = new ArrayList<>();
        Iterator<Cell> cells = row.cellIterator();
        
        while (cells.hasNext()) {
        	Cell cell = cells.next();
        	
            Object cellValue;
            switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
            	cellValue = cell.getStringCellValue();
            	break;
            	
            case Cell.CELL_TYPE_NUMERIC:
            	cellValue = cell.getNumericCellValue();
            	break;
            	
            default:
            	cellValue = null;
            	break;
            }
            
            rowData.add(cellValue);
        }
        
        Object[] result = new Object[rowData.size()];
        rowData.toArray(result);
        
        return result;
    }
}
