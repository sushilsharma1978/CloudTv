package com.cloud6.pargmedia.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

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

import static android.content.Context.MODE_PRIVATE;

public class SignUpActivity extends AppCompatActivity {
AppCompatButton btn_signup;
AVLoadingIndicatorView avi_loader2;
SharedPreferences sp_signup;
TextView tv_loginn;
String userid;
SharedPreferences.Editor ed_signup;
TextInputEditText ed_email,ed_password,ed_mobile,ed_name,ed_contactname,ed_ntwoperator,ed_cableoperator;
String mac_address,ip_address;
    private String CountryIso;
    private String NetworkOperatorName;
    private String devicename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_sign_up);


        sp_signup=getSharedPreferences("SIGNUP",MODE_PRIVATE);
        ed_signup=sp_signup.edit();


        btn_signup=findViewById(R.id.btn_signup);
        ed_email=findViewById(R.id.ed_email);
        ed_password=findViewById(R.id.ed_password);
        ed_mobile=findViewById(R.id.ed_mobile);
        ed_contactname=findViewById(R.id.ed_contactname);
        ed_ntwoperator=findViewById(R.id.ed_ntwoperator);
        ed_cableoperator=findViewById(R.id.ed_cableoperator);
        ed_name=findViewById(R.id.ed_name);
        avi_loader2=findViewById(R.id.avi_loader2);
        tv_loginn=findViewById(R.id.tv_loginn);

        mac_address = getMacAddr();

        ip_address=getPublicIPAddress(getApplicationContext());


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
        //ip_address=getIPAddress(true );
       // Toast.makeText(this, "IP:"+ip_address, Toast.LENGTH_SHORT).show();

        NetworkOperatorName = mTelephonyMgr.getNetworkOperatorName();
        devicename = android.os.Build.MODEL;

       // CountryIso = mTelephonyMgr.getNetworkCountryIso();

//        Locale loc = new Locale("", CountryIso.toUpperCase());
       // CountryIso = loc.getDisplayCountry();
        CountryIso = getUserCountry(SignUpActivity.this);
      //  Toast.makeText(this, "Country::"+CountryIso, Toast.LENGTH_SHORT).show();

        tv_loginn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));

      finish();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_name.getText().toString().isEmpty()||
             ed_mobile.getText().toString().isEmpty()||ed_password.getText().toString().isEmpty()
                || ed_ntwoperator.getText().toString().isEmpty()){
                    Snackbar.make(v,"Fill all necessary fields",Snackbar.LENGTH_SHORT).show();
                }
                else {
                    avi_loader2.setVisibility(View.VISIBLE);
                signup_method(v);


                }
              //  startActivity(new Intent(SignUpActivity.this,MainActivity.class));
            }
        });
    }

    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        }
        catch (Exception e) { }
        return null;
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
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {

        } // for now eat exceptions
        return "";
    }

public void signup_method(final View view){

    String register_url="http://pargmedia.com/channel/webservice/user_register?name="+
            ed_name.getText().toString().trim()+ "&phone_no="+ed_mobile.getText().toString().trim()+
            "&password="+ed_password.getText().toString().trim()+"&network_name="+
            ed_ntwoperator.getText().toString().trim();
    RequestQueue rq= Volley.newRequestQueue(SignUpActivity.this);
    StringRequest srq=new StringRequest(Request.Method.GET, register_url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            try {
                JSONObject jsonObject=new JSONObject(response);
                 userid=jsonObject.getString("result");
                //Toast.makeText(SignUpActivity.this, ""+response, Toast.LENGTH_SHORT).show();
              //  Toast.makeText(SignUpActivity.this, ""+userid, Toast.LENGTH_SHORT).show();
                    storeDeviceCredentials();



            } catch (JSONException e) {
                e.printStackTrace();
                Snackbar.make(view,"Something went wrong..",Snackbar.LENGTH_SHORT).show();
                avi_loader2.setVisibility(View.GONE);
        }

           /* */
          //  Toast.makeText(SignUpActivity.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(SignUpActivity.this,MainActivity.class));
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            avi_loader2.setVisibility(View.GONE);
            Snackbar.make(view,"Something went wrong..",Snackbar.LENGTH_SHORT).show();

        }
    });
    rq.add(srq);
}

    private void storeDeviceCredentials() {

        String device_url="http://pargmedia.com/channel/webservice/user_address?macaddress="+mac_address+
                "&ipaddress="+ip_address+"&devicename="+devicename+"&operator="+NetworkOperatorName+
                "&country="+CountryIso+"&user_id="+userid+"&status=offline";
        Log.e("msgg", "storeDeviceCredentialss: "+device_url );
        RequestQueue rq= Volley.newRequestQueue(SignUpActivity.this);
        StringRequest srq=new StringRequest(Request.Method.GET, device_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject =new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    if(status.equals("1")){
                        avi_loader2.setVisibility(View.GONE);

                        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                        finish();
                    }
                    else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(SignUpActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(SignUpActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        rq.add(srq);

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
                    res1.append(String.format("%02X:",b));
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

    public String GetDeviceipMobileData(){
        try {
            for (java.util.Enumeration<java.net.NetworkInterface> en = java.net.NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                java.net.NetworkInterface networkinterface = en.nextElement();
                for (java.util.Enumeration<java.net.InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    java.net.InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Current IP", ex.toString());
        }
        return null;
    }


    public String GetDeviceipWiFiData(){
        android.net.wifi.WifiManager wm = (android.net.wifi.WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        @SuppressWarnings("deprecation")
        String ip = android.text.format.Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
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

}
