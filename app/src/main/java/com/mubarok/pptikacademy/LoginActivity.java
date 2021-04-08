package com.mubarok.pptikacademy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    Button mBtn_signin;
    Intent a;
    TextInputLayout mTxt_username, mTxt_password;
    String url, success;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarLogin = (Toolbar)findViewById(R.id.toolbar_signin);
        setSupportActionBar(ToolBarLogin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sessionManager = new SessionManager(getApplicationContext());
        Toast.makeText(getApplicationContext(),
                "User Login Status: " + sessionManager.isLoggedIn(), Toast.LENGTH_LONG).show();

        //inisialisasi button & text input
        mBtn_signin = (Button) findViewById(R.id.btnsignin2);
        mTxt_username = (TextInputLayout) findViewById(R.id.textInputUsernameL);
        mTxt_password = (TextInputLayout) findViewById(R.id.textInputPasswordL);

        //function button
        mBtn_signin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                url = "http://192.168.43.206/pptik-academy-android/login.php?" + "username=" + mTxt_username.getEditText().getText().toString() + "&password=" + mTxt_password.getEditText().getText().toString();
                if (mTxt_username.getEditText().getText().toString().trim().length() > 0 && mTxt_password.getEditText().getText().toString().trim().length() > 0) {
                    new Masuk(getApplicationContext()).execute();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Username/password masih kosong gan.!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public class Masuk extends AsyncTask<String, String, String> {
        ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
        ProgressDialog pDialog;
        android.content.Context context;
        public Masuk (android.content.Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... arg0) {
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url);
            try {
                success = json.getString("success");
                Log.e("error", "nilai sukses=" + success);

                if (success.equals("1")) {
                    JSONArray hasil = json.getJSONArray("login");
                    for (int i = 0; i < hasil.length(); i++) {
                        JSONObject c = hasil.getJSONObject(i);
                        String name = c.getString("nama_siswa").trim();
                        String username = c.getString("username").trim();
                        String password = c.getString("password").trim();
                        String email = c.getString("email").trim();
                        String city = c.getString("kota").trim();
                        String country = c.getString("negara").trim();
                        String id = c.getString("id_siswa").trim();
                        sessionManager.createLoginSession(name, username, password, email, city, country, id);
                        Log.e("ok", " ambil = data");
                    }
                } else {
                    Log.e("erro", "tidak bisa ambil data 0");
                }
            } catch (Exception e) {
                // TODO: handle exception

                e.printStackTrace();
                Log.e("erro", "tidak bisa ambil data 1");
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (success!=null && success.equals("1")) {
                a = new Intent(LoginActivity.this, CourseActivity.class);
                startActivity(a);
                finish();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Username/password salah gan.!!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
