package com.pest.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import com.pest.demo.Map.Colour;

public class Game 
{
	private static int turns = 0;
	private static Player[] players, winners;
	private static Map map = new Map();
	
	public static void main(String args[])
	{
		Scanner sc = new Scanner(System.in);
		int numOfPlayers, mapSize;

		do
		{
			System.out.println("Please Enter the Number of Players(2-8): ");
			numOfPlayers = sc.nextInt();
		} 
		while (!setNumPlayers(numOfPlayers));
				
		if(numOfPlayers <= 4)
		{			
			do
			{
				System.out.println("Please Enter the Size of the Map ([5x5]-[50x50]): ");
				mapSize = sc.nextInt();
			} 
			while (!map.setSize(mapSize, players.length));						
		}
		
		else
		{									
			do
			{
				System.out.println("Please Enter the Size of the Map ([8x8]-[50x50]): ");
				mapSize = sc.nextInt();
			} 
			while (!map.setSize(mapSize, players.length));			
		}
		map.createMap();
		
		startGame();		
		sc.close();
	}
	
	/**   
	 * Initializes the players
	 * Returns true if conditions are satisfied
	 */	
	public static boolean setNumPlayers(int n)
	{
		if((n >= 2) && (n <= 8))
		{
			players = new Player[n];
			
			//initializing the players
			for (int i=0; i < n; i++)
			{
				players[i] = new Player();
			}			
			return true;
		}
		else
			return false;
	}
	
	private static void startGame()
	{		
		Scanner sc = new Scanner(System.in); 
		boolean valid; //to check if move is within map bounds
		boolean won = false; //to see if anyone won
		char move;
		winners = new Player[players.length];
		
		//assign starting position to each player
		for (int i = 0; i < players.length; i++)
		{				
			players[i].setStartingPosition(map);
		}
		generateHTMLFiles(map);			
		do
		{			
			for (int i = 0; i < players.length; i++) //running turn for each player. 
			{								
				do 
				{ 
					System.out.println("Player " + (i+1) + " Enter Move: (U)p, (D)own, (L)eft, (R)ight");
					move = sc.next().charAt(0);
					valid = players[i].move(move, map.getSize());	
					if (valid == false) 
						System.out.println("Invalid move!!!");
					
				} while(!valid);					
			}
			
			for (int i = 0; i < players.length; i++) //running turn for each player. 
			{	
				players[i].updateTrail();	//add position to trail
				
				if(players[i].checkColour(map) == Colour.YELLOW) //adding players into winners array
				{	
					winners[i] = new Player();
					winners[i] = players[i];
					won = true;			
				}
				
				//check if player hits water tile
				else if(players[i].checkColour(map) == Colour.BLUE)
				{			
					players[i].moveBackToStart(i+1);						
				}
			}	
			turns++;
			generateHTMLFiles(map);
		} while(won == false);
		
		System.out.println("After " + turns + " turns, Winner(s):");
		for (int i = 0; i < winners.length; i++) //running turn for each player.
		{
			if(winners[i] == null)
				continue;
			else
				System.out.println("Player " + (i+1));
		}
		
		sc.close();		
	}
	
	

	/*
	 * Returns the players array
	 */
	public static Player[] getPlayers()
	{	
		return players;	
	}

}