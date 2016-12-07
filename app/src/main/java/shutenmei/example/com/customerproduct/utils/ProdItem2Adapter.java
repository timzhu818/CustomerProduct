package shutenmei.example.com.customerproduct.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import shutenmei.example.com.customerproduct.R;
import shutenmei.example.com.customerproduct.app.AppController;

/**
 * Created by shutenmei on 16/5/3.
 */
public class ProdItem2Adapter extends BaseAdapter {
    private Context mContext;
    ArrayList itemimage;
    ArrayList itemid;
    ArrayList itemname;
    ArrayList itemdes;
    ArrayList itemquan;
    ArrayList itempri;
    Activity context;
    LayoutInflater inflater;

    private int layoutResourceId;
    private ArrayList<ProdItem2> mProdData = new ArrayList<ProdItem2>();

    private ImageLoader mImageLoader;

    public ProdItem2Adapter(Context c, ArrayList itemid, ArrayList itemname, ArrayList itemdes, ArrayList itemquan, ArrayList itempri, ArrayList itemimage, ArrayList<ProdItem2> mProdData) {
        mContext = c;
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mProdData = mProdData;
        this.itemimage = itemimage;
        this.itemid = itemid;
        this.itemname = itemname;
        this.itemdes = itemdes;
        this.itemquan = itemquan;
        this.itempri = itempri;
    }

    public int getCount() {
        return itemimage.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public void setmProdData(ArrayList<ProdItem2> mProdData) {
        this.mProdData = mProdData;
        notifyDataSetChanged();
    }

    public void clear()
    {
        itemid.clear();
        itemname.clear();
        itemdes.clear();
        itemquan.clear();
        itempri.clear();
        itemimage.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;


        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi = inflater.inflate(R.layout.prod_item2_layout, null);

        TextView txtid = (TextView) vi.findViewById(R.id.prod_item_id);
        TextView txtname = (TextView) vi.findViewById(R.id.prod_item_title);
        NetworkImageView imageView = (NetworkImageView) vi.findViewById(R.id.prod_item_image);
        TextView txtdis = (TextView) vi.findViewById(R.id.prod_item_discription);
        TextView txtpri = (TextView) vi.findViewById(R.id.prod_item_price);
        TextView txtquan = (TextView) vi.findViewById(R.id.prod_item_quantity);

        txtid.setText("Id: "+itemid.get(position).toString());
        String url = itemimage.get(position).toString();
        txtname.setText("Name: " + itemname.get(position).toString());
        txtdis.setText("Description: " + itemdes.get(position).toString());
        txtquan.setText("Remain Quantity: " + itemquan.get(position).toString());
        txtpri.setText("Price: " + itempri.get(position).toString());

        mImageLoader = AppController.getInstance(mContext).getImageLoader();

        imageView.setImageUrl(url, mImageLoader);

        return vi;
    }

}

