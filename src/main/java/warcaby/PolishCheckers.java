package warcaby;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.text.Font;

import java.util.List;

public class PolishCheckers extends Application implements Checkers {

    private Group tilesGroup = new Group();
    private Group piecesGroup = new Group();
    private Square[][] tiles = new Square[10][10];
    
    // to pole jest wykorzystywane przy uruchamianiu aplikacji z poziomu klasy LaunchingCheckers
    static Stage classStage = new Stage();

    /*
     * tutaj tworzy się plansza do gry
     */
    @Override
    public void start(Stage stage) {
        classStage = stage;

        AnchorPane pane = new AnchorPane();
        Button button = new Button("Opponent's turn");
        button.setFont(Font.font("Times New Roman", 20));
        pane.getChildren().addAll(tilesGroup, piecesGroup, button);
        AnchorPane.setBottomAnchor(button, 0.0);

        int x = 0, y = 0;
        for(int i = 0; i<10; i++) {
            for(int j = 0; j<10; j++) {
                if((i+j)%2==0) {
                    Square square = new Square(x,y,70,70,Color.WHEAT);
                    tiles[j][i] = square;
                    tilesGroup.getChildren().add(square);
                    x += 70;
                }
                else {
                    if(i<4) {
                        Piece piece = createPiece(x+35, y+35, 30, Color.BLACK, new PolishManState());
                        Square square = new Square(x,y,70,70,Color.BROWN);
                        square.setPiece(piece);
                        tiles[j][i] = square;
                        tilesGroup.getChildren().add(square);
                        piecesGroup.getChildren().add(piece);
                    }
                    else if(i == 4 || i == 5) {
                        Square square = new Square(x,y,70,70,Color.BROWN);
                        tiles[j][i] = square;
                        tilesGroup.getChildren().add(square);
                    }
                    else if(i>5) {
                        Piece piece = createPiece(x+35, y+35, 30, Color.WHITE, new PolishManState());
                        Square square = new Square(x,y,70,70,Color.BROWN);
                        square.setPiece(piece);
                        tiles[j][i] = square;
                        tilesGroup.getChildren().add(square);
                        piecesGroup.getChildren().add(piece);
                    }
                    x += 70;
                }
            }
            x = 0;
            y += 70;
        }

        Scene scene = new Scene(pane,700,736);
        stage.setTitle("Polish checkers");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * metoda, która wylicza, w którym kwadracie znajduje się podana liczba
     * @param x współrzędna punktu z planszy
     * @return numer kwadratu, w którym znajduje się podana liczba
     */
    private int getScenePlace(double x) {
        return (int)x/70;
    }

    /**
     * @param piece a piece whose move legality we want to check
     * @param x a coordinate of the left top corner of a square where we want to place the piece
     * @param y a coordinate of the left top corner of a square where we want to place the piece
     * @return true if we can move or false in opposite case
     */
    private boolean checkMove(Piece piece, int x, int y) {
        List<Square> availableMoves = piece.getAvailibleMoves(tiles);
        if(availableMoves.contains(tiles[x][y]))
            return true;
        else
            return false;
    }

    /**
     * in this method a new piece is created with requested features
     * and some actions are set to this piece
     * @param x a x coordinate of the middle of the piece
     * @param y a y coordinate of the middle of the piece
     * @param r a value of piece radius
     * @param color a color of a piece
     * @param state a state of a piece
     * @return created piece
     */
    private Piece createPiece(int x, int y, int r, Color color, State state) {
        Piece piece = new PolishPiece(x,y,r,color,state);

        /*
         * metoda aktywowana, kiedy puszczamy pionek
         * ona odpowiada za umieszczenie pionka w odpowiednim miejscu
         * zgodnie z wykonanym przez gracza ruchem
         */
        piece.setOnMouseReleased(e -> {
            int newX = getScenePlace(e.getSceneX());
            int newY = getScenePlace(e.getSceneY());

            if(newX<0 || newY<0 || newX>9 || newY>9 || !checkMove(piece,newX,newY))
                piece.notMove();
            else {
                int startX = getScenePlace(piece.getOldX());
                    int startY = getScenePlace(piece.getOldY());
                    if(checkMove(piece,newX,newY) && piece.getState() instanceof PolishManState) {
                        if(Math.abs(newX-startX)==1) {
                            piece.move(newX,newY);
                            tiles[startX][startY].setPiece(null);
                            tiles[newX][newY].setPiece(piece);
                        }
                        else {
                            piece.move(newX,newY);
                            tiles[startX][startY].setPiece(null);
                            tiles[newX][newY].setPiece(piece);
                            int middleX = startX + (newX - startX)/2;
                            int middleY = startY + (newY - startY)/2;
                            Piece killedPiece = tiles[middleX][middleY].getPiece();
                            tiles[getScenePlace(killedPiece.getOldX())][getScenePlace(killedPiece.getOldY())].setPiece(null);
                            piecesGroup.getChildren().remove(killedPiece);
                        }
                    }
                    else if(checkMove(piece,newX,newY) && piece.getState() instanceof PolishKingState) {
                        piece.move(newX,newY);
                        tiles[startX][startY].setPiece(null);
                        tiles[newX][newY].setPiece(piece);
                    }
                }
        });

        piece.setOnMouseClicked(e -> {
            List<Square> availibleMoves = piece.getAvailibleMoves(tiles);
            Square temp;
            for(int i=0;i< availibleMoves.size();i++)
            {
                temp=availibleMoves.get(i);
                temp.setStroke(Color.BLUE);
                temp.setStrokeWidth(5);
            }
        });

        piece.setOnMouseExited(e -> {
            List<Square> availibleMoves = piece.getAvailibleMoves(tiles);
            Square temp;
            for(int i=0;i< availibleMoves.size();i++)
            {
                temp=availibleMoves.get(i);
                temp.setStroke(null);
            }
        });
        
        piece.setOnMouseDragged(e -> {
            piece.toFront();
        });

        return piece;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
