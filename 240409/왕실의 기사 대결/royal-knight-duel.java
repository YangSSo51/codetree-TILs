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
    static int totalDamage;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        L = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        board = new int[L+1][L+1];
        knightBoard = new int[L+1][L+1];
        knights = new Knight[N+1];
//        canMoveKnights = new ArrayDeque<>();

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
//        for (int i = 1; i <= L; i++) {
//            for (int j = 1; j <= L; j++) {
//                System.out.print(knightBoard[i][j]+" ");
//            }
//            System.out.println();
//        }

        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());

            int idx = Integer.parseInt(st.nextToken());
            int direction = Integer.parseInt(st.nextToken());
            moveKnight(idx,direction);
//            System.out.println(moveKnight(idx,direction));
            if(moveKnight(idx,direction)){
                damageKnight(idx,direction);
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
//                System.out.println(now+" ");
                int nx = knights[now].r+(dx[direction] > 0 ? knights[now].h : dx[direction]);
                int ny = knights[now].c+(dy[direction] > 0 ? knights[now].w : dy[direction]);
                int range = 0;
                //상,하일 때는 넓이로 비교
                if(direction % 2 == 0) range = knights[now].w;
                else range = knights[now].h;

                for (int j = 0; j < range; j++) {
                    if (direction % 2 == 0)  ny += j;
                    else nx += j;

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
                }
            }
        }

        return result;
    }

    private static void damageKnight(int idx, int direction) {
        while (!canMoveKnights.isEmpty()){
            int now = canMoveKnights.pollLast();
            int nx = knights[now].r+(dx[direction] > 0 ? knights[now].h : dx[direction]);
            int ny = knights[now].c+(dy[direction] > 0 ? knights[now].w : dy[direction]);

            int px = knights[now].r;
            if(direction==0) px+=knights[now].h-1;
            int py = knights[now].c;
            if(direction==3) py += knights[now].w-1;

            int range = 0;
            //상,하일 때는 넓이로 비교
            if(direction % 2 == 0) range = knights[now].w;
            else range = knights[now].h;
//            System.out.println("range : "+range);
            for (int j = 0; j < range; j++) {
                if (direction % 2 == 0) {
                	ny += j;
                	py += j;
                }
                else {
                	nx += j;
                	px +=j;
                }
//                System.out.println("nx ny :"+nx+" "+ny);
//                System.out.println("px py :"+px+" "+py);

                knightBoard[nx][ny] = now;
                knightBoard[px][py] = 0;
            }
            knights[now].r += dx[direction];
            knights[now].c += dy[direction];
////            System.out.println(now);
//            for (int i = 1; i <= L; i++) {
//                for (int j = 1; j <= L; j++) {
//                    System.out.print(knightBoard[i][j]+" ");
//                }
//                System.out.println();
//            }
            //피해 더함
            for (int i = knights[now].r; i < knights[now].r + knights[now].h; i++) {
                for (int j = knights[now].c; j < knights[now].c+knights[now].w ; j++) {
                    if(board[i][j] == 1){
                        if(knights[now].damage < knights[now].k && idx!=now) {
                            knights[now].damage++;
                            totalDamage++;
                            if(knights[now].damage == knights[now].k){
                            	totalDamage-= knights[now].k;
                            }
                        }
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