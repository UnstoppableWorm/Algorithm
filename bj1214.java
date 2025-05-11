import java.io.*;
import java.util.StringTokenizer;

public class bj1214 {
    public static void main(String[] args) throws IOException {
        InputStream input1 = System.in;
        //InputStream input2 = new FileInputStream("input.txt");

        BufferedReader br = new BufferedReader(new InputStreamReader(input1));
        StringTokenizer st = new StringTokenizer(br.readLine()," ");

        int D = Integer.parseInt(st.nextToken());
        int P = Integer.parseInt(st.nextToken());
        int Q = Integer.parseInt(st.nextToken());

        if(P<Q){
            int temp = P;
            P = Q;
            Q = temp;
        }
        int INF = 2000000001;
        int mini = INF;
        for(int i=0;i<=Math.min(D/P, Q)+1;i++){
            int remain = D - P*i;
            int mok = 0;
            if(remain > 0){
                mok = remain/Q;
                if(remain % Q != 0) mok++;
            }
            mini = Math.min(mini, P*i+mok*Q);
        }

        System.out.println(mini);
    }
}
