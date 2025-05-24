import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class bj1017 {
    static boolean[] prime;
    static int n;
    static int[] to;
    static boolean[] visited;
    static int[][] arr;
    static List<Integer>[] graph;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        n = Integer.parseInt(br.readLine());
        int size = n/2;

        try {
            arr = caculcateArr(br.readLine());
        } catch (Exception e) {
            return;
        }

        prime = getPrime(2000);

        graph = new ArrayList[size];
        for (int i = 0; i < size; i++) {
            graph[i] = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                if (prime[arr[i][0] + arr[j][1]]) {
                    graph[i].add(j);
                }
            }
        }

        printAnser(getAnswer());
    }

    private static void printAnser(List<Integer> list) {
        for(int i = 0; i< list.size(); i++){
            System.out.print(list.get(i)+" ");
        }
        if(list.isEmpty()){
            System.out.println(-1);
        }
    }

    private static List<Integer> getAnswer() {
        List<Integer> list = new ArrayList<>();

        for(int i=0;i<n/2;i++){
            if(!tryBinaryMatch(i)) continue;
            list.add(arr[i][1]);
        }
        Collections.sort(list);
        return list;
    }

    private static int[][] caculcateArr(String numbers) throws Exception {
        int firstIdx = 0;
        int secondIdx = 0;
        int firstRemain = 0;

        String[] tokens = numbers.split(" ");

        int[][] arr = new int[n/2][2];

        firstRemain = Integer.parseInt(tokens[0])%2;

        for(int i=0;i<n;i++){
            int temp = Integer.parseInt(tokens[i]);

            int idx = temp%2 == firstRemain ? (firstIdx++) : (secondIdx++);
            int idx2 = temp%2 == firstRemain ? 0 : 1;

            if(idx >= n/2){
                System.out.println("-1");
                throw new Exception("IDX OVERFLOW");
            }

            arr[idx][idx2] = temp;
        }

        return arr;
    }

    private static boolean tryBinaryMatch(int j) {
        if(!isPrime(0,j)) return false;

        to = new int[n/2];
        Arrays.fill(to,-1);
        to[j] = 0;

        for(int k=1;k<n/2;k++){
            visited = new boolean[n/2];
            visited[j] = true;
            if(!match(k)){
                return false;
            }
        }

        return true;
    }

    private static boolean isPrime(int i, int j) {
        return prime[arr[i][0]+arr[j][1]];
    }

    private static boolean match(int i) {
        for (int j : graph[i]) {
            if (!isPrime(i, j) || visited[j]) continue;
            visited[j] = true;

            if (to[j] == -1 || match(to[j])) {
                to[j] = i;
                return true;
            }
        }
        return false;
    }

    private static boolean[] getPrime(int max){
        boolean[] prime = new boolean[max+1];
        Arrays.fill(prime, true);
        prime[0] = false;
        prime[1] = false;

        for(int i=2;i*i<=max;i++){
            if(!prime[i]) continue;

            for(int j=i*i;j<=max;j+=i){
                prime[j] = false;
            }
        }
        return prime;
    }
}