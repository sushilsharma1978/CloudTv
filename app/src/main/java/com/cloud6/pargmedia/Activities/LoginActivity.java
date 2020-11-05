package com.cloud6.pargmedia.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wang.avi.AVLoadingIndicatorView;
import com.cloud6.pargmedia.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;


public class LoginActivity extends AppCompatActivity {
    TextInputEditText edlg_email, edlg_password;
    AppCompatButton btn_login;
    TextView tv_forgotpassword;
    SharedPreferences sp_signup;
    SharedPreferences.Editor ed_signup;
    AVLoadingIndicatorView avi_loader3;
    String mac_address, ip_address;
    private String CountryIso;
    private String NetworkOperatorName;
    private String devicename, account_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.activity_login);
        btn_login = findViewById(R.id.btn_login);
        edlg_email = findViewById(R.id.edlg_email);
        edlg_password = findViewById(R.id.edlg_password);
        avi_loader3 = findViewById(R.id.avi_loader3);
        tv_forgotpassword = findViewById(R.id.tv_forgotpassword);

        sp_signup = getSharedPreferences("SIGNUP", MODE_PRIVATE);
        ed_signup = sp_signup.edit();

        try {
            if (getIntent().hasExtra("account_status")) {
                account_status = getIntent().getStringExtra("account_status");
                if (account_status.equalsIgnoreCase("deleted")) {
                    Toast.makeText(this, "Your account has been deleted", Toast.LENGTH_SHORT).show();
                } else if (account_status.equalsIgnoreCase("deactivated")) {
                    Toast.makeText(this, "Your account has been temporarily suspended", Toast.LENGTH_SHORT).show();

                }
            }

        } catch (Exception e) {
        }


        mac_address = getMacAddr();


        String ts = Context.TELEPHONY_SERVICE;
        TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(ts);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
       /* WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        ip_address = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());*/
        ip_address = getPublicIPAddress(getApplicationContext());


        NetworkOperatorName = mTelephonyMgr.getNetworkOperatorName();
        devicename = android.os.Build.MODEL;

        CountryIso = mTelephonyMgr.getNetworkCountryIso();

        Locale loc = new Locale("", CountryIso.toUpperCase());
        CountryIso = loc.getDisplayCountry();


