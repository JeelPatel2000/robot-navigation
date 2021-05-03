package src;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Hashtable;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Visualizer implements Runnable {

    public MyCanvas canvas;
    public Frame f;
    public List<Cell> path;

    public Visualizer(Cell[][] grid, Hashtable<Integer, Boolean> visitedNodes, Point startNode, List<Point> endNodes, List<Cell> path) throws InterruptedException {

        this.path = path;

        int rows = grid.length;
        int columns = grid[0].length;
        int cellSize = 80;

        this.canvas = new MyCanvas(columns * cellSize, rows * cellSize);
        canvas.grid = grid;
        canvas.visitedNodes = visitedNodes;
        canvas.cellSize = cellSize;
        canvas.startNode = startNode;
        canvas.endNodes = endNodes;
        canvas.path = path;
        canvas.path_found = false;

        canvas.setSize(columns * cellSize, rows * cellSize);

        f = new Frame("Canvas Example");
        f.add(canvas);
        f.setLayout(null);
        f.setSize(columns * cellSize, rows * cellSize);
        f.setResizable(false);

    }

    @Override
    public void run() {

        f.setVisible(true);
        //terminates the program when clicked the close button
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                f.dispose();
                System.exit(0);
            }
        });

        while (true) {

            if (!path.isEmpty()) {
                canvas.path_found = true;
                break;
            }

            canvas.repaint();

            try {
                Thread.sleep(70);
            } catch (InterruptedException ex) {
                Logger.getLogger(Visualizer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        canvas.repaint();

    }

}

class MyCanvas extends Canvas {

    public Queue<Cell> queue;
    public Hashtable<Integer, Boolean> visitedNodes;
    public Cell[][] grid;
    public Point startNode;
    public List<Point> endNodes;
    public List<Cell> path;
    public boolean path_found;
    int cellSize;
    int WIDTH;
    int HEIGHT;

    public MyCanvas(int w, int h) {
        setBackground(Color.WHITE);
        setSize(w, h);
    }

    @Override
    public void paint(Graphics g) {

        //draw the grid
        int rows = grid.length;
        int columns = grid[0].length;
        WIDTH = columns * cellSize;
        HEIGHT = rows * cellSize;

        // fill the visited nodes with cyan
        for (int id : visitedNodes.keySet()) {
            if (visitedNodes.get(id)) {
                Cell node = findCellInGrid(this.grid, id);
                drawRectangle(g, node.getX() * this.cellSize, node.getY() * this.cellSize, this.cellSize, Color.cyan);
            }
        }

        //fill the wall nodes
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (grid[i][j].isWall()) {
                    drawRectangle(g, j * this.cellSize, i * this.cellSize, this.cellSize, Color.LIGHT_GRAY);
                }
            }
        }

        //fill the stating position with green
        drawRectangle(g, this.startNode.getX() * this.cellSize, this.startNode.getY() * this.cellSize, this.cellSize, Color.RED);

        //fill the destination nodes with green
        for (Point p : endNodes) {
            drawRectangle(g, p.getX() * this.cellSize, p.getY() * this.cellSize, this.cellSize, Color.GREEN);
        }

        //change the stroke color to black
        g.setColor(Color.BLACK);

        //draw the horizontal lines
        for (int i = 1; i < rows; i++) {
            g.drawLine(0, i * HEIGHT / rows, WIDTH, i * HEIGHT / rows);
        }
        //draw the vertical lines
        for (int i = 1; i < columns; i++) {
            g.drawLine(i * WIDTH / columns, 0, i * WIDTH / columns, HEIGHT);
        }

        //draw out the path if path is found
        if (this.path_found) {
            for (Cell c : path) {
                drawRectangle(g, c.getX() * this.cellSize, c.getY() * this.cellSize, this.cellSize, Color.YELLOW);
                try {
                    Thread.sleep(150);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MyCanvas.class.getName()).log(Level.SEVERE, null, ex);
                }

                //fill the stating position with green
                drawRectangle(g, this.startNode.getX() * this.cellSize, this.startNode.getY() * this.cellSize, this.cellSize, Color.RED);

                //fill the destination nodes with green
                for (Point p : endNodes) {
                    drawRectangle(g, p.getX() * this.cellSize, p.getY() * this.cellSize, this.cellSize, Color.GREEN);
                }

                g.setColor(Color.BLACK);
                //draw the horizontal lines
                for (int i = 1; i < rows; i++) {
                    g.drawLine(0, i * HEIGHT / rows, WIDTH, i * HEIGHT / rows);
                }
                //draw the vertical lines
                for (int i = 1; i < columns; i++) {
                    g.drawLine(i * WIDTH / columns, 0, i * WIDTH / columns, HEIGHT);
                }

            }
        }

    }

    void drawRectangle(Graphics g, int x, int y, int cellSize, Color c) {
        g.setColor(c);
        g.fillRect(x, y, cellSize, cellSize);
    }

    static Cell findCellInGrid(Cell[][] grid, int id) {
        Cell result = null;
        boolean found = false;
        for (int i = 0; i < grid.length && !found; i++) {
            for (int j = 0; j < grid[i].length && !found; j++) {
                if (grid[i][j].getId() == id) {
                    result = grid[i][j];
                    found = true;
                }
            }
        }

        return result;
    }

}
