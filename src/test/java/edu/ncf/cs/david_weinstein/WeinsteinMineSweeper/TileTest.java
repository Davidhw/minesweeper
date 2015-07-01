package edu.ncf.cs.david_weinstein.WeinsteinMineSweeper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * This class test the Tile class. It currently only tests the label functionality.
 */
public class TileTest {
  String blankLabel = " ";
  String revealedBombLabel = "X";
  int someAdjacentBombs = 3;
  int zeroAdjacentBombs = 0;

  @Test
  public void testLabelBombUnvisited() {
    Tile tile = new Tile();
    tile.setIsBomb(true);
    tile.setAdjacentBombs(someAdjacentBombs);
    tile.setVisited(false);
    assertEquals(blankLabel, tile.label);
  }

  @Test
  public void testLabelBombRevealed() {
    Tile tile = new Tile();
    tile.setIsBomb(true);
    tile.setAdjacentBombs(someAdjacentBombs);
    tile.setVisited(true);
    assertEquals(revealedBombLabel, tile.label);
  }

  @Test
  public void testLabelNotBombAdjacentBombs() {
    Tile tile = new Tile();
    tile.setIsBomb(false);
    tile.setAdjacentBombs(someAdjacentBombs);
    tile.setVisited(true);
    assertEquals(Integer.toString(someAdjacentBombs), tile.label);
  }

  @Test
  public void testLabelNotBombNoAdjacentBombs() {
    Tile tile = new Tile();
    tile.setIsBomb(false);
    tile.setAdjacentBombs(zeroAdjacentBombs);
    tile.setVisited(true);
    assertEquals(blankLabel, tile.label);
  }

}
