package shutenmei.example.com.customerproduct;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class ActivityCheckout extends Activity {

    Button button_paypal;
    EditText editpayemail;
    TextView TotalPrice;

    DBhelper dbhelper;

    ArrayList<ArrayList<Object>> data;
    public static ArrayList<Double> Sub_total_price = new ArrayList<Double>();
    double Total_price;
    DecimalFormat formatData = new DecimalFormat("#.##");

    private String price;

    private static final int REQUEST_CODE_PAYMENT=1;

    private static PayPalConfiguration paypalConfig=new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Util.paypal_sdk_id)
            .acceptCreditCards(true)
            .merchantName("Code_Crash")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.paypal.com/webapps/mpp/ua/privacy-full"))
            .merchantUserAgreementUri(Uri.parse("https://www.paypal.com/webapps/mpp/ua/useragreement-full"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_checkout);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header)));
        bar.setTitle("Make Payment");
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

        Intent intent=new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);

        startService(intent);

        dbhelper = new DBhelper(this);

        try{
            dbhelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }

        getDataFromDatabase();

        price=String.valueOf(Total_price);

        TotalPrice = (TextView) findViewById(R.id.totalprice);
        TotalPrice.setText("Final Price : " + price+ "$");
        editpayemail=(EditText)findViewById(R.id.paypal_email);

        button_paypal=(Button)findViewById(R.id.buttonPaypal);
        button_paypal.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
                lauchPayPalPayment();
            }

        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void lauchPayPalPayment(){
        dbhelper.transferdata();
        dbhelper.deleteAllData();
        dbhelper.close();
        startActivity(new Intent(getApplicationContext(), OrderHistoryActivity.class));
        finish();
/*        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(price),"USD",editpayemail.getText().toString(),PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent= new Intent(ActivityCheckout.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,paypalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,thingToBuy);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==REQUEST_CODE_PAYMENT){
            if(resultCode== Activity.RESULT_OK){
                Toast.makeText(getApplicationContext(),"Payment done Successfully",Toast.LENGTH_LONG).show();
                /*dbhelper.deleteAllData();*/
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            else if (resultCode==Activity.RESULT_CANCELED){
                Toast.makeText(getApplicationContext(),"Payment canceled",Toast.LENGTH_LONG).show();
            }
            else if (resultCode==PaymentActivity.RESULT_EXTRAS_INVALID){
                Toast.makeText(getApplicationContext(),"Payment failed, please try again",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void getDataFromDatabase(){

        Total_price = 0;
        Sub_total_price.clear();
        data = dbhelper.getAllData();

        // store data to arraylist variables
        for(int i=0;i<data.size();i++){
            ArrayList<Object> row = data.get(i);

            Sub_total_price.add(Double.parseDouble(formatData.format(Double.parseDouble(row.get(3).toString()))));
            Total_price += Sub_total_price.get(i);
        }

        // count total order
        Total_price = Double.parseDouble(formatData.format(Total_price));
    }

}
