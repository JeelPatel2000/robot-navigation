/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author jeelp
 */
public class Helper {

    //finds the neighbours of the cell
    //will find in order (UP, LEFT, DOWN, RIGHT)
    static List<Cell> findNeighbours(Cell[][] grid, Cell currentNode, int rows, int columns) {
        List<Cell> neighbours = new ArrayList<Cell>();

        int currentX = currentNode.getX();
        int currentY = currentNode.getY();
        Cell tempCell = new Cell(-1, -1, -1, false);

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
        if (columns > currentX + 1) {
            tempCell = grid[currentY][currentX + 1];
            if (!tempCell.isWall()) {
                neighbours.add(tempCell);
            }
        }

        return neighbours;
    }

    //sets the direction of the path
    static List<Cell> setDirection(List<Cell> path) {
        List<Cell> newPathWithDirection = new ArrayList<Cell>();
        for (int i = 0; i < path.size() - 1; i++) {
            Cell current_cell = path.get(i);
            Cell next_cell = path.get(i + 1);
            if (next_cell.getX() == current_cell.getX() + 1) {
                current_cell.setDirection("Right");
            } else if (next_cell.getX() == current_cell.getX() - 1) {
                current_cell.setDirection("Left");
            } else if (next_cell.getY() == current_cell.getY() + 1) {
                current_cell.setDirection("Down");
            } else if (next_cell.getY() == current_cell.getY() - 1) {
                current_cell.setDirection("Up");
            }
            newPathWithDirection.add(current_cell);
        }
        return newPathWithDirection;
    }

    //finds the cell from grid using id
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

    //checks if gievn coordinates are equal
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

    static void retracePath(Cell[][] grid, Cell startNode, List<Cell> endNodes, List<Cell> final_path) {
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
        //revers the path
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

    static boolean containsCell(Collection<Cell> list, Cell cell) {
        boolean result = false;
        for (Cell c : list) {
            if (c.getId() == cell.getId()) {
                result = true;
            }
        }
        return result;
    }
}