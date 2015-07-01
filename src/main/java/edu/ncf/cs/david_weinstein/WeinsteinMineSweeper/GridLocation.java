package edu.ncf.cs.david_weinstein.WeinsteinMineSweeper;

/**
 * Class representing locations on the minesweeper grid.
 * 
 * @author d
 *
 */
public class GridLocation {

  /**
   * Create a new grid location.
   * 
   * @param inputR
   *          input row
   * @param inputC
   *          input column
   */
  GridLocation(int inputR, int inputC) {

    row = inputR;
    col = inputC;
  }

  /**
   * row.
   * 
   */
  final int row;

  /**
   * column.
   * 
   */
  final int col;

  /**
   * get the row.
   * 
   * @return row
   */
  protected final int getR() {
    return row;
  }

  /**
   * get the column.
   * 
   * @return column
   */
  protected final int getC() {
    return col;
  }

  /**
   * hashcode's are unique unless you have a really big board.
   * 
   */
  @Override
  public int hashCode() {
    return 10000 * row + col;
  }

  /**
   * To be equal they need to have the same row and column.
   *
   */
  @Override
  public boolean equals(Object obj) {
    GridLocation other = (GridLocation) obj;
    return col == other.col && row == other.row;
  }

}
