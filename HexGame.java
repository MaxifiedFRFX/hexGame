// >>>>>>>> DO NOT MODIFY THIS FILE! <<<<<<<<

package hexGame;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


/**
 * The launching point for the Hex game application
 */
public class HexGame {
    
    private static final int ROWS = 11;
    private static final int NUM_CELLS = ROWS * ROWS;
    
    /**
     * Provides the graphical interface for a Hex game.
     */
    @SuppressWarnings("serial")
    private static class HexGamePanel extends JPanel {

        /** The game engine that controls the flow of the game and
            determines a winner. */
        private static HexGameLogic engine = new HexGameEngine(ROWS);

        /**
         * The number of rows in the Hex game determines the scaling factor
         * (more rows mean smaller cells).
         */
        public static final int WINDOW_WIDTH = 850;
        
        /**
         * A magic scaling factor that allows the playing field to look good 
         * with the given window size.
         */
        public static final double SCALE = WINDOW_WIDTH / (3.2 * ROWS);

        /**
         * The visual representation of a Hex game cell. Draws the hexagonal
         * cell, provides visual feedback to users (players), and cooperates with
         * the Hex game engine to provide a graphical user interface to a Hex game.
         */
        static class VisualHexCell extends JPanel implements HexCell {
            /** This cell's index within the Hex board. */
            private int index; 
            /** The player owning this cell */
            private Player player; 

            /** 
             * True if the mouse cursor is over the active part of a 
             * visual Hex cell; otherwise, false.
             */
            private boolean mouseOver;
            
            /**
             * Precompute some trigonometric functions to speed up the runtime.
             * These are static, so they are precomputed once when the program
             * begins running.
             */
            private static final double SIN30 = 0.5;
            private static final double COS30 = 0.866;

            /**
             * Scaled unit circle point positions for the hexagon vertices.
             * These are static, so they are precomputed once when the program
             * begins running.
             */
            private static final Point TOP_VERTEX = new Point((int) Math.round(0.0 * SCALE), (int) Math.round(1.0 * SCALE));
            private static final Point UPPER_LEFT_VERTEX = new Point((int) Math.round(-COS30 * SCALE),
                                                                     (int) Math.round(SIN30 * SCALE));
            private static final Point LOWER_LEFT_VERTEX = new Point((int) Math.round(-COS30 * SCALE),
                                                                     (int) Math.round(-SIN30 * SCALE));
            private static final Point BOTTOM_VERTEX = new Point((int) Math.round(0.0 * SCALE),
                                                                     (int) Math.round(-1.0 * SCALE));
            private static final Point LOWER_RIGHT_VERTEX = new Point((int) Math.round(COS30 * SCALE),
                                                                     (int) Math.round(-SIN30 * SCALE));
            private static final Point UPPER_RIGHT_VERTEX = new Point((int) Math.round(COS30 * SCALE),
                                                                     (int) Math.round(SIN30 * SCALE));
            /** Width of a hexagonal cell. */
            private static final double WIDTH = 2 * COS30 * SCALE;
            /**
             *  Width and height of the graphical component bounding box of a 
             *  hexagonal cell.
             */
            private static final int CELL_SIZE = TOP_VERTEX.y - BOTTOM_VERTEX.y;

            private static final double START_X = SCALE * ROWS;
            private static final double START_Y = 70.0;
            
            /** The color of the cell for a provisional move for the blue player. */
            private static final Color LIGHT_BLUE = new Color(200, 200, 255);
            
            /** The color of the cell for a provisional move for the red player. */
            private static final Color LIGHT_RED = new Color(255, 200, 200);
            
            /** The shape of a visual cell. */
            private Polygon hexagon;

            /**
             * Map a Hex grid cell address (row, column) to a point (x, y)
             * in the graphics window. Returns a graphical point.
             * @param row a row on the playing board.
             * @param column a column on the playing board.
             * @return an (<i>x</i>, <i>y</i>) point representing the cell's visual center.
             */
            private static Point locationToPoint(int row, int column) {
                double x = START_X + WIDTH * column + START_Y - 0.5 * WIDTH * (ROWS - row), 
                       y = START_Y + 1.5 * SCALE * row;
                return new Point((int) Math.round(x), (int) Math.round(y));
            }


            /**
             * Initializes a visual Hex cell with a given index.
             * @param idx the index for the new Hex cell.
             */
            public VisualHexCell(int idx) {
                setBackground(Color.WHITE);
                setOpaque(false);
                index = idx;
                player = Player.None;
                mouseOver = false;
                Point p = locationToPoint(index / ROWS, index % ROWS);
                int center_x = p.x;
                int center_y = p.y;
                setBounds(center_x - CELL_SIZE/2, center_y - CELL_SIZE/2, CELL_SIZE, CELL_SIZE);
                // Register this new graphical Hex game cell with the game engine.
                engine.addCell(index, this);

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (engine.getWinner() == Player.None && player == Player.None) {
                            move();
                        }
                        getParent().repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        mouseOver = false;
                        repaint();
                    }
                    
                });

