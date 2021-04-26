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
public class Wall extends Point {

    private int wide;
    private int high;

    public Wall(int wide, int high, int x, int y) {
        super(x, y);
        this.wide = wide;
        this.high = high;
    }

    public int getWide() {
        return wide;
    }

    public void setWide(int wide) {
        this.wide = wide;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    @Override
    public String toString() {
        return String.format("X: %d, Y: %d, Wide: %d, High: %d", this.getX(), this.getY(), this.getWide(), this.getHigh());
    }

}
