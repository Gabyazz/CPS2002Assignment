package com.pest.demo;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.File;

import lib.commons.io.FileUtils;
import org.junit.*;

import com.pest.demo.Game;
import com.pest.demo.Map;
import com.pest.demo.Player;
import com.pest.demo.Map.Colour;

public class GameTest
{
	Game game = null;
	Map map = null;
	Player player = null;
		
	@Before
	public void before()
	{	
		game = new Game();
		map = new Map();
		player = new Player();
	}		
	
	@Test
	public void amntOfPlayersBounds() 
	{		
		assertEquals(Game.setNumPlayers(1), false);
		assertEquals(Game.setNumPlayers(9), false);
		assertEquals(Game.setNumPlayers(2), true);
		assertEquals(Game.setNumPlayers(5), true);				
	}
	
	@Test
	public void htmlMapTest() throws IOException
	{		
		Game.setNumPlayers(2);
		map.setSize(5,2);
		map.createMap();
		Player[] players = Game.getPlayers();
		
		for (int i=0; i < players.length; i++)
		{
			players[i].setPositionManual(0, 0);
		}				
		
		Game.generateHTMLFiles(map);
		File map1 = new File("player1_map.html");
		File map2 = new File("player2_map.html");
		
		assertEquals(FileUtils.readFileToString(map1, "utf-8"), FileUtils.readFileToString(map2, "utf-8"));
	}
	
	@Test
	public void amntOfPlayersBounds1() 
	{			
		assertEquals(map.setSize(4, 3), false);
		assertEquals(map.setSize(51, 3), false);
		assertEquals(map.setSize(6, 3), true);
		assertEquals(map.setSize(25, 3), true);
	}
	
	@Test
	public void amntOfPlayersBounds2() 
	{		
		assertEquals(map.setSize(4, 8), false);
		assertEquals(map.setSize(51, 8), false);
		assertEquals(map.setSize(6, 8), false);
		assertEquals(map.setSize(25, 8), true);						
	}
	
	@Test
	public void generateTestSize1()
	{
		map.setSize(5, 2);
		map.createMap();

		assertEquals(map.getTiles().length, 5);
		assertEquals(map.getTiles()[1].length, 5);
		assertEquals(map.getSize(), 5);
	}
	
	@Test
	public void generateTestSize2() 
	{
		map.setSize(8, 6);
		map.createMap();

		assertEquals(map.getTiles().length, 8);
		assertEquals(map.getTiles()[1].length, 8);
		assertEquals(map.getSize(), 8);		
	}
	
	@Test
	public void upperBoundTest() 
	{	
		map.setSize(5,2);
		map.createMap();
		player.setPositionManual(4,4);	
		assertEquals(player.move('d', 5), false);
	}
	
	@Test
	public void upperMoveTest() 
	{	
		map.setSize(5,2);
		map.createMap();
		player.setPositionManual(2,4);
		player.move('u', 5);	
		assertEquals(player.getPositionR(), 1);
	}
	
	@Test
	public void lowerBoundTest() 
	{	
		map.setSize(5,2);
		map.createMap();
		player.setPositionManual(0,4);
		assertEquals(player.move('u', 5), false);		
	}
	
	@Test
	public void lowerMoveTest()
	{	
		player.setPositionManual(2,4);
		player.move('d', 5);	
		assertEquals(player.getPositionR(), 3);
	}
	
	@Test
	public void rightBoundTest() 
	{	
		map.setSize(5,2);
		map.createMap();
		player.setPositionManual(1,4);
		assertEquals(player.move('r', 5), false);
	}
	
	@Test
	public void rightMoveTest() 
	{
		map.setSize(5,2);
		map.createMap();
		player.setPositionManual(2,2);
		player.move('r', 5);
		assertEquals(player.getPositionC(), 3);
	}
	
	@Test
	public void leftBoundTest() 
	{	
		map.setSize(5,2);
		map.createMap();
		player.setPositionManual(1,0);
		assertEquals(player.move('l', 5), false);
	}
	
	@Test
	public void leftMoveTest()
	{	
		player.setPositionManual(2,2);
		player.move('l', 5);
		assertEquals(player.getPositionC(), 1);
	}
	
	@Test
	public void invalidMoveTest() 
	{
		map.setSize(5,2);
		map.createMap();
		player.setStartingPosition(map);			
		assertEquals(player.move('n', 5), false);
	}
	
	@Test
	public void moveBackToStartTest() 
	{
		map.setSize(5,2);
		map.createMap();
		player.setStartingPosition(map);
		player.move('u', 5);
		player.move('u', 5);
		player.move('u', 5);
		player.move('u', 5);
		player.moveBackToStart(1);
		
		assertEquals(player.getPositionC(), player.getStartingPosition().getC());
		assertEquals(player.getPositionR(), player.getStartingPosition().getR());	
	}
	
	@Test
	public void trailTest() 
	{	
		map.setSize(5,2);
		map.createMap();
		player.setPositionManual(0,0);
		player.move('d', 5);
		player.updateTrail();
		player.move('d', 5);
		player.updateTrail();
		player.move('d', 5);
		player.updateTrail();
		player.move('d', 5);
		player.updateTrail();
		
		for(int i = 0; i < player.getTrail().size(); i++)
		{
			assertEquals(player.getTrail().get(i).getR(), i);			
		}		
	}
	
	@Test
	public void startOnGreenTest() 
	{	
		map.setSize(5,2);
		map.createMap();
		player.setStartingPosition(map);
		assertEquals(player.checkColour(map), Colour.GREEN);		
	}	
}