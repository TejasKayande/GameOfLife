
package life;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// TODO(Tejas): Remove This!!!
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Random;

public class GameOfLife extends JPanel {

    // NOTE(Tejas): CELL_WIDTH and CELL_HEIGHT are in pixels
    public static final int CELL_WIDTH     = 10;
    public static final int CELL_HEIGHT    = 10;

    public static final int NUMBER_OF_ROWS = 100;
    public static final int NUMBER_OF_COLS = 100;

    private enum State { Dead, Alive }

    private State[][] state = new State[NUMBER_OF_ROWS][NUMBER_OF_COLS];
    private boolean paused;

    private final Random rnd = new Random();

    public GameOfLife() {

        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLS; j++) {
                state[i][j] = State.Dead;
            }
        }

        resetRandomState();

        InputMap input_map = getInputMap();
        input_map.put(KeyStroke.getKeyStroke('c'), "ClearState");
        input_map.put(KeyStroke.getKeyStroke('f'), "TogglePause");
        input_map.put(KeyStroke.getKeyStroke('r'), "ResetRandom");

        ActionMap action_map = getActionMap();
        action_map.put("ClearState", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paused = true;
                clearState();
            }
        });
        action_map.put("TogglePause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePause();
            }
        });
        action_map.put("ResetRandom", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearState();
                resetRandomState();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if (!paused) return;

                int x = e.getX();
                int y = e.getY();

                // pixel to cell
                int row = y / CELL_HEIGHT;
                int col = x / CELL_WIDTH;

                setState(State.Alive, row, col);
            }
        });

        paused = false;

        int delay = 100; // in millisecond
        ActionListener task = e -> {
            update();
            repaint();
        };

        new Timer(delay, task).start();
    }

    private void resetRandomState() {

        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLS; j++) {
                boolean r = rnd.nextBoolean();
                if (r) state[i][j] = State.Alive;
            }
        }
    }

    private void setState(State s, int row, int col) {

        if (row >= NUMBER_OF_ROWS || col >= NUMBER_OF_COLS) return;
        if (row < 0 || col < 0) return;

        state[row][col] = s;
    }

    private void clearState() {

        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLS; j++) {
                state[i][j] = State.Dead;
            }
        }
    }

    private void togglePause() {
        paused = !paused;
    }

    private void update() {

        if (!paused) nextGeneration();
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

        // The Rules of Game of Life:
        //    Any live cell with fewer than two live neighbours dies, as if by underpopulation.
        //    Any live cell with two or three live neighbours lives on to the next generation.
        //    Any live cell with more than three live neighbours dies, as if by overpopulation.
        //    Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
        // Source: https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life

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

                    Color c = new Color(red, green, 1.0f);
                    g.setColor(c);
                    g.fillRect(x, y, CELL_WIDTH, CELL_HEIGHT);
                }
            }
        }
    }
}
