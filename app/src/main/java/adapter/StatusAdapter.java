package adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.myorder.R;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.zip.Inflater;

import model.OrderModel;
import model.StatusModel;

import static android.content.ContentValues.TAG;

/**
 * Created by harry on 31/05/2018.
 */
public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusOrderViewHolder> {
    private Context context;
    private List<StatusModel> statusModels;
    private String TAG = "STATUS_ADAPTER";
    public StatusAdapter(Context context, List<StatusModel> statusModels) {
        this.context =context;
        this.statusModels = statusModels;
    }

    @Override
    public StatusOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.fragment_item,parent,false);
        return new StatusOrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StatusOrderViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: "+statusModels.get(position).getTime());
        holder.tvTime.setText(statusModels.get(position).getTime());
        String str = statusModels.get(position).getFood().substring(0,
                statusModels.get(position).getFood().length()-1);
        holder.tvFood.setText(str);
        holder.tvNo.setText(String.valueOf(position+1));
        if (position%2==0){
            holder.itemBackground.setBackgroundResource(R.drawable.status_background);
        }
        switch (statusModels.get(position).getStatus()){
            case 0:
                holder.ivStatus.setImageResource(R.drawable.ic_checked);
                break;
            case 1:
                holder.ivStatus.setImageResource(R.drawable.ic_sand_clock);
                break;
            case 2:
                holder.ivStatus.setImageResource(R.drawable.ic_error);
                break;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return statusModels.size();
    }



    public class StatusOrderViewHolder extends RecyclerView.ViewHolder{
        TextView tvTime;
        TextView tvFood;
        ImageView ivStatus;
        TextView tvNo;
        LinearLayout itemBackground;
        public StatusOrderViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvFood = itemView.findViewById(R.id.tv_food);
            ivStatus = itemView.findViewById(R.id.iv_status);
            tvNo = itemView.findViewById(R.id.tv_no);
            itemBackground = itemView.findViewById(R.id.item_background);
        }
    }
}
