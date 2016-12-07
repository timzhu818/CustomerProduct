package shutenmei.example.com.customerproduct.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import shutenmei.example.com.customerproduct.R;
import shutenmei.example.com.customerproduct.app.AppController;

/**
 * Created by shutenmei on 16/4/27.
 */
public class ProdItemAdapter extends ArrayAdapter<ProdItem> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<ProdItem> mProdData=new ArrayList<ProdItem>();

    private ImageLoader mImageLoader;

    public ProdItemAdapter(Context mContext, int layoutResourceId, ArrayList<ProdItem> mProdData){
        super(mContext,layoutResourceId,mProdData);
        this.layoutResourceId=layoutResourceId;
        this.mContext=mContext;
        this.mProdData=mProdData;
    }
    public void setmProdData(ArrayList<ProdItem> mProdData){
        this.mProdData=mProdData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int Position, View convertView, ViewGroup parent){
        View vi=convertView;
        ViewHolder holder;

        if(vi==null){
            LayoutInflater inflater=((Activity)mContext).getLayoutInflater();
            vi=inflater.inflate(layoutResourceId,parent,false);

            holder=new ViewHolder();
            holder.titleTextView=(TextView) vi.findViewById(R.id.prod_item_title);
            holder.imageView=(NetworkImageView) vi.findViewById(R.id.prod_item_image);
            holder.discriptionView=(TextView) vi.findViewById(R.id.prod_item_discription);
            holder.priceView=(TextView) vi.findViewById(R.id.prod_item_price);
            holder.quantityView=(TextView) vi.findViewById(R.id.prod_item_quantity);

            vi.setTag(holder);
        }else{
            holder=(ViewHolder) vi.getTag();
        }
        ProdItem item=mProdData.get(Position);
        holder.titleTextView.setText(item.getTitle());
        holder.quantityView.setText(item.getQuantity());
        holder.priceView.setText(item.getPrice());
        holder.discriptionView.setText(item.getDiscription());
        mImageLoader= AppController.getInstance(mContext).getImageLoader();
        String url=item.getUrl();
        holder.imageView.setImageUrl(url,mImageLoader);

        return vi;
    }
    static class ViewHolder{
        TextView titleTextView;
        TextView discriptionView;
        TextView priceView;
        TextView quantityView;
        NetworkImageView imageView;

    }
}
