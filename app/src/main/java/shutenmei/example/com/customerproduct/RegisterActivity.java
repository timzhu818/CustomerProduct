package shutenmei.example.com.customerproduct;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends Activity implements View.OnClickListener{

    private static final String REGISTER_URL = "http://rjtmobile.com/ansari/regtestdriver.php";
    public static final String KEY_USERNAME = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE ="mobile";
    public static final String KEY_VEHICLE="vechile";
    public static final String KEY_LICENSE="license";
    public static final String KEY_CITY="city";


    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextMobile;
    private EditText editTextVehicle;
    private EditText editTextLicense;
    private EditText editTextCity;

    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header)));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle("Register");

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail= (EditText) findViewById(R.id.editTextEmail);
        editTextMobile=(EditText) findViewById(R.id.editTextMobile);
        editTextVehicle=(EditText) findViewById(R.id.editTextVehicle);
        editTextLicense=(EditText) findViewById(R.id.editTextLicense);
        editTextCity=(EditText) findViewById(R.id.editTextCity);


        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(this);
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

    private void registerUser(){
        final String name = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String mobile = editTextMobile.getText().toString().trim();
        final String vechile = editTextVehicle.getText().toString().trim();
        final String license = editTextLicense.getText().toString().trim();
        final String city = editTextCity.getText().toString().trim();

        String uri=String.format("http://rjtmobile.com/ansari/regtestdriver.php?name=%1$s&email=%2$s&mobile=%3$s&password=%4$s&vechile=%5$s&license=%6$s&city=%7$s",name,email,mobile,password,vechile,license,city);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (name.length()>10){
                            Toast.makeText(RegisterActivity.this, "Name should be less than 10 letter!", Toast.LENGTH_LONG).show();
                        }
                        else if (mobile.length()<10){
                            Toast.makeText(RegisterActivity.this, "Mobile should be at least 10 letter!", Toast.LENGTH_LONG).show();
                        }
                        else if (!isEmail(email)){
                            Toast.makeText(RegisterActivity.this, "Illegal email address input!", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
/*            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,name);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_EMAIL, email);
                params.put(KEY_MOBILE, mobile);
                params.put(KEY_VEHICLE, vechile);
                params.put(KEY_LICENSE, license);
                params.put(KEY_CITY, city);
                return params;
            }*/

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
            registerUser();
    }

    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }
}
