/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
[5,11]    //hight,width
(0,1)
(7,0) | (10,3)
(2,0,2,2)
(8,0,1,2)
(10,0,1,1)
(2,3,1,2)
(3,4,3,1)
(9,3,1,1)
(8,4,2,1)

 */
package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jeelp
 */
public class Program {

    public static void main(String[] args) throws Exception {

        String filename = args[0];

        File file = new File(filename);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        //first line is number of [columns,rows]
        st = br.readLine();

        String regex = "\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(st);

        matcher.find();
        int rows = Integer.parseInt(matcher.group());
        matcher.find();
        int columns = Integer.parseInt(matcher.group());

        System.out.println(String.format("Rows: %d", rows));
        System.out.println(String.format("Columns: %d", columns));

        // find the starting postion
        st = br.readLine();

        matcher = pattern.matcher(st);
        matcher.find();
        int start_x = Integer.parseInt(matcher.group());
        matcher.find();
        int start_y = Integer.parseInt(matcher.group());

        Point startPos = new Point(start_x, start_y);

        System.out.println(String.format("Starting Postion : %s", startPos.toString()));

        //array of ending positions
        List<Point> finish_positions = new ArrayList<Point>();
        // finds the end position
        st = br.readLine();

        matcher = pattern.matcher(st);

        while (matcher.find()) {
            int _x = Integer.parseInt(matcher.group());
            matcher.find();
            int _y = Integer.parseInt(matcher.group());

            finish_positions.add(new Point(_x, _y));
        }

        for (int i = 0; i < finish_positions.size(); i++) {
            System.out.println(String.format("End position %d : %s", i + 1, finish_positions.get(i).toString()));
        }

        //Array for storing wall coordinates
        List<Wall> walls = new ArrayList<Wall>();
        st = br.readLine();
        while (st != null) {
            matcher = pattern.matcher(st);

            while (matcher.find()) {
                int _x = Integer.parseInt(matcher.group());
                matcher.find();
                int _y = Integer.parseInt(matcher.group());
                matcher.find();
                int _wide = Integer.parseInt(matcher.group());
                matcher.find();
                int _high = Integer.parseInt(matcher.group());

                walls.add(new Wall(_wide, _high, _x, _y));
            }

            st = br.readLine();
        }

        for (int i = 0; i < walls.size(); i++) {
            System.out.println(walls.get(i).toString());
        }

        //empty grid of cells
        Cell[][] grid = new Cell[rows][columns];
        int id = 1;
        //creates grid of cells
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {

                if (isWall(j, i, walls)) {
                    grid[i][j] = new Cell(j, i, id, true);
                } else {
                    grid[i][j] = new Cell(j, i, id, false);
                }
                id++;
            }
        }

        //prints the Grid
        printGrid(rows, columns, grid);

        //BFS Search
        BFSSearch(startPos, finish_positions, grid, rows, columns);
        //A star search
//        AStar.astar(grid, rows, columns, startPos, finish_positions);
        //DFS Search
//        DFS dfsAlgo = new DFS(grid, rows, columns, startPos, finish_positions);
//        dfsAlgo.DFSSearch();
//        Visualizer v = new Visualizer();

    }

    //finds if the given cell is wall or empty space
    static boolean isWall(int xPos, int yPos, List<Wall> walls) {
        boolean result = false;

        for (int i = 0; i < walls.size(); i++) {
            Wall _wall = walls.get(i);

            if (xPos >= _wall.getX() && yPos >= _wall.getY() && xPos < (_wall.getX() + _wall.getWide()) && yPos < (_wall.getY() + _wall.getHigh())) {
                result = true;
                break;
            }
        }

        return result;
    }

    //prints the Grid
    static void printGrid(int rows, int columns, Cell[][] grid) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(grid[i][j].toString());
            }
            System.out.println();
        }
    }

    static void BFSSearch(Point startPos, List<Point> endPos, Cell[][] grid, int rows, int columns) throws InterruptedException {

        //Result of the BFS i.e. PATH
        List<Cell> final_path = new ArrayList<Cell>();

        //Queue of nodes and List of visited nodes
        Queue<Cell> queue = new ArrayDeque<Cell>();
        Hashtable<Cell, Cell> trackNodes = new Hashtable<Cell, Cell>();
        Hashtable<Integer, Boolean> visited1 = new Hashtable<Integer, Boolean>();

        //Visualizer
        Visualizer v = new Visualizer(grid, queue, visited1, startPos, endPos, final_path);

        //Run visualizer in seperate thread
        Thread t = new Thread(v);
        t.start();

        Thread.sleep(250);
        //to keep track of visited nodes
        int id = 1;
        //initialize all values with false
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                visited1.put(id, Boolean.FALSE);
                id++;
            }
        }

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
                for (Cell cell : neighbours) {
                    if (!visited1.get(cell.getId()) && cellNotInQueue(queue, cell)) {
                        queue.add(cell);
                        visited1.replace(cell.getId(), Boolean.TRUE);
                        trackNodes.put(cell, currentNode);
                    }
                }

                if (currentNode.getX() == endPos.get(0).getX() && currentNode.getY() == endPos.get(0).getY()) {
                    destinationCell = currentNode;
                    System.out.println("destination reached!!!");
                    break;
                }
            }

            Thread.sleep(250);
            //Visualize the current state of the grid
        }

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

    static boolean cellNotInQueue(Queue<Cell> queue, Cell cell) {
        boolean result = true;

        for (Cell c : queue) {
            if (c.getId() == cell.getId()) {
                result = false;
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

}
