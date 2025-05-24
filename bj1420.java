import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;

public class bj1420 {
    static int INF = 999999;
    static int H;
    static int W;
    public static int[] s;
    public static int[] e;

    public static int startIdx;
    public static int endIdx;

    public static int[] di = {1,0,-1,0};
    public static int[] dj = {0,1,0,-1};

    public static char[][] map;
    public static int[][][] graph; //[각각 시작 포인트], [4방향+내부 역방향], [도착지 포인트, 흐름, 캐파]
    public static boolean[][] visited;

    static int answer = -1;

    public static void main(String[] args) throws Exception{
        //InputStream ip = System.in;
        InputStream ip = new FileInputStream("input.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(ip));
        StringTokenizer st = new StringTokenizer(br.readLine()," ");

        H = Integer.parseInt(st.nextToken());
        W = Integer.parseInt(st.nextToken());
        graph = new int[H*W*2][5][3];
        map = new char[H][W];
        for(int i=0;i<H*W*2;i++){
            for(int j=0;j<5;j++){
                for(int k=0;k<3;k++){
                    graph[i][j][k] = -1;
                }
            }
        }

        String temp;
        for(int i=0;i<H;i++){
            temp = br.readLine();
            for(int j=0;j<W;j++){
                map[i][j] = temp.charAt(j);
                if(map[i][j] == 'K'){
                    s = new int[]{i,j};
                    startIdx = to1D(i,j);
                }

                if(map[i][j] == 'H'){
                    e = new int[]{i,j};
                    endIdx = to1D(i,j);
                }

                graphInit(i,j);
            }
        }


        if(!neighbor()){
            solve();
        }

        System.out.println(answer);
    }

    private static boolean neighbor() {
        for(int i=0;i<4;i++){
            int ni = s[0]+di[i];
            int nj = s[1]+dj[i];

            if(e[0] == ni && e[1] == nj){
                return true;
            }
        }

        return false;
    }

    private static void solve() {
        Queue<Integer> q = new ArrayDeque<>();
        int[] parent = new int[H*W*2];

        while(true){
            Arrays.fill(parent,-1);
            parent[startIdx] = startIdx;
            q.clear();
            q.add(startIdx);
            //System.out.println("START");

            while(!q.isEmpty()){
                int cur = q.poll();
                if(cur == endIdx){
                    //System.out.println("REACH END!");
                    int maxFlow = INF;
                    while(true){
                        int before = parent[cur];
                        for(int i=0;i<5;i++){
                            int idx = graph[before][i][0];
                            if(idx != cur) continue;

                            int flow = graph[before][i][1];
                            int capa = graph[before][i][2];
                            maxFlow = Math.min(maxFlow, capa-flow);
                            break;
                        }
                        //System.out.println((to2D(before)[0]+1)+","+(to2D(before)[1]+1)+(before%2==0? " IN":" OUT") +"->"+(to2D(cur)[0]+1)+","+(to2D(cur)[1]+1)+(cur%2==0? " IN":" OUT"));
                        cur = before;
                        if(cur == startIdx) {
                            break;
                        }
                    }
                    //System.out.println("MAX FLOW IS "+maxFlow);

                    cur = endIdx;
                    while(true) {
                        int before = parent[cur];
                        for (int i = 0; i < 4; i++) {
                            int idx = graph[before][i][0];
                            if (idx != cur) continue;

                            graph[before][i][1] += maxFlow;
                            graph[cur][(i+2)%4][1] -= maxFlow;
                            //System.out.println((to2D(before)[0]+1)+","+(to2D(before)[1]+1)+(before%2==0? " IN":" OUT") +"->"+(to2D(graph[before][i][0])[0]+1)+","+(to2D(graph[before][i][0])[1]+1)+((graph[before][i][0])%2==0? " IN ":" OUT ")+"ADDED");
                            //.out.println((to2D(cur)[0]+1)+","+(to2D(cur)[1]+1)+(cur%2==0? " IN":" OUT") +"->"+(to2D(graph[cur][(i+2)%4][0])[0]+1)+","+(to2D(graph[cur][(i+2)%4][0])[1]+1)+((graph[cur][(i+2)%4][0])%2==0? " IN ":" OUT ")+"DELETED");
                            break;
                        }
                        for (int i = 4; i < 5; i++) {
                            int idx = graph[before][i][0];
                            if (idx != cur) continue;

                            graph[before][4][1] += maxFlow;
                            graph[cur][4][1] -= maxFlow;
                            //System.out.println((to2D(before)[0]+1)+","+(to2D(before)[1]+1)+(before%2==0? " IN":" OUT") +"->"+(to2D(graph[before][i][0])[0]+1)+","+(to2D(graph[before][i][0])[1]+1)+((graph[before][i][0])%2==0? " IN ":" OUT ")+"ADDED");
                            //System.out.println((to2D(cur)[0]+1)+","+(to2D(cur)[1]+1)+(cur%2==0? " IN":" OUT") +"->"+(to2D(graph[cur][i][0])[0]+1)+","+(to2D(graph[cur][i][0])[1]+1)+((graph[cur][i][0])%2==0? " IN ":" OUT ")+"DELETED");
                            break;
                        }
                        cur = before;
                        if(cur == startIdx) break;
                    }

                    break;
                }

                //System.out.println("SEE ON "+(to2D(cur)[0]+1)+" "+(to2D(cur)[1]+1)+" "+(cur%2==0? "IN":"OUT"));
                //4+1방향으로 캐파 남는곳 추적
                for(int k=0;k<5;k++){
                    int next = graph[cur][k][0];
                    int flow = graph[cur][k][1];
                    int capa = graph[cur][k][2];

                    //System.out.println("NEXT IS "+(to2D(next)[0]+1)+","+(to2D(next)[1]+1));
                    //System.out.println("FLOW IS "+flow);
                    //System.out.println("CAPA IS "+capa);

                    if(next == -1) continue; //존재하지 않는 대상이면 무시
                    if(flow == capa) continue; //꽉찼으면 무시
                    if(parent[next] != -1) continue; // 이미 누군가 찜했으면 무시
                    parent[next] = cur;
                    q.add(next);
                }
            }

            //더이상 증가하는 방법 없으면 종료
            if(parent[endIdx] == -1) break;
        }

        answer = graph[startIdx][4][1];
    }

    private static void graphInit(int i, int j) {
        if(map[i][j] == '#') return;
        int si = to1D(i,j);
        int so = si+1;

        int innerFlow = (si == startIdx) ? 4:1;

        //0~3까지는 일반 간선에 쓸것
        //4번을 내부 아웃간선 -> 내부 인 간선에 할당
        graph[si][4][0] = so;
        graph[si][4][1] = 0;
        graph[si][4][2] = innerFlow;

        //0~3까지는 일반 간선에 쓸것
        //4번을 내부 아웃간선 -> 내부 인 간선에 할당
        graph[so][4][0] = si;
        graph[so][4][1] = 0;
        graph[so][4][2] = 0;

        //셀간 간선
        for(int k=0;k<4;k++){
            int ni = i +di[k];
            int nj = j +dj[k];
            if(ni < 0 || nj < 0 || ni >= H || nj >= W) continue;
            if(map[ni][nj] == '#') continue;
            int ei = to1D(ni,nj);

            //si -> so -> ei -> eo
            graph[so][k][0] = ei;
            graph[so][k][1] = 0;
            graph[so][k][2] = 1;

            //ei -> so 로 오는것의 역방향 간선
            graph[ei][(k+2)%4][0] = so;
            graph[ei][(k+2)%4][1] = 0;
            graph[ei][(k+2)%4][2] = 0;
        }
    }

    public static int to1D(int i, int j){
        return 2*(i*W+j);
    }

    public static int[] to2D(int idx){
        idx /= 2;
        return new int[]{idx/W,idx%W};
    }
}
