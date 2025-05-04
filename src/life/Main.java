
package life;

import javax.swing.*;

// TODO(Tejas): Add a User Interface to allow editing the state with mouse

public class Main extends JFrame {

    public Main() {

        setTitle("Game of Life");
        setSize(GameOfLife.CELL_WIDTH * GameOfLife.NUMBER_OF_COLS, GameOfLife.CELL_HEIGHT * GameOfLife.NUMBER_OF_ROWS);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        GameOfLife game = new GameOfLife();
        add(game);

        setVisible(true);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(Main::new);
    }
}
