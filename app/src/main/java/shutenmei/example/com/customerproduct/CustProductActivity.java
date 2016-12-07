package shutenmei.example.com.customerproduct;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
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
import java.util.Collections;

import shutenmei.example.com.customerproduct.app.AppController;
import shutenmei.example.com.customerproduct.utils.ProdItem2;
import shutenmei.example.com.customerproduct.utils.ProdItem2Adapter;

public class CustProductActivity extends Activity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{

    public static final String KEY_ProdId="Id";
    public static final String KEY_ProdName="ProductName";
    public static final String KEY_ProdQuantity="Quantity";
    public static final String KEY_ProPrize="Prize";
    public static final String KEY_ProdDiscription="Discription";
    public static final String KEY_ProdImage="Image";

    ArrayList<String> proid1ist;
    ArrayList<String> proName1ist;
    ArrayList<String> proQuantity1ist;
    ArrayList<String> proPrize1ist;
    ArrayList<String> proDiscription1ist;
    ArrayList<String> proimage1ist;


    private ListView custCategory;
    private ProdItem2Adapter prodItemAdapter;
    private ArrayList<ProdItem2> prodItemArray;
    private ProgressDialog pDialog;

    String proid,proname,proquan,propri,proimag,prodescription;

    private String id;
    private String productname;
    private String productimage;
    private String productdiscription;
    private String productprize;
    private String productquantity;
    private String main_id;

