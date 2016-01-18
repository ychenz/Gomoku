package life.is.game.gomoku;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by Yuchen on 1/8/16.
 */
public class Board implements AdapterView.OnItemClickListener{

    private int ai = 1; //ai moves first by default
    private int hplayer = 2;
    private int current_player = ai;
    private int current_move = 1;
    private Context mcontext;
    private GridView boardview;
    private Position[][] all_positions;
    private BoardAdapter adapter;
    private Move move;

    //Initialize all positions on the board
    public Board(Context context,GridView boardview){
        this.mcontext = context;
        this.all_positions = new Position[15][15];
        this.boardview = boardview; //grid board to put in squares
        Move move = new Move(all_positions);

        for ( int row = 0 ; row < 15 ; row++ ){
            for ( int col = 0 ; col < 15 ; col++ ) {
                this.all_positions[row][col] = new Position();
                this.all_positions[row][col].setPosition(row, col);
            }
        }

        adapter = new BoardAdapter(mcontext, all_positions);
        boardview.setAdapter(adapter);//put unit squares into the board

    }

    //clear entire board
    public void clear(){
        for ( int r=0;r < 15;r++ ){
            for ( int c=0;c<15;c++ ){
                all_positions[r][c].put(0);
                adapter.notifyDataSetChanged(); //update board
            }
        }
    }

