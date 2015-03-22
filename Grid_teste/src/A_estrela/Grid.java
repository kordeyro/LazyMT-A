package A_estrela;

import java.awt.Point;
import java.util.Random;

public class Grid{
	int row, col;
	Point posAgent, posTarget;
	Cell [][]grid;
	
	Grid(int row, int col, int blocking, Point posA, Point posT){
		this.row=row;
		this.col=col;
		posAgent = posA;
		posTarget = posT;
		grid = new Cell[row][col];
		
		init();
		placeAgent(posA);
		placeTarget(posT);
		placeBlockouts(blocking, row, col);
		printGrid();
	}
	
	//initialize grid and all cell values
	void init(){
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				//assign cells to the grid, and create them with their initial values
				grid[i][j]= new Cell();
			}
		}
		System.out.println("Grid is ready");
		System.out.println("Has size " + row +" x "+ col);
	}
	
	//update the information on the cell that it is holding the agent
	void placeAgent(Point posA){
		grid[posA.x][posA.y].hasAgent = true;
	}
	
	//update the information on the cell that it is holding the target
	void placeTarget(Point posT){
		grid[posT.x][posT.y].hasTarget = true;
	}
	
	//place the set number of blockages in random positions on the grid
	void placeBlockouts(int blocking, int row, int col){
		Random rand = new Random();
		for(int i =0; i< blocking; i++){
			int rowB = rand.nextInt(row);
			int colB = rand.nextInt(col);
			if(grid[rowB][colB].hasTarget==false && grid[rowB][colB].hasAgent==false && grid[rowB][colB].isFree==true)
				grid[rowB][colB].isFree = false;
			else
				i--;
		}
	}
	
	//print the grid to the console
	void printGrid(){
		boolean lastCol = false;
		for(int i =0; i<row;i++){
			for(int j=0;j<col;j++){
				printCell(i, j);
			}
		}
	}
	
	//print each of the cell on the grid
	void printCell(int row, int col){
		if(col==this.col-1){
			//last column, so should print the last division
			System.out.print("|");
			printCellInfo(row, col);
			System.out.println("|");
		}else{
			System.out.print("|");
			printCellInfo(row, col);
		}
	}
	
	//print the information regarding the cell
	void printCellInfo(int row, int col){
		if(grid[row][col].hasAgent == true)
			System.out.print(" A ");
		if(grid[row][col].hasTarget == true)
			System.out.print(" T ");
		if(grid[row][col].isFree == false)
			System.out.print(" X ");
		if(grid[row][col].isFree == true && grid[row][col].hasAgent == false && grid[row][col].hasTarget==false)
			System.out.print(" - ");
	}
}

//This class represents the cell of the grid. It hold the information that will be passed to the grafo 
class Cell{
	//represents the values g(s), h(s), f(s) and 'parent'
	int g, h, f;
	Cell parent;
	boolean hasAgent = false;
	boolean hasTarget = false;
	boolean isFree = true;
	
	//default constructor
	public Cell(){
		g = h = f = -1;
		parent = null;
	}
	
	//constructor with values specified
	public Cell(int g, int h, int f, Cell parent){
		this.g = g;
		this.h = h;
		this.f = f;
		this.parent=parent;
	}
	
	void setValues(int g, int h, int f, Cell parent){
		//trocar setValues por serValue e escolher um valor que precisa ser alterado frequentemente
		
		/*this.g = g;
		this.h = h;
		this.f = f;
		this.parent=parent;*/
	}
}