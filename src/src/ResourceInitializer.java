/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static src.Program.isWall;
import static src.Program.printGrid;

/**
 *
 * @author jeelp
 */
public class ResourceInitializer {

    private Cell[][] grid;
    private Point startPos;
    private List<Point> endPos;

    public Cell[][] getGrid() {
        return grid;
    }

    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }

    public Point getStartPos() {
        return startPos;
    }

    public void setStartPos(Point startPos) {
        this.startPos = startPos;
    }

    public List<Point> getEndPos() {
        return endPos;
    }

    public void setEndPos(List<Point> endPos) {
        this.endPos = endPos;
    }

    public static void Initialize(String filename) throws IOException {

        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        int counter = 0;

        ///////
        //Rows, Columns
        int rows = 0, columns = 0;
        //Start Postion
        Point startPos;
        //Finish positions
        List<Point> finish_positions = new ArrayList<Point>();
        //Array for storing wall coordinates
        List<Wall> walls = new ArrayList<Wall>();
        //Grid of Cells

        //
        ///////
        String st;
        String regex = "\\d+";
        Matcher matcher;
        Pattern pattern = Pattern.compile(regex);

        while ((st = br.readLine()) != null) {
            //Rows and Columns of grid
            if (counter == 0) {
                matcher = pattern.matcher(st);
                matcher.find();
                rows = Integer.parseInt(matcher.group());
                matcher.find();
                columns = Integer.parseInt(matcher.group());
                System.out.println(String.format("Rows: %d", rows));
                System.out.println(String.format("Columns: %d", columns));
            }
            //starting postion of the robot
            if (counter == 1) {
                matcher = pattern.matcher(st);
                matcher.find();
                int start_x = Integer.parseInt(matcher.group());
                matcher.find();
                int start_y = Integer.parseInt(matcher.group());
                startPos = new Point(start_x, start_y);
                System.out.println(String.format("Starting Postion : %s", startPos.toString()));
            }
            //array of ending positions
            if (counter == 2) {
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
            }
            //Walls
            if (counter >= 3) {

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
            }

            counter++;
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
    }

    public static void main(String[] args) throws IOException {
        Initialize("C:\\Users\\jeelp\\Documents\\NetBeansProjects\\Assignment1_COS30019\\src\\sample\\RobotNavTest.txt");
    }

}
