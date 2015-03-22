package A_estrela;

import java.awt.Point;
import java.util.ArrayList;

public class Tree {
	Grid grid;
	State agent; //root
	State target;
	State [][]stateArray;
	public Tree(Grid grid, Point agentPos, Point targetPos){
		this.grid=grid;
		target = new State(targetPos.x, targetPos.y, -1, 0, -1, 1, null);
		target.isTarget=true;
		int agentH = calcH(new Point(agentPos.x,agentPos.y));
		agent = new State(agentPos.x, agentPos.y, 0, agentH, agentH, 0, null);
		agent.isAgent=true;
		stateArray = new State[grid.row][grid.col];
		createStates();
	}
	
	//Transform the grid into a two dimensional array of States objects
	public void createStates(){
		for(int i=0; i<grid.row; i++){
			for(int j =0; j<grid.col; j++){
				if(i==grid.posAgent.x && j==grid.posAgent.y){
					stateArray[i][j] = agent;
					System.out.println("agent position = "+i+"-"+j);
					}else{
						if(i==grid.posTarget.x && j==grid.posTarget.y){
							System.out.println("target position = "+i+"-"+j);
							stateArray[i][j] = target;
							}else{
								int h = calcH(new Point(i,j));
								stateArray[i][j] = new State(i,j,-1,h,-1,1,null);
								if(grid.grid[i][j].isFree)
									stateArray[i][j].isFree=true;
							}
					}
			}	
		}
		for(int i=0; i<grid.row; i++){
			for(int j =0; j<grid.col; j++){
				System.out.println(stateArray[i][j].h);
			}
		}
	}
	
	void buildTree(){
		for(int i=0; i<grid.row; i++){
			for(int j =0; j<grid.col; j++){
				
			}
		}		
	}
	
	int calcH(Point s){
		int h;
		h = Math.abs(s.x - target.posX) + Math.abs(s.y-target.posY);
		return h;
	}
}

class State{
	int g,h,f,costToParent;
	int posX,posY;
	State parent;
	boolean isAgent = false;
	boolean isTarget = false;
	boolean isFree = true;
	ArrayList<State> successors = new ArrayList<State>();
	
	public State(int posX, int posY, int g, int h, int f, int costToParent, State parent){
		this.posX=posX;
		this.posY=posY;
		this.g = g;
		this.h = h;
		this.f = f;
		this.costToParent = costToParent;
		this.parent=parent;
	}
	
	public void setH(int h){
		this.h=h;
	}
}
