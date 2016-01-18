package life.is.game.gomoku;

/**
 * Created by Yuchen on 1/8/16.
 * AI algorithm thanks to  Trinh Le's source code: http://icetea09.com/blog/2014/01/27/c-5-row-ai/
 */
public class Move {

    private Position[][] board;
    private int[] AScore;
    private int[] DScore;

    public Move(Position[][] all_position){
        this.board = all_position;
        AScore = new int[]{0,2,18,162,1458};
        DScore = new int[]{0,1,9,81,729};
    }

    //get position with maximum score
    public Position getBestMove(int player){
        this.evalSituation(player);
        int bestScore = 0;
        //Position bestPosition = board[7][7];
        Position bestPosition = (board[7][7].getPlace_holder() == 0)? board[7][7] : board[0][0]; //solve the bug that if no point's score is larger than [7,7], ai does nothing
        for ( int r = 0; r <15; r++ ){
            for ( int c = 0; c < 15; c++ ){
                int current_score = board[r][c].getScore();
                if ( current_score > bestScore ){
                    bestScore = current_score;
                    bestPosition = board[r][c];
                }
                board[r][c].setScore(0); //clear score
            }
        }

        return bestPosition;
    }

    //Main AI algrithm,used by Move.getBestMove and board.getHint method
    public void evalSituation(int player){
        int pPlayer1,pPlayer2;

        //scan all positions horizontally
        for ( int r=0;r < 15;r++ ){
            for ( int c=0;c<15-4;c++ ){
                pPlayer1 = 0;
                pPlayer2 = 0;
                for ( int i=0;i<5;i++){
                    if ( board[r][c+i].getPlace_holder() == 1 ) pPlayer1++; //num of black in these 5
                    if ( board[r][c+i].getPlace_holder() == 2 ) pPlayer2++; //num of white in these 5
                }

                if ( pPlayer1 * pPlayer2 == 0 && pPlayer1 != pPlayer2){
                    for ( int i=0;i<5;i++){
                        if ( board[r][c+i].getPlace_holder() == 0 ){
                            int current_score = board[r][c+i].getScore();
                            if ( pPlayer1 == 0 ) {  //when no black in this 5 positions
                                if (player == 1) {  //current player is black
                                    board[r][c + i].setScore(current_score + DScore[pPlayer2]);
                                } else {            //current player is white
                                    board[r][c + i].setScore(current_score + AScore[pPlayer2]);
                                }
                            }

                            if ( pPlayer2 == 0 ){ //when no white in this 5 positions
                                if ( player == 2 ) { //current player is white
                                    board[r][c+i].setScore( current_score + DScore[pPlayer1] );
                                }else{               //current player is black
                                    board[r][c+i].setScore( current_score + AScore[pPlayer1] );
                                }
                            }

                            //One more move to win or lost, doubling the score
                            if ( pPlayer1 == 4 || pPlayer2 == 4 ){
                                board[r][c+i].setScore( board[r][c+i].getScore() * 2 );
                            }
                        }
                    }
                }
            }
        }

        //Scan all positions vertically
        for ( int c=0;c<15;c++ ){
            for ( int r=0;r<15-4;r++ ){
                pPlayer1 = 0;
                pPlayer2 = 0;
                for ( int i=0;i<5;i++){
                    if ( board[r+i][c].getPlace_holder() == 1 ) pPlayer1++; //num of black in these 5
                    if ( board[r+i][c].getPlace_holder() == 2 ) pPlayer2++; //num of white in these 5
                }

                if ( pPlayer1 * pPlayer2 == 0 && pPlayer1 != pPlayer2){
                    for ( int i=0;i<5;i++){
                        if ( board[r+i][c].getPlace_holder() == 0 ){
                            int current_score = board[r+i][c].getScore();
                            if ( pPlayer1 == 0 ) {  //when no black in this 5 positions
                                if (player == 1) {  //current player is black
                                    board[r+i][c].setScore(current_score + DScore[pPlayer2]);
                                } else {            //current player is white
                                    board[r+i][c].setScore(current_score + AScore[pPlayer2]);
                                }
                            }

                            if ( pPlayer2 == 0 ){ //when no white in this 5 positions
                                if ( player == 2 ) { //current player is white
                                    board[r+i][c].setScore(current_score + DScore[pPlayer1]);
                                }else{               //current player is black
                                    board[r+i][c].setScore( current_score + AScore[pPlayer1] );
                                }
                            }

                            //One more move to win or lost, doubling the score
                            if ( pPlayer1 == 4 || pPlayer2 == 4 ){
                                board[r+i][c].setScore( board[r+i][c].getScore() * 2 );
                            }
                        }
                    }
                }
            }
        }

        //scan all positions from left-top to right-bottom
        for ( int c = 0; c < 15 - 4; c++ ) {
            for (int r = 0; r < 15 - 4; r++) {
                pPlayer1 = 0;
                pPlayer2 = 0;
                for ( int i=0;i<5;i++){
                    if ( board[r+i][c+i].getPlace_holder() == 1 ) pPlayer1++; //num of black in these 5
                    if ( board[r+i][c+i].getPlace_holder() == 2 ) pPlayer2++; //num of white in these 5
                }

                if ( pPlayer1 * pPlayer2 == 0 && pPlayer1 != pPlayer2){
                    for ( int i=0;i<5;i++){
                        if ( board[r+i][c+i].getPlace_holder() == 0 ){
                            int current_score = board[r+i][c+i].getScore();
                            if ( pPlayer1 == 0 ) {  //when no black in this 5 positions
                                if (player == 1) {  //current player is black
                                    board[r+i][c+i].setScore(current_score + DScore[pPlayer2]);
                                } else {            //current player is white
                                    board[r+i][c+i].setScore(current_score + AScore[pPlayer2]);
                                }
                            }

                            if ( pPlayer2 == 0 ){ //when no white in this 5 positions
                                if ( player == 2 ) { //current player is white
                                    board[r+i][c+i].setScore(current_score + DScore[pPlayer1]);
                                }else{               //current player is black
                                    board[r+i][c+i].setScore( current_score + AScore[pPlayer1] );
                                }
                            }

                            //One more move to win or lost, doubling the score
                            if ( pPlayer1 == 4 || pPlayer2 == 4 ){
                                board[r+i][c+i].setScore( board[r+i][c+i].getScore() * 2 );
                            }
                        }
                    }
                }
            }
        }

        //scan all positions from left-bottom to right-top
        for ( int r = 4; r < 15; r++ ) {
            for (int c = 0; c < 15 - 4; c++) {
                pPlayer1 = 0;
                pPlayer2 = 0;
                for ( int i=0;i<5;i++){
                    if ( board[r-i][c+i].getPlace_holder() == 1 ) pPlayer1++; //num of black in these 5
                    if ( board[r-i][c+i].getPlace_holder() == 2 ) pPlayer2++; //num of white in these 5
                }

                if ( pPlayer1 * pPlayer2 == 0 && pPlayer1 != pPlayer2){
                    for ( int i=0;i<5;i++){
                        if ( board[r-i][c+i].getPlace_holder() == 0 ){
                            int current_score = board[r-i][c+i].getScore();
                            if ( pPlayer1 == 0 ) {  //when no black in this 5 positions
                                if (player == 1) {  //current player is black
                                    board[r-i][c+i].setScore(current_score + DScore[pPlayer2]);
                                } else {            //current player is white
                                    board[r-i][c+i].setScore(current_score + AScore[pPlayer2]);
                                }
                            }

                            if ( pPlayer2 == 0 ){ //when no white in this 5 positions
                                if ( player == 2 ) { //current player is white
                                    board[r-i][c+i].setScore(current_score + DScore[pPlayer1]);
                                }else{               //current player is black
                                    board[r-i][c+i].setScore( current_score + AScore[pPlayer1] );
                                }
                            }

                            //One more move to win or lost, doubling the score
                            if ( pPlayer1 == 4 || pPlayer2 == 4 ){
                                board[r-i][c+i].setScore( board[r-i][c+i].getScore() * 2 );
                            }
                        }
                    }
                }
            }
        }
    }
}
