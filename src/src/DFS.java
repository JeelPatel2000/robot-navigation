/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import static src.AStar.findCellInGrid;
import static src.Program.setDirection;

/**
 *
 * @author jeelp
 */
public class DFS {

    Cell[][] grid;
    int rows, columns;
    Point startPos;
    List<Point> endPos;

    Hashtable<Integer, Boolean> visited = new Hashtable<Integer, Boolean>();

    public DFS(Cell[][] grid, int rows, int columns, Point startPos, List<Point> endPos) {
        this.grid = grid;
        this.rows = rows;
        this.columns = columns;
        this.startPos = startPos;
        this.endPos = endPos;

        int id = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                visited.put(id, Boolean.FALSE);
                id++;
            }
        }
    }

    public void DFSSearch() {

        Cell startNode = this.grid[this.startPos.getY()][this.startPos.getX()];
        List<Cell> endNodes = new ArrayList<Cell>();
        for (Point p : endPos) {
            endNodes.add(this.grid[p.getY()][p.getX()]);
        }

        dfs(startNode, endNodes);

        retracePath(this.grid, startNode, endNodes);
    }

    boolean dfs(Cell at, List<Cell> endNodes) {

        //check if any of the end nodes have been reached
        if (areCoordinatesEqual(at, endNodes)) {
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

    static boolean areCoordinatesEqual(Cell c1, List<Cell> cList) {
        boolean result = false;

        for (Cell c : cList) {
            if (c1.getX() == c.getX() && c1.getY() == c.getY()) {
                result = true;
                break;
            }
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

    public static void retracePath(Cell[][] grid, Cell startNode, List<Cell> endNodes) {
        List<Cell> path = new ArrayList<Cell>();

        for (Cell endNode : endNodes) {
            if (endNode.getParentNodeId() != 0) {
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
            }
        }

        //reversed path
        List<Cell> new_path = new ArrayList<Cell>();
        //revers the path
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