    private TextView txtCount;
    int cart_count;
    static DBhelper dbhelper;
    Button setbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_product);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header)));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle("Products");

        main_id = getIntent().getStringExtra("Id");

        if (prodItemAdapter != null) {
            proid1ist.clear();
            proName1ist.clear();
            proDiscription1ist.clear();
            proQuantity1ist.clear();
            proPrize1ist.clear();
            proimage1ist.clear();
            prodItemAdapter.clear();
        }

        custCategory = (ListView) findViewById(R.id.prolist);
        setbtn = (Button) findViewById(R.id.btnset);
        setbtn.setOnClickListener(this);

        proid1ist = new ArrayList<String>();
        proName1ist = new ArrayList<String>();
        proDiscription1ist = new ArrayList<String>();
        proimage1ist = new ArrayList<String>();
        proQuantity1ist = new ArrayList<String>();
        proPrize1ist = new ArrayList<String>();

        prodItemArray = new ArrayList<>();

        prodItemAdapter = new ProdItem2Adapter(this, proid1ist, proName1ist,proDiscription1ist,proQuantity1ist,proPrize1ist,proimage1ist, prodItemArray);
        custCategory.setAdapter(prodItemAdapter);

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
                ProdItem2 item = (ProdItem2) parent.getItemAtPosition(position);

                //Pass the image title and url to DetailsActivity
                Intent intent = new Intent(CustProductActivity.this, SubProductActivity.class);
                proid = proid1ist.get(position);
                proname = proName1ist.get(position);
                prodescription=proDiscription1ist.get(position);
                proquan = proQuantity1ist.get(position);
                propri = proPrize1ist.get(position);
                proimag = proimage1ist.get(position);
                intent.putExtra("Id", proid);
                intent.putExtra("Name",proname);
                intent.putExtra("Discription", prodescription);
                intent.putExtra("Price",proquan);
                intent.putExtra("Quantity",propri);
                intent.putExtra("Image",proimag);

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
                Intent iMyOrder = new Intent(CustProductActivity.this, CartActivity.class);
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
                Intent iMyOrder = new Intent(CustProductActivity.this, CartActivity.class);
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

        final String uri = String.format("http://rjtmobile.com/ansari/shopingcart/androidapp/cust_product.php?Id=%1$s",main_id);

        JsonObjectRequest prodreq = new JsonObjectRequest(Request.Method.GET, uri, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hidePDialog();
                        try {
                            // Parsing json array response
                            // loop through each json object
                            JSONArray jsonArray = response.getJSONArray("Product");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject category = (JSONObject) jsonArray.get(i);

                                id = category.getString(KEY_ProdId);
                                productname = category.getString(KEY_ProdName);
                                productimage = category.getString(KEY_ProdImage);
                                productdiscription = category.getString(KEY_ProdDiscription);
                                productprize = category.getString(KEY_ProPrize);
                                productquantity = category.getString(KEY_ProdQuantity);

                                proid1ist.add(category.getString("Id"));
                                proName1ist.add(category.getString("ProductName"));
                                proQuantity1ist.add(category.getString("Quantity"));
                                proPrize1ist.add(category.getString("Prize"));
                                proDiscription1ist.add(category.getString("Discription"));
                                proimage1ist.add(category.getString("Image"));


                                ProdItem2 prodItem = new ProdItem2(id,productimage, productname,productdiscription,productprize,productquantity);
                                prodItemArray.add(prodItem);

                                prodItemAdapter.notifyDataSetChanged();

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
                Toast.makeText(CustProductActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        });

        // Adding request to request queue
        AppController.getInstance(getApplicationContext()).addToRequestQueue(prodreq);
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
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnset) {

            PopupMenu pm = new PopupMenu(this, setbtn);
            pm.inflate(R.menu.menu2);
            pm.show();

            pm.setOnMenuItemClickListener(this);
        }
    }
    public boolean onMenuItemClick(MenuItem item) {
        final Context context = this;
        int i = item.getItemId();
        if (i == R.id.action_sort) {
            Sort();
            prodItemAdapter = new ProdItem2Adapter(this, proid1ist, proName1ist,proDiscription1ist,proQuantity1ist,proPrize1ist,proimage1ist, prodItemArray);
            custCategory.setAdapter(prodItemAdapter);
            prodItemAdapter.notifyDataSetChanged();
            return true;

        }
        if (i == R.id.action_filter) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Price filter");
            final EditText begin = new EditText(this);
            final EditText end = new EditText(this);

            begin.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            end.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.addView(begin);
            ll.addView(end);
            alertDialog.setView(ll);

            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String value1 = begin.getText().toString().trim();
                    String value2 = end.getText().toString().trim();
                    filter(value1, value2);
                    prodItemAdapter = new ProdItem2Adapter(context, proid1ist, proName1ist,proDiscription1ist,proQuantity1ist,proPrize1ist,proimage1ist, prodItemArray);
                    custCategory.setAdapter(prodItemAdapter);
                    prodItemAdapter.notifyDataSetChanged();

                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alert = alertDialog.create();
            alert.show();
            return true;
        }

        return false;
    }
    public void Sort() {
        for (int i = 0; i < proPrize1ist.size(); i++) {

            for (int j = i + 1; j < proPrize1ist.size(); j++) {
                if (Double.parseDouble(proPrize1ist.get(i).toString()) > Double.parseDouble(proPrize1ist.get(j).toString())) {
                    Collections.swap(proPrize1ist, i, j);
                    Collections.swap(proid1ist, i, j);
                    Collections.swap(proName1ist, i, j);
                    Collections.swap(proQuantity1ist, i, j);
                    Collections.swap(proDiscription1ist, i, j);
                    Collections.swap(proimage1ist, i, j);
                }
            }

        }
    }


    public void filter(String value1, String value2) {
        for (int i = 0; i < proPrize1ist.size(); i++) {
            if ((Double.parseDouble(proPrize1ist.get(i).toString()) < Double.parseDouble(value1)) || (Double.parseDouble(proPrize1ist.get(i).toString()) > Double.parseDouble(value2))) {
                proPrize1ist.remove(i);
                proid1ist.remove(i);
                proName1ist.remove(i);
                proQuantity1ist.remove(i);
                proDiscription1ist.remove(i);
                proimage1ist.remove(i);
            }
        }

    }

}
