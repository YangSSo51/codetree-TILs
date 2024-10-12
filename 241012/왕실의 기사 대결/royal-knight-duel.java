import java.util.*;
import java.io.*;

public class Main { 
	static int L,N,Q;
	static int[][] board;
	static int[][] knightBoard;
	//위쪽, 오른쪽, 아래쪽, 왼쪽
	static int[] dx = {-1,0,1,0};
	static int[] dy = {0,1,0,-1};
	
//	static ArrayList<Knight> knightList = new ArrayList<>();
	static Knight[] knightArr;
	static int totalDamage;
	
    public static void main(String[] args) throws IOException{
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	StringTokenizer st = new StringTokenizer(br.readLine());
    	
    	L = Integer.parseInt(st.nextToken());
    	N = Integer.parseInt(st.nextToken());
    	Q = Integer.parseInt(st.nextToken());
    	board = new int[L+1][L+1];
    	knightBoard = new int[L+1][L+1];
    	knightArr = new Knight[N+1];
    	
    	for (int i = 1; i <= L; i++) {
    		st = new StringTokenizer(br.readLine());
			for (int j = 1; j <= L; j++) {
				board[i][j] = Integer.parseInt(st.nextToken());
			}
		}
    	
    	for (int i = 1; i <= N; i++) {
			//r,c,h,w,k
    		st = new StringTokenizer(br.readLine());
    		int r = Integer.parseInt(st.nextToken());
    		int c = Integer.parseInt(st.nextToken());
    		int h = Integer.parseInt(st.nextToken());
    		int w = Integer.parseInt(st.nextToken());
    		int k = Integer.parseInt(st.nextToken());
    		    		
    		knightArr[i] = new Knight(r,c,h,w,k);
    		
    		int damageCount = 0;
    		for (int j = r; j < r+h; j++) {
				for (int k1 = c; k1 < c+w; k1++) {
					knightBoard[j][k1] = i;
					if(board[j][k1] == 1) damageCount++;
				}
			}
    		knightArr[i].damage = damageCount;
		}
//    	print();
    	
    	for (int i = 0; i < Q; i++) {
    		st = new StringTokenizer(br.readLine());
    		int num = Integer.parseInt(st.nextToken());
    		int dir = Integer.parseInt(st.nextToken());
			moveKnight(num, dir);
		}    	
    	
    	for (int i = 1; i <= N; i++) {
			if(knightArr[i].k > 0) totalDamage+= knightArr[i].totalDamage;
		}
    	System.out.println(totalDamage);
    }
    private static void moveKnight(int num, int dir) {
    	// 또, 체스판에서 사라진 기사에게 명령을 내리면 아무런 반응이 없게 됩니다.		
    	if(knightArr[num].k < 0) return;
    	Queue<Integer> Q = new LinkedList<>();
    	Deque<Integer> movedKnight = new ArrayDeque<>();
    	Q.add(num);
    	movedKnight.add(num);
    	boolean[] visited = new boolean[N+1];
    	
    	while(!Q.isEmpty()) {
    		int now = Q.poll();
    		Knight knight = knightArr[now];
    		
    		//위쪽, 오른쪽, 아래쪽, 왼쪽
    		if(dir == 0 || dir == 2) {
	    		for (int i = knight.c; i < knight.c + knight.w; i++) {
					int nx = knight.r + (dir == 2 ? knight.h : -1);
					int ny = i;
					
					// 이때 만약 이동하려는 위치에 다른 기사가 있다면 그 기사도 함께 연쇄적으로 한 칸 밀려나게 됩니다. 
					// 그 옆에 또 기사가 있다면 연쇄적으로 한 칸씩 밀리게 됩니다. 
					// 하지만 만약 기사가 이동하려는 방향의 끝에 벽이 있다면 모든 기사는 이동할 수 없게 됩니다.
					if(!isRange(nx,ny) || board[nx][ny] == 2) {
						return;
					}
					
					if(knightBoard[nx][ny] !=0 && !visited[knightBoard[nx][ny]]) {
//						System.out.println("다음 기사 "+knightBoard[nx][ny]);
						visited[knightBoard[nx][ny]] = true;
						Q.add(knightBoard[nx][ny]);
						movedKnight.add(knightBoard[nx][ny]);
					}	
				}
    		}else {
    			for (int i = knight.r; i < knight.r + knight.h; i++) {
    				int nx = i;
					int ny = knight.c + (dir == 1 ? knight.w : -1);
					
					if(!isRange(nx,ny) || board[nx][ny] == 2) {
						return;
					}
					if(knightBoard[nx][ny] !=0 && !visited[knightBoard[nx][ny]]) {
//						System.out.println("다음 기사 "+knightBoard[nx][ny]);

						visited[knightBoard[nx][ny]] = true;
						Q.add(knightBoard[nx][ny]);
						movedKnight.add(knightBoard[nx][ny]);
					}	
				}
    		}
    	}
//    	System.out.println(movedKnight.size());
    	
    	
    	//밀려난 기사만 데미지를 입음. 
    	// 명령을 받은 기사가 다른 기사를 밀치게 되면, 밀려난 기사들은 피해를 입게 됩니다. 
    	int firstKnight = movedKnight.peekFirst();
    	
    	while(!movedKnight.isEmpty()) {
    		// 이때 각 기사들은 해당 기사가 이동한 곳에서 w×h 직사각형 내에 놓여 있는 함정의 수만큼만 피해를 입게 됩니다. 
    		// 각 기사마다 피해를 받은 만큼 체력이 깎이게 되며, 현재 체력 이상의 대미지를 받을 경우 기사는 체스판에서 사라지게 됩니다. 
    		// 단, 명령을 받은 기사는 피해를 입지 않으며, 기사들은 모두 밀린 이후에 대미지를 입게 됩니다. 
    		int now = movedKnight.pollLast();
    		Knight knight = knightArr[now];
    		
    		int nx = 0, ny = 0;
    		if(dir == 0 || dir == 2) {
	    		for (int i = knight.c; i < knight.c + knight.w; i++) {
					nx = knight.r + (dir == 2 ? knight.h : -1);
					ny = i;
					
					//이동할 위치 채움 
					knightBoard[nx][ny] = now;
					if(board[nx][ny] == 1) {
						knight.k--;
						knight.damage++;
					}
					
					int px = knight.r + (dir == 2 ? 0 : knight.h-1);
					int py = i;
					
					//이전 위치 비움 
					knightBoard[px][py] = 0;
					if(board[px][py] == 1) knight.damage--;
	    		}
    		}else {
    			for (int i = knight.r; i < knight.r + knight.h; i++) {
    				nx = i;
					ny = knight.c + (dir == 1 ? knight.w : -1);

					knightBoard[nx][ny] = now;
					if(board[nx][ny] == 1) {
						knight.k--;
						knight.damage++;
					}
					
					int px = i;
					int py = knight.c + (dir == 1 ? 0 : knight.w-1);
										

					//이전 위치 비움 
					knightBoard[px][py] = 0;
					if(board[px][py] == 1) knight.damage--;
					
    			}
    		}
    		knight.r += dx[dir];
    		knight.c += dy[dir];
    		
    		if(now == firstKnight) {
    			knight.k += knight.damage;
    		}else {
        		knight.totalDamage += knight.damage;
    		}
    		
    		if(knight.k <= 0) {
        		removeKnight(knight);
    		}
    	}
//    	print();
	}
	private static void removeKnight(Knight k) {
		for (int i = k.r; i < k.r+k.h; i++) {
			for (int j = k.c; j < k.c + k.w; j++) {
				knightBoard[i][j] = 0;
			}
		}
				
	}
	private static boolean isRange(int nx, int ny) {
		return nx >= 1 && nx <= L && ny >=1 && ny <= L;
	}
	private static void print() {
    	for (int i = 1; i <= L; i++) {
			for (int j = 1; j <= L; j++) {
				System.out.print(knightBoard[i][j] +" ");
			}
			System.out.println();
		}
	}
	static class Knight{
    	int r;
    	int c;
    	int h;
    	int w;
    	int k;
    	int damage;
    	int totalDamage;
    	
    	Knight(int r,int c, int h, int w, int k){
    		this.r = r;
    		this.c = c;
    		this.h = h;
    		this.w = w;
    		this.k = k;
    	}
    }
}