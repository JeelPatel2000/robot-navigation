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
import java.util.Queue;
import static src.Program.setDirection;

/**
 *
 * @author jeelp
 */
public class AStar {

    public static void initializeGrid(Cell[][] grid, int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j].setfCost(99);
                grid[i][j].setgCost(1);
                grid[i][j].sethCost(99);
            }
        }
    }

    public static void astar(Cell[][] grid, int rows, int columns, Point startPos, List<Point> endPos) {
        initializeGrid(grid, rows, columns);
        //creating a start node using the given start coordinates
        Cell startNode = grid[startPos.getY()][startPos.getX()];
        Cell endNode = grid[endPos.get(1).getY()][endPos.get(1).getX()];

        //set of nodes to be evaluated
        List<Cell> open = new ArrayList<Cell>();
        //set of nodes already evaluated
        List<Cell> closed = new ArrayList<Cell>();

        open.add(startNode);
        Cell current;

        while (open.size() != 0) {

            //sorts the array list in ascending order by fCost
            Collections.sort(open);
            //get top node from the open list i.e. node with lowest fCost
            current = open.remove(0);
            //add the current node to closed list
            closed.add(current);

            //check if current node is the target node, if so break out of loop
            if (areCoordinatesEqual(current, endNode)) {
                break;
            }
            List<Cell> neighbours = findNeighbours(grid, current, rows, columns);
            for (Cell cell : neighbours) {
                //check if the cell is in the closed list, if so then continue to the next neighbour
                if (containsCell(closed, cell)) {
                    continue;
                }

                int newCostToNeighbour = current.getgCost() + getDistance(current, cell);
                if (!containsCell(open, cell) || newCostToNeighbour < cell.getgCost()) {
                    cell.setgCost(newCostToNeighbour);
                    cell.sethCost(getDistance(cell, endNode));
                    cell.setParentNodeId(current.getId());

                    if (!containsCell(open, cell)) {
                        open.add(cell);
                    }
                }
            }
        }

        retracePath(grid, startNode, endNode);
    }

    public static void retracePath(Cell[][] grid, Cell startNode, Cell endNode) {
        List<Cell> path = new ArrayList<Cell>();
        boolean success = true;
        Cell currentNode = endNode;

        while (true) {
            path.add(currentNode);
            int id = currentNode.getParentNodeId();
            currentNode = findCellInGrid(grid, id);
            if (currentNode.getId() == startNode.getId()) {
                path.add(startNode);
                break;
            }

        }

        if (success) {
            //reversed path
            List<Cell> new_path = new ArrayList<Cell>();
            //reversing the path using the loop
            for (int i = path.size() - 1; i >= 0; i--) {
                new_path.add(path.get(i));
            }
            //setting direction of the path
            new_path = setDirection(new_path);
            //print out the path
            for (Cell _point : new_path) {
                System.out.print(_point.getDirection() + " ");
            }
        }
    }

    public static Cell findCellInGrid(Cell[][] grid, int id) {
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

    //manhattan distance
    public static int getDistance(Cell c1, Cell c2) {
        return (Math.abs(c2.getX() - c1.getX()) + Math.abs(c2.getY() - c1.getY()));
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

    public static boolean areCoordinatesEqual(Cell c1, Cell c2) {
        boolean result = false;

        if (c1.getX() == c2.getX() && c1.getY() == c2.getY()) {
            result = true;
        }

        return result;
    }

    static List<Cell> findNeighbours(Cell[][] grid, Cell currentNode, int rows, int columns) {
        List<Cell> neighbours = new ArrayList<Cell>();

        int currentX = currentNode.getX();
        int currentY = currentNode.getY();
        Cell tempCell = new Cell(-1, -1, -1, false);

        if (columns > currentX + 1) {
            tempCell = grid[currentY][currentX + 1];
            if (!tempCell.isWall()) {
                neighbours.add(tempCell);
            }
        }
        if (0 <= currentY - 1) {
            tempCell = grid[currentY - 1][currentX];
            if (!tempCell.isWall()) {
                neighbours.add(tempCell);
            }
        }
        if (0 <= currentX - 1) {
            tempCell = grid[currentY][currentX - 1];
            if (!tempCell.isWall()) {
                neighbours.add(tempCell);
            }
        }
        if (rows > currentY + 1) {
            tempCell = grid[currentY + 1][currentX];
            if (!tempCell.isWall()) {
                neighbours.add(tempCell);
            }
        }

        return neighbours;
    }
}
