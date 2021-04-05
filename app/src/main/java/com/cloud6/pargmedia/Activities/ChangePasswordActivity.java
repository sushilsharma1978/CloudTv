package com.cloud6.pargmedia.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.wang.avi.AVLoadingIndicatorView;
import com.cloud6.pargmedia.R;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


public class ChangePasswordActivity extends AppCompatActivity {
    TextInputEditText edlg_oldpassword,edlg_newpassword;
    AppCompatButton btn_submit;
    AVLoadingIndicatorView avi_loader6;
    SharedPreferences sp_signup;
    SharedPreferences.Editor ed_signup;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.activity_change_password);
        sp_signup=getSharedPreferences("SIGNUP",MODE_PRIVATE);
        ed_signup=sp_signup.edit();
        userid=sp_signup.getString("userid","");


        btn_submit=findViewById(R.id.btn_submit);
        edlg_oldpassword=findViewById(R.id.edlg_oldpassword);
        edlg_newpassword=findViewById(R.id.edlg_newpassword);
        avi_loader6=findViewById(R.id.avi_loader6);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edlg_oldpassword.getText().toString().isEmpty() || edlg_newpassword.getText().toString().isEmpty()){
                    Snackbar.make(v,"Fill all necessary fields",Snackbar.LENGTH_SHORT).show();

                }
                else if(edlg_oldpassword.getText().toString().trim().
                        equals(edlg_newpassword.getText().toString().trim())){
                    Snackbar.make(v,"Old and new password cant be same",Snackbar.LENGTH_SHORT).show();

                }
                else {
                    avi_loader6.setVisibility(View.VISIBLE);
                    change_password(v);

                }
                // startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });
    }

    public void change_password(final View view){


        String changepassword_url="http://pargmedia.com/channel/webservice/change_password_TV?user_id="+userid+"&old_password="
                +edlg_oldpassword.getText().toString().trim()+"&password="+edlg_newpassword.getText().toString().trim();

        Log.e("msgg", "login_methodd: "+changepassword_url);

        RequestQueue rq= Volley.newRequestQueue(ChangePasswordActivity.this);
        StringRequest srq=new StringRequest(Request.Method.GET, changepassword_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject =new JSONObject(response);
                    String result=jsonObject.getString("result");
                    String message=jsonObject.getString("message");


                        if(result.equals("old password is not match")){
                            Snackbar.make(view,"Incorrect old password",Snackbar.LENGTH_SHORT).show();

                            avi_loader6.setVisibility(View.GONE);
                        }
                        else {
                            avi_loader6.setVisibility(View.GONE);
                            Snackbar.make(view,"Your password has been changed successfully",Snackbar.LENGTH_SHORT).show();
                        edlg_newpassword.setText("");
                        edlg_oldpassword.setText("");
                        }





                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(view,"Something went wrong..",Snackbar.LENGTH_SHORT).show();
                    avi_loader6.setVisibility(View.GONE);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(view,"Something went wrong..",Snackbar.LENGTH_SHORT).show();
                avi_loader6.setVisibility(View.GONE);

            }
        });
        rq.add(srq);

    }
}
