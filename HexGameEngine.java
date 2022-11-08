package hexGame;

/** 
 * The Hex game engine enforces the Hex game rules ensuring that
 * players cannot move into occupied cells or move to 
 * invalid cells (attempt to use bad cell indices).  It also 
 * monitors the progress of the game and declares a winner 
 * when the game is over. 
 */

public class HexGameEngine implements HexGameLogic {
	
	private int turn;
	private int rows;
	private HexCell[] cells = new HexCell[125];
	private String status = "";
	private DisjointSet set;
	private int[] left = {11, 22, 33, 44, 55, 66, 77, 88, 99};
	private int[] right = {21, 32, 43, 54, 65, 76, 87, 98, 109};
    
    /** 
     * The constructor sets the blue player to start and ensures the
     * the game status is "playing." Note: You should not call the reset
     * method in this constructor; the reset method attempts to clear the
     * the players associated with each Hex cell, but Hex cells may be
     * added to the game board only after it has been created (see the
     * Hex cell constructor in the GUI code).
     * @param rows the number of rows on the game board (number of cells is rows * rows)
     */
    public HexGameEngine(int rows) {
        turn = 0;
        status = "playing";
        this.rows = rows;
        set = new DisjointSet(rows * rows - 1 + 5);
        //cells[121].setPlayer(Player.Blue); // Left border (blue) is 121
        //cells[122].setPlayer(Player.Red); // Top border (red) is 122
        //cells[123].setPlayer(Player.Blue); // Right border (blue) is 123
        //cells[124].setPlayer(Player.Red); // Bottom border (red) is 124
        
    }
    

    /**
     * If the current board configuration contains a winning path for
     * a player, this function returns that player; otherwise, the
     * function returns Player.None if neither player has yet won.
     * @return the winning player (or Player.None if no winner yet)
     */
    @Override
    public Player getWinner() {
        // Replace with your code
    	if (set.find(121) == set.find(123)) {
    		return Player.Blue;
    	}
    	if (set.find(122) == set.find(124)) {
    		return Player.Red;
    	}
        return Player.None; // No one won yet
    }
    
    

    /**
     * Returns the player (<tt>Player.Blue</tt> or <tt>Player.Red</tt>) 
     * whose turn it is
     * @return the player whose turn it is
     */
    @Override
    public Player currentPlayer() {
        return (turn % 2 == 0) ? Player.Blue : Player.Red ;
    }
    

    /**
     * Claims the Hex cell at the given index for the current player
     * if the cell currently is unoccupied. If the move is possible,
     * and the move is not a winning move, the opposing player then
     * takes control of the next move.
     * @param index the cell to claim by the current player
     */
    @Override
    public void move(int index) {
        if (turn % 2 == 0) {
        	cells[index].setPlayer(Player.Blue);
        }
        else {
        	cells[index].setPlayer(Player.Red);
        }
        setUnion(index);
        set.print();
        turn++;
    }
    
