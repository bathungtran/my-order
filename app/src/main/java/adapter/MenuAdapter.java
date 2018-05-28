package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.myorder.MenuActivity;
import com.project.myorder.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.FoodModel;
import model.OrderModel;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.RecyclerViewHolder>{

    private List<FoodModel> foodModels = new ArrayList<>();
    private Context context;
    public MenuAdapter(List<FoodModel> foodModels, Context context) {
        this.foodModels = foodModels;
        this.context=context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.recycler_view_item, viewGroup, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {

        holder.name.setText(foodModels.get(position).getFoodName());
        holder.price.setText(String.format("%.0f",foodModels.get(position).getPrice()));
        holder.itemNumber.setText("0");
        holder.imageView.setTag(foodModels.get(position).getImage());
        String url = foodModels.get(position).getImage();
        Picasso.with(context).load(url).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return foodModels.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        private TextView price;
        private EditText itemNumber;
        private Button btnDelete;
        private Button btnOrder;
        private ImageView imageView;
        private OrderModel model=null;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txt_itemName);
            price = (TextView) itemView.findViewById(R.id.txt_itemPrice);
            itemNumber = (EditText) itemView.findViewById(R.id.edt_itemNumber);
            btnDelete = (Button) itemView.findViewById(R.id.btn_deleteButton);
            btnOrder = (Button) itemView.findViewById(R.id.btn_orderButton);
            imageView = (ImageView) itemView.findViewById(R.id.img_itemImage);
            //set listener for button here
            btnOrder.setOnClickListener(this);
            btnDelete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (MenuActivity.orderLists.contains(model)) {
                MenuActivity.orderLists.remove(model);
            }
            //set action for button here
            int itemNum = Integer.parseInt(itemNumber.getText().toString());
            if (v.getId() == btnDelete.getId()){

                if (itemNum > 0) {
                    itemNum -= 1;
                }
                else {
                    itemNum = 0;
                }

                itemNumber.setText(String.valueOf(itemNum));
            }

            if (v.getId() == btnOrder.getId()){
                itemNum ++;
                itemNumber.setText(String.valueOf(itemNum));
            }
            model=new OrderModel(name.getText().toString(),Double.valueOf(price.getText().toString()),itemNum);
            if(model.getQuantity()>0) {
                MenuActivity.orderLists.add(model);
            }else {
                MenuActivity.orderLists.remove(model);
            }
        }
    }

}
