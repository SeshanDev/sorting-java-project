// src/GUI.java
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class GUI extends JFrame {
    private FileHandler fileHandler;
    private JComboBox<String> columnDropdown;
    private JTextArea resultArea;
    private List<Integer> sortedData; // Store sorted data

    public GUI() {
        fileHandler = new FileHandler();
        setTitle("Sorting Algorithm Performance Evaluator");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupComponents();
        setVisible(true);
    }

    private void setupComponents() {
        JPanel panel = new JPanel();
        JButton uploadButton = new JButton("Upload CSV");
        uploadButton.addActionListener(e -> uploadCSV());

        columnDropdown = new JComboBox<>();
        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(e -> sortAndDisplay());

        JButton showSortedButton = new JButton("Show Sorted Data");
        showSortedButton.addActionListener(e -> showSortedData()); // Button to show sorted data

        resultArea = new JTextArea(10, 50);
        resultArea.setEditable(false);

        panel.add(uploadButton);
        panel.add(new JLabel("Select Numeric Column:"));
        panel.add(columnDropdown);
        panel.add(sortButton);
        panel.add(showSortedButton); // Add the "Show Sorted Data" button
        panel.add(new JScrollPane(resultArea));

        add(panel);
    }

    // Upload the CSV file and load numeric columns
    private void uploadCSV() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (fileHandler.loadCSV(file.getPath())) {
                columnDropdown.removeAllItems();
                List<Integer> numericColumnIndexes = fileHandler.getNumericColumnIndexes();
                String[] columns = fileHandler.getColumns();
                // Add only numeric columns to the dropdown
                for (int index : numericColumnIndexes) {
                    columnDropdown.addItem(columns[index]);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to load CSV file.");
            }
        }
    }

    // Sort the selected column and display the results
    private void sortAndDisplay() {
        int columnIndex = columnDropdown.getSelectedIndex();
        List<Integer> data = fileHandler.getColumnData(columnIndex);
        sortedData = new ArrayList<>(data); // Save a copy of the original data for later use

        // Measure and store the times for each algorithm
        long bestTime = Long.MAX_VALUE;
        String bestAlgorithm = "";
        StringBuilder results = new StringBuilder();

        // Algorithms to evaluate
        for (String sortType : new String[]{"Insertion Sort", "Shell Sort", "Merge Sort", "Quick Sort", "Heap Sort"}) {
            long time = PerformanceEvaluator.measureSortTime(data, sortType);
            results.append(sortType).append(": ").append(time).append(" ns\n");

            // Track the best-performing algorithm
            if (time < bestTime) {
                bestTime = time;
                bestAlgorithm = sortType;
            }
        }

        // Display the results and the best-performing algorithm
        results.append("\nBest Performing Algorithm: ").append(bestAlgorithm).append(" with ").append(bestTime).append(" ns");
        resultArea.setText(results.toString());
    }

    // Show the sorted data in a separate dialog
    private void showSortedData() {
        if (sortedData == null || sortedData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No data has been sorted yet!");
            return;
        }

        // Create a new JTextArea to display sorted data in a separate dialog
        JTextArea sortedDataArea = new JTextArea(10, 50);
        sortedDataArea.setEditable(false);

        // Display the sorted data in the new JTextArea
        StringBuilder sortedResults = new StringBuilder("Sorted Data:\n");
        for (Integer value : sortedData) {
            sortedResults.append(value).append("\n");
        }
        sortedDataArea.setText(sortedResults.toString());

        // Create and show a new dialog to display sorted data
        JOptionPane.showMessageDialog(this, new JScrollPane(sortedDataArea), "Sorted Data", JOptionPane.INFORMATION_MESSAGE);
    }
}
