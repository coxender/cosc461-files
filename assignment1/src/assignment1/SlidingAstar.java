package assignment1;

import java.util.LinkedList;
import java.lang.System;
 
//This program solves sliding puzzle using A* algorithm
public class SlidingAstar
{
    //Board class (inner class)
    private class Board
    {
        private int[][] array;                 //board array
        private int gvalue;                     //path cost
        private int hvalue;                     //heuristic value
        private int fvalue;                     //gvalue plus hvalue
        private Board parent;                   //parent board
    
        //Constructor of board class
        private Board(int[][] array, int size)
        {
            this.array = new int[size][size];  //create board array

            for (int i = 0; i < size; i++)      //copy given array
                for (int j = 0; j < size; j++)
                    this.array[i][j] = array[i][j];

            this.gvalue = 0;                    //path cost, heuristic value,
            this.hvalue = 0;                    //fvalue are all 0
            this.fvalue = 0;
            
            this.parent = null;                 //no parent
        }
    }

    private Board initial;                         //initial board
    private Board goal;                            //goal board
    private int size;                              //board size
    private int evalOpt;
    private int heurOpt;
    private int boardnum;
    private int swapCount;
    private long runTime;
    public String summary;                  //allows a user to pull a asummary of a run

    //Constructor of SlidingAstar class
    public SlidingAstar(int[][] initial, int[][] goal, int size, int evalOpt, int heurOpt)
    {
        this.size = size;                          //set size of board
        this.initial = new Board(initial, size);   //create initial board
        this.goal = new Board(goal, size);         //create goal board
        this.evalOpt = evalOpt;
        this.heurOpt = heurOpt;
        this.boardnum=0;
        this.swapCount=0;
        this.summary="";
    }

    //Method solves sliding puzzle
    public void solve()
    {
    	long nano = System.nanoTime();
    	
         LinkedList<Board> openList = new LinkedList<Board>();  //open list
         LinkedList<Board> closedList = new LinkedList<Board>();//closed list

         openList.addFirst(initial);   //add initial board to open list     

         while (!openList.isEmpty())   //while open list has more boards
         {
             int best = selectBest(openList);       //select best board
             Board board = openList.remove(best);   //remove board                                       
             closedList.addLast(board);             //add board to closed list

             if (goal(board))                       //if board is goal
             {       
                 long nano2 = System.nanoTime();
                 runTime=(nano2 - nano)/1000000;
                 displayPath(board);                //display path to goal
                 finalOutput();
                 return;                            //stop search
             }
             else                                   //if board is not goal
             {
                 LinkedList<Board> children = generate(board);//create children

                 for (int i = 0; i < children.size(); i++)
                 {                                     //for each child
                     Board child = children.get(i);    
                                                       
                     if (!exists(child, closedList))   //if child is not in closed list
                     {
                          if (!exists(child, openList))//if child is not in open list
                              openList.addLast(child); //add to open list
                          else                          
                          {                            //if child is already in open list
                              int index = find(child, openList);
                              if (child.fvalue < openList.get(index).fvalue)
                              {                            //if fvalue of new copy
                                  openList.remove(index);  //is less than old copy
                                  openList.addLast(child); //replace old copy
                              }                            //with new copy
                          }                               
                     }     
                 }                                  
             }                                       
         }
         long nano2 = System.nanoTime();
         runTime=(nano2 - nano)/1000000;
         finalOutput();
         System.out.println("no solution");            //no solution if there are
         
    }                                                  //no boards in open list
    private void finalOutput(){
        summary+="Solve time: " + runTime + " miliseconds\n";
        summary+="Total Board Created: "+boardnum+"\n";
        summary+="Total swaps: "+ swapCount+"\n";
    }
    //Method creates children of a board
    private LinkedList<Board> generate(Board board)
    {
        int i = 0, j = 0;
        boolean found = false;

        for (i = 0; i < size; i++)              //find location of empty slot
        {                                       //of board
            for (j = 0; j < size; j++)
                //if (board.array[i][j] == ' ') // this line is used for blank space to swap
                if (board.array[i][j] == 0)   // this line is used for zero to swap
                {   
                    found = true;
                    break;
                }
            
            if (found)
               break;
        }
        
        boolean north, south, east, west;       //decide whether empty slot
        north = i == 0 ? false : true;          //has N, S, E, W neighbors
        south = i == size-1 ? false : true;
        east = j == size-1 ? false : true; 
        west = j == 0 ? false : true;
        
        LinkedList<Board> children = new LinkedList<Board>();//list of children

        if (north) children.addLast(createChild(board, i, j, 'N')); //add N, S, E, W
        if (south) children.addLast(createChild(board, i, j, 'S')); //children if
        if (east) children.addLast(createChild(board, i, j, 'E'));  //they exist
        if (west) children.addLast(createChild(board, i, j, 'W'));  
                                                                    
        return children;                        //return children      
    }

