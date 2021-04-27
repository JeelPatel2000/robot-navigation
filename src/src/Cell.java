/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

/**
 *
 * @author jeelp
 */
public class Cell extends Point implements Comparable<Cell> {

    private int id;
    private boolean wall;
    private String direction;

    //variables for A* algorithm
    //fCost = gCost + hCost
    private int fCost;
    private int gCost;
    private int hCost;
    private int parentNodeId;

    public int getgCost() {
        return gCost;
    }

    public void setgCost(int gCost) {
        this.gCost = gCost;
    }

    public int getfCost() {
        return fCost;
    }

    public void setfCost(int fCost) {
        this.fCost = fCost;
    }

    public int gethCost() {
        return hCost;
    }

    public void sethCost(int hCost) {
        this.hCost = hCost;
    }

    public int getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(int parentNodeId) {
        this.parentNodeId = parentNodeId;
    }

    Cell(Cell cell) {
        super(cell.getX(), cell.getY());
        this.id = cell.getId();
    }

    @Override
    public String toString() {

        if (this.isWall()) {
            return String.format("* ");
        } else {
            return String.format("%d ", 0);
        }
    }

    public String printPoint() {
        return super.toString();
    }

    public Cell(int x, int y, int id, boolean wall) {
        super(x, y);
        this.wall = wall;
        this.id = id;
    }

    public void setWall(boolean is_wall) {
        this.wall = is_wall;
    }

    public boolean isWall() {
        return wall;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    //compares fCost
    @Override
    public int compareTo(Cell o) {

        return (this.getgCost() + this.gethCost()) - (o.getgCost() + o.gethCost());
    }

}
