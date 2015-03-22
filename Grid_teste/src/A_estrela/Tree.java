package A_estrela;

import java.awt.List;
import java.awt.Point;
import java.util.ArrayList;
//Here the value for "infinite" was defined as 100.
public class Tree {
	Grid grid;
	State agent; //root
	State target;
	State [][]stateArray;
	public Tree(Grid grid, Point agentPos, Point targetPos){
		this.grid=grid;
		target = new State(targetPos.x, targetPos.y, 100, 0, 100, 1, null);
		target.isTarget=true;
		int agentH = calcH(new Point(agentPos.x,agentPos.y));
		agent = new State(agentPos.x, agentPos.y, 0, agentH, agentH, 0, null);
		agent.isAgent=true;
		stateArray = new State[grid.row][grid.col];
		createStates();
		//buildTree(); is it necessary?
	}
	
	public void Asearch(){
		//initialization of the search
		ArrayList<State> OPEN = new ArrayList<State>();
		ArrayList<State> CLOSED = new ArrayList<State>();
		OPEN.add(agent);
		
		State current = getLowestF(OPEN);
		while(target.g > current.g){
			System.out.print("States on the OPEN list: ");
			for(State s : OPEN){
				System.out.print(s.posX+","+s.posY+" - ");
			}
			System.out.println();
			System.out.println("Now expanding state ("+current.posX+","+current.posY+")");
			OPEN.remove(current);
			OPEN.trimToSize(); //resize the ArrayList
			CLOSED.add(current);
			ArrayList<State> successors = getSuccessors(current);
			for(State s: successors){				
				if((s.g) > (current.g + s.costToParent)){
					//System.out.println("New shortest path found (cost = "+(current.g+s.costToParent)+") for state ("+s.posX+","+s.posY+")");
					s.g=current.g + s.costToParent;
					s.parent=current;
					if(OPEN.contains(s)){
						OPEN.remove(s);
					}
					s.f=s.g+s.h;
					//System.out.println("g: "+s.g+" h: "+s.h);
					//System.out.println("new F = "+s.f);
					OPEN.add(s);
				}
			}
			//update the minimun value of F for the members on the OPEN queue.
			if(OPEN.isEmpty()){
				System.out.println("There is no accessible between Agent and target");
				return;
			}
			current = getLowestF(OPEN);
		}
		
		System.out.print("PATH: ");
		State aux = current;
		while(true){
			System.out.print("("+aux.posX+","+aux.posY+") - ");
			if(aux.parent==null)
				break;
			aux=aux.parent;
		}
		System.out.println();
		System.out.println("COST: " + current.g);
	}	
	
	public ArrayList<State> getSuccessors(State current){
		int currX = current.posX;
		int currY = current.posY;
		ArrayList<State> successors = new ArrayList<State>();
		
		if(currX > 0){
			if(stateArray[current.posX-1][current.posY].isFree)
				successors.add(stateArray[current.posX-1][current.posY]);
			if(currX<grid.row-1){
				if(stateArray[current.posX+1][current.posY].isFree)
					successors.add(stateArray[current.posX+1][current.posY]);
			}
		}else{
			if(currX<grid.row-1){
				if(stateArray[current.posX+1][current.posY].isFree)
					successors.add(stateArray[current.posX+1][current.posY]);
			}
		}
		if(currY > 0){
			if(stateArray[current.posX][current.posY-1].isFree)
				successors.add(stateArray[current.posX][current.posY-1]);
			if(currY<grid.col-1){
				if(stateArray[current.posX][current.posY+1].isFree)
					successors.add(stateArray[current.posX][current.posY+1]);
			}
		}else{
			if(currY<grid.col-1){
				if(stateArray[current.posX][current.posY+1].isFree)
					successors.add(stateArray[current.posX][current.posY+1]);
			}
		}
		
		return successors;
	}
	
	public State getLowestF(ArrayList<State> OPEN){
		int lowestF=100;
		int index=0;
		State chosen;
		for(State state : OPEN){
			if(state.f<lowestF){
				lowestF = state.f;
				index = OPEN.indexOf(state);
			}
			//System.out.println("lowestF found so far: "+lowestF +" is of state ("+OPEN.get(index).posX +","+OPEN.get(index).posY+")");
		}
		chosen = OPEN.get(index);
		return chosen;
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
								stateArray[i][j] = new State(i,j,100,h,100+h,1,null);
								if(grid.grid[i][j].isFree){
									stateArray[i][j].isFree=true;
								}else{
									stateArray[i][j].isFree=false;
								}
							}
					}
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