    //Method creates a child of a board by swapping empty slot in a 
    //given direction
    //swap all zeros to a different character if you want to use that character instead.
    private Board createChild(Board board, int i, int j, char direction)
    {
        Board child = copy(board);                   //create copy of board
        this.boardnum+=1;
        if (direction == 'N')                        //swap empty slot to north
        {
            child.array[i][j] = child.array[i-1][j];
            child.array[i-1][j] = 0;				
        }
        else if (direction == 'S')                   //swap empty slot to south
        {
            child.array[i][j] = child.array[i+1][j];
            child.array[i+1][j] = 0;
        }
        else if (direction == 'E')                   //swap empty slot to east
        {
            child.array[i][j] = child.array[i][j+1];
            child.array[i][j+1] = 0;
        }
        else                                         //swap empty slot to west
        {
            child.array[i][j] = child.array[i][j-1];
            child.array[i][j-1] = 0;
        }

        child.gvalue = board.gvalue + 1;             //parent path cost plus one
        
        switch (heurOpt) {
        	case 1:
        		child.hvalue = heuristic(child);             //heuristic value of child
        		break;
        	case 2:
        		child.hvalue = heuristic2(child);
        		break;
        }
        //child.hvalue = heuristic(child);             //heuristic value of child

        switch (evalOpt) {
        case 1:
        	child.fvalue = child.hvalue; 
        	break;
        case 2:
        	child.fvalue = child.gvalue;
        	break;
        case 3:
        	child.fvalue = child.hvalue + child.gvalue;
        	break;
        }
        //child.fvalue = child.gvalue +  child.hvalue;  //gvalue plus hvalue
        
        child.parent = board;                        //assign parent to child

        return child;                                //return child
    }

    //Method computes heuristic value of board based on misplaced values
    private int heuristic(Board board)
    {
        int value = 0;                               //initial heuristic value

        for (int i = 0; i < size; i++)               //go thru board and
            for (int j = 0; j < size; j++)           //count misplaced values
                if (board.array[i][j] != goal.array[i][j])
                   value += 1;                       
  
        return value;                                //return heuristic value
    }

    //Method computes heuristic value of board
    //Heuristic value is the sum of distances of misplaced values
    private int heuristic2(Board board)
    {
        //initial heuristic value
        int value = 0;

        //go thru board
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                //if value mismatches in goal board    
                if (board.array[i][j] != goal.array[i][j]) 
                {                                
                    //locate value in goal board
                    int x = 0, y = 0;         
                    boolean found = false;
                    for (x = 0; x < size; x++)
                    {                       
                        for (y = 0; y < size; y++)
                            if (goal.array[x][y] == board.array[i][j])
                            {                 
                                found = true; 
                                break;
                            } 
                        if (found)
                           break;                        
                    }
            
                    //find city distance between two locations
                    value += (int)Math.abs(x-i) + (int)Math.abs(y-j);
                }
                     
        //return heuristic value               
        return value;
    }

    //Method locates the board with minimum fvalue in a list of boards
    private int selectBest(LinkedList<Board> list)
    {
        int minValue = list.get(0).fvalue;           //initialize minimum
        int minIndex = 0;                            //value and location

        for (int i = 0; i < list.size(); i++)
        {
            int value = list.get(i).fvalue;
            if (value < minValue)                    //updates minimums if
            {                                        //board with smaller
                minValue = value;                    //fvalue is found
                minIndex  = i;
            } 
        }

        return minIndex;                             //return minimum location
    }   

    //Method creates copy of a board
    private Board copy(Board board)
    {
         return new Board(board.array, size);
    }

    //Method decides whether a board is goal
    private boolean goal(Board board)
    {
        return identical(board, goal);           //compare board with goal
    }                                             

    //Method decides whether a board exists in a list
    private boolean exists(Board board, LinkedList<Board> list)
    {
        for (int i = 0; i < list.size(); i++)    //compare board with each
            if (identical(board, list.get(i)))   //element of list
               return true;

        return false;
    }

    //Method finds location of a board in a list
    private int find(Board board, LinkedList<Board> list)
    {
        for (int i = 0; i < list.size(); i++)    //compare board with each
            if (identical(board, list.get(i)))   //element of list
               return i;

        return -1;
    }
    
    //Method decides whether two boards are identical
    private boolean identical(Board p, Board q)
    {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (p.array[i][j] != q.array[i][j])
                    return false;      //if there is a mismatch then false

        return true;                   //otherwise true
    }

    //Method displays path from initial to current board
    private void displayPath(Board board)
    {
        LinkedList<Board> list = new LinkedList<Board>();

        Board pointer = board;         //start at current board
        summary+=("\n---Total Moves---\n");
        while (pointer != null)        //go back towards initial board
        {
            list.addFirst(pointer);    //add boards to beginning of list
            swapCount+=1;
            pointer = pointer.parent;  //keep going back
        }
                                       //print boards in list
        for (int i = 0; i < list.size(); i++)  
            iterateBoard(list.get(i));

       summary+=("---------\n");
    }

    //Method displays board
    private void iterateBoard(Board board)
    {   
        String boards="";
        for (int i = 0; i < size; i++) //print each element of board
        {
            for (int j = 0; j < size; j++)
            {
                boards += board.array[i][j] + " ";
            }
            boards +="\n";
        }   
        boards +="\n";
        summary +=boards;    
    }
    
}
