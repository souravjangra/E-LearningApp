package androiddev.com.elearning;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText emailText,passwordText;
    private Button signIn,register;

    private String FirstName="",LastName="";
    public static final String SHARED_PREFS = "sharedPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        emailText=(EditText) findViewById(R.id.et_StudentId);
        passwordText=(EditText) findViewById(R.id.et_Password);

        signIn = (Button) findViewById(R.id.signinBtn);
        register = (Button) findViewById(R.id.registerBtn);

    }

    public void Signin(View view){
        if(!validateFields()){
            onLoginFailed();
            return;
        }

        signIn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating user...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        onLoginSuccess();
                        progressDialog.dismiss();
                    }
                },3000);


    }

    public void onLoginSuccess() {

        String emailStr=emailText.getText().toString();
        String passStr=passwordText.getText().toString();
        checkUser(emailStr,passStr);
        signIn.setEnabled(true);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        signIn.setEnabled(true);
    }

    public Boolean validateFields() {
        boolean validation = true;

        String email = emailText.getText().toString();
        String pass = passwordText.getText().toString();

        // email validation
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailText.setError("enter a valid email address");
            validation = false;
        }
        else {
            emailText.setError(null);
        }

        // password validation
        if(pass.isEmpty() || pass.length() < 6 || pass.length() > 14) {
            passwordText.setError("password should be atleast 6 characters long");
            validation = false;
        }
        else {
            passwordText.setError(null);
        }

        return validation;
    }

    private void storeAndLaunch(String emailStr, String passStr) {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("id", emailStr);
        editor.putString("password", passStr);
        editor.putString("FirstName",FirstName);
        editor.putString("LastName",LastName);
        editor.apply();
//        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();

        Intent intent=new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(intent);
    }

    private void checkUser(final String idstr,final String pdstr) {


        FirstName="";
        LastName="";
        RequestQueue queue=Volley.newRequestQueue(this);
        Map<String,String> creds=new HashMap<String,String>();
        creds.put("Email",idstr);
        creds.put("Password",pdstr);
        JSONObject postJson=new JSONObject(creds);
//        Toast.makeText(this, postJson.toString(), Toast.LENGTH_SHORT).show();
        String url="https://apj-learning.herokuapp.com/signin";

        JsonObjectRequest objectRequest =new JsonObjectRequest(Request.Method.POST, url, postJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                result=response;
                try {
                    FirstName=response.getString("FirstName");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    LastName=response.getString("LastName");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                if(FirstName.length()!=0 && LastName.length()!=0){
                    storeAndLaunch(idstr,pdstr);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(LoginActivity.this, "Error from Volley", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(objectRequest);
    }

}
