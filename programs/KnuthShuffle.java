import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.StdIn;

public class KnuthShuffle {
    public static void main(String[] args) {
        List<Integer> input = new ArrayList<>();
        while (!StdIn.isEmpty()) {
            input.add(StdIn.readInt());
        }
        shuffle(input);
    }

    public static void shuffle(List<Integer> input) {
        for (int i = 0; i < input.size(); i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            swap(input, i, randomIndex);
        }
        for (int i = 0; i < input.size(); i++) {
            System.out.print(input.get(i) + " ");
        }
    }

    private static void swap(List<Integer> input, int i, int j) {
        int tmp = input.get(i);
        input.set(i, input.get(j));
        input.set(j, tmp);
    }
    
}
