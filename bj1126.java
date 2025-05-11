import java.io.*;
import java.util.*;

public class bj1126 {
    public static int[] blocks;
    public static int[][] dp;
    public static int maxH;
    public static int INF = 999999999;
    public static int N;
    public static void main(String[] args) throws IOException {
        InputStream input1 = System.in;
        InputStream input2 = new FileInputStream("input.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(input2));

        N = Integer.parseInt(br.readLine());
        blocks = new int[N];
        StringTokenizer st = new StringTokenizer(br.readLine()," ");
        int total = 0;
        for(int i=0;i<N;i++){
            blocks[i] = Integer.parseInt(st.nextToken());
            total += blocks[i];
        }

        maxH = total/2;
        dp = new int[N][maxH+1];

        //Arrays.sort(blocks);

        int answer = doBlock(0, 0,0);
        answer = answer > 0 ? answer/2 : -1;

        System.out.println(answer);
    }

    private static int doBlock(int higher, int diff, int idx) {
        if(higher > maxH) return -INF;
        if(diff > maxH) return -INF;

        if(idx == N){
            if(diff == 0){
                return 0;
            }else{
                return -INF;
            }
        }

        if(dp[idx][diff] != 0) return dp[idx][diff];

        int blockH = blocks[idx];

        int answer = -INF;
        answer = Math.max(answer,blockH+doBlock(higher+blockH,diff+blockH,idx+1));

        answer = Math.max(answer,doBlock(higher,diff,idx+1));

        if(blockH > diff){
            answer = Math.max(answer,blockH+doBlock(higher-diff+blockH,blockH-diff,idx+1));
        }else{
            answer = Math.max(answer,blockH+doBlock(higher,diff-blockH,idx+1));
        }

        return dp[idx][diff] = answer;
    }
}
