package src;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import static src.Helper.*;

/**
 *
 * @author jeelp
 */
public class DFS {

    Cell[][] grid;
    int rows, columns;
    Point startPos;
    List<Point> endPos;
    boolean found;

    Hashtable<Integer, Boolean> visited = new Hashtable<Integer, Boolean>();

    public DFS(Cell[][] grid, int rows, int columns, Point startPos, List<Point> endPos) {
        this.grid = grid;
        this.rows = rows;
        this.columns = columns;
        this.startPos = startPos;
        this.endPos = endPos;
        this.found = false;

        int id = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                visited.put(id, Boolean.FALSE);
                id++;
            }
        }
    }

    public void DFSSearch() throws InterruptedException {

        List<Cell> path = new ArrayList<Cell>();
        Cell startNode = this.grid[this.startPos.getY()][this.startPos.getX()];
        List<Cell> endNodes = new ArrayList<Cell>();
        for (Point p : endPos) {
            endNodes.add(this.grid[p.getY()][p.getX()]);
        }

        //Visualizer
        Visualizer v = new Visualizer(grid, visited, startPos, endPos, path);

        //Run visualizer in seperate thread
        Thread t = new Thread(v);
        t.start();

        dfs(startNode, endNodes);

        if (!found) {
            System.out.print("No solution found!");
        } else {
            System.out.println(numberOfVisitedNodes(visited));
            retracePath(this.grid, startNode, endNodes, path);
        }
    }

    boolean dfs(Cell at, List<Cell> endNodes) throws InterruptedException {

        Thread.sleep(100);

        //check if any of the end nodes have been reached
        if (areCoordinatesEqual(at, endNodes)) {
            this.found = true;
            return true;
        }
        //mark the current node as visited
        visited.replace(at.getId(), Boolean.TRUE);

        //find all the neighbours of the current node
        List<Cell> neighbours = findNeighbours(this.grid, at, this.rows, this.columns);

        //dfs on all the neighbour nodes
        for (Cell c : neighbours) {

            if (!visited.get(c.getId())) {
                c.setParentNodeId(at.getId());
                //break condition
                if (dfs(c, endNodes)) {
                    return true;
                }
            }
        }

        return false;
    }

}
