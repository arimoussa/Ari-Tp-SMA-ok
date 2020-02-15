
public class Coordonnees {
    private int x;
    private int y;

    public Coordonnees(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Coordonnees coordonnees) {
        return coordonnees.getX() == x && coordonnees.getY() == y;
    }

    public Coordonnees clone() {
        return new Coordonnees(x, y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public String toString() {
    	
    	return "X:"+this.getX()+" / Y:"+this.y ;
    }

}