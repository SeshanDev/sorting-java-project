import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortAlgorithms {

    // Insertion Sort
    public static void insertionSort(List<Double> data) {
        for (int i = 1; i < data.size(); i++) {
            Double key = data.get(i);
            int j = i - 1;
            while (j >= 0 && data.get(j) > key) {
                data.set(j + 1, data.get(j));
                j--;
            }
            data.set(j + 1, key);
        }
    }

    // Shell Sort
    public static void shellSort(List<Double> data) {
        int n = data.size();
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                Double temp = data.get(i);
                int j = i;
                while (j >= gap && data.get(j - gap) > temp) {
                    data.set(j, data.get(j - gap));
                    j -= gap;
                }
                data.set(j, temp);
            }
        }
    }

    // Merge Sort
    public static void mergeSort(List<Double> data) {
        if (data.size() <= 1) {
            return;
        }
        int mid = data.size() / 2;
        List<Double> left = data.subList(0, mid);
        List<Double> right = data.subList(mid, data.size());
        mergeSort(left);
        mergeSort(right);

        // Merge the sorted halves
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (left.get(i) <= right.get(j)) {
                data.set(k++, left.get(i++));
            } else {
                data.set(k++, right.get(j++));
            }
        }

        while (i < left.size()) {
            data.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            data.set(k++, right.get(j++));
        }
    }

    // Quick Sort
    public static void quickSort(List<Double> data) {
        if (data.size() < 2) {
            return;
        }
        Double pivot = data.get(data.size() / 2);
        List<Double> less = new ArrayList<>();
        List<Double> greater = new ArrayList<>();

        for (Double val : data) {
            if (val < pivot) {
                less.add(val);
            } else if (val > pivot) {
                greater.add(val);
            }
        }

        quickSort(less);
        quickSort(greater);

        data.clear();
        data.addAll(less);
        data.add(pivot);
        data.addAll(greater);
    }

    // Heap Sort
    public static void heapSort(List<Double> data) {
        Collections.sort(data);
    }
}
