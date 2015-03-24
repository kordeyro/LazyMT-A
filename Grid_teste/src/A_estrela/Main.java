package A_estrela;
import java.awt.Point;
import java.util.ArrayList;

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
		Point prevAgentPos = tree.agent.getPos();
		int movCounter = 2;
		/*--------------MAIN-------------*/
		int counter = 0;
		deltah.add(0, 0);
		deltah.add(0, 0);//deltah(1):=0
		//for every cell s E S
		for(int i = 0; i<tree.getNumRows(); i++){
			for(int j = 0; j<tree.getNumCols(); j++){
				tree.setSearch(i,j,0);
				tree.setG(i,j,100);
			}
		}
		//while Sstart != Starget
		while(!(tree.agent.posX == tree.target.posX && tree.agent.posY == tree.target.posY)){
			counter++;
			tree.initState(tree.agent, counter, deltah);
			tree.initState(tree.target, counter, deltah);
			tree.agent.g=0;
			OPEN.clear();//OPEN := 0
			OPEN.add(tree.agent);
			tree.agent.f=tree.agent.g+tree.agent.h;
			System.out.println("New search path found!!!!");
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
			while((tree.agent.posX != tree.target.posX || tree.agent.posY != tree.target.posY) && prevTargetPos.equals(tree.target.getPos())){
				tree.movAgent();
				tree.printGrid();
				//System.out.println("("+tree.agent.posX+","+tree.agent.posY+")");
				//System.out.println("("+tree.target.posX+","+tree.target.posY+")");
				if(tree.agent.posX == tree.target.posX && tree.agent.posY == tree.target.posY){
					break;
				}
				if(movCounter%3==0){
					tree.movTarget();
				}
				/*try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.out.println("error in Thread.sleep(2000)");
					e.printStackTrace();
				}*/
				movCounter++;
			}
			if(prevAgentPos != tree.agent.getPos()){
				//set Sstart to the current cell of the agent -> not necessary because the position of the agent is already up to date
			}
			if(prevTargetPos != tree.target.getPos()){
				System.out.println(" TARGET MOVED! ");
				//set sNewTarget to the current cell of thet arget -> also not necessary for the same reason
			}
			//update the increased action costs (if any) -> not implemented
			if(!prevTargetPos.equals(tree.target.getPos())){
				tree.initState(tree.target, counter,deltah);
				if(tree.target.g + tree.target.h < tree.pathcost.get(counter))
					tree.target.h = tree.pathcost.get(counter) - tree.target.g;
				deltah.add(counter+1,deltah.get(counter)+tree.target.h);
			}
		}
		System.out.println("END!!!!!!!!!!!!!!!!!!");
		System.out.println("agent position at the end: "+tree.agent.posX+"-"+tree.agent.posY);
		System.out.println("target position at the end: "+tree.target.posX+"-"+tree.target.posY);
	}
}