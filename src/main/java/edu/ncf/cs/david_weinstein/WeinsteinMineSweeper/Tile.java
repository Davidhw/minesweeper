package edu.ncf.cs.david_weinstein.WeinsteinMineSweeper;

/**
 * Class representing the tiles on the Minesweeper grid. tiles don't actually know anything about
 * the board, the board knows about and stores info in tile objects
 * 
 * @author david weinstein
 */
public class Tile {
  /**
   * Label of tile.
   */
  String label;
  /**
   * label that reveals no information.
   */
  public static String blankLabel = " ";
  /**
   * label that reveals something is a bomb.
   */
  public static String revealedBombLabel = "X";

  protected Tile() {
    visited = false;
    isBomb = false;
    adjacentBombs = 0;
    label = blankLabel;
    updateLabel();
  }

  /**
   * update the string label of a tile. eg "2"(meaning two adjacent bombs), " " (meaning no adjacent
   * bombs), "X" (meaning is a bomb)
   */
  private void updateLabel() {
    if (getVisited()) {
      if (isBomb) {
        label = revealedBombLabel;
        return;
      } else {
        if (getAdjacentBombs() != 0) {
          label = getAdjacentBombs().toString();
          return;
        }
      }
      label = blankLabel;
    }
  }

  @Override
  /** String representation of a tile.
   */
  public String toString() {
    String vis = visited ? "t" : "f";
    String bomb = isBomb ? "t" : "f";
    return "Tile" + vis + bomb + Integer.toString(adjacentBombs);
  }

  /**
   * Whether or not a tile has been visited.
   */
  Boolean visited;
  /**
   * Whether or not a tile has a bomb.
   */
  Boolean isBomb;
  /**
   * how many tiles adjacent to the current tile have bombs.
   */
  Integer adjacentBombs;
  /**
   * row of the tile on the grid.
   */
  int row;
  /**
   * column of the tile on the grid.
   */
  int column;

  /**
   * increase the number of adjacent bombs var by one and update label.
   */
  protected final void incrementAdjacentBombs() {
    adjacentBombs += 1;
    updateLabel();
  }

  /**
   * get whether or not the tile has been visited.
   * 
   * @return has the tile been visited?
   */
  protected final Boolean getVisited() {
    return visited;
  }

  /**
   * set whether or not the tile has been visited.
   * 
   * @param explored
   *          board has been visited
   */
  protected final void setVisited(Boolean explored) {
    visited = explored;
    updateLabel();
  }

  /**
   * set whether or not there's a bomb at the given tile.
   * 
   * @param hasBomb
   *          is bomb on tile.
   */
  protected final void setIsBomb(Boolean hasBomb) {
    this.isBomb = hasBomb;
    updateLabel();
  }

  /**
   * get the number of bombs next to a tile.
   * 
   * @return number of adjacent bombs
   */
  protected final Integer getAdjacentBombs() {
    return adjacentBombs;
  }

  /**
   * set the number of bombs adjacent to a tile.
   * 
   * @param adjacentBombs
   *          number of adjacent bombs.
   */
  protected final void setAdjacentBombs(Integer adjacentBombs) {
    this.adjacentBombs = adjacentBombs;
    updateLabel();
  }

  /**
   * get whether or not there is a bomb at a tile.
   * 
   * @return is bomb on tile
   */
  protected final Boolean getIsBomb() {
    return isBomb;
  }

}
