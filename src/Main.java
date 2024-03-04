import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        int numThreads = 1000;
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new RouteGenerator());
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        printResults();
    }

    public static void printResults() {
        int maxFreq = 0;
        int maxFreqCount = 0;
        Map<Integer, Integer> otherFreqs = new HashMap<>();

        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            int freq = entry.getValue();
            if (freq > maxFreq) {
                maxFreq = freq;
                maxFreqCount = 1;
            } else if (freq == maxFreq) {
                maxFreqCount++;
            } else {
                otherFreqs.put(entry.getKey(), freq);
            }
        }

        System.out.println("Самое частое количество повторений " + maxFreq + " (встретилось " + maxFreqCount + " раз)");
        System.out.println("Другие размеры:");
        otherFreqs.forEach((size, freq) -> System.out.println("- " + size + " (" + freq + " раз)"));
    }

    public static class RouteGenerator implements Runnable {
        @Override
        public void run() {
            String route = generateRoute("RLRFR", 100);
            int rightTurns = countRightTurns(route);
            synchronized (sizeToFreq) {
                sizeToFreq.put(rightTurns, sizeToFreq.getOrDefault(rightTurns, 0) + 1);
            }
        }

        public String generateRoute(String letters, int length) {
            Random random = new Random();
            StringBuilder route = new StringBuilder();
            for (int i = 0; i < length; i++) {
                route.append(letters.charAt(random.nextInt(letters.length())));
            }
            return route.toString();
        }

        public int countRightTurns(String route) {
            int count = 0;
            for (int i = 0; i < route.length(); i++) {
                if (route.charAt(i) == 'R') {
                    count++;
                }
            }
            return count;
        }
    }
}
