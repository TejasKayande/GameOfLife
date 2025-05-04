
package life;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// TODO(Tejas): Remove This!!!
import java.util.Random;


public class GameOfLife extends JPanel {

    // NOTE(Tejas): CELL_WIDTH and CELL_HEIGHT are in pixels
    public static final int CELL_WIDTH     = 5;
    public static final int CELL_HEIGHT    = 5;

    public static final int NUMBER_OF_ROWS = 800;
    public static final int NUMBER_OF_COLS = 800;

    enum State {
        Dead,
        Alive
    }

    private static State[][] state = new State[NUMBER_OF_ROWS][NUMBER_OF_COLS];

    public GameOfLife() {

        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLS; j++) {
                state[i][j] = State.Dead;
            }
        }

        // glider
//        state[10 - 1][10] = State.Alive;
//        state[10][10 + 1] = State.Alive;
//        state[10 + 1][10 - 1] = State.Alive;
//        state[10 + 1][10] = State.Alive;
//        state[10 + 1][10 + 1] = State.Alive;

        Random rnd = new Random();
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLS; j++) {
                boolean r = rnd.nextBoolean();
                if (r) state[i][j] = State.Alive;
            }
        }

        int delay = 100; // in millisecond
        ActionListener task = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextGeneration();
                repaint();
            }
        };

        new Timer(delay, task).start();
    }

    private int getNeighbours(int row, int col) {

        int neighbours = 0;

         for (int yoff = -1; yoff <= 1; yoff++) {
             for (int xoff = -1; xoff <= 1; xoff++){

                 int orow = yoff + row;
                 int ocol = xoff + col;

                 if (orow >= NUMBER_OF_ROWS) orow = 0;
                 if (ocol >= NUMBER_OF_COLS) ocol = 0;

                 if (orow < 0) orow = NUMBER_OF_ROWS - 1;
                 if (ocol < 0) ocol = NUMBER_OF_COLS - 1;

                 if (orow == row && ocol == col) continue;
                 if (state[orow][ocol] == State.Alive) neighbours++;
             }
         }

         return neighbours;
    }

    private void nextGeneration() {

        // Source: https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
        // The Rules of Game of Life:
        //    Any live cell with fewer than two live neighbours dies, as if by underpopulation.
        //    Any live cell with two or three live neighbours lives on to the next generation.
        //    Any live cell with more than three live neighbours dies, as if by overpopulation.
        //    Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

        State[][] next_gen = new State[NUMBER_OF_ROWS][NUMBER_OF_COLS];

        for (int row = 0; row < NUMBER_OF_ROWS; row++) {

            for (int col = 0; col < NUMBER_OF_COLS; col++) {

                int neighbours = getNeighbours(row, col);
                State current_state = state[row][col];

                if (current_state == State.Alive) {
                    next_gen[row][col] = (neighbours < 2 || neighbours > 3) ? State.Dead : State.Alive;
                } else if (current_state == State.Dead) {
                    next_gen[row][col] = (neighbours == 3) ? State.Alive : State.Dead;
                }
            }
        }

        state = next_gen;
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // drawing state
        for (int row = 0; row < NUMBER_OF_ROWS; row++) {

            for (int col = 0; col < NUMBER_OF_ROWS; col++) {

                if (state[row][col] == State.Alive) {

                    int x = col * CELL_WIDTH;
                    int y = row * CELL_HEIGHT;

                    float red   = row / (float)NUMBER_OF_COLS;
                    float green = col / (float)NUMBER_OF_ROWS;

                    Color c = new Color(red, green, 0.3f);
                    g.setColor(c);
                    g.fillRect(x, y, CELL_WIDTH, CELL_HEIGHT);
                }
            }
        }
    }
}
