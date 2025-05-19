import java.io.*;
import java.util.StringTokenizer;

public class bj1420 {
    static int H;
    static int W;
    public static int[] s;
    public static int[] e;

    public static int[] dy = {1,-1,0,0};
    public static int[] dx = {0,0,1,-1};

    public static char[][] map;
    public static int[][][] graph;
    public static boolean[][] visited;

    public static void main(String[] args) throws IOException {
        map = mapInitialize(H, W);
        graph = graphInitialize();
        solve();
    }

    private static void solve() {
        int result = 0;
        while(true){
            result += dfs(convertTo1D(s),99999);
        }
    }

    private static int dfs(int idx, int maxFlow) {
        for(int[] arr: graph[idx]){
            int nIdx = arr[0];
            int curFlow = arr[1];
            int capa = arr[2];

            if(capa - curFlow <= 0) continue; //이미 꽉참!

            int flow = dfs(nIdx, Math.min(maxFlow,capa-curFlow));
            arr[1] -= flow;
            for(int[] arr2: graph[nIdx]){
                if(arr2[0] != idx) continue;

                arr2[1] += flow; //반대방향 나머지 흐름은 더해줌
            }
        }

        return 0;
    }

    public static int convertTo1D(int[] arr){
        return convertTo1D(arr[0], arr[1]);
    }

    public static int convertTo1D(int i, int j){
        return i*W+j;
    }

    public static int[] convertTo2D(int idx){
        int[] result = new int[2];
        result[0] = idx/W;
        result[1] = idx%W;
        return result;
    }

    private static int[][][] graphInitialize() {
        int[][][] graph = new int[H*W][4][3]; //시작점 1d, 도착점 1d, 현재 흐름량, 캐파
        for(int i=0;i<H;i++){
            for(int j=0;j<W;j++){
                int idx = convertTo1D(i,j);
                int count = 0;
                for(int k=0;k<4;k++){
                    int ni = i+dy[k];
                    int nj = j+dx[k];
                    int nIdx = convertTo1D(ni,nj);

                    graph[idx][count][0] = nIdx; //도착점
                    graph[idx][count][1] = 0; //현재 흐름량
                    graph[idx][count++][2] = canGo(ni,nj) ? 1 : 0;; //최대 흐름량
                }
            }
        }

        return graph;
    }

    private static char[][] mapInitialize(int H, int W) throws IOException {
        InputStream ip1 = System.in;
        InputStream ip2 = new FileInputStream("input.txt");

        BufferedReader br = new BufferedReader(new InputStreamReader(ip2));
        StringTokenizer st = new StringTokenizer(br.readLine()," ");
        H = Integer.parseInt(st.nextToken());
        W = Integer.parseInt(st.nextToken());
        char[][] map = new char[H][W];
        visited = new boolean[H][W];

        s = new int[2];
        e = new int[2];

        for(int i = 0; i< H; i++){
            map[i] = br.readLine().toCharArray();
            for(int j = 0; j< W; j++){
                if(map[i][j] == 'K'){
                    s[0] = i;
                    s[1] = j;
                    continue;
                }

                if(map[i][j] == 'H'){
                    e[0] = i;
                    e[1] = j;
                    continue;
                }
            }
        }

        return map;
    }

    public static boolean canGo(int i, int j){
        if(i<0 || i>=H) return false;
        if(j<0 || j>=W) return false;

        if(visited[i][j]) return false;

        char c = map[i][j];
        if(c == '.') return true;
        if(c == 'H') return true;
        if(c == '#') return false;
        if(c == 'K') return false;
        return false;
    }
}