    //clear all highlighted point
    public void clearHightlight(){
        for ( int r=0;r < 15;r++ ){
            for ( int c=0;c<15;c++ ){
                all_positions[r][c].setHighlight(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void startGame(){
        if (Setting.player_num == 1) {
            move = new Move(all_positions);
            if ( current_player == ai){
                makeMove();
            }
            this.boardview.setOnItemClickListener(this);
        }
    }

    //AI move
    public void makeMove(){
        Position best_move = move.getBestMove(ai);//ai move
        best_move.put(ai);
        best_move.setMove(current_move);
        adapter.notifyDataSetChanged();
        current_player =  hplayer;
        this.current_move ++;
    }

    //get hint
    public void getHint(){
        Position best_move = move.getBestMove(current_player);
        best_move.setHighlight(true); //display the hint on the board
        adapter.notifyDataSetChanged();

    }

    //undo last move(for human player)
    public void undo(){
        for ( int r=0;r < 15;r++ ){
            for ( int c=0;c<15;c++ ){
                if ( all_positions[r][c].getMove() == current_move -1 || all_positions[r][c].getMove() == current_move -2 ){
                    all_positions[r][c].put(0);
                }
            }
        }

        if ( current_move > 2 ){
            current_move -= 2;
        }
        clearHightlight();
        adapter.notifyDataSetChanged();
    }

    //listening to board touch
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int row = position / 15;
        int col = position % 15;
        int game_end = checkEnd();
        String winner = null;

        //Toast.makeText(mcontext, "You Clicked at " + row + "," + col, Toast.LENGTH_SHORT).show();
        if (current_player != ai && all_positions[row][col].getPlace_holder() == 0) {
            clearHightlight();
            all_positions[row][col].put(hplayer);
            all_positions[row][col].setMove(current_move);

            //check if the player has win
            int check_end = checkEnd();
            if ( check_end != 0 ){
                endGameDialog(check_end);
                return;
            }

            current_player = ai;
            current_move ++;
            makeMove();

            //heck if AI has win
            check_end = checkEnd();
            if ( check_end != 0 ){
                endGameDialog(check_end);
                return;
            }
        }


        /*switch (state) {
            case 1:
                Setting.move_done = false;
                Setting.current_player = 1;
                if (Setting.move_done) { //check if player has finished his move
                    if (game_end != 0) {
                        state = 3;
                    } else {
                        state = 2;
                    }
                }
                break;
            case 2:
                Setting.move_done = false;
                Setting.current_player = 2;
                if (Setting.move_done) {
                    if (game_end != 0) {
                        state = 3;
                    } else {
                        state = 1;
                    }
                }
                break;
            case 3:
                if (game_end == 1) {
                    winner = "black";
                } else {
                    winner = "white";
                }
                Toast.makeText(mcontext, winner + "has won the game!", Toast.LENGTH_SHORT).show();
                clear();
                state = 1;
                break;
        }*/

    }

    private void endGameDialog(int result){

        final int r = result;
        String content = null;
        CharSequence title = "Game over";

        if ( result == ai ){
            content = "YOu lost！";
        }else if ( result == hplayer ){
            content = "YOu won！";
        }else if ( result == 3 ){
            content = "Its a draw!！";
        }

        AlertDialog.Builder alert_dialog_builder = new AlertDialog.Builder(this.mcontext);
        alert_dialog_builder.setTitle(title);
        alert_dialog_builder.setMessage(content);

        alert_dialog_builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                clearHightlight();
                clear();

                if (r == ai) {
                    clearHightlight();
                    clear();
                    ai = 2;
                    hplayer = 1;
                    current_player = 1;
                } else if (r == hplayer) {
                    ai = 1;
                    hplayer = 2;
                    current_player = 1;
                    makeMove();

                } else if (r == 3) {
                    clearHightlight();
                    clear();
                    if (ai == 1) {
                        makeMove();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        AlertDialog game_over = alert_dialog_builder.create();
        //game_over.getWindow().setBackgroundDrawable(new ColorDrawable(mcontext.getResources().getColor(R.color.trans_blue)));
        game_over.show();
    }

    //Check if game is over
    private int checkEnd(){
         /* return values:
         *   0: game still not over
         *   1: player1 wins
         *   2: player2 wins
         *   3: draw
         */
        int pPlayer1,pPlayer2;

        //scan all positions horizontally
        for ( int r=0;r < 15;r++ ){
            for ( int c=0;c<15-4;c++ ) {
                pPlayer1 = 0;
                pPlayer2 = 0;
                for (int i = 0; i < 5; i++) {
                    if (all_positions[r][c + i].getPlace_holder() == 1)
                        pPlayer1++; //num of black in these 5
                    if (all_positions[r][c + i].getPlace_holder() == 2)
                        pPlayer2++; //num of white in these 5
                }

                if ( pPlayer1 == 5 ){
                    return 1;
                }else if ( pPlayer2 == 5){
                    return 2;
                }
            }
        }

        //Scan all positions vertically
        for ( int c=0;c<15;c++ ){
            for ( int r=0;r<15-4;r++ ){
                pPlayer1 = 0;
                pPlayer2 = 0;
                for ( int i=0;i<5;i++){
                    if ( all_positions[r+i][c].getPlace_holder() == 1 ) pPlayer1++; //num of black in these 5
                    if ( all_positions[r+i][c].getPlace_holder() == 2 ) pPlayer2++; //num of white in these 5
                }

                if ( pPlayer1 == 5 ){
                    return 1;
                }else if ( pPlayer2 == 5){
                    return 2;
                }
            }
        }

        //scan all positions from left-top to right-bottom
        for ( int c = 0; c < 15 - 4; c++ ) {
            for (int r = 0; r < 15 - 4; r++) {
                pPlayer1 = 0;
                pPlayer2 = 0;
                for (int i = 0; i < 5; i++) {
                    if (all_positions[r + i][c + i].getPlace_holder() == 1)
                        pPlayer1++; //num of black in these 5
                    if (all_positions[r + i][c + i].getPlace_holder() == 2)
                        pPlayer2++; //num of white in these 5
                }

                if ( pPlayer1 == 5 ){
                    return 1;
                }else if ( pPlayer2 == 5){
                    return 2;
                }
            }
        }

        //scan all positions from left-bottom to right-top
        for ( int r = 4; r < 15; r++ ) {
            for (int c = 0; c < 15 - 4; c++) {
                pPlayer1 = 0;
                pPlayer2 = 0;
                for ( int i=0;i<5;i++){
                    if ( all_positions[r-i][c+i].getPlace_holder() == 1 ) pPlayer1++; //num of black in these 5
                    if ( all_positions[r-i][c+i].getPlace_holder() == 2 ) pPlayer2++; //num of white in these 5
                }

                if ( pPlayer1 == 5 ){
                    return 1;
                }else if ( pPlayer2 == 5){
                    return 2;
                }
            }
        }

        for ( int r=0;r < 15;r++ ){
            for ( int c=0;c<15;c++ ){
                if ( all_positions[r][c].getPlace_holder() == 0 )
                    return 0;
            }
        }

        return 3; //draw
    }
}
