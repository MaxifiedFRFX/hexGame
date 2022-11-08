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
	private DisjointSet setBlue, setRed;
    
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
        setBlue = new DisjointSet(rows * rows - 1 + 5);
        setRed = new DisjointSet(rows * rows - 1 + 5);
        for (int i = 0; i <= 10; i++) {
        	setRed.union(i, (rows * rows - 1 + 3));
        	System.out.println("root: " + setRed.find(i));
        }
        /*for (int i = (rows * rows) - rows; i < rows * rows; i++) {
        	setRed.union((rows * rows - 1 + 4), i);
        	System.out.println("root: " + setRed.find());
        }*/
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
        // Add your code
        System.out.println("Cell #" + index + " selected");
        if (turn % 2 == 0) {
        	cells[index].setPlayer(Player.Blue);
        }
        else {
        	cells[index].setPlayer(Player.Red);
        }
        //setUnion(index);
        turn++;
    }
    
    public void setUnion(int index) { 
    	if (cells[index].getPlayer() == Player.Red) {
    		
    	}
    	if (cells[index].getPlayer() == cells[index - 1].getPlayer()) {
    		cells[index] = null;
    	}
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
