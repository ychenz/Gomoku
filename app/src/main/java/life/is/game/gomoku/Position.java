package life.is.game.gomoku;

/**
 * Created by Yuchen on 1/8/16.
 */
public class Position {
    private int X;
    private int Y;
    private int score;
    private int place_holder;//0:nothing 1:x 2:o

    private int move; //the count of the move on this position
    private boolean highlight; //highlight the current move or hint position, used in the adapter

    public Position(){
        score = 0;
        place_holder = 0;
        highlight = false;
    }

    public void setPosition(int x,int y){
        this.X = x;
        this.Y = y;
    }

    @Override
    public String toString() {
        String c = "[" + X + "," + Y + "]";
        return c;
    }

    public void put(int player){ this.place_holder = player; }

    public int getPlace_holder(){
        return this.place_holder;
    }

    public void setScore(int score){ this.score = score; }

    public int getScore(){
        return this.score;
    }

    public void setMove(int n){
        this.move = n;
        this.highlight = true;
    }

    public int getMove(){ return this.move; }

    public boolean highlighted(){
        return this.highlight;
    }

    public void setHighlight(boolean v){
        this.highlight = v;
    }

}
