import java.io.IOException;
import java.io.*;
import java.util.*;

public class Main {
	static int N,M,P,C,D,K,gameOverCount;
	static int rudolf_r, rudolf_c;
	static int[][] board;
	static Santa[] santas;
	static int[] dr = {1,1,1,0,0,-1,-1,-1};
	static int[] dc = {1,0,-1,1,-1,1,0,-1};
	
	//상우하좌
	static int[] dx = {1,0,-1,0};
	static int[] dy = {0,1,0,-1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        //N: 게임판의 크기 (3≤N≤50)
        N = Integer.parseInt(st.nextToken());
        
        //M: 게임 턴 수 (1≤M≤1000)
        M = Integer.parseInt(st.nextToken());

        //P: 산타의 수 (1≤P≤30)
        P = Integer.parseInt(st.nextToken());

        //C: 루돌프의 힘 (1≤C≤N)
        C = Integer.parseInt(st.nextToken());

        //D: 산타의 힘 (1≤D≤N)
        D = Integer.parseInt(st.nextToken());

        board = new int[N+1][N+1];
        santas = new Santa[P+1];
        
        st = new StringTokenizer(br.readLine());
        rudolf_r = Integer.parseInt(st.nextToken());
        rudolf_c = Integer.parseInt(st.nextToken());
        board[rudolf_r][rudolf_c] = -1;
        
        for(int i=0;i<P;i++) {
        	st = new StringTokenizer(br.readLine());
    		int idx = Integer.parseInt(st.nextToken());
    		int r = Integer.parseInt(st.nextToken());
    		int c = Integer.parseInt(st.nextToken());
    		santas[idx] = new Santa(r,c);
    		board[r][c] = idx;
        }
        //  만약 P 명의 산타가 모두 게임에서 탈락하게 된다면 그 즉시 게임이 종료됩니다.

        while(M > K ) {
//        	print();
        	moveRudolf();
//        	System.out.println("rudolf");
//        	print();
        
        	if(gameOverCount == P) break;
        	moveSantas();
//        	System.out.println("santa");
//        	print();

        	if(gameOverCount == P) break;

        	for(int i=1;i<=P;i++) {
                //  매 턴 이후 아직 탈락하지 않은 산타들에게는 1점씩을 추가로 부여합니다.
        		 if(!santas[i].isOver) {
        			 santas[i].score++;
        		 }
//         		System.out.print(santas[i].score+" ");
        	}
//        	System.out.println();
        	K++;
        }
       
        for(int i=1;i<=P;i++) {
    		System.out.print(santas[i].score+" ");
    	}
    }

    private static void print() {
    	for(int i=1;i<=N;i++) {
    		for(int j=1;j<=N;j++) {
    			System.out.print(board[i][j]+" ");
    		}
    		System.out.println();
    	}
	}

	//(3) 산타의 움직임
	 private static void moveSantas() {
		 for(int i=1;i<=P;i++) {
			 //존재하는 산타이고 기절하지 않아야함 
			 if(!santas[i].isOver && (santas[i].stun == 0 || K-santas[i].stun >= 2) ) {
				 double distance = getDistance(santas[i].r,santas[i].c);
				 
				 double minDistance = Integer.MAX_VALUE;
			     int minDir = -1;
			    	
				 for(int j = 0 ; j < dx.length;j++) {
					 int nx = santas[i].r + dx[j];
					 int ny = santas[i].c + dy[j];
					 
					 //움직일 수 있음 
					 if(isRange(nx,ny) && board[nx][ny] <= 0 ) {
						double nextDistance = getDistance(nx,ny);
				    	if(nextDistance < distance && nextDistance < minDistance) {
				    			minDistance = nextDistance;
				    			minDir = j;
						 }
					 }
				 }
				 if(minDir !=-1) {
//					 System.out.println(i+" "+minDir);
					 board[santas[i].r][santas[i].c] = 0;
					 santas[i].r += dx[minDir];
					 santas[i].c += dy[minDir];
					 //루돌프와 충돌 
					 if(board[santas[i].r][santas[i].c] == -1) {
//						 System.out.println("산타가 충돌 "+i);
						 santas[i].score += D;
						 santas[i].stun = K;
						 int reverseDir = minDir >= 2 ? minDir-2 : minDir+2;
						 
						 pushSanta(i,D,reverseDir,dx,dy);
					 }else {
						 board[santas[i].r][santas[i].c] = i;
					 }
				 }
			 }
		 }
	}

