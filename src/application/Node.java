package application;

public class Node implements Cloneable{
    public String color;
    public int bottom, rightTop, rightBottom;

    public Node() {
        bottom = rightBottom = rightTop = 0;
        color = "";
    }
    
    public Node(Node nod) {
    	this.color = nod.getColor();
    	this.bottom = nod.getX();
    	this.rightTop = nod.getY();
    	this.rightBottom = nod.getZ();
    }
 
    
    public Node(String color, int bottom, int rightTop, int rightBottom) {
        this.color = color;
        this.bottom = bottom;
        this.rightTop = rightTop;
        this.rightBottom = rightBottom;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public String getKey()
    {
    	return Integer.toString(bottom) + "," + Integer.toString(rightTop)+"," + Integer.toString(rightBottom);
    }
    public int getX() {
        return bottom;
    }

    public void setX(int bottom) {
        this.bottom = bottom;
    }

    public int getY() {
        return rightTop;
    }

    public void setY(int rightTop) {
        this.rightTop = rightTop;
    }

    public int getZ() {
        return rightBottom;
    }

    public void setZ(int rightBottom) {
        this.rightBottom = rightBottom;
    }
}


