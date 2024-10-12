import java.util.*;
import java.io.*;

//1:45
public class Main { 
	static int[][] board;
	static Queue<Integer> newArtifact;
	static int maxCount;
	static int[][] updateBoard;
	static int[] dx = {-1,1,0,0};
	static int[] dy = {0,0,-1,1};
	
    public static void main(String[] args) throws IOException{
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	StringTokenizer st = new StringTokenizer(br.readLine());
    	
    	int K = Integer.parseInt(st.nextToken());
    	int M = Integer.parseInt(st.nextToken());
    	
    	board = new int[6][6];
    	newArtifact = new LinkedList<>();
    	
    	for (int i = 1; i <= 5; i++) {
    		st = new StringTokenizer(br.readLine());
			for (int j = 1; j <= 5 ; j++) {
				board[i][j]	= Integer.parseInt(st.nextToken());
			}
		}
    	
    	st = new StringTokenizer(br.readLine());
    	for (int i = 0; i < M; i++) {
			newArtifact.add(Integer.parseInt(st.nextToken()));
		}
    	
    	for (int i = 0; i < K; i++) {
    		maxCount = 0;
    		findCenter();
    		if(maxCount > 0) System.out.print(maxCount+" ");
		}
    }

	private static void findCenter() {
		for (int i = 2; i <= 4; i++) {
			for (int j = 2; j <= 4; j++) {
				for (int k = 0; k < 3; k++) {
					int[][] rotate = rotation(j,i,1);
					int count = findArtifact(rotate);

					if(maxCount < count) {
						maxCount = count;
						updateBoard = rotate;
					}	
				}
			}
		}
		
//		print(updateBoard);
		while(true) {
			//유물 채워넣기, 연쇄 획득 
			//(1) 열 번호가 작은 순으로 조각이 생겨납니다. 만약 열 번호가 같다면 (2) 행 번호가 큰 순으로 조각이 생겨납니다. 
			for (int i = 1; i <= 5; i++) {
				for (int j = 5; j >= 1; j--) {
					if(updateBoard[j][i] == 0) {
						updateBoard[j][i] = newArtifact.poll();
					}
				}
			}
			int count = findArtifact(updateBoard);
			maxCount+= count;
			if(count == 0) break;
		}
		board = updateBoard;
	}

	private static int findArtifact(int[][] rotate) {
		boolean[][] visited = new boolean[6][6];
		int count = 0;
		
		for (int i = 0; i < rotate.length; i++) {
			for (int j = 0; j < rotate[i].length; j++) {
				if(!visited[i][j]) {
					count += findSame(i,j,rotate,visited);
				}
			}
		}
		return count;
	}

	private static int findSame(int x, int y, int[][] rotate, boolean[][] visited) {
		Queue<int[]> Q = new LinkedList<>();
		Q.offer(new int[] {x,y});
		visited[x][y] = true;
		int num = rotate[x][y];
		int count = 1;
		
		Queue<int[]> deleteQ = new LinkedList<>();
		deleteQ.offer(new int[] {x,y});
		
		while(!Q.isEmpty()) {
			int[] now = Q.poll();
			
			for (int i = 0; i < dx.length; i++) {
				int nx = now[0] + dx[i];
				int ny = now[1] + dy[i];
				
				if(nx >= 1 && nx <= 5 && ny >= 1 && ny <= 5 && !visited[nx][ny] && num == rotate[nx][ny]) {
					visited[nx][ny] = true;
					Q.offer(new int[] {nx,ny});
					deleteQ.offer(new int[] {nx,ny});
					count++;
				}
			}
		}
		if(count >= 3 ) {
			while(!deleteQ.isEmpty()) {
				int[] now = deleteQ.poll();
				rotate[now[0]][now[1]] = 0;
			}
			
			return count;
		}
		return 0;
	}

	//x,y를 중심으로 회전 
	private static int[][] rotation(int x, int y,int count) {
		int[][] rotate = new int[6][6];
		
		for (int i = 1; i <= 5; i++) {
			rotate[i] = board[i].clone();
		}
		
		for (int i = 0; i < count; i++) {
			rotate[x-1][y-1] = board[x+1][y-1];
			rotate[x-1][y+0] = board[x][y-1];
			rotate[x-1][y+1] = board[x-1][y-1];

			rotate[x][y-1] = board[x+1][y];
			rotate[x][y+1] = board[x-1][y];

			rotate[x+1][y-1] = board[x+1][y+1];
			rotate[x+1][y+0] = board[x][y+1];
			rotate[x+1][y+1] = board[x-1][y+1];

		}
		
//		print(rotate);
		return rotate;
	}
	
	private static void print(int[][] temp) {
		for (int i = 1; i <= 5; i++) {
			for (int j = 1; j <= 5 ; j++) {
				System.out.print(temp[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}

	static class Center implements Comparable<Center>{
		int value;
		int rotationCount;
		int x;
		int y;
		Center(int value, int rotationCount,int x, int y){
			this.value = value;
			this.rotationCount = rotationCount;
			this.x = x;
			this.y = y;
		}
		@Override
		public int compareTo(Center o) {
			if(this.value == o.value) {
				if(this.rotationCount == o.rotationCount) {
					if(this.y == o.y) {
						return this.x - o.x;
					}
					return this.y - o.y;
				}
				return this.rotationCount - o.rotationCount;
			}
			return o.value - this.value;
		}
	}
}