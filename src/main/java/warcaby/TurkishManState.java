package warcaby;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
/**
 * Klasa, która określa zachowanie zwykłego pionka w tureckich warcabach
 */
public class TurkishManState implements State{
    /**
     * @see State
     */
    @Override
    public List<SingleMove> availibleMoves(Piece piece, Square[][] board) {
        List<SingleMove> result= new ArrayList<>();

        for (Direction direction : Direction.values()) {
            result.addAll(move(piece, board, direction));
        }
        return result;
    }
    /**
     * @see State
     * Funkcja uniemożliwia ponowne zbicie pionka
     * Zbija maksymalną liczbę pionków
     */
    @Override
    public List<List<SingleMove>> moveSequence(Piece piece, Square[][] board, List<SingleMove> steps) {
        //Inicjalizacja zmiennych
        List<SingleMove> current, list;
        List<List<SingleMove>> result = new ArrayList<>(), endResult = new ArrayList<>();
        current = availibleMoves(piece, board);
        //usunięcie pionka z pola startowego
        board[piece.getOldX()/70][piece.getOldY()/70].setPiece(null);
        //zmienna do sprawdzenia czy przeskoczyliśmy nad danym pionkiem
        if(current.size()>0){
            for (SingleMove square : current) {

                list = new ArrayList<>(steps);
                if (square.getKilled()!=null) {
                    square.setKilledPiece(board[(int) (square.getKilled().getX() / 70)][(int) (square.getKilled().getY() / 70)].getPiece());
                    list.add(square);
                    Piece p = new TurkishPiece((int) square.getEnd().getX()+35,(int) square.getEnd().getY()+35,30, piece.getColor(), new TurkishManState());
                    board[(int) (square.getKilled().getX()/70)][(int) (square.getKilled().getY()/70)].setPiece(null);
                    if(p.getOldY()/70==7&&p.getColor()==Color.BLACK){
                        result.add(list);
                    }
                    else if(p.getOldY()/70==0&&p.getColor()==Color.WHITE){
                        result.add(list);
                    }
                    else result.addAll(moveSequence(p, board, list));
                } else {
                    if (steps.isEmpty()) {
                        list.add(square);
                        result.add(list);
                    }
                    else result.add(list);
                }
            }
        }
        else result.add(steps);
        for (List<SingleMove> squares : result) {
            if (!endResult.contains(squares)) {
                {
                    endResult.add(squares);
                }
            }
        }
        result.clear();
        for (List<SingleMove> squares : endResult) {
            if(result.isEmpty())result.add(squares);
            else if(result.get(0).size()<squares.size()){
                result.clear();
                result.add(squares);
            }
            else if(result.get(0).size()==squares.size()){
                if(result.get(0).get(0).getKilled()==null&&squares.get(0).getKilled()!=null){
                    result.clear();
                }
                result.add(squares);
            }
        }
        return result;
    }

    /**
     *
     * @return zwraca TurkishKingState
     */
    @Override
    public State changeState() {
        return new TurkishKingState();
    }
    /**
     * W zależności od kierunku sprawdzane są możliwości ruchu
     * Pionek porusza się po prostej o 1 pole
     * Pionek bije do przodu i do tyłu
     * @see State
     */
    @Override
    public List<SingleMove> move(Piece piece, Square[][] tiles, Direction direction) {
        List<SingleMove> result=new ArrayList<>();
        int x = piece.getOldX()/70;
        int y = piece.getOldY()/70;
        int moveDirection = piece.getMoveDirection();
        switch (direction){
            case Up -> {
                if(y-1>=0){
                    if(moveDirection==-1) {
                        Piece p = tiles[x][y - 1].getPiece();
                        if (p == null) {
                            result.add(new SingleMove(tiles[x][y], tiles[x][y - 1], null));
                        } else if (piece.getColor() != p.getColor() && y - 2 >= 0) {
                            if (tiles[x][y - 2].getPiece() == null)
                                result.add(new SingleMove(tiles[x][y], tiles[x][y - 2], tiles[x][y - 1]));
                        }
                    }
                }
            }
            case Down -> {
                if(y+1<=7){
                    if(moveDirection==1) {
                        Piece p = tiles[x][y + 1].getPiece();
                        if (p == null) {
                            result.add(new SingleMove(tiles[x][y], tiles[x][y + 1], null));
                        } else if (piece.getColor() != p.getColor() && y + 2 <= 7) {
                            if (tiles[x][y + 2].getPiece() == null)
                                result.add(new SingleMove(tiles[x][y], tiles[x][y + 2], tiles[x][y + 1]));
                        }
                    }
                }
            }
            case Left -> {
                if(x-1>=0){
                    Piece p = tiles[x-1][y].getPiece();
                    if(p==null){
                        result.add(new SingleMove(tiles[x][y],tiles[x-1][y],null));
                    }
                    else if(piece.getColor()!=p.getColor()&&x-2>=0){
                        if(tiles[x-2][y].getPiece()==null)result.add(new SingleMove(tiles[x][y],tiles[x-2][y],tiles[x-1][y]));
                    }
                }
            }
            case Right -> {
                if(x+1<=7){
                    Piece p = tiles[x+1][y].getPiece();
                    if(p==null){
                        result.add(new SingleMove(tiles[x][y],tiles[x+1][y],null));
                    }
                    else if(piece.getColor()!=p.getColor()&&x+2<=7){
                        if(tiles[x+2][y].getPiece()==null)result.add(new SingleMove(tiles[x][y],tiles[x+2][y],tiles[x+1][y]));
                    }
                }
            }
        }
        return result;
    }
}
