package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.myorder.R;

import java.util.List;

import model.OrderModel;


public class BillAdapter extends BaseAdapter {
    private Context context;
    private List<OrderModel> modelList;
    private LayoutInflater layoutInflater;
    public BillAdapter(Context context, List<OrderModel> modelList){
        this.context=context;
        this.modelList=modelList;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int i) {
        return modelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = layoutInflater.inflate(R.layout.bill_item_layout,null);
        TextView txtName = (TextView)view.findViewById(R.id.txt_bill_foodName);
        TextView textPrice = (TextView) view.findViewById(R.id.txt_bill_foodPrice);
        TextView textQuantity = (TextView) view.findViewById(R.id.txt_bill_quantity);
        TextView textAmount = (TextView) view.findViewById(R.id.txt_bill_amount);

        OrderModel orderModel = modelList.get(i);
        txtName.setText(orderModel.getFoodName());
        textPrice.setText(orderModel.getFoodPrice()+"");
        textQuantity.setText(orderModel.getQuantity()+"");
        textAmount.setText(orderModel.getAmount()+"");
        return view;
    }
}
