package shutenmei.example.com.customerproduct;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import shutenmei.example.com.customerproduct.utils.ProdItem;
import shutenmei.example.com.customerproduct.utils.ProdItemAdapter;

public class SubProductActivity extends Activity {

    // declare dbhelper object
    static DBhelper dbhelper;
    SQLiteDatabase db;

    private Button btnAdd;


    private ListView custCategory;
    private ProdItemAdapter prodItemAdapter;
    private ArrayList<ProdItem> prodItemArray;
    private ProgressDialog pDialog;

    private String id;
    private String productname;
    private String productimage;
    private String productdiscription;
    private String productprize;
    private String productquantity;
    private double Menu_price;

    private TextView txtCount;
    int cart_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_product);
        id = getIntent().getStringExtra("Id");
        productname=getIntent().getStringExtra("Name");
        productimage=getIntent().getStringExtra("Image");
        productdiscription=getIntent().getStringExtra("Discription");
        productprize=getIntent().getStringExtra("Price");
        productquantity=getIntent().getStringExtra("Quantity");

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header)));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle(productname);

        Menu_price=Double.parseDouble(productprize);
        dbhelper = new DBhelper(this);

        try{
            dbhelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }

        cart_count=dbhelper.getAllData().size();

        btnAdd=(Button) findViewById(R.id.btn_add);
        custCategory = (ListView) findViewById(R.id.list);
        prodItemArray = new ArrayList<>();

        prodItemAdapter = new ProdItemAdapter(this, R.layout.subprod_item_layout, prodItemArray);
        custCategory.setAdapter(prodItemAdapter);

        pDialog = new ProgressDialog(this);

        pDialog.setMessage("Loading...");
        pDialog.show();

        //Calling the getData method
        makeJsonArrayRequest();


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show input dialog
                inputDialog();
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        final View menu_shop = menu.findItem(R.id.cart).getActionView();
        menu_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iMyOrder = new Intent(SubProductActivity.this, CartActivity.class);
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
                Intent iMyOrder = new Intent(SubProductActivity.this, CartActivity.class);
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

        hidePDialog();

        ProdItem prodItem = new ProdItem(id, productimage, productname, productdiscription, productprize, productquantity);
        prodItemArray.add(prodItem);

        prodItemAdapter.notifyDataSetChanged();


    }
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    void inputDialog(){

/*        try {
            dbhelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }*/
        // open database first

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Order");
        alert.setMessage("Number of order");
        alert.setCancelable(false);
        final EditText edtQuantity= new EditText(this);
        int maxLength = 3;
        edtQuantity.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        edtQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(edtQuantity);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String temp = edtQuantity.getText().toString();
                int quantity = 0;

                // when add button clicked add menu to order table in database
                if (!temp.equalsIgnoreCase("")) {
                    quantity = Integer.parseInt(temp);
                    if (dbhelper.isDataExist(id)) {
                        dbhelper.updateData(id, quantity, (Menu_price * quantity));
                    } else {
                        dbhelper.addData(id, productname, quantity, (Menu_price * quantity));
                    }
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(getApplicationContext(), "Add " + temp + " item to cart", Toast.LENGTH_LONG).show();
                } else {
                    dialog.cancel();
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                // when cancel button clicked close dialog
                dialog.cancel();
            }
        });

        alert.show();
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
