import java.io.*;
import java.util.*;

public class FileHandler {
    private List<String[]> rows;
    private String[] columns;

    public FileHandler() {
        rows = new ArrayList<>();
    }

    // Clear previous data
    public void clearData() {
        rows.clear();
        columns = null;
    }

    // Load the CSV file and read its content
    public boolean loadCSV(String filePath) {
        clearData();  // Clear any previous data
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            columns = reader.readLine().split(",");  // First row as column names
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != columns.length) {
                    // Skip rows with incorrect number of columns
                    System.err.println("Skipping row due to column mismatch: " + line);
                    continue;
                }
                rows.add(values);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get indexes of columns containing numeric data
    public List<Integer> getNumericColumnIndexes() {
        List<Integer> numericColumns = new ArrayList<>();
        if (rows.isEmpty()) {
            System.err.println("No data available to analyze numeric columns.");
            return numericColumns;
        }

        for (int i = 0; i < columns.length; i++) {
            try {
                Double.parseDouble(rows.get(0)[i]);  // Check the first row for numeric data
                numericColumns.add(i);
            } catch (NumberFormatException e) {
                // Column is not numeric
            }
        }
        return numericColumns;
    }

    // Get the names of columns
    public String[] getColumns() {
        return columns;
    }

    // Get column data as a list of Double values
    public List<Double> getColumnDataAsDouble(int columnIndex) {
        List<Double> columnData = new ArrayList<>();
        for (String[] row : rows) {
            try {
                columnData.add(Double.parseDouble(row[columnIndex]));  // Parse the value as double
            } catch (NumberFormatException e) {
                // Skip invalid data
                System.err.println("Invalid data in row, skipping value: " + Arrays.toString(row));
            }
        }
        return columnData;
    }
}
