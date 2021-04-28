/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import static src.Helper.*;

/**
 *
 * @author jeelp
 */
public class CUS1 {

    Cell[][] grid;
    int rows, columns;
    Point startPos;
    List<Point> endPos;

    public CUS1(Cell[][] grid, int rows, int columns, Point startPos, List<Point> endPos) {
        this.grid = grid;
        this.rows = rows;
        this.columns = columns;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public void cusSearch() throws InterruptedException {

        Hashtable<Integer, Boolean> visited = new Hashtable<Integer, Boolean>();
        int id = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                visited.put(id, Boolean.FALSE);
                id++;
            }
        }

        //result path
        List<Cell> path = new ArrayList<Cell>();

        int agent_life = 100;
        boolean found = false;
        Random random = new Random();
        Cell startNode = grid[startPos.getY()][startPos.getX()];
        List<Cell> endNodes = new ArrayList<Cell>();
        for (Point p : endPos) {
            endNodes.add(grid[p.getY()][p.getX()]);
        }

        Cell current = startNode;
        boolean stuckedInLoop = false;

        //Visualizer
        Visualizer v = new Visualizer(grid, null, visited, startPos, endPos, path);

        //Run visualizer in seperate thread
        Thread t = new Thread(v);
        t.start();

        Thread.sleep(100);

        while (agent_life != 0) {

            visited.replace(current.getId(), Boolean.TRUE);

            List<Cell> neighbours = findNeighbours(this.grid, current, this.rows, this.columns);

            Cell randomNeighbour;// = neighbours.get(random.nextInt(neighbours.size()));

            do {
                for (Cell n : neighbours) {
                    if (!visited.get(n.getId())) {
                        stuckedInLoop = false;
                        break;
                    } else {
                        stuckedInLoop = true;
                    }
                }
                randomNeighbour = neighbours.get(random.nextInt(neighbours.size()));
            } while (!stuckedInLoop && (randomNeighbour.equals(startNode) || visited.get(randomNeighbour.getId())));

            randomNeighbour.setParentNodeId(current.getId());

            if (areCoordinatesEqual(current, endNodes)) {
                found = true;
                break;
            } else if (stuckedInLoop) {
                break;
            }

            current = randomNeighbour;

            agent_life--;

            Thread.sleep(100);
        }

        if (found) {
            retracePath(this.grid, startNode, endNodes, path);
        } else if (stuckedInLoop) {
            System.out.print("Stucked in Loop");
        } else {
            System.out.print("No path found");
        }
    }

}
