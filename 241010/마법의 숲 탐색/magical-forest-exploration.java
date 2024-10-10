import java.util.*;
import java.io.*;

public class Main {
    static int R,C;
    static int[][] board;
    //북, 동, 남, 서
    static int[] dx = {-1,0,1,0};
    static int[] dy = {0,1,0,-1};
    
    //아래 방향 
    static int[] downX = {1,2,1};
    static int[] downY = {-1,0,1};
    
    //왼쪽 방향
    static int[] leftX = {-1,0,1};
    static int[] leftY = {-1,-2,-1};
    
    //오른쪽 방향
    static int[] rightX = {-1,0,1};
    static int[] rightY = {1,2,1};
    
    static int answer;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());
        board = new int[R+1][C+1];

        for(int i = 0; i < K ; i++){
            st = new StringTokenizer(br.readLine());
            int column = Integer.parseInt(st.nextToken());
            int direction = Integer.parseInt(st.nextToken());

            moveGolem(column, direction, i+1);
//            print();
        }
        
        System.out.println(answer);
    }
    private static void print() {
    	for (int i = 1; i <= R; i++) {
			for (int j = 1; j <= C; j++) {
				System.out.print(board[i][j]+" ");
			}
			System.out.println();
		}
	}
	//골렘 이동 
    static void moveGolem(int column, int dir, int num){
    	int x = -1;
    	int y = column;
    	int[] next;
    	
    	while(true) {
	    	next = moveDown(x,y);
	    	
	    	next = moveLeft(next[0],next[1],dir);
	    	
	    	next = moveRight(next[0],next[1],next[2]);
	    	
	    	if(next[0] != x || next[1] !=y) {
	    		x = next[0];
	    		y = next[1];
	    		dir = next[2];
	    	}else {
	    		break;
	    	}
    	}
    	//골렘 표시 
    	for (int i = 0; i < dx.length; i++) {
			int nx = x + dx[i];
			int ny = y + dy[i];
			
			if(nx >=1 && nx <= R && ny >=1 && ny <= C) {
				board[nx][ny] = num;
			}else {
				board = new int[R+1][C+1];	//초기화
				return;
			}
		}

    	board[x][y] = num;
    	//출구 표시 
    	board[x + dx[dir]][y + dy[dir]] = -num;
    	
    	moveAngel(x,y);
    	
    }
	private static int[] moveRight(int x, int y, int dir) {
		boolean moveRight = true;
	    
	    while(moveRight) {
			//오른쪽으로 이동 가능한지 확인 
			for (int i = 0; i < rightX.length; i++) {
				int nx = x + rightX[i];
				int ny = y + rightY[i];
				
				if((isRange(nx,ny) && board[nx][ny] == 0) || nx < 0) continue;
				else {
					moveRight = false;
					break;
				}
			}

			//오른쪽으로 이동 가능 
	    	if(moveRight) {
		    	int[] next = moveDown(x,y+1);
		    	//아래로 이동 가능함 
		    	if(next[0] != x){
		    		x = next[0];
		    		y = y+1;
		    		dir++;
		    		if(dir > 3) dir = 0;
		    	}else {
		    		moveRight = false;
		    	}
	    	}
	    }
    	//이동 불가능함 
    	return new int[] {x,y,dir};
	}

	private static int[] moveLeft(int x, int y,int dir) {
		boolean moveLeft = true;
	    
	    while(moveLeft) {
			//왼쪽으로 이동 가능한지 확인 
			for (int i = 0; i < leftX.length; i++) {
				int nx = x + leftX[i];
				int ny = y + leftY[i];
				
				if((isRange(nx,ny) && board[nx][ny] == 0) || nx < 0) continue;
				else {
					moveLeft = false;
					break;
				}
			}

			//왼쪽으로 이동 가능 
	    	if(moveLeft) {
		    	int[] next = moveDown(x,y-1);
		    	//아래로 이동 가능함 
		    	if(next[0] != x){
		    		x = next[0];
		    		y = y-1;
		    		dir--;
		    		if(dir < 0) dir = 3;
		    	}else {
		    		moveLeft = false;
		    	}
	    	}
	    }
    	//이동 불가능함 
    	return new int[] {x,y,dir};
	}
	
	private static int[] moveDown(int x, int y) {
		boolean moveDown = true;
		
    	while(moveDown) {
	        //한 칸 내려갑니다.
	    	for (int i = 0; i < downX.length; i++) {
				int nx = x + downX[i];
				int ny = y + downY[i];
				
				// 비어있음 
				if(nx <= R && board[nx][ny] == 0) continue;
				else {
					moveDown = false;
					break;
				}
			}
	    	if(moveDown) x++;
    	}		
    	return new int[] {x,y};
	}
	
	private static boolean isRange(int nx, int ny) {
		return nx >=0 && nx <= R && ny >=1 && ny <= C;
	}
	
	private static void moveAngel(int x, int y) {
		Queue<int[]> Q = new LinkedList<>();
		boolean[][] visited = new boolean[R+1][C+1];
		visited[x][y] = true;
		Q.add(new int[] {x,y});
		int maxRow = 0;
		
		while(!Q.isEmpty()) {
			int[] now = Q.poll();
			
			for (int i = 0; i < dx.length; i++) {
				int nx = now[0] + dx[i];
				int ny = now[1] + dy[i];
				
				//범위에 속하고 골렘으로 이동이 가능한 경우 
				//같은 골렘이거나 현재가 출구,다음이 다른 골렘인 경우, 다음이 출구인 경우 
				if(!isRange(nx, ny )) continue;
				if(!visited[nx][ny] && (board[nx][ny] == board[now[0]][now[1]] || (board[now[0]][now[1]] == -board[nx][ny]) || 
					board[now[0]][now[1]] < 0 && board[nx][ny] !=0)) {
					visited[nx][ny] = true;
					Q.add(new int[] {nx,ny});
					maxRow = Math.max(maxRow, nx);
				}
			}
		}
//		System.out.println(maxRow);
		answer += maxRow;
	}
}