package edu.ncf.cs.david_weinstein.WeinsteinMineSweeper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Class representing a Minesweeper board.
 * 
 * @author david weinstein
 * 
 */
public class Board {
  /**
   * how many squares wide the board is.
   *
   */
  public int width = 16;
  /**
   * how many squares vertically the board is.
   * 
   */
  public int height = 16;

  /**
   * how many bombs are on the board.
   * 
   */
  protected static final int bombCount = 40;
  /**
   * has the player lost yet.
   * 
   */
  private boolean lost;
  /**
   * has the player won yet.
   * 
   */
  private boolean won;
  /**
   * width x height 2d array of tiles that make up the board.
   * 
   */
  Tile[][] grid;

  /**
   * get a new board.
   * 
   */
  public Board() {
    grid = new Tile[width][height];
    setBoard();
  }

  // row, collumn, 0-15

  /**
   * returns the tile at a location.
   * 
   * @param location
   *          of tile
   * @return tile at that loc
   */
  Tile getTile(GridLocation loc) {
    return grid[loc.getR()][loc.getC()];
  }

  /**
   * returns the tile at a location.
   * 
   * @param row
   *          of tile
   * @param column
   *          of tile
   * @return tile at that location
   */
  Tile getTile(int row, int col) {
    return grid[row][col];
  }

  /**
   * is the tile at a given location visited?.
   * 
   * @param row
   *          of the tile
   * @param column
   *          of the tile
   * @return tile visited?
   */
  boolean isVisited(int row, int col) {
    return getTile(row, col).getVisited();
  }

  /**
   * assign a spot on the grid to a tile.
   * 
   * @param row
   *          row of tile
   * @param col
   *          column of tile
   * @param tile
   *          tile
   */
  private void setTile(int row, int col, Tile tile) {
    grid[row][col] = tile;
  }

  /**
   * set a given location on the board to be a bomb or not.
   * 
   * @param loc
   *          on grid
   * @param isBomb
   *          is the tile a bomb?
   */
  private void setBomb(GridLocation loc, boolean isBomb) {
    getTile(loc).setIsBomb(isBomb);
  }

  /**
   * Is a given location on the grid?.
   * 
   * @param loc
   *          we're checking the validity of
   * @return is the loc on the board?
   */
  private boolean validLocation(GridLocation loc) {
    if (loc.getC() < 0 || loc.getC() == height || loc.getR() < 0
        || loc.getR() >= width) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Check if all non-bombs have been visited (player won).
   * 
   * @return did the player win?
   * 
   */
  private boolean allNonBombsVisited() {
    for (int row = 0; row < width; row++) {
      for (int col = 0; col < height; col++) {
        if (!(getTile(row, col).getVisited()) && !getTile(row, col).getIsBomb()) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * visit a given location on the grid.
   * 
   * @param loc
   *          the player is visiting
   */
  public void play(GridLocation loc) {
    // getTile(loc).setVisited(true);
    if (getTile(loc).isBomb) {
      lost = true;
      setAllVisited();
    } else {
      recSetAdjacentsVisitedIfNoAdjacentBombs(loc);
      won = allNonBombsVisited();
      if (won) {
        setAllVisited();
      }
    }
  }

  /**
   * make everything on the board visited (for when the game is over).
   * 
   */
  private void setAllVisited() {
    for (int r = 0; r < width; r++) {
      for (int c = 0; c < height; c++) {
        getTile(r, c).setVisited(true);
      }
    }
  }

  /**
   * Get the legal adjacent locations.
   * 
   * @param loc
   *          to get adjacent locations of
   * @return adjacent locations
   */
  private List<GridLocation> getAdjacentLocations(GridLocation loc) {
    List<GridLocation> potentials = new ArrayList<GridLocation>();
    int[] modifiers = new int[] { -1, 0, 1 };
    for (int rowMod : modifiers) {
      for (int colMod : modifiers) {
        potentials.add(new GridLocation(loc.getR() + rowMod, loc.getC()
            + colMod));
      }
    }
    potentials.remove(loc);
    return potentials.stream().filter(l -> validLocation(l))
        .collect(Collectors.toList());
  }

  /**
   * Assign tiles to the grid and disperse the bombs at random locations.
   * 
   */
  private void setBoard() {
    List<GridLocation> bombLocations = new ArrayList<GridLocation>();

    for (int r = 0; r < width; r++) {
      for (int c = 0; c < height; c++) {
        setTile(r, c, new Tile());
      }
    }

    Random generator = new Random();

    while (bombLocations.size() < bombCount) {
      int col = generator.nextInt(width);
      int row = generator.nextInt(height);
      GridLocation possibleLocation = new GridLocation(row, col);
      if (!bombLocations.contains(possibleLocation)) {
        bombLocations.add(possibleLocation);
        setBomb(possibleLocation, true);
        for (GridLocation adjacent : getAdjacentLocations(possibleLocation)) {
          getTile(adjacent).incrementAdjacentBombs();
        }
      }
    }
  }

  /**
   * Recursively check if the given location has zero adjacent bombs. bombs and make it visited and
   * call this function on its adjacent nonbomb, unvisited neighbors
   * 
   * @param boardLoc
   *          location on board
   */
  public void recSetAdjacentsVisitedIfNoAdjacentBombs(GridLocation boardLoc) {
    ArrayList<GridLocation> alreadyVisited = new ArrayList<GridLocation>();
    LinkedList<GridLocation> visitThese = new LinkedList<GridLocation>();

    visitThese.add(boardLoc);

    while (!visitThese.isEmpty()) {
      GridLocation currentLoc = visitThese.pop();
      Tile currentTile = getTile(currentLoc);
      currentTile.setVisited(true);
      if (currentTile.getAdjacentBombs() == 0) {
        List<GridLocation> adjacents = getAdjacentLocations(currentLoc)
            .stream()
            .filter(
                loc -> (!(getTile(loc).getIsBomb())
                    && !(getTile(loc).getVisited()) && !(alreadyVisited
                    .contains(loc)))).collect(Collectors.toList());
        visitThese.addAll(adjacents);
        alreadyVisited.addAll(adjacents);
      }

    }
  }

  /**
   * did the player lose the game.
   * 
   * @return is the game lost?
   */
  public final boolean isLost() {
    return lost;
  }

  /**
   * did the player win the game.
   * 
   * @return is the game won?
   */
  public final boolean isWon() {
    return won;
  }

  /**
   * return a string representing the tile at a given location.Currently unused.
   * 
   * @param row
   *          row of the tile
   * @param col
   *          column of the tile
   * @return string representing the tile
   */
  public String get(int row, int col) {
    Tile tile = getTile(row, col);
    if (tile.getVisited()) {
      if (tile.getIsBomb()) {
        return "X";
      } else {
        return tile.getAdjacentBombs().toString();
      }
    } else {
      return "";
    }
  }

}
