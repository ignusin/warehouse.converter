package iglabs.warehouse.converter;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class DefaultCsvWriter implements CsvWriter {
	public void write(String path, DataTable dataTable, int[] columnMap) throws Exception {
        try (PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8.name())) {
            generateCsv(columnMap, dataTable, writer);
        }
	}
	
	private void generateCsv(int[] columnMap, DataTable dataTable, PrintWriter writer) {
        String[] headers = dataTable.getColumns();

        for (int i = 0; i < columnMap.length; ++i) {
            String headerValue = headers[columnMap[i]];

            if (i > 0) {
                writer.print(";");
            }
            writer.print(headerValue.replace(';', ','));
        }
        writer.println();

        Iterator<Object[]> rowIterator = dataTable.getRows().iterator();
        while (rowIterator.hasNext()) {
            Object[] row = rowIterator.next();

            try {
                String code = (String)row[columnMap[0]];                
                String name = (String)row[columnMap[1]];
                String description = (String)row[columnMap[2]];
                String unit = (String)row[columnMap[3]];
                double quantity = (double)row[columnMap[4]];
                double price = (double)row[columnMap[5]];
                
                writer.print(filterStringValue(code));
                writer.print(";");
                writer.print(filterStringValue(name));
                writer.print(";");
                writer.print(filterStringValue(description));
                writer.print(";");
                writer.print(filterStringValue(unit));
                writer.print(";");
                writer.print(quantity);
                writer.print(";");
                writer.print(price);
                writer.println();
            }
            catch (RuntimeException ex) {
            }
        }
	}
	
    private static String filterStringValue(String source) {
        if (source == null) {
            return "";
        }
        
        return source
	        .replace(';', ',')
	        .replace("\r", " ")
	        .replace("\n", " ");
    }
}
