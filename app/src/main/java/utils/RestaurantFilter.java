package utils;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import adapter.RestaunrantAdapter;
import model.RestaurantModel;

/**
 * Created by bathu on 11/26/2017.
 */

public class RestaurantFilter extends Filter {

    static List<RestaurantModel> restaurantModelCurrentList;
    static RestaunrantAdapter restaunrantAdapter;

    public RestaurantFilter(List<RestaurantModel> restaurantModels, RestaunrantAdapter restaunrantAdapter){
        RestaurantFilter.restaurantModelCurrentList = restaurantModels;
        RestaurantFilter.restaunrantAdapter=restaunrantAdapter;
    }
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();

        if(constraint!=null && constraint.length()>0){
            constraint=constraint.toString().toLowerCase();

            List<RestaurantModel> restaurantModelFoundList = new ArrayList<>();

            for(RestaurantModel restaurantModel : restaurantModelCurrentList){

                if(restaurantModel.getResName().toLowerCase().contains(constraint)||
                        restaurantModel.getDescription().toLowerCase().contains(constraint)) {
                    restaurantModelFoundList.add(restaurantModel);
                }
            }

            filterResults.count= restaurantModelFoundList.size();
            filterResults.values= restaurantModelFoundList;

        }else {
            filterResults.count= restaurantModelCurrentList.size();
            filterResults.values= restaurantModelCurrentList;
        }

        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        restaunrantAdapter.setRestaurants((List<RestaurantModel>) results.values);
    }
}
