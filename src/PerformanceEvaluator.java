// src/PerformanceEvaluator.java
import java.util.ArrayList;
import java.util.List;

public class PerformanceEvaluator {
    public static long measureSortTime(List<Integer> data, String sortType) {
        List<Integer> list = new ArrayList<>(data);
        long startTime = System.nanoTime();

        switch (sortType) {
            case "Insertion Sort" -> SortAlgorithms.insertionSort(list);
            case "Shell Sort" -> SortAlgorithms.shellSort(list);
            case "Merge Sort" -> SortAlgorithms.mergeSort(list);
            case "Quick Sort" -> SortAlgorithms.quickSort(list, 0, list.size() - 1);
            case "Heap Sort" -> SortAlgorithms.heapSort(list);
        }

        return System.nanoTime() - startTime;
    }
}
