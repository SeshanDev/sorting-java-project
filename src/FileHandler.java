// src/FileHandler.java
import java.io.*;
import java.util.*;

public class FileHandler {
    @SuppressWarnings("FieldMayBeFinal")
    private List<String[]> data = new ArrayList<>();

    // Load CSV and return true if successful
    public boolean loadCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
            return false;
        }
    }

    // Get the column names (headers) for dropdown menu
    public String[] getColumns() {
        return data.isEmpty() ? new String[0] : data.get(0);
    }

    // Get numeric data from the selected column (by index)
    public List<Integer> getColumnData(int columnIndex) {
        List<Integer> columnData = new ArrayList<>();
        for (int i = 1; i < data.size(); i++) {
            try {
                columnData.add(Integer.parseInt(data.get(i)[columnIndex]));
            } catch (NumberFormatException e) {
                // Ignore non-numeric data
            }
        }
        return columnData;
    }

    // Get numeric column indexes
    public List<Integer> getNumericColumnIndexes() {
        List<Integer> numericColumnIndexes = new ArrayList<>();
        String[] columns = getColumns();
        for (int i = 0; i < columns.length; i++) {
            if (isNumericColumn(i)) {
                numericColumnIndexes.add(i);
            }
        }
        return numericColumnIndexes;
    }

    // Check if a column is numeric (by checking the first few rows)
    private boolean isNumericColumn(int columnIndex) {
        for (int i = 1; i < Math.min(10, data.size()); i++) {
            try {
                Integer.parseInt(data.get(i)[columnIndex]);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
}
