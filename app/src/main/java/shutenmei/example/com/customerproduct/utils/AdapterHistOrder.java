package shutenmei.example.com.customerproduct.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import shutenmei.example.com.customerproduct.OrderHistoryActivity;
import shutenmei.example.com.customerproduct.R;

/**
 * Created by shutenmei on 16/4/28.
 */
public class AdapterHistOrder extends BaseAdapter {
    private LayoutInflater inflater;

    public AdapterHistOrder(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return OrderHistoryActivity.Menu_ID.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.order_hist_item, null);
            holder = new ViewHolder();
            holder.txtMenuName = (TextView) convertView.findViewById(R.id.txtMenuName);
            holder.txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtMenuName.setText(OrderHistoryActivity.Menu_name.get(position));
        holder.txtQuantity.setText(String.valueOf(OrderHistoryActivity.Quantity.get(position)));
        holder.txtPrice.setText(String.valueOf(OrderHistoryActivity.Sub_total_price.get(position))+"$");

        return convertView;
    }

    static class ViewHolder {
        TextView txtMenuName, txtQuantity, txtPrice;
    }

}
