package shutenmei.example.com.customerproduct;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.util.ArrayList;

public class ActivityHome extends Fragment implements OnItemClickListener {
    GridView gridview;
    AdapaterGridView gridviewAdapter;
    ArrayList<GridViewItem> data = new ArrayList<GridViewItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, container, false);

        gridview = (GridView) v.findViewById(R.id.gridView1);
        gridview.setOnItemClickListener(this);

        data.add(new GridViewItem(getResources().getString(R.string.menu_product), getResources().getDrawable(R.drawable.ic_product)));
        data.add(new GridViewItem(getResources().getString(R.string.menu_cart), getResources().getDrawable(R.drawable.ic_cart)));
        data.add(new GridViewItem(getResources().getString(R.string.menu_checkout), getResources().getDrawable(R.drawable.ic_checkout)));
        data.add(new GridViewItem("History", getResources().getDrawable(R.drawable.ic_history)));
        data.add(new GridViewItem(getResources().getString(R.string.menu_share), getResources().getDrawable(R.drawable.ic_share)));

        setDataAdapter();

        return v;
    }

    // Set the Data Adapter
    private void setDataAdapter() {
        gridviewAdapter = new AdapaterGridView(getActivity(), R.layout.fragment_list_item, data);
        gridview.setAdapter(gridviewAdapter);
    }

    @Override
    public void onItemClick(final AdapterView<?> arg0, final View view, final int position, final long id) {
        if (position==0){
            startActivity(new Intent(getActivity(), MainActivity.class));

        }
        else if (position==1){
            startActivity(new Intent(getActivity(), CartActivity.class));

        }
        else if (position==2){
            startActivity(new Intent(getActivity(), ActivityCheckout.class));
        }
        else if (position==3){
            startActivity(new Intent(getActivity(), OrderHistoryActivity.class));
        }
        else if (position==4){
            Intent sendInt = new Intent(Intent.ACTION_SEND);
            sendInt.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            sendInt.putExtra(Intent.EXTRA_TEXT, "E-Commerce Android App\n\""+getString(R.string.app_name)+"\" \nhttps://play.google.com/store/apps/details?id="+getActivity().getPackageName());
            sendInt.setType("text/plain");
            startActivity(Intent.createChooser(sendInt, "Share"));
        }
    }
}