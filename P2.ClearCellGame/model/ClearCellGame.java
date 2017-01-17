package model;

import java.util.Random;

/* This class must extend Game */
public class ClearCellGame extends Game{
	
	//fields
	private int score;
	private int strategy;
	private Random randomNum;

	/**
	 * Initializes a new game with maximum rows and columns from user input.
	 * Keeps track of random number for generating unique cells and an integer
	 * to determine which strategy will be used.
	 * 
	 * @param maxRows
	 * @param maxCols
	 * @param randomNum
	 * @param num
	 */
	public ClearCellGame(int maxRows, int maxCols, Random randomNum, int num) {
		super(maxRows, maxCols);
		this.score = 0;
		this.randomNum = randomNum;
		this.strategy = num;
	}

	/**
	 * Checks if last row has all clear cells
	 * @return true if all cells in last row are clear
	 */
	private boolean allClear(int row){
		
		boolean allClear = true;
		int rowToCheck = row;
		
		for(int col = 0; col < super.board[rowToCheck].length; col++)
		{
			allClear = 
					(this.getBoardCell(rowToCheck, col)
							== BoardCell.EMPTY)? true:false;
			
			if(!allClear)
			{
				break;
			}
		}
		
		return allClear;
	}
	
	@Override
	/**
	 * Verifies when the game has ended
	 * @returns true if last row is not empty
	 */
	public boolean isGameOver() {
		// TODO Auto-generated method stub
		return !allClear(super.board.length-1);
	}

	@Override
	/**
	 * @returns current score
	 */
	public int getScore() {
		// TODO Auto-generated method stub
		return this.score;
	}

	@Override
	/**
	 * Inserts a row of random colors in the first row if last row is empty
	 */
	public void nextAnimationStep() {
		// TODO Auto-generated method stub
		
		int lastRow = super.board.length-1;
		
		if(allClear(lastRow))
		{
			for(int row = lastRow-1; row >= 0 ; row--)
			{
				for(int col = 0; col < super.board[lastRow].length; col++)
				{
					this.setBoardCell(row+1, col,
							this.getBoardCell(row, col));
					if(row == 0)
					{
						this.setBoardCell(row, col, 
								BoardCell.getNonEmptyRandomBoardCell(this.randomNum));
					}
				}
			}
			
		}
	}

	/**
	 * processes cells according to specifications
	 */
	@Override
	public void processCell(int rowIndex, int colIndex) {
		if(allClear(super.board.length-1))
		{
			if(this.strategy == 1)
			{
				process(rowIndex,colIndex);
				collapseRows();
			}
		}
	}
	
	/*
	 * Method to process cells in the game
	 */
	private void process(int row,int col)
	{
		//Temp store of cell type
		BoardCell cell = this.getBoardCell(row, col);
		
		//Clear cell position
		this.setBoardCell(row, col, BoardCell.EMPTY);
		this.score++;
		
		//variables to check if a block is valid
		int leftOutOfBounds = -1;
		int rightOutOfBounds = super.board[row].length;
		int topOutOfBounds = -1;
		int bottomOutOfBounds = super.board.length;
		
		/* ************************************
		 ***********Clear vertically*********
		 **************************************/ 
		
			//up
			for(int i = 1; !((row-i)==topOutOfBounds) 
								&& super.board[row-i][col] == cell; i++)
			{
				this.setBoardCell(row-i, col, BoardCell.EMPTY);
				this.score++;
			}
		
			//down
			for(int i = 1; !((row+i)==bottomOutOfBounds) 
					&& super.board[row+i][col] == cell; i++)
			{
				this.setBoardCell(row+i, col, BoardCell.EMPTY);
				this.score++;
			}
		
		
		/* ************************************
		 ***********Clear horizontally*********
		 **************************************/
		
			//right
			for(int i = 1; !((col+i)==rightOutOfBounds) 
					&& super.board[row][col+i] == cell; i++)
			{
				this.setBoardCell(row, col+i, BoardCell.EMPTY);
				this.score++;
			}
		
			//left
			for(int i = 1; !((col-i)==leftOutOfBounds) 
					&& super.board[row][col-i] == cell; i++)
			{
				this.setBoardCell(row, col-i, BoardCell.EMPTY);
				this.score++;
			}
		
		/* ************************************
		 **********Clear right diagonal********
		 **************************************/
		
			//down-right
			for(int i = 1; !((col+i)==rightOutOfBounds) 
					&& !((row+i)==bottomOutOfBounds) 
					&& super.board[row+i][col+i] == cell; i++)
			{
				this.setBoardCell(row+i, col+i, BoardCell.EMPTY);
				this.score++;
			}
		
			//up-left
			for(int i = 1; !((col-i)==leftOutOfBounds) 
					&& !((row-i)==topOutOfBounds) 
					&& super.board[row-i][col-i] == cell; i++)
			{
				this.setBoardCell(row-i, col-i, BoardCell.EMPTY);
				this.score++;
			}
		
		/* ************************************
		 **********Clear left diagonal*********
		 **************************************/
		
			//down-left
			for(int i = 1; !((col-i)==leftOutOfBounds) 
					&& !((row+i)==bottomOutOfBounds) 
					&& super.board[row+i][col-i] == cell; i++)
			{
				this.setBoardCell(row+i, col-i, BoardCell.EMPTY);
				this.score++;
			}
		
			//up-right
			for(int i = 1; !((col+i)==rightOutOfBounds) 
					&& !((row-i)==topOutOfBounds) 
					&& super.board[row-i][col+i] == cell; i++)
			{
				this.setBoardCell(row-i, col+i, BoardCell.EMPTY);
				this.score++;
			}
	}
	
	/*
	 * Method to collapse empty rows on the board
	 */
	private void collapseRows()
	{
		int lastRow = super.board.length-1;
		//check for clear rows
		for(int row = 0; row <= lastRow-1;row++)
		{
			if(allClear(row))
			{
				for(int col = 0; col < super.board[row].length; col++)
				{
					this.setBoardCell(row, col, 
							this.getBoardCell(row+1, col));
					this.setBoardCell(row+1, col, BoardCell.EMPTY);
				}
			}
		}
	}
	
}