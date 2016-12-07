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
 * Created by shutenmei on 16/4/26.
 */
public class CateItemAdapter extends ArrayAdapter<CateItem> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<CateItem>mCateData=new ArrayList<CateItem>();

    private ImageLoader mImageLoader;

    public CateItemAdapter(Context mContext,int layoutResourceId,ArrayList<CateItem> mCateData){
        super(mContext,layoutResourceId,mCateData);
        this.layoutResourceId=layoutResourceId;
        this.mContext=mContext;
        this.mCateData=mCateData;
    }
    public void setmCateData(ArrayList<CateItem> mCateData){
        this.mCateData=mCateData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View vi=convertView;
        ViewHolder holder;

        if(vi==null){
            LayoutInflater inflater=((Activity)mContext).getLayoutInflater();
            vi=inflater.inflate(layoutResourceId,parent,false);

            holder=new ViewHolder();
            holder.titleTextView=(TextView) vi.findViewById(R.id.cate_item_title);
            holder.imageView=(NetworkImageView) vi.findViewById(R.id.cate_item_image);

            vi.setTag(holder);
        }else{
            holder=(ViewHolder) vi.getTag();
        }
        CateItem item=mCateData.get(position);
        holder.titleTextView.setText(item.getTitle());
        mImageLoader=AppController.getInstance(mContext).getImageLoader();
        String url=item.getUrl();
        holder.imageView.setImageUrl(url,mImageLoader);

        return vi;
    }

    static class ViewHolder{
        TextView titleTextView;
        NetworkImageView imageView;
    }
}
