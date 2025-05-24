import java.io.*;
import java.util.StringTokenizer;

public class bj11049 {
    static int[][] matrix;
    static int[][] dp;
    static int n;
    public static void main(String[] args) throws IOException {
        InputStream ip1 = System.in;
        InputStream ip2 = new FileInputStream("input.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(ip2));

        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        matrix = new int[n][2];
        dp = new int[n][n];

        for(int i=0;i<n;i++){
            st = new StringTokenizer(br.readLine());
            matrix[i][0] = Integer.parseInt(st.nextToken());
            matrix[i][1] = Integer.parseInt(st.nextToken());
        }

        System.out.println(solve());
    }

    private static int solve() {
        for(int i=2;i<=n;i++){
            for(int j=0;j+i-1<n;j++){
                dp[j][j+i-1] = calculate(j,j+i-1);
            }
        }

        return dp[0][n-1];
    }

    public static int calculate(int s, int e){
        if(s==e) return 0;

        if(dp[s][e] > 0) return dp[s][e];

        if(s+1 == e) return matrix[s][0]*matrix[s][1]*matrix[e][1];

        int result = (1<<31)-1;

        for(int j=s;j<e;j++){
            result = Math.min(result, dp[s][j]+dp[j+1][e] + matrix[s][0]*matrix[j][1]*matrix[e][1]);
        }

        return dp[s][e] = result;
    }
}
