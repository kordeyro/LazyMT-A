package A_estrela;
import java.awt.Point;

public class Main {
	static Point agentPos= new Point(1,3);
	static Point targetPos= new Point(4,1);
	public static void main(String[] args) {
        Grid grid = new Grid(5, 5, 3, agentPos, targetPos);
        Tree tree = new Tree(grid, agentPos, targetPos);
        tree.buildTree();
    }
}