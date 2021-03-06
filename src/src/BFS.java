package src;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Queue;
import static src.Helper.*;

/**
 *
 * @author jeelp
 */
public class BFS {

    static void BFSSearch(Point startPos, List<Point> endPos, Cell[][] grid, int rows, int columns) throws InterruptedException {

        //Result of the BFS i.e. PATH
        List<Cell> final_path = new ArrayList<Cell>();

        //Queue of nodes and List of visited nodes
        Queue<Cell> queue = new ArrayDeque<Cell>();
        Hashtable<Cell, Cell> trackNodes = new Hashtable<Cell, Cell>();
        Hashtable<Integer, Boolean> visited1 = new Hashtable<Integer, Boolean>();

        //destination nodes
        List<Cell> endNodes = new ArrayList<Cell>();
        for (Point p : endPos) {
            endNodes.add(grid[p.getY()][p.getX()]);
        }

        //Visualizer
        Visualizer v = new Visualizer(grid, visited1, startPos, endPos, final_path);

        //Run visualizer in seperate thread
        Thread t = new Thread(v);
        t.start();

        Thread.sleep(100);
        //to keep track of visited nodes
        int id = 1;
        //initialize all values with false
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                visited1.put(id, Boolean.FALSE);
                id++;
            }
        }

        //found flag
        boolean found = false;

        //Starting node
        Cell startCell = grid[startPos.getY()][startPos.getX()];
        //initialising expanding node
        Cell currentNode = new Cell(-1, -1, -1, false);
        Cell destinationCell = null;
        queue.add(startCell);

        trackNodes.put(currentNode, new Cell(-1, -1, 0, false));
        while (!queue.isEmpty()) {
            //Dequeing the first node of the queue
            currentNode = queue.remove();
            visited1.replace(currentNode.getId(), Boolean.TRUE);

            //find all neighbours of visitedNode and add them in queue
            List<Cell> neighbours = findNeighbours(grid, currentNode, rows, columns);
            if (!neighbours.isEmpty()) {

                //if (currentNode.getX() == endPos.get(0).getX() && currentNode.getY() == endPos.get(0).getY()) {
                if (areCoordinatesEqual(currentNode, endNodes)) {
                    destinationCell = currentNode;
                    System.out.println("destination reached!!!");
                    found = true;
                    break;
                }

                for (Cell cell : neighbours) {
                    if (!visited1.get(cell.getId()) && !containsCell(queue, cell)) {
                        queue.add(cell);
                        visited1.replace(cell.getId(), Boolean.TRUE);
                        trackNodes.put(cell, currentNode);
                    }
                }
            }
            //By the time BFS thread is sleeping, the visualize thread displays current state of the grid
            Thread.sleep(120);  //sleep the BFS search thread
        }

        if (!found) {
            System.out.print("No solution found");
        } else {
            System.out.println(numberOfVisitedNodes(visited1));

            Cell at = destinationCell;
            List<Cell> path = new ArrayList<Cell>();
            //extract the path using trackingNodes
            while (at != null) {
                path.add(at);
                at = trackNodes.get(at);
            }
            //reversed path = final_path
            //reversing the path using the loop
            for (int i = path.size() - 1; i >= 0; i--) {
                final_path.add(path.get(i));
            }
            //setting direction of the path
            final_path = setDirection(final_path);
            //print out the path
            for (Cell _point : final_path) {
                System.out.print(_point.getDirection() + " ");
            }
        }
    }

}