	private static double getDistance(int r,int c) {
			return Math.pow(rudolf_r-r,2)+Math.pow(rudolf_c-c,2);
	}
	
	private static double getDistance(int r,int c,int x,int y) {
		return Math.pow(x-r,2)+Math.pow(y-c,2);
}

	//(2) 루돌프의 움직임
    static void moveRudolf() {
    	double minDistance = Integer.MAX_VALUE;
    	int minIdx = 0;
    	
    	for(int i=1;i<=P;i++) {
    		if(!santas[i].isOver) {
	    		double distance = getDistance(santas[i].r,santas[i].c);
	    		if(distance < minDistance) {
	    			minDistance = distance;
	    			minIdx = i;
	    		}else if(distance == minDistance) {
	    			if(minIdx>0) {
	    				if(santas[minIdx].r < santas[i].r) {
	    					minIdx = i;
	    				}else if(santas[minIdx].r == santas[i].r) {
	    					if(santas[minIdx].c < santas[i].c) {
	    						minIdx = i;
	    					}
	    				}
	    			}else {
	    				minIdx = i;
	    			}
	    		}
    		}
    	}
//    	System.out.println(minIdx);
    	
    	minDistance = Integer.MAX_VALUE;
    	int minDir = -1;
    	
    	for(int i=0;i<dr.length;i++) {
    		int nx = rudolf_r+dr[i];
    		int ny = rudolf_c+dc[i];
    		
    		if(isRange(nx,ny)) {
    			double distance = getDistance(santas[minIdx].r,santas[minIdx].c,nx,ny);
	    		if(distance < minDistance) {
	    			minDistance = distance;
	    			minDir = i;
	    		}
    		}
    	}
//    	System.out.println("이동 방향 "+minDir);
		board[rudolf_r][rudolf_c]=0;    				
		rudolf_r += dr[minDir];
		rudolf_c += dc[minDir];
	    if(board[rudolf_r][rudolf_c]==minIdx) {
	        //루돌프가 움직여서 충돌 - 산타가 C만큼 점수 획득하고 산타는 루돌프가 이동해온 방향으로 C만큼 밀림
	    	santas[minIdx].score += C;
	    	santas[minIdx].stun = K;
	    	pushSanta(minIdx, C, minDir,dr,dc);	        
	    }
    	board[rudolf_r][rudolf_c] = -1;	//루돌프
    	return;
    }
    
    private static void pushSanta(int idx, int dis, int dir,int[] dr,int[] dc) {
    	Santa santa = santas[idx];
        santa.r += dis*dr[dir];
        santa.c += dis*dc[dir];
        // 게임판 안에서 이동 
        if(isRange(santa.r,santa.c)) {
        	int nextIdx = board[santa.r][santa.c];
        	board[santa.r][santa.c] = idx;
        	
        	//해당 칸에 산타가 있는 경우 다시 밀기 
        	if(nextIdx > 0) {
        		pushSanta(nextIdx,1,dir,dr,dc);
        	}
        }
        //탈락함 
        else {
        	santa.isOver = true;
        	gameOverCount++;
        }		
	}

	private static boolean isRange(int nr, int nc) {
		return nr >=1 && nr <= N && nc >=1 && nc <= N;
	}
	static class Santa{
    	int r;
    	int c;
    	int stun;
    	int score;
    	boolean isOver = false;
    	Santa(int r,int c){
    		this.r = r;
    		this.c = c;
    	}
    }
}