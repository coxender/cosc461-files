package assignment1;

import java.io.*;
import java.util.Scanner;

//Tester program for sliding board solver with A* search
public class SlidingAstarTester
{
    //main method for testing
    public static void main(String[] args) throws FileNotFoundException
    {   
    	/*
        //initial board
        char[][] initial = {{'5', '7', '1'},
                            {'2', '0', '8'},
                            {'4', '6', '3'}};
                            */
    	/*
        //final board
        char[][] goal = {{'1', '4', '8'},
                         {'5', '2', '6'},
                         {'0', '3', '7'}};
        */
    	
    	//text file should contain the following:
    	//1 int for size n of board (n*n)
    	//9 ints for the initial board
    	//9 ints for goal board
    	//1 int (1, 2 or 3) for evaluation function
    	//1 int (1 or 2) for heuristic function option

    	//declare file, scanner, and writer
		File file;
		FileWriter out;
    	Scanner sc = new Scanner(new File("file1.txt"));
    	
    	//first int is the size
    	int size = sc.nextInt();
    	
    	//initialize evaluation and heuristic function variables
    	int evalFunc;
    	int heurFunc;
		
    
    	//initialize our initial (next 9 ints) and goal (9 more ints) board
    	char[][] initial = new char[size][size];
    	char[][] goal = new char[size][size];
    	
    	//set values for initial board
    	for (int i = 0; i < size; i++) {
    		for (int j = 0; j < size; j++) {
    			initial[i][j] = (char)(sc.nextInt() + '0');
    		}
    	}
    	
    	//set values for goal board
    	for (int i = 0; i < size; i++) {
    		for (int j = 0; j < size; j++) {
    			goal[i][j] = (char)(sc.nextInt() + '0');
    		}
    	}
    	
    	evalFunc = sc.nextInt();
    	heurFunc = sc.nextInt();
    	
    	
        //solve sliding puzzle
        SlidingAstar s = new SlidingAstar(initial, goal, size, evalFunc, heurFunc);
        s.solve();

		System.out.println(s.summary);
		//write to file
		file = new File("output.txt");
		try{
			file.createNewFile();
			out = new FileWriter(file);
			out.write(s.summary);
			out.close();
			sc.close();
			
		}catch(Exception e){
			System.out.println(e.getStackTrace());
		}
		
    }
}