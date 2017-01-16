package model;

/**
 * This class represents the logic of a game where a board is updated on each
 * step of the game animation. The board can also be updated by selecting a
 * board cell.
 * 
 * @author Dept of Computer Science, UMCP
 */

public abstract class Game {
	protected BoardCell[][] board;

	/**
	 * Defines a board with BoardCell.EMPTY cells.
	 * 
	 * @param maxRows
	 * @param maxCols
	 */
	public Game(int maxRows, int maxCols) {
		this.board = new BoardCell[maxRows][maxCols];
		defineBoard(maxRows,maxCols,BoardCell.EMPTY);
	}
	
	/*
	 * Define a board with specified cells
	 */
	private void defineBoard(int rowMax,int colMax,BoardCell cell){
		for(int row = 0; row < rowMax; row++)
		{
			for(int col = 0; col < colMax; col++)
			{
				this.board[row][col] = cell;
			}
		}
	}

	public int getMaxRows() {
		return this.board.length;
	}

	public int getMaxCols() {
		return this.board[0].length;
	}

	public void setBoardCell(int rowIndex, int colIndex, BoardCell boardCell) {
		this.board[rowIndex][colIndex] = boardCell;
	}

	public BoardCell getBoardCell(int rowIndex, int colIndex) {
		return this.board[rowIndex][colIndex];
	}

	/**
	 * Initializes row with the specified color.
	 * @param rowIndex
	 * @param cell
	 */
	public void setRowWithColor(int rowIndex, BoardCell cell) {
		for(int col = 0; col < this.board[rowIndex].length; col++)
		{
			this.board[rowIndex][col] = cell;
		}
	}
	
	/**
	 * Initializes column with the specified color.
	 * @param colIndex
	 * @param cell
	 */
	public void setColWithColor(int colIndex, BoardCell cell) {
		for(int row = 0; row < this.board.length; row++)
		{
			this.board[row][colIndex] = cell;
		}
	}
	
	/**
	 * Initializes the board with the specified color.
	 * @param cell
	 */
	public void setBoardWithColor(BoardCell cell) {
		for(int i = 0; i < this.board.length; i++)
		{
			defineBoard(this.board.length,this.board[i].length,cell);
		}
	}	
	
	public abstract boolean isGameOver();

	public abstract int getScore();

	/**
	 * Advances the animation one step.
	 */
	public abstract void nextAnimationStep();

	/**
	 * Adjust the board state according to the current board state and the
	 * selected cell.
	 * 
	 * @param rowIndex
	 * @param colIndex
	 */
	public abstract void processCell(int rowIndex, int colIndex);
}