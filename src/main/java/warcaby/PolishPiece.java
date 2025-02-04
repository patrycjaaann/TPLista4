package warcaby;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class PolishPiece extends Piece{
    
    /**
     * @param x one of the coordinates of the middle of the circle
     * @param y one of the coordinates of the middle of the circle
     * @param r the value of circle radius
     * @param color color of a piece - can be black or white
     * @param state state of a piece - piece can become a king
     */
    PolishPiece(int x, int y, int r, Color color, State state) {
        super(x,y,r);
        setFill(color);
        oldX = x-35;
        oldY = y-35;
        if(color == Color.WHITE)
            moveDirection = -1;
        else
            moveDirection = 1;

        this.state = state;
    }
    @Override
    public void move(int newX, int newY) {
        oldX = newX*70;
        oldY = newY*70;
        setCenterX(oldX+35);
        setCenterY(oldY+35);
        if((this.getColor()==Color.WHITE && newY==0) || (this.getColor()==Color.BLACK && newY==9)) {
            state = state.changeState();
            //Image im = new Image("https://image.pngaaa.com/500/1998500-middle.png",false);
            //setFill(new ImagePattern(im));
            setStroke(Color.GOLD);
            setStrokeWidth(5);
        }
    }
}
