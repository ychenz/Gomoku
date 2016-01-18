package life.is.game.gomoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    Board board;
    Button hint,undo,leave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        hint = (Button) findViewById(R.id.hint);
        undo = (Button) findViewById(R.id.undo);
        leave = (Button) findViewById(R.id.leave);
        GridView gridView = (GridView) findViewById(R.id.board);
        board = new Board(this,gridView); //initialize the board
        board.startGame();

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.getHint();
            }
        });

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.undo();
            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
