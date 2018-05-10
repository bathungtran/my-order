package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.project.myorder.Menu;
import com.project.myorder.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.RestaurantModel;
import utils.RestaurantFilter;

public class RestaunrantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> implements Filterable {

    private List<RestaurantModel> restaurantModelArrayList, filterList;
    private Context context;
    private RestaurantFilter restaurantFilter;
    private int customerId;
    public RestaunrantAdapter(List<RestaurantModel> restaurantModelArrayList, Context context,int customerId) {
        this.restaurantModelArrayList = restaurantModelArrayList;
        this.context = context;
        this.filterList= restaurantModelArrayList;
        this.customerId = customerId;
    }


    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.restaurant_item_layout,parent,false);

        return new RestaurantViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RestaurantViewHolder holder, int position) {
        holder.txtRestaurantName.setText(restaurantModelArrayList.get(position).getResName());
        holder.txtRestaurantAddress.setText(restaurantModelArrayList.get(position).getAddress());
        holder.txtRestaurantDiscription.setText(restaurantModelArrayList.get(position).getDescription());

        String url = restaurantModelArrayList.get(position).getResImage();
        Picasso.with(context).load(url).into(holder.imgRestaurantLogo);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //put restaurant id to next activity
                changeIntent(position);
            }
        });

    }

    private void changeIntent(int pos) {
        Intent i = new Intent(context,Menu.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("RES_NAME", restaurantModelArrayList.get(pos).getResName());
        i.putExtra("RES_ID", restaurantModelArrayList.get(pos).getResId());
        i.putExtra("CUSTOMER_ID",customerId);
        System.out.println("RES_ID_HOME: "+ restaurantModelArrayList.get(pos).getResId());
        context.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return restaurantModelArrayList.size();
    }

    public void setRestaurants(List<RestaurantModel> restaurantModels){
        this.restaurantModelArrayList = restaurantModels;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if(restaurantFilter==null){
            restaurantFilter= new RestaurantFilter(filterList,this);
        }
        return restaurantFilter;
    }
}