// >>>>>>>> DO NOT MODIFY THIS FILE! <<<<<<<<

package hexGame;


/**
 * The Hex game engine must interoperate with
 * the game's presentation (GUI, in the case of this
 * project). The game engine expects a type that
 * provides the following interface.
 */
public interface HexCell {
    
    /** 
     * The game engine must be able to see which 
     * player owns the Hex cell. 
     */
    Player getPlayer();

    /** 
     * The game engine must be able to change the owner 
     * of a Hex cell if it needs to.
     * @param p the player to assign to the cell.
     */
    void setPlayer(Player p);

    /**
     * The game engine must be be able to make a move on 
     * behalf of a Hex cell.
     */
    void move();

    /**
     * Place (or clear) a mark on this cell (for example, to 
     * denote it is part of a winning path)
     * @param flag
     */
    void setMark(boolean flag);
    
    /**
     * Returns the marked status of the cell.
     * @return true, if the cell is marked; otherwise, returns false.
     */
    boolean getMark();
}
