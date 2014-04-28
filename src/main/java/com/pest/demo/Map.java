package com.test.demo;

import java.util.Random;

public class Map 
{
	private int size;
	public enum Colour {GREEN, BLUE, YELLOW}; 
	private Colour[][] tiles;
	
	/**
	 * Set the size of the map depending on the number of players
	 * Returns true if map was successfully created
	 */
	public boolean setSize(int mapSize, int numOfPlayers)
	{				
		if ((mapSize < 5) || (mapSize > 50))
			return false;		
		
		if ((5 <= numOfPlayers) && (numOfPlayers <= 8))
		{			
			if (mapSize < 8)
				return false;			
		}		
		size = mapSize;
		return true;
	}
	
	public void createMap()
	{		
		int random, x, y;
		tiles = new Colour[size][size];
		Random rand = new Random();
		
		for (int i = 0; i < tiles.length; i++)
		{
			for (int j = 0; j < tiles[i].length; j++)
			{
				random = rand.nextInt(5);

				if (random == 0)
					tiles[i][j] = Colour.BLUE;	//probability of blue tile is 1/5
				else 
					tiles[i][j] = Colour.GREEN;
			}
		}		
		//assign random coordinates to treasure
		x = (int)(Math.random()*(size-1))+1;
		y = (int)(Math.random()*(size-1))+1;
		tiles[x][y] = Colour.YELLOW;	
	}	
	
	public int getSize()
	{
		return size;
	}
	
	public Colour[][] getTiles()
	{
		return tiles;
	}	
}
