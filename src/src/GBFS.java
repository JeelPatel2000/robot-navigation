/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import static src.Helper.areCoordinatesEqual;
import static src.Helper.findNeighbours;
import static src.Helper.retracePath;

/**
 *
 * @author jeelp
 */
public class GBFS {

    public static void initializeGrid(Cell[][] grid, int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j].setfCost(0);
                grid[i][j].setgCost(0);
                grid[i][j].sethCost(99);
            }
        }
    }

    public static void gbfs(Cell[][] grid, int rows, int columns, Point startPos, List<Point> endPos) throws InterruptedException {
        initializeGrid(grid, rows, columns);
        //creating a start node using the given start coordinates
        Cell startNode = grid[startPos.getY()][startPos.getX()];
        List<Cell> endNodes = new ArrayList<Cell>();
        for (Point p : endPos) {
            endNodes.add(grid[p.getY()][p.getX()]);
        }
        Cell endNode = endNodes.get(0);

        //result path
        List<Cell> path = new ArrayList<Cell>();

        //Hashtable for storing visited nodes
        Hashtable<Integer, Boolean> visited = new Hashtable<Integer, Boolean>();
        int id = 1;
        //initialize all values with false
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                visited.put(id, Boolean.FALSE);
                id++;
            }
        }

        //set of nodes to be evaluated
        List<Cell> open = new ArrayList<Cell>();
        //set of nodes already evaluated
        List<Cell> closed = new ArrayList<Cell>();

        //Visualizer
        Visualizer v = new Visualizer(grid, null, visited, startPos, endPos, path);

        //Run visualizer in seperate thread
        Thread t = new Thread(v);
        t.start();

        Thread.sleep(100);

        //add the start node to the open list
        open.add(startNode);
        Cell current;

        while (open.size() != 0) {

            //sorts the array list in ascending order by fCost
            Collections.sort(open);
            //get top node from the open list i.e. node with lowest fCost
            current = open.remove(0);
            //add the current node to closed list
            closed.add(current);
            //same as above statement just keeps track of visited nodes using cell id
            visited.replace(current.getId(), Boolean.TRUE);

            //check if current node is the target node, if so break out of loop
            if (areCoordinatesEqual(current, endNodes)) {
                break;
            }
            List<Cell> neighbours = findNeighbours(grid, current, rows, columns);
            for (Cell cell : neighbours) {
                //check if the cell is in the closed list, if so then continue to the next neighbour
                if (containsCell(closed, cell)) {
                    continue;
                }

                int newCostToNeighbour = getDistance(current, cell);
                if (!containsCell(open, cell)) {
//                    cell.setgCost(newCostToNeighbour);
                    cell.sethCost(getDistance(cell, endNode));
                    cell.setParentNodeId(current.getId());

                    if (!containsCell(open, cell)) {
                        open.add(cell);
                    }
                }
            }

            //Visualize the current state of the grid
            Thread.sleep(100);
        }

        retracePath(grid, startNode, endNodes, path);

    }

    //manhattan distance
    public static int getDistance(Cell c1, Cell c2) {
        return (Math.abs(c2.getX() - c1.getX()) + Math.abs(c2.getY() - c1.getY()));
//        return (int) Math.sqrt(Math.pow(c2.getX() - c1.getX(), 2) + Math.pow(c2.getY() - c1.getY(), 2));
    }

    /**
     *
     * @return true if the given cell is present in the list
     */
    public static boolean containsCell(List<Cell> list, Cell cell) {
        boolean result = false;
        for (Cell c : list) {
            if (c.getId() == cell.getId()) {
                result = true;
            }
        }
        return result;
    }
}