    public void setUnion(int index) { 
    	// Corner cases (0, 10, 110, 120)
    	if (index == 0) {
    		if (cells[index].getPlayer() == cells[121].getPlayer()) {set.union(index, 121);}
    		if (cells[index].getPlayer() == cells[122].getPlayer()) {set.union(index, 122);}
    		if (cells[index].getPlayer() == cells[1].getPlayer()) {set.union(index, 1);}
    		if (cells[index].getPlayer() == cells[11].getPlayer()) {set.union(index, 11);}
    	}
    	else if (index == 10) {
    		if (cells[index].getPlayer() == cells[122].getPlayer()) {set.union(index, 122);}
    		if (cells[index].getPlayer() == cells[123].getPlayer()) {set.union(index, 123);}
    		if (cells[index].getPlayer() == cells[9].getPlayer()) {set.union(index, 9);}
    		if (cells[index].getPlayer() == cells[20].getPlayer()) {set.union(index, 20);}
    		if (cells[index].getPlayer() == cells[21].getPlayer()) {set.union(index, 21);}
    	}
    	else if (index == 110) {
    		if (cells[index].getPlayer() == cells[121].getPlayer()) {set.union(index, 121);}
    		if (cells[index].getPlayer() == cells[124].getPlayer()) {set.union(index, 124);}
    		if (cells[index].getPlayer() == cells[99].getPlayer()) {set.union(index, 99);}
    		if (cells[index].getPlayer() == cells[100].getPlayer()) {set.union(index, 100);}
    		if (cells[index].getPlayer() == cells[111].getPlayer()) {set.union(index, 111);}
    	}
    	else if (index == 120) {
    		if (cells[index].getPlayer() == cells[123].getPlayer()) {set.union(index, 123);}
    		if (cells[index].getPlayer() == cells[124].getPlayer()) {set.union(index, 124);}
    		if (cells[index].getPlayer() == cells[109].getPlayer()) {set.union(index, 109);}
    		if (cells[index].getPlayer() == cells[119].getPlayer()) {set.union(index, 119);}
    	}
    	
    	// Cases if index is on the left row (see "left" array)
    	if (contains(left, index)) {
    		if (cells[index].getPlayer() == cells[121].getPlayer()) {set.union(index, 121);}
    		if (cells[index].getPlayer() == cells[index-11].getPlayer()) {set.union(index, index-11);}
    		if (cells[index].getPlayer() == cells[index-10].getPlayer()) {set.union(index, index-10);}
    		if (cells[index].getPlayer() == cells[index+1].getPlayer()) {set.union(index, index+1);}
    		if (cells[index].getPlayer() == cells[index+11].getPlayer()) {set.union(index, index+11);}
    	}
    	
    	// Cases if index is on the top row (1 - 9)
    	else if (index < 10 && index > 0) {
    		if (cells[index].getPlayer() == cells[122].getPlayer()) {set.union(index, 122);}
    		if (cells[index].getPlayer() == cells[index-1].getPlayer()) {set.union(index, index-1);}
    		if (cells[index].getPlayer() == cells[index+10].getPlayer()) {set.union(index, index+10);}
    		if (cells[index].getPlayer() == cells[index+1].getPlayer()) {set.union(index, index+1);}
    		if (cells[index].getPlayer() == cells[index+11].getPlayer()) {set.union(index, index+11);}
    	}
    	
    	// Cases if index is on the right row (see "right" array)
    	else if (contains(right, index)) {
    		if (cells[index].getPlayer() == cells[123].getPlayer()) {set.union(index, 123);}
    		if (cells[index].getPlayer() == cells[index-11].getPlayer()) {set.union(index, index-11);}
    		if (cells[index].getPlayer() == cells[index-1].getPlayer()) {set.union(index, index-1);}
    		if (cells[index].getPlayer() == cells[index+10].getPlayer()) {set.union(index, index+11);}
    		if (cells[index].getPlayer() == cells[index+11].getPlayer()) {set.union(index, index+11);}
    	}
    	
    	// Cases if index is on the bottom row (111 - 119)
    	else if (index < 120 && index > 110) {
    		if (cells[index].getPlayer() == cells[124].getPlayer()) {set.union(index, 124);}
    		if (cells[index].getPlayer() == cells[index-1].getPlayer()) {set.union(index, index-1);}
    		if (cells[index].getPlayer() == cells[index-11].getPlayer()) {set.union(index, index-11);}
    		if (cells[index].getPlayer() == cells[index-10].getPlayer()) {set.union(index, index-10);}
    		if (cells[index].getPlayer() == cells[index+1].getPlayer()) {set.union(index, index+1);}
    	}
    	
    	else {
    		if (cells[index].getPlayer() == cells[index-11].getPlayer()) {set.union(index, index-11);}
    		if (cells[index].getPlayer() == cells[index-10].getPlayer()) {set.union(index, index-10);}
    		if (cells[index].getPlayer() == cells[index-1].getPlayer()) {set.union(index, index-1);}
    		if (cells[index].getPlayer() == cells[index+1].getPlayer()) {set.union(index, index+1);}
    		if (cells[index].getPlayer() == cells[index+10].getPlayer()) {set.union(index, index+10);}
    		if (cells[index].getPlayer() == cells[index+11].getPlayer()) {set.union(index, index+11);}
    	}
    	
    	
    	/*
    	if (index > 10 
    			&& cells[index].getPlayer() == cells[index - 11].getPlayer()) {
    		set.union(index, index - 11);
    	}
    	if (!contains(right, index)
    			&& index > 10 
    			&& cells[index].getPlayer() == cells[index - 10].getPlayer()) {
    		set.union(index, index - 10);
    	}
    	if (!contains(left, index) 
    			&& cells[index].getPlayer() == cells[index - 1].getPlayer()) {
    		set.union(index, index - 1);
    	}
    	if (!contains(right, index) 
    			&& cells[index].getPlayer() == cells[index + 1].getPlayer()) {
    		set.union(index, index + 1);
    	}
    	if (!contains(left, index) 
    			&& index < 110 
    			&& cells[index].getPlayer() == cells[index + 10].getPlayer()) {
    		set.union(index, index + 10);
    	}
    	if (index < 110  
    			&& cells[index].getPlayer() == cells[index + 11].getPlayer()) {
    		set.union(index, index + 11);
    	}*/
    }
    
    public boolean contains(int[] array, int num) {
    	for (int value: array) {
    		if (value == num) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Positions the current player at an open cell pseudorandomly
     * chosen on the Hex game board
     */
    @Override
    public void randomMove() {
        // Add your code
    }

    /**
     * Makes up to <tt>n</tt> pseudorandom moves during a Hex game.
     * Makes fewer than <tt>n</tt> moves if a move results in a game winner.
     * @param n an upper limit on the number of pseudorandom moves to make
     */
    @Override
    public void randomMove(int n) {
        // Add your code
    }

    /**
     * Adds a Hex cell with index idx to the Hex board.  Also adds it to 
     * the disjoint set used to detect a game winner.
     * @param idx index of the cell to add
     * @param cell the <tt>HexCell</tt> object to add
     */
    @Override
    public void addCell(int idx, HexCell cell) {
        cells[idx] = cell;
    }


    /**
     * Reinitializes the Hex board to begin a new game:
     * <ul>
     *   <li> All the Hex cells should have no associated player</li>
     *   <li> All the Hex cells should have no marks</li>
     *   <li> All the sets in the disjoint set should have cardinality one 
     *        (that is, be in there own equivalence class by themselves)</li>
     *   <li> The game's status should be "playing" </li>
     *   <li> The opening move should be the blue player's </li>
     *   <li> There should be no winning player </li>
     * </ul>
     */
    @Override
    public void reset() {
        // Add your code
    }

}
