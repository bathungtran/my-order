package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.myorder.R;


/**
 * Created by bathu on 11/26/2017.
 */

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView txtRestaurantName,txtRestaurantAddress,txtRestaurantDiscription;
    ImageView imgRestaurantLogo;
    ItemClickListener itemClickListener;
    public RestaurantViewHolder(View itemView) {
        super(itemView);
        txtRestaurantName = (TextView) itemView.findViewById(R.id.txtRestaurantName);
        txtRestaurantAddress = (TextView) itemView.findViewById(R.id.txtRestaurantAddress);
        txtRestaurantDiscription = (TextView) itemView.findViewById(R.id.txtRestaurantDiscription);
        imgRestaurantLogo = (ImageView) itemView.findViewById(R.id.imgRestaurantLogo);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
    }
    public  void setItemClickListener(ItemClickListener ic){
            this.itemClickListener=ic;
    }
}
