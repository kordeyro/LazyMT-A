package A_estrela;
import java.awt.Point;
import java.util.ArrayList;

import javax.sound.midi.Patch;

public class Main {
	static Point agentPos= new Point(1,4);
	static Point targetPos= new Point(4,1);
	public static void main(String[] args) {
        Grid grid = new Grid(6, 6, 12, agentPos, targetPos);
        Tree tree = new Tree(grid, agentPos, targetPos);
        run(tree);
    }
	
	public static void run(Tree tree){
		/*----Initializing variables----*/
		ArrayList<State> OPEN = new ArrayList<State>();
		ArrayList<State> CLOSED = new ArrayList<State>();
		ArrayList<Integer> deltah = new ArrayList<Integer>();
		Point prevTargetPos = tree.target.getPos();
		int movCounter = 0;
		/*--------------MAIN-------------*/
		int counter = 0;
		deltah.add(0, 0);
		//for every cell s E S
		for(int i = 0; i<tree.getNumRows(); i++){
			for(int j = 0; j<tree.getNumCols(); j++){
				tree.setSearch(i,j,0);
				tree.setG(i,j,100);
			}
		}
		//while Sstart != Starget
		while(tree.agent.posX != tree.target.posX && tree.agent.posY != tree.target.posY){
			counter++;
			tree.initState(tree.agent, counter);
			tree.initState(tree.target, counter);
			tree.agent.g=0;
			OPEN.clear();//OPEN := 0
			OPEN.add(tree.agent);
			tree.agent.f=tree.agent.g+tree.agent.h;
			tree.Asearch(OPEN,CLOSED);//ComputePath();
			if(OPEN.isEmpty()){//if OPEN = 0
				System.out.println("No solution found");
				break;
			}
			tree.pathcost.add(counter, tree.target.g);
			/*move the agent along the path identified by the tree-pointers until:
			 *  it reaches Starget;
			 *  the current cell of the target changes;
			 *  actions costs increase*/
			prevTargetPos = tree.target.getPos();
			while((tree.agent.posX != tree.target.posX || tree.agent.posY != tree.target.posY) || prevTargetPos == tree.target.getPos()){
				tree.movAgent();
				tree.printGrid();
				//System.out.println("("+tree.agent.posX+","+tree.agent.posY+")");
				//System.out.println("("+tree.target.posX+","+tree.target.posY+")");
				if(tree.agent.posX == tree.target.posX && tree.agent.posY == tree.target.posY){
					break;
				}
				//if(movCounter%5==0)
					//tree.movTarget();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.out.println("error in Thread.sleep(2000)");
					e.printStackTrace();
				}
			}
			System.out.println("END!!!!!!!!!!!!!!!!!!");
			for(int i = 0;i<tree.stateArray[0].length; i++){
				for(int j = 0;j<tree.stateArray.length; j++){
					if(tree.stateArray[i][j].isAgent)
						System.out.println("Agent on ("+i+"-"+j+")");
				}
			}
			/*System.out.println("agent position at the end: "+tree.agent.posX+"-"+tree.agent.posY);
			System.out.println("target position at the end: "+tree.target.posX+"-"+tree.target.posY);*/
		}
	}
}