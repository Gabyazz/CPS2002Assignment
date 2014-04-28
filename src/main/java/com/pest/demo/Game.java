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
	
	/**
	 * Create an html file for each player 
	 */
	public static void generateHTMLFiles(Map map)
	{
		File file = null;
		Colour colour;		
		Colour[][] tiles = map.getTiles();
		int found = 0, inTrail = 0;	//to check if treasure was found .... and.... to check if a position is already in the trail
		
		try
		{
			for (int i = 0; i < players.length; i++)	//for each player
			{
				file = new File("player" + (i+1) + "_map.html");
				PrintWriter writer = new PrintWriter(file.getPath(), "UTF-8");
			
				writer.println("<html>");
				writer.println("<head>");
				writer.println("<META HTTP-EQUIV=\"refresh\" content=\"5\">");
				writer.println("</head>");
				writer.println("<body>");
				writer.println("<table style=\"border-collapse: collapse;\">");
			
				for (int j = 0; j < tiles.length; j++)	//for each row
				{
					writer.println("<tr>");
					for (int k = 0; k < tiles[j].length; k++)	//for each column
					{	
						
						for(int l = 0; l < players[i].getTrail().size(); l++)	//for each item in the list
						{							 
							if((players[i].getTrail().get(l).getR() == j) && (players[i].getTrail().get(l).getC() == k))	//if position in trail
							{	
								inTrail = 1;
								break;
							}
						}
						
						if (inTrail==1)	//if position in trail add is colour to file
						{
							colour = tiles[j][k];
							
							if(colour == Colour.GREEN)
								writer.println("<td style=\"background-color:#00CC00;width:50px;height:50px;border:1px solid black;\">");							
							
							else if(colour == Colour.BLUE)
								writer.println("<td style=\"background-color:#00CCFF;width:50px;height:50px;border:1px solid black;\">");	
							
							else if(colour == Colour.YELLOW)
							{
								found = 1;
								writer.println("<td style=\"background-color:#C0C0C0;width:50px;height:50px;border:1px solid black;\">");
								writer.println("<img src=\"images/treasure.jpg\" width=\"50px\" height=\"50px\">");
							}							
						}
						else 
						{				
							writer.println("<td style=\"background-color:#C0C0C0;width:50px;height:50px;border:1px solid black;\">");
						}			
						
						if ((j == players[i].getPositionR()) && (k == players[i].getPositionC()) && (found==0))	//if player is at this position and treasure is not
						{
							writer.println("<img src=\"images/player.jpg\" width=\"50px\" height=\"48px\"");
						}
						else
						{
							found = 0;	//set back to 0 as other players haven't found it yet
						}
						inTrail = 0;
						writer.println("</td>");				
					}					
					writer.println("</tr>");		
				}		
				writer.println("</table>");
				writer.println("</body>");
				writer.print("</html>");
				writer.close();
			}
		}
		catch (FileNotFoundException | UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}	

	/*
	 * Returns the players array
	 */
	public static Player[] getPlayers()
	{	
		return players;	
	}

}