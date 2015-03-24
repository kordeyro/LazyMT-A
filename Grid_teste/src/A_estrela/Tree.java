package A_estrela;

import java.awt.List;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
//Here the value for "infinite" was defined as 100.
public class Tree {
	ArrayList<Point> PATH = new ArrayList<Point>();
	Grid grid;
	State agent; //root
	State target;
	State [][]stateArray;
	ArrayList<Integer> pathcost = new ArrayList<Integer>();
	public Tree(Grid grid, Point agentPos, Point targetPos){
		this.grid=grid;
		target = new State(targetPos.x, targetPos.y, 100, 0, 100, 1, null);
		target.isTarget=true;
		int agentH = calcH(new Point(agentPos.x,agentPos.y));
		agent = new State(agentPos.x, agentPos.y, 0, agentH, agentH, 0, null);
		agent.isAgent=true;
		stateArray = new State[grid.row][grid.col];
		pathcost.add(0);
		createStates();
	}
	
	//Move the target to a random available position
	public void movTarget(){
		Random rand = new Random();
		ArrayList <State> possibleStates = getSuccessors(target);
		int index = rand.nextInt(possibleStates.size());
		moveFromTo(stateArray[target.posX][target.posY], stateArray[possibleStates.get(index).posX][possibleStates.get(index).posY]);
		target.posX=possibleStates.get(index).posX;
		target.posY=possibleStates.get(index).posY;
	}
	
	//move the agent towards the target position, followiung the PATH defined in the Asearch method.
	public void movAgent(){
		//get always the latest element on the path list
		//System.out.println("agent position pre move: "+agent.posX+"-"+agent.posY);
		Point nextPos=PATH.get(PATH.size()-1);
		//System.out.println("moving to "+nextPos.x + "-"+nextPos.y+" "+PATH.size()+" more tiles to move!");
		moveFromTo(stateArray[agent.posX][agent.posY], stateArray[nextPos.x][nextPos.y]);
		agent.posX = nextPos.x;
		agent.posY = nextPos.y;
		//System.out.println("agent position after moving: "+agent.posX+"-"+agent.posY);
		PATH.remove(PATH.get(PATH.size()-1));
		printPATH();
		//System.out.println();
	}
	
	public void Asearch(ArrayList<State> OPEN,ArrayList<State> CLOSED){
		//initialization of the search
		//ArrayList<State> OPEN = new ArrayList<State>();
		//ArrayList<State> CLOSED = new ArrayList<State>();
		//OPEN.add(agent);
		PATH.clear();
		
		State current = getLowestF(OPEN);
		while(target.g > current.g){
			//System.out.print("States on the OPEN list: ");
			for(State s : OPEN){
				//System.out.print(s.posX+","+s.posY+" - ");
			}
			//System.out.println();
			//System.out.println("Now expanding state ("+current.posX+","+current.posY+")");
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
				System.out.println("There is no accessible path between Agent and target");
				return;
			}
			current = getLowestF(OPEN);
		}
		
		System.out.print("PATH: ");
		State aux = current;
		while(true){
			System.out.print("("+aux.posX+","+aux.posY+") - ");
			PATH.add(new Point(aux.posX,aux.posY));
			if(aux.parent==agent)
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
	
	int calcH(Point s){
		int h;
		h = Math.abs(s.x - target.posX) + Math.abs(s.y-target.posY);
		return h;
	}
	
	public void initState(State s, int counter,ArrayList<Integer> deltah){
		if(s.search != counter && s.search!=0){
			if(pathcost.size()!=0){
				if(s.g + s.h < pathcost.get(s.search))					
					s.h = pathcost.get(s.search)-s.g;
				s.h = s.h-(deltah.get(counter)-deltah.get(s.search));
				s.h = (s.h > calcH(new Point(s.posX,s.posY))) ? s.h : calcH(new Point(s.posX,s.posY));
				s.g=100;
			}
		}else{
			if(s.search==0)
				s.h=calcH(new Point(s.posX,s.posY));
		}
		s.search=counter;
	}
	
	public void moveFromTo(State s1, State s2){
		State aux = new State(0, 0, 0, 0, 0, 0, null);
		aux.copyData(s2);
		if(s2.isAgent)
			System.out.println("moving to AGENT position!");
		if(s2.isTarget)
			System.out.println("moving to TARGET position!");
		if(!(s1 == agent && s2 == target)){
		s2.isAgent=s1.isAgent;
		s2.isTarget=s1.isTarget;
		s1.isAgent=aux.isAgent;
		s1.isTarget=aux.isTarget;
			if(s1 == agent){
				agent = s2;
			}
			if(s1 == target)
				target = s2;
		}else{
			s1.isAgent=false;
			s2.isAgent=true;
		}
	}
	
	public int getNumRows(){
		return (stateArray[0].length);
	}
	
	public int getNumCols(){
		return (stateArray.length);
	}
	
	public void setSearch(int row, int col, int search){
		stateArray[row][col].search=search;
	}
	
	public void setG(int row, int col, int g){
		stateArray[row][col].g=g;
	}
	
	public void printPATH(){
		System.out.println("PATH: ");
		for(Point p : PATH){
			System.out.print("("+p.x+","+p.y+") - ");
		}
		System.out.println();
	}
	
	//print the grid to the console
	public void printGrid(){
		for(int i =0; i<stateArray[0].length;i++){
			for(int j=0;j<stateArray.length;j++){
				printCell(i,j);
			}
		}
	}
	
	//print each of the cell on the grid
	void printCell(int row, int col){
		if(col==stateArray.length-1){
			//last column, so should print the last division
			System.out.print("|");
			printStateInfo(row, col);
			System.out.println("|");
		}else{
			System.out.print("|");
			printStateInfo(row, col);
		}
	}
	
	//print the information regarding the cell
	void printStateInfo(int row, int col){
		if(stateArray[row][col].isAgent == true)
			System.out.print(" A ");
		if(stateArray[row][col].isTarget == true)
			System.out.print(" T ");
		if(stateArray[row][col].isFree == false)
			System.out.print(" X ");
		if(stateArray[row][col].isFree == true && stateArray[row][col].isAgent == false && stateArray[row][col].isTarget==false)
			System.out.print(" - ");
	}
}

class State{
	int g,h,f,costToParent;
	int posX,posY;
	State parent;
	int search;
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
	
	public Point getPos(){
		return new Point(posX,posY);
	}
	
	public void copyData(State s){
		//should copy the 'search', 'costToParent' variable?
		this.posX = s.posX;
		this.posY = s.posY;
		this.costToParent=s.costToParent;
		this.search=s.search;
		this.isFree=s.isFree;
		this.g = s.g;
		this.h = s.h;
		this.f = s.f;
		this.parent = s.parent;
		this.isAgent = s.isAgent;
		this.isTarget = s.isTarget;
	}

}