//storeDeviceCredentials();


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edlg_email.getText().toString().isEmpty() || edlg_password.getText().toString().isEmpty()) {
                    Snackbar.make(v, "Fill all necessary fields", Snackbar.LENGTH_SHORT).show();

                } else {
                    avi_loader3.setVisibility(View.VISIBLE);
                    login_method(v);

                }
                // startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });

        tv_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                avi_loader3.setVisibility(View.VISIBLE);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                alertDialog.setTitle("FORGOT PASSWORD");
                alertDialog.setMessage("Enter Password");
                alertDialog.setCancelable(false);

                final EditText input = new EditText(LoginActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                // alertDialog.setIcon(R.drawable.key);

                alertDialog.setPositiveButton("SUBMIT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String forgotpassword_url = "http://pargmedia.com/channel/webservice/forgot_password_TV?email=" +
                                        input.getText().toString().trim();
                                RequestQueue rq = Volley.newRequestQueue(LoginActivity.this);
                                StringRequest srq = new StringRequest(Request.Method.GET, forgotpassword_url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String result = jsonObject.getString("message");
                                            if (result.equals("successfull")) {
                                                Snackbar.make(v, "Password has been sent to your email", Snackbar.LENGTH_SHORT).show();

                                                avi_loader3.setVisibility(View.GONE);
                                            } else {
                                                Snackbar.make(v, "Email is not being registered..please check again", Snackbar.LENGTH_SHORT).show();

                                                avi_loader3.setVisibility(View.GONE);

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Snackbar.make(v, "Something went wrong..", Snackbar.LENGTH_SHORT).show();
                                            avi_loader3.setVisibility(View.GONE);

                                        }

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Snackbar.make(v, "Something went wrong..Try again later", Snackbar.LENGTH_SHORT).show();
                                        avi_loader3.setVisibility(View.GONE);

                                    }
                                });
                                rq.add(srq);

                            }
                        });

                alertDialog.setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();

               /*  final EditText editText = new EditText(LoginActivity.this);
                LinearLayout linearLayout = (LinearLayout)dialog.findViewById(R.id.loading);
                int index = linearLayout.indexOfChild(linearLayout.findViewById(R.id.content_text));
                linearLayout.addView(editText, index+1);
                final SweetAlertDialog dialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("title")
                        .setContentText("content")
                        .setConfirmText("confirme")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                String text = editText.getText().toString();
                            }
            dialog.dismiss();
                        });
            });*/
            }
        });
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    public void login_method(final View view) {


        String loginurl = "http://pargmedia.com/channel/webservice/user_Login?phone_no="
                + edlg_email.getText().toString().trim() + "&password=" + edlg_password.getText().toString() +
                "&macaddress=" + mac_address + "&ipaddress=" + ip_address + "&devicename=" + devicename +
                "&country=" + CountryIso + "&status=online&operator=" + NetworkOperatorName;

        Log.e("msgg", "login_methodd: " + loginurl);

        RequestQueue rq = Volley.newRequestQueue(LoginActivity.this);
        StringRequest srq = new StringRequest(Request.Method.GET, loginurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("result");
                    String message = jsonObject.getString("message");
                    Log.e("msg", "onResponseLogin: " + response);
                    //     Toast.makeText(LoginActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                    if (message.equals("unsuccessfull,  Contact support @9659857985.")) {
                        Snackbar.make(view, "Please contact support @9659857985", Snackbar.LENGTH_SHORT).show();

                        avi_loader3.setVisibility(View.GONE);

                    } else {
                        if (result.equals("unsuccessfull")) {
                            Snackbar.make(view, "Incorrect email or password", Snackbar.LENGTH_SHORT).show();

                            avi_loader3.setVisibility(View.GONE);
                        } else {
                            JSONObject jObject = jsonObject.getJSONObject("result");
                            if (jObject.getString("approval_status")
                                    .equalsIgnoreCase("pending")) {
                                Snackbar.make(view, "TO ACTIVATE APP contact 9842422619 or 9842972619**\n*You will get User ID & password register mobile number*", Snackbar.LENGTH_SHORT).show();
                                avi_loader3.setVisibility(View.GONE);
                            } else {
                                if(jObject.getString("user_status")
                                        .equalsIgnoreCase("deactivated")){
                                    Snackbar.make(view, "Your account has been temporarily suspended", Snackbar.LENGTH_SHORT).show();
                                    avi_loader3.setVisibility(View.GONE);

                                }
                                else  if(jObject.getString("user_status")
                                        .equalsIgnoreCase("deleted")){
                                    Snackbar.make(view, "Your account has been deleted", Snackbar.LENGTH_SHORT).show();
                                    avi_loader3.setVisibility(View.GONE);

                                }
                                else if(jObject.getString("user_status")
                                        .equalsIgnoreCase("activated")){
                                    avi_loader3.setVisibility(View.GONE);

                                    String userid = jObject.getString("id");
                                    ed_signup.putString("userid", userid);
                                    ed_signup.putString("email", edlg_email.getText().toString());
                                    ed_signup.putString("password", edlg_password.getText().toString());
                                    ed_signup.commit();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }

                            }

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(view, "Something went wrong.." + e.toString(), Snackbar.LENGTH_SHORT).show();
                    avi_loader3.setVisibility(View.GONE);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(view, "Something went wrong.." + error.toString(), Snackbar.LENGTH_SHORT).show();
                avi_loader3.setVisibility(View.GONE);

            }
        });
        rq.add(srq);

    }

    public static String getPublicIPAddress(Context context) {
        //final NetworkInfo info = NetworkUtils.getNetworkInfo(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();

        RunnableFuture<String> futureRun = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                if ((info != null && info.isAvailable()) && (info.isConnected())) {
                    StringBuilder response = new StringBuilder();

                    try {
                        HttpURLConnection urlConnection = (HttpURLConnection) (
                                new URL("http://checkip.amazonaws.com/").openConnection());
                        urlConnection.setRequestProperty("User-Agent", "Android-device");
                        //urlConnection.setRequestProperty("Connection", "close");
                        urlConnection.setReadTimeout(15000);
                        urlConnection.setConnectTimeout(15000);
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setRequestProperty("Content-type", "application/json");
                        urlConnection.connect();

                        int responseCode = urlConnection.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_OK) {

                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }

                        }
                        urlConnection.disconnect();
                        return response.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //Log.w(TAG, "No network available INTERNET OFF!");
                    return null;
                }
                return null;
            }
        });

        new Thread(futureRun).start();

        try {
            return futureRun.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    // res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }
}
