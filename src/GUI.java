import java.awt.BorderLayout;
import java.io.File;
import java.util.*;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class GUI extends JFrame {
    private FileHandler fileHandler;
    private JComboBox<String> columnDropdown;
    private JTextArea resultArea;
    private JPanel chartPanelContainer; // Container to hold the chart in the same window
    private Map<String, List<Double>> sortedDataMap;

    public GUI() {
        fileHandler = new FileHandler();
        setTitle("Sorting Algorithm Performance Evaluator");
        setSize(800, 600);  // Increased height for chart space
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sortedDataMap = new HashMap<>();
        setupComponents();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private void setupComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();

        JButton uploadButton = new JButton("Upload CSV");
        uploadButton.addActionListener(e -> uploadCSV());

        columnDropdown = new JComboBox<>();
        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(e -> sortAndDisplay());

        JButton showSortedButton = new JButton("Show Sorted Data");
        showSortedButton.addActionListener(e -> showSortedData());

        resultArea = new JTextArea(15, 50);
        resultArea.setEditable(false);

        // Panel for chart
        chartPanelContainer = new JPanel();
        chartPanelContainer.setLayout(new BorderLayout()); // Chart panel to be added later

        topPanel.add(uploadButton);
        topPanel.add(new JLabel("Select Numeric Column:"));
        topPanel.add(columnDropdown);
        topPanel.add(sortButton);
        topPanel.add(showSortedButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        panel.add(chartPanelContainer, BorderLayout.SOUTH); // Add chart panel below text area

        add(panel);
    }

    private void uploadCSV() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (fileHandler.loadCSV(file.getPath())) {
                columnDropdown.removeAllItems(); // Clear the dropdown for new columns
                List<Integer> numericColumnIndexes = fileHandler.getNumericColumnIndexes();
                String[] columns = fileHandler.getColumns();

                if (numericColumnIndexes.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No numeric columns found in the selected CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Populate the dropdown with numeric column options
                for (int index : numericColumnIndexes) {
                    columnDropdown.addItem(columns[index]);
                }
                JOptionPane.showMessageDialog(this, "CSV file loaded successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to load CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void sortAndDisplay() {
        int columnIndex = columnDropdown.getSelectedIndex();
        if (columnIndex < 0) {
            JOptionPane.showMessageDialog(this, "Please select a numeric column first.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        List<Double> data = fileHandler.getColumnDataAsDouble(columnIndex);
        sortedDataMap.clear(); // Clear previous sorted data
    
        long bestTime = Long.MAX_VALUE;
        String bestAlgorithm = "";
        StringBuilder results = new StringBuilder("Sorting Performance:\n\n");
    
        Map<String, Long> sortingTimes = new LinkedHashMap<>(); // Store algorithm names and their times
    
        // List of sorting algorithms
        String[] algorithms = {"Insertion Sort", "Shell Sort", "Merge Sort", "Heap Sort", "Quick Sort"};
    
        for (String sortType : algorithms) {
            List<Double> dataCopy = new ArrayList<>(data); // Make a copy of the data to avoid modifying the original
    
            long startTime = System.nanoTime(); // Measure start time in nanoseconds
            PerformanceEvaluator.sortData(dataCopy, sortType); // Perform sorting
            long endTime = System.nanoTime(); // Measure end time in nanoseconds
    
            long elapsedTime = endTime - startTime; // Calculate elapsed time in nanoseconds
            sortingTimes.put(sortType, elapsedTime); // Save the elapsed time
    
            String resultLine = String.format("%s: %,d ns%n", sortType, elapsedTime);
            results.append(resultLine); // Append to the GUI's text area
            System.out.print(resultLine); // Print to the terminal
    
            sortedDataMap.put(sortType, dataCopy); // Store sorted data for this algorithm
    
            if (elapsedTime < bestTime) {
                bestTime = elapsedTime;
                bestAlgorithm = sortType;
            }
        }
    
        // Append the best-performing algorithm and time
        String bestPerformanceLine = "\nBest Performing Algorithm: " + bestAlgorithm
                + " with " + String.format("%,d ns", bestTime);
        results.append(bestPerformanceLine); // Append to the GUI's text area
        System.out.println(bestPerformanceLine); // Print to the terminal
    
        resultArea.setText(results.toString());
    
        // Now show the graph after displaying the results
        SwingUtilities.invokeLater(() -> showPerformanceGraph(sortingTimes));
    }
    
    private void showPerformanceGraph(Map<String, Long> sortingTimes) {
        // Clear any previous chart before displaying a new one
        chartPanelContainer.removeAll();

        // Create the dataset for the chart
        var dataset = new DefaultCategoryDataset();

        // Populate the dataset with sorting times in nanoseconds
        for (Map.Entry<String, Long> entry : sortingTimes.entrySet()) {
            dataset.addValue(entry.getValue(), "Time (ns)", entry.getKey());
        }

        // Create the chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Sorting Performance",     // Chart title
                "Sorting Algorithm",       // Category axis label
                "Time (ns)",               // Value axis label
                dataset                    // Dataset
        );

        // Convert the chart to a panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 300)); // Set chart panel size

        // Add the chart panel to the container
        chartPanelContainer.add(chartPanel, BorderLayout.CENTER);
        chartPanelContainer.revalidate();
        chartPanelContainer.repaint();
    }

    private void showSortedData() {
        if (sortedDataMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No data has been sorted yet!");
            return;
        }

        // Let the user select which sorted data to view
        String[] algorithms = sortedDataMap.keySet().toArray(new String[0]);
        String selectedAlgorithm = (String) JOptionPane.showInputDialog(
                this,
                "Select sorting algorithm:",
                "Show Sorted Data",
                JOptionPane.PLAIN_MESSAGE,
                null,
                algorithms,
                algorithms[0] // Default selected item
        );

        if (selectedAlgorithm == null) {
            return; // User canceled
        }

        // Retrieve the sorted data for the selected algorithm
        List<Double> sortedData = sortedDataMap.get(selectedAlgorithm);
        JTextArea sortedDataArea = new JTextArea(10, 50);
        sortedDataArea.setEditable(false);

        StringBuilder sortedResults = new StringBuilder(selectedAlgorithm + " - Sorted Data:\n");
        for (Double value : sortedData) {
            sortedResults.append(String.format("%.2f", value)).append("\n"); // Display each value with 2 decimal places
        }

        sortedDataArea.setText(sortedResults.toString());

        // Show the sorted data in a scrollable dialog
        JOptionPane.showMessageDialog(this, new JScrollPane(sortedDataArea), "Sorted Data", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
