// >>>>>>>> DO NOT MODIFY THIS FILE! <<<<<<<<

package hexGame;


/** 
 * The Hex game logic interface specifies the services that
 * an implementing game engine must provide. 
 */
public interface HexGameLogic {
    /**
     * Returns the player (<tt>Player.Blue</tt> or <tt>Player.Red</tt>) 
     * whose turn it is
     * @return the player whose turn it is
     */
    Player currentPlayer();
    
    /**
     * Returns the player (<tt>Player.Blue</tt> or <tt>Player.Red</tt>) 
     * who won the game or <tt>Player.None</tt> if the game is not yet complete.
     * @return the player who won (or <tt>Player.None</tt> if the game is not over
     */
    Player getWinner();

    /**
     * Claims the Hex cell at the given index for the current player
     * if the cell currently is unoccupied. If the move is possible,
     * and the move is not a winning move, the opposing player then
     * takes control of the next move.
     * @param index the cell to claim by the current player
     */
    void move(int index);

    /**
     * Positions the current player at an open cell pseudorandomly
     * chosen on the Hex game board
     */
    void randomMove();

    /**
     * Makes up to <tt>n</tt> pseudorandom moves during a Hex game.
     * Makes fewer than <tt>n</tt> moves if a move results in a game winner.
     * @param n an upper limit on the number of pseudorandom moves to make
     */
    void randomMove(int n);

    /**
     * Adds a Hex cell with index idx to the Hex board.  Also adds it to 
     * the disjoint set used to detect a game winner.
     * @param idx index of the cell to add
     * @param cell the <tt>HexCell</tt> object to add
     */
    void addCell(int idx, HexCell cell);
    
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
    void reset();
    
}
