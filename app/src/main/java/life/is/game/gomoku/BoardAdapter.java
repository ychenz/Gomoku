package life.is.game.gomoku;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Yuchen on 1/9/16.
 */
public class BoardAdapter extends BaseAdapter {

    private Context context;
    private Position[][] all_positions;

    public BoardAdapter(Context c, Position[][] all_positions){
        this.context = c;
        this.all_positions = all_positions;
    }

    @Override
    public int getCount() {
        return 225;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        int row = position / 15;
        int col = position % 15;
        Position cPosition = all_positions[row][col];
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(context);
            grid = inflater.inflate(R.layout.square, null);
        } else {
            grid = (View) convertView;
        }

        ImageView imageView = (ImageView)grid.findViewById(R.id.square_background);
        RelativeLayout square = (RelativeLayout) grid.findViewById(R.id.square);

        //highlight current move or hint
        if ( cPosition.highlighted()){
            square.setBackgroundResource(R.drawable.hsquare);
        }else{
            square.setBackgroundResource(R.drawable.square);
        }

        //drawing black or white chess
        if ( cPosition.getPlace_holder() == 1 ){
            imageView.setImageResource(R.drawable.black);   //put black
        } else if ( cPosition.getPlace_holder() == 2 ){
            imageView.setImageResource(R.drawable.white);   //put white
        }else{
            imageView.setImageResource(0);
        }

        return grid;
    }
}