                addMouseMotionListener(new MouseMotionAdapter() {
                    // Ensures a cell knows when the mouse cursor is over it.
                    // Enables the GUI to provide feedback to the user about
                    // which cell may be chosen and which player would be its
                    // potential owner.
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        if (hexagon.contains(e.getX(), e.getY())) {
                            mouseOver = true;
                        } else {
                            mouseOver = false;
                        }
                        repaint();
                    }
                });

                // (x, y) points for the vertices of a hexagon.
                int[] xVertices = new int[7];
                int[] yVertices = new int[7];
                center_x = CELL_SIZE/2;
                center_y = CELL_SIZE/2;
                xVertices[0] = TOP_VERTEX.x + center_x;
                yVertices[0] = TOP_VERTEX.y + center_y;
                xVertices[1] = UPPER_LEFT_VERTEX.x + center_x;
                yVertices[1] = UPPER_LEFT_VERTEX.y + center_y;
                xVertices[2] = LOWER_LEFT_VERTEX.x + center_x;
                yVertices[2] = LOWER_LEFT_VERTEX.y + center_y;
                xVertices[3] = BOTTOM_VERTEX.x + center_x;
                yVertices[3] = BOTTOM_VERTEX.y + center_y;
                xVertices[4] = LOWER_RIGHT_VERTEX.x + center_x;
                yVertices[4] = LOWER_RIGHT_VERTEX.y + center_y;
                xVertices[5] = UPPER_RIGHT_VERTEX.x + center_x;
                yVertices[5] = UPPER_RIGHT_VERTEX.y + center_y;
                xVertices[6] = TOP_VERTEX.x + center_x;
                yVertices[6] = TOP_VERTEX.y + center_y;

                hexagon = new Polygon(xVertices, yVertices, 7);

                setVisible(true);
            }


            /**
             *  Returns this cell's current owner (<tt>Player.None</tt> if no one).
             */
            @Override
            public Player getPlayer() {
                return player;
            }

            
            /**
             *  Assigns an owner (blue or red) to this cell.
             * @param p the player to assign as owner of this cell.
             */
            @Override
            public void setPlayer(Player p) {
                player = p;
            }

            
            /**
             * Renders the cell within a graphical window.
             * 
             * @param g the graphics context object.
             */
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                boolean gameOver = (engine.getWinner() != Player.None);
                switch (player) {
                    case Blue:
                        g.setColor(Color.BLUE);
                        break;
                    case Red:
                        g.setColor(Color.RED);
                        break;
                    default:
                        if (mouseOver) {
                            if (engine.currentPlayer() == Player.Blue) {
                                g.setColor(LIGHT_BLUE);
                            } else {
                                g.setColor(LIGHT_RED);
                            }
                        } else {
                            g.setColor(Color.WHITE);
                        }
                        break;
                }
                g.fillPolygon(hexagon);

                // Draw Hex cell frame.
                g.setColor(Color.BLACK);
                g.drawPolygon(hexagon);
                
                // Print the cell's index
                int x = 14;
                if (index < 100) {
                    x = 17;
                }
                if (index < 10) {
                    x = 20;
                }
                g.drawString(Integer.toString(index), x, 28);
            }


            /**
             * Assigns this cell's owner to the current player, but only if
             * the cell does not have a pre-existing owner.
             */
            @Override
            public void move() {
                if (player == Player.None) {  // Move only if cell not occupied
                    // player = engine.current_player();
                    engine.move(index);
                    Player winner = engine.getWinner();
                    if (winner != Player.None) {
                        JFrame window = (JFrame) SwingUtilities.windowForComponent(this);
                        if (winner == Player.Blue) {
                            System.out.println("Blue wins!");
                            window.setTitle("Blue wins!");
                        } else {
                            System.out.println("Red wins!");
                            window.setTitle("Red wins!");
                            
                        }
                    }
                }
            }

            
            /**
             * Sets the mark on this cell according to the <tt>flag<tt> parameter.
             */
            @Override
            public void setMark(boolean flag) {
                // Unused at this point
            }
        
            
            /**
             * Returns the marked status of the cell.
             * @return true, if the cell is marked; otherwise, returns false.
             */
            @Override
            public boolean getMark() {
                return false;  // Unused at this point
            }
        }

        
        /**
         * Constructor initializes the graphical interface.
         */
        public HexGamePanel() {
            setPreferredSize(new Dimension(Math.round(WINDOW_WIDTH), Math.round((int)(0.6*WINDOW_WIDTH))));
            setBackground(Color.WHITE);
            setLayout(null);
            for (int idx = 0; idx < NUM_CELLS; idx++) {
                VisualHexCell cell = new VisualHexCell(idx);
                add(cell);
            }
            initializeBorders();
            setFocusable(true);

            // Provides user access via the keyboard to special functions
            // like random moves.
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyChar()) {
                    case 'm':
                    case 'M':
                        engine.randomMove();
                        break;
                    case 'r':
                    case 'R':
                        engine.randomMove(10);
                        break;
                    case 'f':
                    case 'F':
                        // Do not uncomment the following util you 
                        // you are sure everything is working; otherwise,
                        // may get into an infinite loop.
                        while (engine.getWinner() == Player.None) {
                            engine.randomMove();
                        }
                        break;
                    case 27: // Ecsape key
                        clearBoard();
                        break;
                    }
                    repaint();
                    System.out.println("Pressed a key");
                }
            });
        }
        
        
        /**
         * Graphical event manager calls this method when this
         * panel is added to a parent window frame.
         */
        @Override
        public void addNotify() {
            super.addNotify();
            requestFocusInWindow();
        }
        

        private static final int HIGH = ROWS - 1;
        
        /** The bases on each of the four edges */
        private Polygon border1;
        private Polygon border2;
        private Polygon border3;
        private Polygon border4;
        
        /** Creates the shapes of the bases (borders) */
        private void initializeBorders() {
            Point p1 = VisualHexCell.locationToPoint(0, 0);
            Point p2 = VisualHexCell.locationToPoint(HIGH, 0);
            Point p3 = VisualHexCell.locationToPoint(ROWS, -1);
            Point p4 = VisualHexCell.locationToPoint(-1, -1);
            border1 = new Polygon(new int[] { p1.x, p2.x, p3.x, p4.x }, 
                                  new int[] { p1.y, p2.y, p3.y, p4.y }, 4);
            p1 = VisualHexCell.locationToPoint(0, HIGH);
            p2 = VisualHexCell.locationToPoint(HIGH, HIGH);
            p3 = VisualHexCell.locationToPoint(ROWS, ROWS);
            p4 = VisualHexCell.locationToPoint(-1, ROWS);
            border2 = new Polygon(new int[] { p1.x, p2.x, p3.x, p4.x }, 
                                  new int[] { p1.y, p2.y, p3.y, p4.y }, 4);
            p1 = VisualHexCell.locationToPoint(0, HIGH);
            p2 = VisualHexCell.locationToPoint(-1, ROWS);
            p3 = VisualHexCell.locationToPoint(-1, -1);
            p4 = VisualHexCell.locationToPoint(0, 0);
            border3 = new Polygon(new int[] { p1.x, p2.x, p3.x, p4.x }, 
                                  new int[] { p1.y, p2.y, p3.y, p4.y }, 4);
            p1 = VisualHexCell.locationToPoint(HIGH, 0);
            p2 = VisualHexCell.locationToPoint(ROWS, -1);
            p3 = VisualHexCell.locationToPoint(ROWS, ROWS);
            p4 = VisualHexCell.locationToPoint(HIGH, HIGH);
            border4 = new Polygon(new int[] { p1.x, p2.x, p3.x, p4.x }, 
                                  new int[] { p1.y, p2.y, p3.y, p4.y }, 4);
        }


        /**
         * Draws the borders for the Hex game (the individual Hex cells
         * draw themselves).
         * @param g the graphical context object.
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLUE);
            g.fillPolygon(border1);
            g.fillPolygon(border2);
            g.setColor(Color.RED);
            g.fillPolygon(border3);
            g.fillPolygon(border4);
        }

        
        /**
         * Resets the Hex board in preparation for a new game.
         * Resets the game engine.
         */
        void clearBoard() {
            engine.reset(); // Reinitializes the game state
            JFrame window = (JFrame) SwingUtilities.windowForComponent(this);
            if (window != null) {
                window.setTitle("Hex Game");
            }
            repaint();
        }
    }
    
    
    @SuppressWarnings("serial")
    private static class ApplicationWindow extends JFrame {
        /**
         * Creates a new application window. The client provides application-specific
         * information.
         * 
         * @param title  the title of the window; appears in the window's title bar
         * @param x      the <i>x</i> coordinate of the window's top-left corner
         * @param y      the <i>y</i> coordinate of the window's top-left corner
         * @param panel  the application's drawing area and application-specific functionality
         */
        public ApplicationWindow(String title, int x, int y, JComponent panel) {
            super(title);
            setLocation(x, y);
            getContentPane().add(panel);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            pack();
            setVisible(true);
        }
    }
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> 
            new ApplicationWindow("Hex Game", 100, 100, new HexGamePanel()));  
    }
}
