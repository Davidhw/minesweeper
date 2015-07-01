package edu.ncf.cs.david_weinstein.WeinsteinMineSweeper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests the Board class.
 * 
 * @author david weinstein
 */
public class BoardTest {

  /**
   * Ensure that the grid actually has as many bombs as it should.
   *
   */
  @Test
  public void testBoardSetsNBombs() {
    Board board = new Board();
    // assumes implementation where bombcount reflects hardcoded desired number of bombs
    int numBombsThatShouldBeSet = Board.bombCount;

    int numBombs = 0;
    for (int i = 0; i < board.width; i++) {
      for (int j = 0; j < board.height; j++) {
        if (board.getTile(i, j).getIsBomb()) {
          numBombs++;
        }
      }
    }
    assertEquals(numBombsThatShouldBeSet, numBombs);
  }

  /**
   * Ensure that the player loses when they click a bomb.
   * 
   */
  @Test
  public void testPlayLosing() {
    Board board = new Board();
    int moveRow = 1;
    int moveCol = 3;
    board.getTile(new GridLocation(moveRow, moveCol)).setIsBomb(true);
    boolean expectedLose = true;
    board.play(new GridLocation(1, 3));
    assertEquals(expectedLose, board.isLost());
  }

  /**
   * Ensure that the player doesn't lose for clicking a non bomb.
   *
   */
  @Test
  // this test could be made less redundant with parameterization, but
  // that's overkill for only two tests
  public void testPlayNotLosing() {
    Board board = new Board();
    assert (board.isLost() == false);
    int moveRow = 1;
    int moveCol = 3;
    board.getTile(new GridLocation(moveRow, moveCol)).setIsBomb(false);
    boolean expectedLose = false;
    board.play(new GridLocation(1, 3));
    assertEquals(expectedLose, board.isLost());
  }

  /**
   * Test that if something has adjacent bombs than none of its neighbors are visited.
   * 
   */
  @Test
  public void testRecSetAdjacentsVisitedIfHasAdjacentBombs() {
    Board board = new Board();
    int row = 0;
    int col = 0;
    GridLocation loc = new GridLocation(row, col);
    Tile tile = board.getTile(loc);
    tile.setAdjacentBombs(1);
    board.recSetAdjacentsVisitedIfNoAdjacentBombs(loc);

    assertTrue(board.getTile(row, col).getVisited());
    assertFalse(board.getTile(row + 1, col + 1).getVisited());
    assertFalse(board.getTile(row, col + 1).getVisited());
    assertFalse(board.getTile(row + 1, col).getVisited());
  }

  /**
   * Ensure recursively mark adjacents as visited depending on things. Ensure that if something
   * doesn't have any adjacent bombs that its neighbors are marked visited and that if any of those
   * neighbors don't adjacent bombs that their neighbors are marked visited.
   */
  @Test
  public void testRecSetAdjacentsVisitedIfNoAdjacentBombs() {
    Board board = new Board();
    int row = 0;
    int col = 0;
    GridLocation loc = new GridLocation(row, col);
    Tile tile = board.getTile(loc);
    tile.setAdjacentBombs(0);

    Tile a1 = board.getTile(row + 1, col + 1);
    Tile adjacentWithBomb = board.getTile(row + 1, col);
    Tile a2 = board.getTile(row, col + 1);
    a1.setIsBomb(false);
    adjacentWithBomb.setIsBomb(true);
    a2.setIsBomb(false);

    Tile adjacentadjacentTile = board.getTile(row, col + 2);
    adjacentadjacentTile.setIsBomb(false);
    a2.setAdjacentBombs(0);

    board.recSetAdjacentsVisitedIfNoAdjacentBombs(loc);

    // check that the adjacent tiles were marked visited
    assertTrue(tile.getVisited());
    assertTrue(a1.getVisited());
    assertFalse(adjacentWithBomb.getVisited());
    assertTrue(a2.getVisited());

    // check that recursion led to a tile adjacent to the adjacent
    // tile was marked visited
    assertTrue(adjacentadjacentTile.getVisited());
  }

}
