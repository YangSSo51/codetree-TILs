import java.io.IOException;
import java.io.*;
import java.util.*;

public class Main {
    static int L,N,Q;
    static int[][] board;
    static int[][] knightBoard;
    static Knight[] knights;
    static int[] dx = {-1,0,1,0};
    static int[] dy = {0,1,0,-1};
    static Deque<Integer> canMoveKnights;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        L = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        board = new int[L+1][L+1];
        knightBoard = new int[L+1][L+1];
        knights = new Knight[N+1];
        int totalDamage = 0;
        
        for (int i = 1; i <= L; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= L; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        
        for (int i = 1; i <= N; i++) {
            st = new StringTokenizer(br.readLine());

            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            knights[i] = new Knight(i,r,c,h,w,k);
            
            for (int j = r; j < r+h; j++) {
                for (int l = c; l < c+w; l++) {
                    knightBoard[j][l] = i;
                }
            }
        }

        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());

            int idx = Integer.parseInt(st.nextToken());
            int direction = Integer.parseInt(st.nextToken());        

            if(knights[idx].damage < knights[idx].k && moveKnight(idx,direction)){
                damageKnight(idx,direction);
            }
        }
       
        for(int i=1;i<=N;i++) {
        	if(knights[i].damage < knights[i].k) {
        		totalDamage+=knights[i].damage;
        	}
        }
        System.out.println(totalDamage);
    }

    private static boolean moveKnight(int idx, int direction) {
        Queue<Integer> Q = new LinkedList<>();
        canMoveKnights = new ArrayDeque<>();
        boolean[] isChecked = new boolean[N+1];
        Q.offer(idx);
        canMoveKnights.offer(idx);
        isChecked[idx] = true;
        boolean result = true;

        while (!Q.isEmpty()){
            int size = Q.size();

            for (int i = 0; i < size; i++) {
                int now = Q.poll();
                int nx = knights[now].r+(dx[direction] > 0 ? knights[now].h : dx[direction]);
                int ny = knights[now].c+(dy[direction] > 0 ? knights[now].w : dy[direction]);
                int range = 0;
                //상,하일 때는 넓이로 비교
                if(direction % 2 == 0) range = knights[now].w;
                else range = knights[now].h;

                for (int j = 0; j < range; j++) {
                    //영역 벗어나면 벽
                    if (isRange(nx, ny)) {
                        if (board[nx][ny] == 2) {
                            return false;
                        }
                        int nextKnight = knightBoard[nx][ny];
                        
                        if (nextKnight > 0 && !isChecked[nextKnight] && knights[nextKnight].k > knights[nextKnight].damage) {
                            isChecked[nextKnight] = true;
                            Q.offer(nextKnight);
                            canMoveKnights.offer(nextKnight);
                        }
                    } else {
                        return false;
                    }
                    if (direction % 2 == 0)  ny ++;
                    else nx ++;
                }
            }
        }

        return result;
    }

    private static void damageKnight(int idx, int direction) {
        while (!canMoveKnights.isEmpty()){
            int now = canMoveKnights.pollLast();
            Knight nowKnight = knights[now];

            int nx = nowKnight.r+(dx[direction] > 0 ? nowKnight.h : dx[direction]);
            int ny = nowKnight.c+(dy[direction] > 0 ? nowKnight.w : dy[direction]);

            int px = nowKnight.r;
            if(direction==0) px += nowKnight.h-1;
            int py = nowKnight.c;
            if(direction==3) py += nowKnight.w-1;

            int range = 0;
            //상,하일 때는 넓이로 비교
            if(direction % 2 == 0) range = nowKnight.w;
            else range = nowKnight.h;

            for (int j = 0; j < range; j++) {

                knightBoard[nx][ny] = now;
                knightBoard[px][py] = 0;
                
                if (direction % 2 == 0) {
                	ny ++;
                	py ++;
                }
                else {
                	nx ++;
                	px ++;
                }
            }
            nowKnight.r += dx[direction];
            nowKnight.c += dy[direction];
            
            //피해 계산 
            for (int i =  nowKnight.r; i < nowKnight.r + nowKnight.h; i++) {
                for (int j = nowKnight.c; j < nowKnight.c+nowKnight.w ; j++) {
                    if(board[i][j] == 1){
                        if(idx!=now) {
                        	nowKnight.damage++;                           
                        }
                    }
                }
            }
            
            if(nowKnight.k <= nowKnight.damage) {
            	for (int i =  nowKnight.r; i < nowKnight.r + nowKnight.h; i++) {
                    for (int j = nowKnight.c; j < nowKnight.c+nowKnight.w ; j++) {
                        knightBoard[i][j]=0;
                    }
                }
            }
        }
    }
    
    static boolean isRange(int x,int y){
        return x>= 1 && x <= L && y >= 1 && y <=L;
    }
    static class Knight{
        int idx;
         int r;
         int c;
         int h;
         int w;
         int k;
         int damage;
         
         Knight(int idx,int r,int c,int h,int w,int k){
             this.idx = idx;
             this.r = r;
             this.c = c;
             this.h = h;
             this.w = w;
             this.k = k;
         }
    }
}