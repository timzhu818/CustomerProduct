package shutenmei.example.com.customerproduct;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import shutenmei.example.com.customerproduct.app.AppController;
import shutenmei.example.com.customerproduct.utils.CateItem;
import shutenmei.example.com.customerproduct.utils.CateItemAdapter;

public class MainActivity extends Activity {

    //Web api url
    public static final String DATA_URL = "http://rjtmobile.com/ansari/shopingcart/androidapp/cust_category.php";

    //Tag values to read from json
    public static final String KEY_ID="Id";
    public static final String KEY_CategoryName = "CatagoryName";
    public static final String KEY_CategoryImage = "CatagoryImage";
    public static final String KEY_CategoryDiscription = "CatagoryDiscription";

    //GridView Object
    private GridView custCategory;
    private CateItemAdapter cateItemAdapter;
    private ArrayList<CateItem> cateItemArray;
    private ProgressDialog pDialog;

    //ArrayList for Storing image urls and titles
    private String id;
    private String categoryname;
    private String categoryimage;
    private String categorydiscription;

    private TextView txtCount;
    int cart_count;
    static DBhelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header)));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle("Category");

        custCategory = (GridView) findViewById(R.id.gridView);

        cateItemArray = new ArrayList<>();

        //Set custom adapter for catagory items
        cateItemAdapter = new CateItemAdapter(this, R.layout.cate_item_layout, cateItemArray);
        custCategory.setAdapter(cateItemAdapter);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        dbhelper = new DBhelper(this);

        try{
            dbhelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }

        cart_count=dbhelper.getAllData().size();

        //Calling the getData method
        makeJsonArrayRequest();

        custCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                //Get item at position
                CateItem item = (CateItem) parent.getItemAtPosition(position);

                //Pass the image title and url to DetailsActivity
                Intent intent = new Intent(MainActivity.this, SubCategoryActivity.class);
                intent.putExtra("Id", item.getId());

                //Start details activity
                startActivity(intent);
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        final View menu_shop = menu.findItem(R.id.cart).getActionView();
        menu_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iMyOrder = new Intent(MainActivity.this, CartActivity.class);
                startActivity(iMyOrder);

            }
        });

        txtCount = (TextView) menu_shop.findViewById(R.id.txt_count);
        updateCartCount(cart_count);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.cart:
                // refresh action
                Intent iMyOrder = new Intent(MainActivity.this, CartActivity.class);
                startActivity(iMyOrder);
                return true;


            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void makeJsonArrayRequest() {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,DATA_URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hidePDialog();
                        try {
                            // Parsing json array response
                            // loop through each json object
                            JSONArray jsonArray = response.getJSONArray("Category");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject category = (JSONObject) jsonArray.get(i);

                                id = category.getString(KEY_ID);
                                categoryname = category.getString(KEY_CategoryName);
                                categoryimage = category.getString(KEY_CategoryImage);
                                categorydiscription = category.getString(KEY_CategoryDiscription);

                                CateItem cateItem = new CateItem(id, categoryimage, categoryname);
                                cateItemArray.add(cateItem);

                                cateItemAdapter.notifyDataSetChanged();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
            }

        });

        // Adding request to request queue
        AppController.getInstance(getApplicationContext()).addToRequestQueue(req);
    }




    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    public void updateCartCount(final int new_cart_count) {
        cart_count = new_cart_count;
        if ( txtCount == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (new_cart_count == 0)
                    txtCount.setVisibility(View.INVISIBLE);
                else {
                    txtCount.setVisibility(View.VISIBLE);
                    txtCount.setText(Integer.toString(new_cart_count));
                }
            }
        });
    }

}