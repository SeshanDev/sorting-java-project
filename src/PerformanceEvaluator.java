import java.util.List;

public class PerformanceEvaluator {

    // Sort data based on the chosen algorithm
    public static void sortData(List<Double> data, String algorithm) {
        switch (algorithm) {
            case "Insertion Sort":
                SortAlgorithms.insertionSort(data);
                break;
            case "Shell Sort":
                SortAlgorithms.shellSort(data);
                break;
            case "Merge Sort":
                SortAlgorithms.mergeSort(data);
                break;
            case "Quick Sort":
                SortAlgorithms.quickSort(data);
                break;
            case "Heap Sort":
                SortAlgorithms.heapSort(data);
                break;
            default:
                throw new IllegalArgumentException("Unknown sorting algorithm: " + algorithm);
        }
    }
}
