package com.cloud6.pargmedia.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cloud6.pargmedia.Result.CategoryResult;
import com.cloud6.pargmedia.database.ChannelDatabase;
import com.cloud6.pargmedia.database.ChannelEntity;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.cloud6.pargmedia.App.AppConfig;
import com.cloud6.pargmedia.R;
import com.cloud6.pargmedia.Response.ChanelResponse;
import com.cloud6.pargmedia.Result.ChanelResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    AudioManager audioManager;
    LinearLayout ll_prp_video;
    PictureInPictureParams.Builder pictureInPictureParamsBuilder;
    private Context mContext = this;
    EditText ed_search_store;
    ImageView iv_channel, iv_channel1;
    private RecyclerView rv_Mneu, rv_Mneu1,rv_category;
    ArrayList<String> langlist = new ArrayList<>();
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private boolean isOutSideClicked;
    private NavAdapter navAdapter;
    private CatAdapter catAdapter;
    private BottomNavAdapter bottomNavAdapter;
    ACProgressFlower progressDialog1;
    // Spinner sp_language;
    List<ChanelResult> list = new ArrayList<>();
    List<CategoryResult> catlist = new ArrayList<>();
    ConstraintLayout layout_constraint;
    LinearLayout layout_squeeze, ll_navbar;
    // String selected_language="Tamil";
    // /root/.android/avd/Android_TV_720p_API_Q.avd
    //Stv@.123456
    private String url = "http://7starcloud.com:1935/jawahartv/jawahartv/playlist.m3u8";
    private BottomSheetDialog mBottomSheetDialog;
    //private List<ChanelResult> list;
    private VideoView video, video1;
    private String imsi;
    private String imei;
    private String CountryIso, mac_address, ip_address;
    private String NetworkOperator;
    private String NetworkOperatorName;
    private String devicename;
    private String BRAND, x;
    private RelativeLayout lay_bottam;
    LinearLayout lay_bottam1;
    private int posi1 = 0,posi = 0, s = 0,s1=0;
    TextView tv_bitrate, tv_resolution, tv_resolution1, tv_standard1, tv_standard;
    // ACProgressFlower pd_newvideo;
    private ImageView img_logo, img_logo1;
    private TextView txt_name, txt_name1, txt_chno, txt_chno1, txt_time, txt_time1,
            txt_error, txt_error1,tv_fullscreen;
    boolean checkrestart1 = false;
    boolean galleryviewclick=false;
     boolean doubleBackToExitPressedOnce = false;
    // ImageView txt_changepassword;

    boolean checked;
    SharedPreferences sp_signup;
    SharedPreferences.Editor ed_signup;
   // String userid,email,password;
    boolean doubleclick, imageclick, clickupdown = false;
    private long mUpKeyEventTime = 0;
    Thread thread;
    ImageView iv_back;
    LinearLayout tv_galleryview,tv_listview;
    boolean checkserver=false;
    ChannelDatabase mchannelDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_main);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mchannelDatabase=ChannelDatabase.getInstance(getApplicationContext());
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        /* pd_newvideo = new ACProgressFlower.Builder(MainActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading...")
                .fadeColor(Color.DKGRAY).build();*/
        sp_signup = getSharedPreferences("SIGNUP", MODE_PRIVATE);
        ed_signup = sp_signup.edit();

     //   userid = sp_signup.getString("userid", "");
     //   email = sp_signup.getString("email","");
       // password = sp_signup.getString("password", "");


        txt_name1 = findViewById(R.id.txt_name1);
        txt_chno1 = findViewById(R.id.txt_chno1);
        ll_prp_video = findViewById(R.id.ll_prp_video);
        tv_bitrate = findViewById(R.id.tv_bitrate);
        //iv_back = findViewById(R.id.iv_back);
        tv_galleryview = findViewById(R.id.tv_galleryview);
        //tv_gallery1= findViewById(R.id.tv_gallery1);
        tv_listview = findViewById(R.id.tv_listview);
       // tv_list1= findViewById(R.id.tv_list1);
        tv_fullscreen = findViewById(R.id.tv_fullscreen);
        finds();
        finds_view();
        tv_fullscreen.setPaintFlags(tv_fullscreen.getPaintFlags() |  Paint.UNDERLINE_TEXT_FLAG);
//        tv_galleryview.setColorFilter(getApplicationContext().getResources().getColor(R.color.colorWhite));
//        tv_listview.setColorFilter(getApplicationContext().getResources().getColor(R.color.colorWhite));
        //checkLogin();

        getCatList();


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) video.getLayoutParams();
        params.width = 3500;
        params.height = 1200;
        params.leftMargin = 0;
        params.topMargin = 0;
        params.rightMargin = 0;
        video.setLayoutParams(params);

        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) iv_channel.getLayoutParams();
        params1.width = 3500;
        params1.height = 1200;
        params1.leftMargin = 0;
        iv_channel.setLayoutParams(params1);
        iv_channel.invalidate();


 /*       RelativeLayout.LayoutParams videoviewlp = new
                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
                    video.setLayoutParams(videoviewlp);
        video.invalidate();*/

        final Handler handler1 = new Handler();
        final int delay = 3000; //milliseconds

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            if (!isNetworkConn()) {
                txt_error.setVisibility(View.VISIBLE);
                txt_error.setText("Waiting for network connection");
                txt_error1.setVisibility(View.VISIBLE);
                txt_error1.setText("Waiting for network connection");
            }
        } else {
            if (!NetworkUtils.isNetworkConnected(MainActivity.this)) {
                txt_error.setVisibility(View.VISIBLE);
                txt_error.setText("Waiting for network connection");
                txt_error1.setVisibility(View.VISIBLE);
                txt_error1.setText("Waiting for network connection");
            }
        }

        Log.e("Error", "-----> two");
        if (!sp_signup.getString("channel_position", "").equals("")) {
            try {
                /*final ACProgressFlower progressDialog = new ACProgressFlower.Builder(MainActivity.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(Color.WHITE)
                        .text("Loading...")
                        .fadeColor(Color.DKGRAY).build();
                progressDialog.show();*/
                s = Integer.parseInt(sp_signup.getString("channel_position", ""));


                RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
                StringRequest sr = new StringRequest(Request.Method.POST, "http://7starcloud.in/tamilcloud/webservice/get_channel", new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();
                        Log.e("msg", "Channel_Response: " + response);
                        try {

                            //Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                            JSONObject object = new JSONObject(response);
                            System.out.println("Login" + object);
                            if (object.getString("status").equals("1")) {
                                Gson gson = new Gson();
                                ChanelResponse successResponse = gson.fromJson(response, ChanelResponse.class);
                                list = successResponse.getResult();
                                //encryptedchannels();

                                navAdapter = new NavAdapter(mContext, list,false);
                                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                                manager.setOrientation(LinearLayoutManager.VERTICAL);
                                rv_Mneu.setLayoutManager(manager);
                                rv_Mneu.setAdapter(navAdapter);

                              /*  LinearLayoutManager manager1 = new LinearLayoutManager(mContext);
                                manager1.setOrientation(LinearLayoutManager.VERTICAL);
                                rv_Mneu1.setLayoutManager(manager1);
                                rv_Mneu1.setAdapter(navAdapter);*/

                               // rv_Mneu1.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL,false));
                                rv_Mneu1.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                                rv_Mneu1.setAdapter(navAdapter);

                                catAdapter=new CatAdapter(mContext,catlist,false);
                                rv_category.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                                rv_category.setAdapter(catAdapter);

                                lay_bottam.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        lay_bottam.setVisibility(View.GONE);
                                        //  lay_bottam1.setVisibility(View.GONE);

                                    }
                                }, 7000);

                               // if (list.get(s).getSubscription().equals("1")) {
                                    tv_standard.setText("");
                                    tv_standard1.setText("");
                                    tv_resolution.setText("Loading..");
                                    tv_resolution1.setText("Loading..");
                                tv_bitrate.setText("");

                                txt_name.setText(list.get(s).getChannelName());
                                    txt_name1.setText(list.get(s).getChannelName());
                                    txt_chno1.setText(list.get(s).getChannelNumber());
                                    txt_chno.setText(list.get(s).getChannelNumber());
                                    video1.setVisibility(View.GONE);
                                    video.setVisibility(View.GONE);
                                    iv_channel.setVisibility(View.VISIBLE);
                                    iv_channel.setImageResource(R.drawable.paid_channel);
                                //} else {
                                    loadUrl(list.get(s).getChannelUrl(),
                                            list.get(s).getChannelName(), list.get(s).getImage(),
                                            list.get(s).getChannelNumber(), s);
                              //  }

                                posi = s;
                                rv_Mneu.smoothScrollToPosition(s);
                                rv_Mneu1.smoothScrollToPosition(s);
                                navAdapter.notifyDataSetChanged();
                                try {
                                    if (s == posi) {
                                        if (navAdapter.lastClickLay == null) {
                                            // Toast.makeText(context, "null"+i, Toast.LENGTH_SHORT).show();

                                            navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                            navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                        } else {
                                            // Toast.makeText(context, "not_null"+i, Toast.LENGTH_SHORT).show();
                                            navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                            navAdapter.lastClickLay.setBackgroundResource(R.drawable.channel_back);
                                            navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                        }
                                    } else {
                                        navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.channel_back);
                                        //lastClickLay.setBackgroundResource(R.drawable.bg_side_unsel);
                                        navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;

                                    }

                                } catch (Exception e) {
                                    //   Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                txt_error.setVisibility(View.VISIBLE);
                                txt_error.setText("No channel exists");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map=new HashMap<>();
                        map.put("cat_id","1");
                        return map;
                    }
                };
                rq.add(sr);


                /*Call<ResponseBody> call = AppConfig.loadInterface().get_userchannel(userid);
                call.enqueue(new Callback<ResponseBody>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        progressDialog.dismiss();
                        Log.e("msg", "Channel_Response: " + response.body().toString());
                        try {
                            if (response.isSuccessful()) {
                                String responseData = response.body().string();
                                //Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                                JSONObject object = new JSONObject(responseData);
                                System.out.println("Login" + object);
                                if (object.getString("status").equals("1")) {
                                    Gson gson = new Gson();
                                    ChanelResponse successResponse = gson.fromJson(responseData, ChanelResponse.class);
                                    list = successResponse.getResult();
                                    //encryptedchannels();

                                    navAdapter = new NavAdapter(mContext, list);
                                    LinearLayoutManager manager = new LinearLayoutManager(mContext);
                                    manager.setOrientation(LinearLayoutManager.VERTICAL);
                                    rv_Mneu.setLayoutManager(manager);
                                    rv_Mneu.setAdapter(navAdapter);

                                    LinearLayoutManager manager1 = new LinearLayoutManager(mContext);
                                    manager1.setOrientation(LinearLayoutManager.VERTICAL);
                                    rv_Mneu1.setLayoutManager(manager1);
                                    rv_Mneu1.setAdapter(navAdapter);

                                    //loadUrl("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample.mp4"
                                    //           , list.get(0).getChannelName(), list.get(0).getImage(), 0);

                                    lay_bottam.setVisibility(View.VISIBLE);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            lay_bottam.setVisibility(View.GONE);
                                            //  lay_bottam1.setVisibility(View.GONE);

                                        }
                                    }, 7000);
                                    loadUrl(list.get(s).getChannelUrl(),
                                            list.get(s).getChannelName(), list.get(s).getImage(),
                                            s);
                                    posi = s;
                                    rv_Mneu.smoothScrollToPosition(s);
                                    rv_Mneu1.smoothScrollToPosition(s);
                                    navAdapter.notifyDataSetChanged();
                                    try {
                                        if (s == posi) {
                                            if (navAdapter.lastClickLay == null) {
                                                // Toast.makeText(context, "null"+i, Toast.LENGTH_SHORT).show();

                                                navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                                navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                            } else {
                                                // Toast.makeText(context, "not_null"+i, Toast.LENGTH_SHORT).show();
                                                navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                                navAdapter.lastClickLay.setBackgroundResource(R.drawable.bg_side_unsel);
                                                navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                            }
                                        } else {
                                            navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_unsel);
                                            //lastClickLay.setBackgroundResource(R.drawable.bg_side_unsel);
                                            navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;

                                        }

                                    } catch (Exception e) {
                                        //   Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    txt_error.setVisibility(View.VISIBLE);
                                    txt_error.setText("No channel exists");
                                }

                            } else {
                                Log.e("response", "" + response.message());
                            }

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        Log.e("msg", "Channel_Response1: " + t.toString());

                        progressDialog.dismiss();
                    }
                });*/
            } catch (Exception e) {
            }
        } else {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                if (isNetworkConn()) {
                    txt_error.setVisibility(View.GONE);
                    iv_channel.setVisibility(View.GONE);

                    txt_error1.setVisibility(View.GONE);
                    iv_channel1.setVisibility(View.GONE);
                    handler1.removeCallbacksAndMessages(null);
                    Log.e("Error", "-----> if");
                    get_all_request("1");
                }

            } else {
                if (NetworkUtils.isNetworkConnected(MainActivity.this)) {
                    txt_error.setVisibility(View.GONE);
                    iv_channel.setVisibility(View.GONE);
                    txt_error1.setVisibility(View.GONE);
                    iv_channel1.setVisibility(View.GONE);
                    handler1.removeCallbacksAndMessages(null);
                    Log.e("Error", "-----> if");
                    get_all_request("1");

                }
            }
        }

        runOnUiThread(new Runnable() {
            public void run() {
                list.clear();
                // Update UI here when network is available.
                checkserver=isURLReachable(getApplicationContext());
                if(checkserver){
                   // Toast.makeText(getApplicationContext(), "Server URL Available", Toast.LENGTH_SHORT).show();
                }
                else{
                    List<ChannelEntity> dblist=   mchannelDatabase.getChannelDao().getList();
                    for(int x=0;x< dblist.size();x++){
                        list.add(new ChanelResult(dblist.get(x).getChannelid(),
                                dblist.get(x).getChannel_Category_id(),dblist.get(x).getChannelNumber(),dblist.get(x).getChannelName(),
                                dblist.get(x).getChannelUrl(),dblist.get(x).getUserName(),dblist.get(x).getImage(),
                                dblist.get(x).getSocialId(),dblist.get(x).getLat(),dblist.get(x).getLon(),
                                dblist.get(x).getRegisterId(),dblist.get(x).getIosRegisterId(),dblist.get(x).getStatus(),
                                dblist.get(x).getDateTime(),dblist.get(x).getAbout(),dblist.get(x).getChannelCategoryName()));

                        Log.e("msg","NotList Number::"+dblist.get(x).getChannelNumber()+
                                ", Note name::"+dblist.get(x).getChannelName()+" , Note Id::"+dblist.get(x).getChannelid());
                    }
                   // Toast.makeText(getApplicationContext(), "Server URL Not Available"+, Toast.LENGTH_SHORT).show();
                }
            }
        });

        getData();

        iv_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageclick == false) {
                    try {
                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);


                        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) iv_channel.getLayoutParams();
                        Display display = getWindowManager().getDefaultDisplay();
                        int width1 = display.getWidth();
                        int height1 = display.getHeight();
                        double ratio1 = ((float) (width1)) / 39.0;
                        double ratio2 = ((float) (height1)) / 36.0;
                        int width2 = (int) (ratio1 * 20);
                        int height2 = (int) (ratio2 * 20);
                        params1.width = width2;
                        params1.height = height2;
                        params1.rightMargin = 20;
                        params1.topMargin = 20;
                        iv_channel.setLayoutParams(params1);

                        ll_navbar.setVisibility(View.GONE);
                        lay_bottam.setVisibility(View.GONE);
                        // video.setVisibility(View.VISIBLE);
                        layout_squeeze.setVisibility(View.VISIBLE);
                        video1.setVisibility(View.GONE);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        imageclick = true;

                    } catch (Exception e) {
                    }
                } else if (imageclick == true) {
                    try {
                        // drawer.closeDrawer(Gravity.LEFT);
                        // Animation slideUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_down);

                        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                iv_channel.getLayoutParams();
                        params1.width = 3500;
                        params1.height = 1200;
                        params1.rightMargin = 0;
                        params1.topMargin = 0;
                        iv_channel.setLayoutParams(params1);

                        layout_squeeze.setVisibility(View.GONE);
                        video1.setVisibility(View.GONE);
                        iv_channel1.setVisibility(View.GONE);

                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

                        imageclick = false;


                    } catch (Exception e) {
                    }
                }
            }
        });
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            layout_constraint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    video.setVisibility(View.VISIBLE);
                    video1.setVisibility(View.VISIBLE);
                    video.setBackgroundColor(Color.TRANSPARENT);
                    video1.setBackgroundColor(Color.TRANSPARENT);
                    if (doubleclick == false) {
                        try {
                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            if (iv_channel.getVisibility() == View.VISIBLE) {
                                iv_channel1.setVisibility(View.GONE);
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.topMargin = 20;
                                params1.rightMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {

                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) video.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 37.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params.width = width2;
                                params.height = height2;
//                            params.width = (int)(380*metrics.density);
//                            params.height = (int)(280*metrics.density);
                                params.topMargin = 20;
                                params.rightMargin = 20;
                                video.setLayoutParams(params);
                            }

                            ll_navbar.setVisibility(View.GONE);
                            lay_bottam.setVisibility(View.GONE);
                            video.setVisibility(View.VISIBLE);
                            layout_squeeze.setVisibility(View.VISIBLE);
                            video1.setVisibility(View.VISIBLE);
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            doubleclick = true;


//                    if (!drawer.isDrawerOpen(Gravity.START)) drawer.openDrawer(Gravity.START);
//                    else drawer.closeDrawer(Gravity.END);


                        } catch (Exception e) {
                        }
                    } else if (doubleclick == true) {
                        try {
                            // drawer.closeDrawer(Gravity.LEFT);
                            // Animation slideUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_down);
                            if (iv_channel.getVisibility() == View.VISIBLE) {
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.leftMargin = 0;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                                        video.getLayoutParams();
                                params.width = 3500;
                                params.height = 1200;
                                params.leftMargin = 0;
                                params.topMargin = 0;
                                params.rightMargin = 0;
                                video.setLayoutParams(params);
                            }

                            layout_squeeze.setVisibility(View.GONE);
                            video1.setVisibility(View.GONE);
                            iv_channel1.setVisibility(View.GONE);
                            ll_navbar.setVisibility(View.VISIBLE);
                            video.setVisibility(View.VISIBLE);

                            layout_constraint.setVisibility(View.VISIBLE);

                            getWindow().addFlags(
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            getWindow().clearFlags(
                                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

                            doubleclick = false;


                        } catch (Exception e) {
                        }
                    }


                }
            });
        }
        else{
            video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    video.setVisibility(View.VISIBLE);
                    video1.setVisibility(View.VISIBLE);
                    video.setBackgroundColor(Color.TRANSPARENT);
                    video1.setBackgroundColor(Color.TRANSPARENT);
                    if (doubleclick == false) {
                        try {
                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            if (iv_channel.getVisibility() == View.VISIBLE) {
                                iv_channel1.setVisibility(View.GONE);
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.topMargin = 20;
                                params1.rightMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {

                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) video.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 37.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params.width = width2;
                                params.height = height2;
//                            params.width = (int)(380*metrics.density);
//                            params.height = (int)(280*metrics.density);
                                params.topMargin = 20;
                                params.rightMargin = 20;
                                video.setLayoutParams(params);
                            }

                            ll_navbar.setVisibility(View.GONE);
                            lay_bottam.setVisibility(View.GONE);
                            video.setVisibility(View.VISIBLE);
                            layout_squeeze.setVisibility(View.VISIBLE);
                            video1.setVisibility(View.VISIBLE);
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            doubleclick = true;


//                    if (!drawer.isDrawerOpen(Gravity.START)) drawer.openDrawer(Gravity.START);
//                    else drawer.closeDrawer(Gravity.END);


                        } catch (Exception e) {
                        }
                    } else if (doubleclick == true) {
                        try {
                            // drawer.closeDrawer(Gravity.LEFT);
                            // Animation slideUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_down);
                            if (iv_channel.getVisibility() == View.VISIBLE) {
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.leftMargin = 0;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                                        video.getLayoutParams();
                                params.width = 3500;
                                params.height = 1200;
                                params.leftMargin = 0;
                                params.topMargin = 0;
                                params.rightMargin = 0;
                                video.setLayoutParams(params);
                            }

                            layout_squeeze.setVisibility(View.GONE);
                            video1.setVisibility(View.GONE);
                            iv_channel1.setVisibility(View.GONE);
                            ll_navbar.setVisibility(View.VISIBLE);
                            video.setVisibility(View.VISIBLE);

                            layout_constraint.setVisibility(View.VISIBLE);

                            getWindow().addFlags(
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            getWindow().clearFlags(
                                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

                            doubleclick = false;


                        } catch (Exception e) {
                        }
                    }


                }
            });
        }





        video1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);


                if (iv_channel.getVisibility() == View.VISIBLE) {
                    RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                            iv_channel.getLayoutParams();
                    params1.width = 3500;
                    params1.height = 1200;
                    params1.leftMargin = 0;
                    iv_channel.setLayoutParams(params1);
                } else {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                            video.getLayoutParams();
                    params.width = 3500;
                    params.height = 1200;
                    params.leftMargin = 0;
                    params.topMargin = 0;
                    params.rightMargin = 0;
                    video.setLayoutParams(params);
                }
              //  hideKeyboard();
                layout_squeeze.setVisibility(View.GONE);
                video1.setVisibility(View.GONE);
                iv_channel1.setVisibility(View.GONE);
                ll_navbar.setVisibility(View.VISIBLE);
                layout_constraint.setVisibility(View.VISIBLE);
                video.setVisibility(View.VISIBLE);

                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);


            }
        });

        tv_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.setVisibility(View.VISIBLE);
                video1.setVisibility(View.VISIBLE);
                video.setBackgroundColor(Color.TRANSPARENT);
                video1.setBackgroundColor(Color.TRANSPARENT);
                if (doubleclick == false) {
                    try {
                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);

                        if (iv_channel.getVisibility() == View.VISIBLE) {
                            iv_channel1.setVisibility(View.GONE);
                            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) iv_channel.getLayoutParams();
                            Display display = getWindowManager().getDefaultDisplay();
                            int width1 = display.getWidth();
                            int height1 = display.getHeight();
                            double ratio1 = ((float) (width1)) / 39.0;
                            double ratio2 = ((float) (height1)) / 36.0;
                            int width2 = (int) (ratio1 * 20);
                            int height2 = (int) (ratio2 * 20);
                            params1.width = width2;
                            params1.height = height2;
                            params1.topMargin = 20;
                            params1.rightMargin = 20;
                            iv_channel.setLayoutParams(params1);
                        } else {

                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) video.getLayoutParams();
                            Display display = getWindowManager().getDefaultDisplay();
                            int width1 = display.getWidth();
                            int height1 = display.getHeight();
                            double ratio1 = ((float) (width1)) / 37.0;
                            double ratio2 = ((float) (height1)) / 36.0;
                            int width2 = (int) (ratio1 * 20);
                            int height2 = (int) (ratio2 * 20);
                            params.width = width2;
                            params.height = height2;
//                            params.width = (int)(380*metrics.density);
//                            params.height = (int)(280*metrics.density);
                            params.topMargin = 20;
                            params.rightMargin = 20;
                            video.setLayoutParams(params);
                        }

                        ll_navbar.setVisibility(View.GONE);
                        lay_bottam.setVisibility(View.GONE);
                        video.setVisibility(View.VISIBLE);
                        layout_squeeze.setVisibility(View.VISIBLE);
                        video1.setVisibility(View.VISIBLE);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        doubleclick = true;


//                    if (!drawer.isDrawerOpen(Gravity.START)) drawer.openDrawer(Gravity.START);
//                    else drawer.closeDrawer(Gravity.END);


                    } catch (Exception e) {
                    }
                } else if (doubleclick == true) {
                    try {
                        // drawer.closeDrawer(Gravity.LEFT);
                        // Animation slideUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_down);
                        if (iv_channel.getVisibility() == View.VISIBLE) {
                            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                    iv_channel.getLayoutParams();
                            params1.width = 3500;
                            params1.height = 1200;
                            params1.leftMargin = 0;
                            iv_channel.setLayoutParams(params1);
                        } else {
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                                    video.getLayoutParams();
                            params.width = 3500;
                            params.height = 1200;
                            params.leftMargin = 0;
                            params.topMargin = 0;
                            params.rightMargin = 0;
                            video.setLayoutParams(params);
                        }

                        layout_squeeze.setVisibility(View.GONE);
                        video1.setVisibility(View.GONE);
                        iv_channel1.setVisibility(View.GONE);
                        ll_navbar.setVisibility(View.VISIBLE);
                        video.setVisibility(View.VISIBLE);
                        layout_constraint.setVisibility(View.VISIBLE);

                        getWindow().addFlags(
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        getWindow().clearFlags(
                                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

                        doubleclick = false;


                    } catch (Exception e) {
                    }
                }
            }
        });

     /*   ed_search_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyboard(MainActivity.this);
            }
        });

        ed_search_store.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                navAdapter.getFilter().filter(s.toString());


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/
      /*  txt_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
            }
        });*/


        tv_galleryview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Gallery", Toast.LENGTH_SHORT).show();
                galleryviewclick=true;
                navAdapter = new NavAdapter(mContext, list,galleryviewclick);
                rv_Mneu1.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                rv_Mneu1.setAdapter(navAdapter);
                //startActivity(new Intent(MainActivity.this,VideoThumbnails.class));
            }
        });
        tv_listview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "List", Toast.LENGTH_SHORT).show();

                galleryviewclick=false;
                navAdapter = new NavAdapter(mContext, list,galleryviewclick);
                rv_Mneu1.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL,false));
                rv_Mneu1.setAdapter(navAdapter);
                //startActivity(new Intent(MainActivity.this,VideoThumbnails.class));
            }
        });


      /*  iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout_squeeze.getVisibility() == View.VISIBLE) {
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);


                    if (iv_channel.getVisibility() == View.VISIBLE) {
                        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                iv_channel.getLayoutParams();
                        params1.width = 3500;
                        params1.height = 1200;
                        params1.leftMargin = 0;
                        iv_channel.setLayoutParams(params1);
                        video.setVisibility(View.GONE);
                        layout_squeeze.setVisibility(View.GONE);
                        video1.setVisibility(View.GONE);
                        iv_channel1.setVisibility(View.GONE);
                        ll_navbar.setVisibility(View.VISIBLE);
                        layout_constraint.setVisibility(View.VISIBLE);
                    } else {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                                video.getLayoutParams();
                        params.width = 3500;
                        params.height = 1200;
                        params.leftMargin = 0;
                        params.topMargin = 0;
                        params.rightMargin = 0;
                        video.setLayoutParams(params);
                        layout_squeeze.setVisibility(View.GONE);
                        video1.setVisibility(View.GONE);
                        iv_channel1.setVisibility(View.GONE);
                        ll_navbar.setVisibility(View.VISIBLE);
                        layout_constraint.setVisibility(View.VISIBLE);
                        video.setVisibility(View.VISIBLE);
                    }
                    hideKeyboard();


                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

                } else {
                    ed_signup.putString("channel_position", String.valueOf(s));
                    ed_signup.commit();

                    if (doubleBackToExitPressedOnce) {
                       finish();
                    }

                   doubleBackToExitPressedOnce= true;
                    Toast.makeText(MainActivity.this, "Press back button twice to exit.", Toast.LENGTH_SHORT).show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    doubleBackToExitPressedOnce = false;
                                }
                            },2000);
              //  statusOfflineExit();
                 *//*   final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.dialog_back);
                    //  dialog.setCancelable(false);
                    // dialog.setCanceledOnTouchOutside(false);
                    //dialog.getWindow().setWindowAnimations(R.style.DialogTheme);
                    dialog.show();
                    Button btncancel = dialog.findViewById(R.id.btn_cancel);
                    Button btnrecharge = dialog.findViewById(R.id.btn_recharge);
                    final CheckBox btn_checkbox = dialog.findViewById(R.id.btn_checkbox);
                    btncancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            statusOfflineExit();
                        }
                    });

                    btnrecharge.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            if (btn_checkbox.isChecked()) {
                                ed_signup.putString("reboot_state", "yes");
                                ed_signup.commit();
                                try {
                                    //Open the specific App Info page:
                                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    //Open the generic Apps page:
                                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                    startActivity(intent);
                                }


                            } else {
                                Toast.makeText(mContext, "Select Checkbox", Toast.LENGTH_SHORT).show();
                                // dialog.dismiss();
                            }

                        }
                    });*//*
                    // doubleBackToExitPressedOnce = true;
                    //Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();


                }
            }
        });*/


        //startService(new Intent(this, Sservice.class));

        /*mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if (intent.getAction().
                        equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    //Getting the registration token from the intent
                    String token = intent.getStringExtra("token");
                    ed_signup.putString("SAVETOKEN",token);
                    ed_signup.commit();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendTokenToServer(sp_signup.getString("SAVETOKEN",""));

                        }
                    },2000);
                    //sendFCMPush();

                    //Displaying the token as toast
                    // Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();
                    Log.d("msg", "onReceiveTokennn: " + token);
                    //if the intent is not with success then displaying error messages
                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    // Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    // Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                //  Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                // Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(MainActivity.this, GCMRegistrationIntentService.class);
            startService(itent);
        }*/

    }

    static public boolean isURLReachable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL url = new URL("http://7starcloud.in/tamilcloud/webservice/get_channell");   // Change to "http://google.com" for www  test.
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(10 * 1000);          // 10 s.
                urlc.connect();
                if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                    Log.wtf("Connection", "Success !");
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    private void getCatList() {
        catlist.clear();
        RequestQueue requestQueue=Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, "http://7starcloud.in/tamilcloud/webservice/get_channel_category",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("result");
                            for(int x=0;x<jsonArray.length();x++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(x);
                                catlist.add(new CategoryResult(jsonObject1.getString("id"),jsonObject1.getString("title")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }


    private void getData() {
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
       // imsi = mTelephonyMgr.getSubscriberId();
        imsi = "";
        mac_address = getMacAddr();

        //imei = mTelephonyMgr.getDeviceId();
        imei = "";
        CountryIso = mTelephonyMgr.getNetworkCountryIso();

        Locale loc = new Locale("", CountryIso.toUpperCase());
        CountryIso = loc.getDisplayCountry();

        NetworkOperator = mTelephonyMgr.getNetworkOperator();
        NetworkOperatorName = mTelephonyMgr.getNetworkOperatorName();
        devicename = Build.MODEL;
        BRAND = Build.BRAND;


        //finding mac and ip address
       /* WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        ip_address = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());*/
        ip_address = getPublicIPAddress(MainActivity.this);
        tv_bitrate.setText("");

      /*  thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!thread.isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                x = getNetworkClass(getApplicationContext());
                                Log.e("msg", "BANDWIDTHH" + x);
                                tv_bitrate.setText(x);

                                //check whether internet fails or not,acc to it show channel url
                               *//* if (x.equals("Not Connected")){
                                } else if(x.equals("2.3528 Mbps")){
//                                    loadUrl(list.get(s).getChannelUrl(), list.get(s).
//                                            getChannelName(), list.get(s).getImage(),s);
                                }*//*
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();*/


        Log.e("imsi", "---->" + imsi);
        Log.e("imei", "---->" + imei);
        Log.e("NetworkCountryIso", "---->" + CountryIso);
        Log.e("NetworkOperator", "---->" + NetworkOperator);
        Log.e("NetworkOperatorName", "---->" + NetworkOperatorName);
        Log.e("devicename", "---->" + devicename);
        Log.e("BRAND", "---->" + BRAND);


    }

    public static String getNetworkClass(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected())
            return "Not Connected"; // not connected
        if (info.getType() == ConnectivityManager.TYPE_WIFI)
            return "2.3528 Mbps";
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return "50-100 kbps"; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:

                    return "14-64 kbps"; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return "50-100 kbps"; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:

                    return "400-1000 kbps"; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return "600-1400 kbps"; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:

                    return "100 kbps"; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:

                    return "2-4 Mbps"; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:

                    return "70-1700 kbps";
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return "1-23 Mbps"; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:


                    return "5000 Kbps"; // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return "2 Mbps"; // ~ 1-2 Mbps

                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return "5 Mbps"; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13

                    return "2 Mbps"; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8

                    return "25 Kbps"; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return "1.1735 Mbps"; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return "1024 Kbps";
            }
        }
        return "1024 Kbps";
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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void loadUrl(final String url, final String name, final String image,
                         final String channelnumber, final int position) {

        try {
            video.setVisibility(View.VISIBLE);
            video1.setVisibility(View.VISIBLE);
            video.setBackgroundColor(Color.TRANSPARENT);
            video1.setBackgroundColor(Color.TRANSPARENT);


            txt_error.setVisibility(View.GONE);
            iv_channel.setVisibility(View.GONE);
            iv_channel1.setVisibility(View.GONE);
            txt_chno.setVisibility(View.VISIBLE);




            try {
                Glide.with(MainActivity.this).load(image).error(R.drawable.applogo_new).into(img_logo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            txt_name.setText(name);
            s = position;
            txt_chno.setText(channelnumber);
            txt_name1.setText(name);
            txt_chno1.setText(channelnumber);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    txt_chno.setVisibility(View.GONE);
                    // txt_chno1.setVisibility(View.GONE);
                }
            }, 10000);

            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("HH:mm aa");
            String formattedDate = df.format(c.getTime());

            txt_time.setText("" + formattedDate);

            video.setVideoURI(Uri.parse(url));

            tv_resolution.setText("Loading..");
            tv_bitrate.setText("Loading..");
            tv_resolution1.setText("Loading..");





            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    video.requestFocus();
                    video.postInvalidate();
                    video.start();
                    txt_error.setText("");
                    txt_error.setVisibility(View.GONE);
                    //  video.setZOrderOnTop(true);

                    video.setZOrderMediaOverlay(true);
                    int width = mp.getVideoWidth();
                    int height = mp.getVideoHeight();
                    tv_resolution.setText(String.valueOf(width + "*" + height));
                    tv_resolution1.setText(String.valueOf(width + "*" + height));
                    if (width > 1080 && height > 576) {
                        tv_standard.setText("HD");
                        tv_standard1.setText("HD");
                        tv_bitrate.setText("360Kbps");
                    }
                    else if(width > 960 && height >400){
                        tv_standard.setText("HD");
                        tv_standard1.setText("HD");
                        tv_bitrate.setText("240Kbps");
                    }
                    else {
                        tv_standard.setText("SD");
                        tv_standard1.setText("SD");
                        tv_bitrate.setText("144Kbps");

                    }

                    //get bitrate of video
                   /* MediaExtractor mex = new MediaExtractor();
                    try {
                        mex.setDataSource(url);
                        MediaFormat mf = mex.getTrackFormat(0);
                        int bitRate = mf.getInteger(MediaFormat.KEY_BIT_RATE);
                        int sampleRate = mf.getInteger(MediaFormat.KEY_SAMPLE_RATE);
                        int channelCount = mf.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
                        Log.e("msg", "BITRATEVIDEO:" + bitRate+"   ,Sample rate:"+sampleRate+",  Channel Count:"+channelCount);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("msg", "BITRATEVIDEO1:" +e.toString() );

                    }*/



                  //  hideKeyboard();
                }
            });

        //    boolean validurl=isServerReachable(getApplicationContext(),url);
         //   Toast.makeText(mContext, "URL::"+validurl, Toast.LENGTH_SHORT).show();

            final MediaPlayer.OnInfoListener onInfoToPlayStateListener = new MediaPlayer.OnInfoListener() {

                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {

                            return true;
                        }

                        case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                            txt_error.setVisibility(View.VISIBLE);
                            Log.e("msg", "onInfo: " + position);
                            Log.e("msg", "onInfo: " + url);
                            txt_error.setText("Buffering....");
                           // checkrestart1 = true;
                            return true;
                        }
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                         //   checkrestart1 = false;
                            txt_error.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.GONE);
                            iv_channel1.setVisibility(View.GONE);
                            // loadUrl(url,name,image,position);
                            video.setVideoURI(Uri.parse(url));
                            video.requestFocus();
                            video.postInvalidate();
                            video.start();
                            //  video.setZOrderOnTop(true);
                            video.setZOrderMediaOverlay(true);


                            return true;
                        }
                        case MediaPlayer.MEDIA_ERROR_SERVER_DIED: {
                            //txt_error.setVisibility(View.VISIBLE);
                           // checkrestart1 = false;
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.channelnew_notavailable);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);

                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }

                            return true;
                        }
                        case MediaPlayer.MEDIA_ERROR_IO: {
                            // txt_error.setVisibility(View.VISIBLE);
                          //  checkrestart1 = false;
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.channelnew_notavailable);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);

                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }
                            return true;
                        }
                        case MediaPlayer.MEDIA_ERROR_MALFORMED: {
                            //checkrestart1 = false;
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            // txt_error.setVisibility(View.VISIBLE);
                            //txt_error.setText("Channel not available this time");
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.channelnew_notavailable);

                            iv_channel1.setVisibility(View.GONE);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);
                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }
                            return true;
                        }
                        case MediaPlayer.MEDIA_ERROR_TIMED_OUT: {
                            //checkrestart1 = false;
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            // txt_error.setVisibility(View.VISIBLE);
                            //txt_error.setText("Channel not available this time");
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.channelnew_notavailable);


                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);
                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }
                            return true;
                        }
                        case MediaPlayer.MEDIA_ERROR_UNKNOWN: {
                           // checkrestart1 = false;
                            // txt_error.setVisibility(View.VISIBLE);
                            // txt_error.setText("Channel not available this time");
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.channelnew_notavailable);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);

                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            }
                            else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }

                            return true;
                        }
                        case MediaPlayer.MEDIA_ERROR_UNSUPPORTED: {
                           // checkrestart1 = false;
                            // txt_error.setVisibility(View.VISIBLE);
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.channelnew_notavailable);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);

                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }
                            return true;
                        }
                        case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING: {
                          //  checkrestart1 = false;
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.channelnew_notavailable);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);

                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }
                            return true;
                        }
                        case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK: {
                            //checkrestart1 = false;
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.channelnew_notavailable);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);

                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }
                            return true;
                        }
                    }
                   /* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                handler.postDelayed(this, 5000);
                                if (checkrestart1 == true && txt_error.getText().toString().
                                        equals("Buffering....")) {
                                    checkrestart1 = false;


                                    loadUrl(list.get(s).getChannelUrl(),
                                            list.get(s).getChannelName(), list.get(s).getImage(),
                                            list.get(s).getChannelNumber(), s);
//                                video.setVideoURI(Uri.parse(url));
//                                video.requestFocus();
//                                video.postInvalidate();
//                                video.start();
//                                //  video.setZOrderOnTop(true);
//                                video.setZOrderMediaOverlay(true);
//                                txt_error.setVisibility(View.GONE);


                                }

                           *//* if (checkrestart1 == true && checkrestart2 == true && txt_error.getText().toString().
                                    equals("Buffering....")) {
                                checkrestart1 = false;

                                txt_error.setVisibility(View.GONE);
                                // loadUrl(url,name,image,position);
                                video.setVideoURI(Uri.parse(url));
                                video.requestFocus();
                                video.postInvalidate();
                                video.start();
                                //  video.setZOrderOnTop(true);
                                video.setZOrderMediaOverlay(true);
                                txt_error.setText("");

                            }*//*


                            }

                        }, 5000);
                    }*/

                    return false;
                }

            };
            video.setOnInfoListener(onInfoToPlayStateListener);


            video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                      //  video1.setVisibility(View.GONE);
                       // video.setVisibility(View.GONE);
                        iv_channel.setVisibility(View.VISIBLE);
                        iv_channel.setImageResource(R.drawable.channelnew_notavailable);

                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);

                        iv_channel1.setVisibility(View.GONE);
                        txt_error.setVisibility(View.VISIBLE);

                        txt_error.setText("No Channel Available at this time...");
                        if (layout_squeeze.getVisibility() == View.VISIBLE) {
                            iv_channel.requestFocus();
                            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                    iv_channel.getLayoutParams();
                            Display display = getWindowManager().getDefaultDisplay();
                            int width1 = display.getWidth();
                            int height1 = display.getHeight();
                            double ratio1 = ((float) (width1)) / 39.0;
                            double ratio2 = ((float) (height1)) / 36.0;
                            int width2 = (int) (ratio1 * 20);
                            int height2 = (int) (ratio2 * 20);
                            params1.width = width2;
                            params1.height = height2;
                            params1.rightMargin = 20;
                            params1.topMargin = 20;
                            iv_channel.setLayoutParams(params1);
                        } else {
                            iv_channel.requestFocus();
                            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                    iv_channel.getLayoutParams();
                            params1.width = 3500;
                            params1.height = 1200;
                            params1.rightMargin = 0;
                            params1.topMargin = 0;
                            iv_channel.setLayoutParams(params1);

                        }
                    }
                    else{
                      //  video1.setVisibility(View.GONE);
                      //  video.setVisibility(View.GONE);
                        iv_channel.setVisibility(View.VISIBLE);
                        iv_channel.setImageResource(R.drawable.channelnew_notavailable);

                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);

                        iv_channel1.setVisibility(View.GONE);
                        txt_error.setVisibility(View.VISIBLE);
                        txt_error.setText("No Channel Available at this time...");

                        if (layout_squeeze.getVisibility() == View.VISIBLE) {
                            iv_channel.requestFocus();
                            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                    iv_channel.getLayoutParams();
                            Display display = getWindowManager().getDefaultDisplay();
                            int width1 = display.getWidth();
                            int height1 = display.getHeight();
                            double ratio1 = ((float) (width1)) / 39.0;
                            double ratio2 = ((float) (height1)) / 36.0;
                            int width2 = (int) (ratio1 * 20);
                            int height2 = (int) (ratio2 * 20);
                            params1.width = width2;
                            params1.height = height2;
                            params1.rightMargin = 20;
                            params1.topMargin = 20;
                            iv_channel.setLayoutParams(params1);
                        } else {
                            iv_channel.requestFocus();
                            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                    iv_channel.getLayoutParams();
                            params1.width = 3500;
                            params1.height = 1200;
                            params1.rightMargin = 0;
                            params1.topMargin = 0;
                            iv_channel.setLayoutParams(params1);

                        }
                    }

                  /*  switch (what) {

                        case MediaPlayer.MEDIA_ERROR_SERVER_DIED: {
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.channelnew_notavailable);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);

                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }
                            return true;
                        }
                        case MediaPlayer.MEDIA_ERROR_IO: {
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.channelnew_notavailable);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);

                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }


                            return true;
                        }
                        case MediaPlayer.MEDIA_ERROR_MALFORMED: {
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.channelnew_notavailable);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);

                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }

                            return true;
                        }


                        case MediaPlayer.MEDIA_ERROR_TIMED_OUT: {
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.channelnew_notavailable);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);

                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }
                            return true;
                        }
                      *//*  case MediaPlayer.MEDIA_ERROR_UNKNOWN: {
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.channelnew_notavailable);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);

                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }
                            return true;
                        }*//*
                        case MediaPlayer.MEDIA_ERROR_UNSUPPORTED: {
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.channelnew_notavailable);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);

                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }

                            return true;
                        }
                        case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING: {
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.channelnew_notavailable);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);

                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }
                            return true;
                        }
                        case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK: {
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.channelnew_notavailable);


                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);
                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }
                            return true;
                        }
                    }*/


                 /*   if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        final Handler handler = new Handler();
                        final int delay = 2000; //milliseconds
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                handler.postDelayed(this, delay);
                                Log.e("Error", "-----> one");

                                if (NetworkUtils.isNetworkConnected(MainActivity.this)) {
                                    video.setVisibility(View.VISIBLE);
                                    video1.setVisibility(View.VISIBLE);
                                    txt_error.setVisibility(View.GONE);
                                    iv_channel.setVisibility(View.GONE);
                                    handler.removeCallbacksAndMessages(null);
                                    video.setVideoURI(Uri.parse(url));
                                    video.requestFocus();
                                    video.postInvalidate();
                                    video.start();
                                    //   video.setZOrderOnTop(true);
                                    video.setZOrderMediaOverlay(true);


                                    // }
                                } else {
                                    // iv_channel.setVisibility(View.VISIBLE);
                                    txt_error.setVisibility(View.VISIBLE);
                                    txt_error.setText("Waiting for network connection");
                                }

                            }
                        }, delay);
                    }*/


                    return true;
                }
            });


        } catch (Exception e) {
        }


    }


    private void finds() {
        rv_Mneu = findViewById(R.id.rv_Mneu);
       // ed_search_store = findViewById(R.id.ed_search_store);
        rv_Mneu1 = findViewById(R.id.rv_Mneu1);
        rv_category = findViewById(R.id.rv_category);
        iv_channel = findViewById(R.id.iv_channel);

        tv_resolution = findViewById(R.id.tv_resolution);
        tv_resolution1 = findViewById(R.id.tv_resolution1);
        tv_standard = findViewById(R.id.tv_standard);
        tv_standard1 = findViewById(R.id.tv_standard1);

        layout_constraint = findViewById(R.id.layout_constraint);
        layout_squeeze = findViewById(R.id.layout_squeeze);
        ll_navbar = findViewById(R.id.ll_navbar);

        //  sp_language = findViewById(R.id.sp_language);

        video = findViewById(R.id.video);
        video1 = findViewById(R.id.video1);
        video.setFocusable(false);
        video1.setFocusable(false);
        video.setZOrderOnTop(true);
        img_logo = findViewById(R.id.img_logo);
        txt_name = findViewById(R.id.txt_name);
        // txt_changepassword = findViewById(R.id.txt_changepassword);
        txt_chno = findViewById(R.id.txt_chno);
        txt_time = findViewById(R.id.txt_time);
        lay_bottam = findViewById(R.id.lay_bottam);
        txt_error = findViewById(R.id.txt_error);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);




    }

    private void finds_view() {
        iv_channel1 = findViewById(R.id.iv_channel1);


        img_logo1 = findViewById(R.id.img_logo1);
        txt_time1 = findViewById(R.id.txt_time1);
        lay_bottam1 = findViewById(R.id.lay_bottam1);

        txt_error1 = findViewById(R.id.txt_error1);



    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

         /*   case R.id.iv_Menu:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;*/


        /*    case R.id.iv_Cancel:
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.iv_Cancel1:
                if (iv_channel.getVisibility() == View.VISIBLE) {
                    RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                            iv_channel.getLayoutParams();
                    params1.width = 3500;
                    params1.height = 1200;
                    params1.leftMargin = 0;
                    iv_channel.setLayoutParams(params1);
                } else {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                            video.getLayoutParams();
                    params.width = 3500;
                    params.height = 1200;
                    params.leftMargin = 0;
                    params.topMargin = 0;
                    params.rightMargin = 0;
                    video.setLayoutParams(params);
                }

                layout_squeeze.setVisibility(View.GONE);
                video1.setVisibility(View.GONE);
                iv_channel1.setVisibility(View.GONE);
                ll_navbar.setVisibility(View.VISIBLE);
                layout_constraint.setVisibility(View.VISIBLE);
                //  iv_channel.setVisibility(View.VISIBLE);

                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                break;*/

           /* case R.id.iv_Grid:
                iv_Menu.setVisibility(View.VISIBLE);
                drawer.closeDrawer(GravityCompat.START);
                ShowBottomSheet();
                break;
*/
        }
    }

    private void ShowBottomSheet() {
        try {
            mBottomSheetDialog = new BottomSheetDialog(mContext, R.style.MyBottomSheetDialogTheme);
            View sheetView = getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);
            mBottomSheetDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            mBottomSheetDialog.setContentView(sheetView);
            BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) sheetView.getParent());
            mBehavior.setPeekHeight(350);
            mBottomSheetDialog.setCanceledOnTouchOutside(false);

            RecyclerView rv_Bottom = sheetView.findViewById(R.id.rv_Bottom);
            ImageView iv_List = sheetView.findViewById(R.id.iv_List);
            this.getWindow().getDecorView().setSystemUiVisibility(View.STATUS_BAR_HIDDEN);

            if (list.size() > 0) {
                bottomNavAdapter = new BottomNavAdapter(mContext, list);
                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv_Bottom.setLayoutManager(manager);
                rv_Bottom.setAdapter(bottomNavAdapter);
            }

            mBottomSheetDialog.show();

            iv_List.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mBottomSheetDialog.dismiss();
                }
            });
        } catch (Exception e) {
        }

    }


    public class NavAdapter extends RecyclerView.Adapter<NavAdapter.MyViewHolder>
            implements Filterable {

        public MyViewHolder myViewHolderr;
        private LinearLayout lastClickLay = null;
        private Context context;
        public List<ChanelResult> mItemList;
        public List<ChanelResult> contactListFiltered;
        boolean galleryviewclick;
        public NavAdapter(Context context, List<ChanelResult> mItemListt,boolean galleryviewclickk) {
            this.context = context;
            mItemList = mItemListt;
            contactListFiltered = mItemListt;
            galleryviewclick = galleryviewclickk;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View mView=null;
          //  if(galleryviewclick==true){
                mView = LayoutInflater.from(context).inflate(R.layout.item_gridview, viewGroup, false);

        //    }
           // else if(galleryviewclick==false){
           //     mView = LayoutInflater.from(context).inflate(R.layout.item_nav, viewGroup, false);

           // }

            return new MyViewHolder(mView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
            myViewHolderr = myViewHolder;
            myViewHolder.tv_Count.setText("0" + contactListFiltered.get(i).getChannelNumber());

            myViewHolder.tv_Name.setText(contactListFiltered.get(i).getChannelName());

            try {
                Glide.with(context).load(contactListFiltered.get(i).getImage()).error(R.drawable.applogo_new).into(myViewHolder.iv_Logo);
            } catch (Exception e) {
                e.printStackTrace();
            }


            myViewHolder.ll_Contain1.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onClick(View v) {
                    //getBitrate();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //drawer.closeDrawer(GravityCompat.START);
                        }
                    }, 200);

                    //  Toast.makeText(context, ""+list.get(i).getChannelName(), Toast.LENGTH_SHORT).show();
                    if (layout_squeeze.getVisibility() == View.VISIBLE) {
                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) video.getLayoutParams();
                        Display display = getWindowManager().getDefaultDisplay();
                        int width1 = display.getWidth();
                        int height1 = display.getHeight();
                        double ratio1 = ((float) (width1)) / 37.0;
                        double ratio2 = ((float) (height1)) / 36.0;
                        int width2 = (int) (ratio1 * 20);
                        int height2 = (int) (ratio2 * 20);
                        params.width = width2;
                        params.height = height2;
                        params.topMargin = 20;
                        params.rightMargin = 20;
                        video.setLayoutParams(params);
                    }
                    //  Toast.makeText(context, ""+contactListFiltered.get(i).getSubscription(), Toast.LENGTH_SHORT).show();
                   // if (contactListFiltered.get(i).getSubscription().equals("1")) {
                        tv_standard1.setText("");
                        tv_standard.setText("");
                        tv_resolution1.setText("Loading..");
                        tv_resolution.setText("Loading..");
                        tv_bitrate.setText("Loading..");
                        txt_name1.setText(contactListFiltered.get(i).getChannelName());
                        txt_name.setText(contactListFiltered.get(i).getChannelName());
                        txt_chno1.setText(contactListFiltered.get(i).getChannelNumber());
                        txt_chno.setText(contactListFiltered.get(i).getChannelNumber());
                        video1.setVisibility(View.GONE);
                        video.setVisibility(View.GONE);
                        iv_channel.setVisibility(View.VISIBLE);
                        iv_channel.setImageResource(R.drawable.paid_channel);

                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);

                        iv_channel1.setVisibility(View.GONE);

                        if (layout_squeeze.getVisibility() == View.VISIBLE) {
                            iv_channel.requestFocus();
                            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                    iv_channel.getLayoutParams();
                            Display display = getWindowManager().getDefaultDisplay();
                            int width1 = display.getWidth();
                            int height1 = display.getHeight();
                            double ratio1 = ((float) (width1)) / 39.0;
                            double ratio2 = ((float) (height1)) / 36.0;
                            int width2 = (int) (ratio1 * 20);
                            int height2 = (int) (ratio2 * 20);
                            params1.width = width2;
                            params1.height = height2;
                            params1.rightMargin = 20;
                            params1.topMargin = 20;
                            iv_channel.setLayoutParams(params1);
                        }
                        else {
                            iv_channel.requestFocus();
                            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                    iv_channel.getLayoutParams();
                            params1.width = 3500;
                            params1.height = 1200;
                            params1.rightMargin = 0;
                            params1.topMargin = 0;
                            iv_channel.setLayoutParams(params1);

                        }
                   // } else {
                        loadUrl(contactListFiltered.get(i).getChannelUrl(),
                                contactListFiltered.get(i).getChannelName(),
                                contactListFiltered.get(i).getImage(),
                                contactListFiltered.get(i).getChannelNumber(), i);
                  //  }





                    posi = i;
                    s = i;
                    notifyDataSetChanged();
                }
            });
            if (i == posi) {
               // if (lastClickLay == null) {
                    // Toast.makeText(context, "null"+i, Toast.LENGTH_SHORT).show();

                    myViewHolder.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                   // lastClickLay = myViewHolder.ll_Contain1;
               // } else {
                    // Toast.makeText(context, "not_null"+i, Toast.LENGTH_SHORT).show();
                  //  myViewHolder.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                   // lastClickLay.setBackgroundResource(R.drawable.channel_back);
                  //  lastClickLay = myViewHolder.ll_Contain1;
              //  }
            } else {
                myViewHolder.ll_Contain1.setBackgroundResource(R.drawable.channel_back);
                //lastClickLay.setBackgroundResource(R.drawable.bg_side_unsel);
              //  lastClickLay = myViewHolder.ll_Contain1;

            }


        }

        @Override
        public int getItemCount() {
            return contactListFiltered.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        contactListFiltered = mItemList;
                    } else {
                        List<ChanelResult> filteredList = new ArrayList<>();
                        for (ChanelResult row : mItemList) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getChannelName().toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getChannelName().toLowerCase().contains(charString.toUpperCase())) {
                                filteredList.add(row);
                            }
                        }

                        contactListFiltered = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = contactListFiltered;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    contactListFiltered = (ArrayList<ChanelResult>) filterResults.values;
                    notifyDataSetChanged();


                }
            };
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView tv_Count, tv_Name;
            public ImageView iv_Logo;
            public LinearLayout ll_Contain1;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                tv_Count = itemView.findViewById(R.id.tv_Count);
                tv_Name = itemView.findViewById(R.id.tv_Name);
                ll_Contain1 = itemView.findViewById(R.id.ll_Contain);
                iv_Logo = itemView.findViewById(R.id.iv_Logo);
            }
        }
    }

    public class CatAdapter extends RecyclerView.Adapter<CatAdapter.MyViewHolder>
            implements Filterable {

        public MyViewHolder myViewHolderr1;
        private LinearLayout lastClickLay = null;
        private Context context;
        public List<CategoryResult> mItemList1;
        public List<CategoryResult> contactListFiltered1;
        boolean galleryviewclick1;

        public CatAdapter(Context context, List<CategoryResult> mItemListt,boolean galleryviewclickk) {
            this.context = context;
            mItemList1 = mItemListt;
            contactListFiltered1 = mItemListt;
            galleryviewclick1 = galleryviewclickk;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View mView=null;
            //  if(galleryviewclick==true){
            mView = LayoutInflater.from(context).inflate(R.layout.item_category, viewGroup, false);

            //    }
            // else if(galleryviewclick==false){
            //     mView = LayoutInflater.from(context).inflate(R.layout.item_nav, viewGroup, false);

            // }

            return new MyViewHolder(mView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
            myViewHolderr1 = myViewHolder;
            myViewHolder.tv_Cat_Name.setText(contactListFiltered1.get(i).getCatname());
            myViewHolder.ll_Contain_cat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posi1 = i;
                    s = i;
                    notifyDataSetChanged();
                    getChannel_accto_cat(contactListFiltered1.get(i).getCatid());
                   // Toast.makeText(context, ""+contactListFiltered1.get(i).getCatid(), Toast.LENGTH_SHORT).show();
                }
            });
            if (i == posi1) {
                    // Toast.makeText(context, "null"+i, Toast.LENGTH_SHORT).show();
                    myViewHolder.tv_Cat_Name.setTextColor(Color.WHITE);
                    myViewHolder.ll_Contain_cat.setBackgroundResource(R.drawable.bg_side_sel);

            } else {
                myViewHolder.tv_Cat_Name.setTextColor(Color.BLACK);
                myViewHolder.ll_Contain_cat.setBackgroundResource(R.drawable.search_editback);

            }


        }

        @Override
        public int getItemCount() {
            return contactListFiltered1.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        contactListFiltered1 = mItemList1;
                    } else {
                        List<CategoryResult> filteredList = new ArrayList<>();
                        for (CategoryResult row : mItemList1) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getCatname().toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getCatname().toUpperCase().contains(charString.toUpperCase())) {
                                filteredList.add(row);
                            }
                        }

                        contactListFiltered1 = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = contactListFiltered1;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    contactListFiltered1 = (ArrayList<CategoryResult>) filterResults.values;
                    notifyDataSetChanged();


                }
            };
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView tv_Cat_Name;
            public LinearLayout ll_Contain_cat;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                tv_Cat_Name = itemView.findViewById(R.id.tv_Cat_Name);
                ll_Contain_cat = itemView.findViewById(R.id.ll_Contain_cat);
            }
        }
    }

    private static void showKeyboard(Activity activity) {
        View v = activity.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null && v != null;
        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideKeyboard() {
        try {
            // use application level context to avoid unnecessary leaks.
            InputMethodManager inputManager = (InputMethodManager) getApplicationContext().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputManager != null;
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class BottomNavAdapter extends RecyclerView.Adapter<BottomNavAdapter.MyViewHolder> {

        private Context context;
        private LinearLayout lastClickLay = null;
        private List<ChanelResult> list;

        public BottomNavAdapter(Context context, List<ChanelResult> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View mView = LayoutInflater.from(context).inflate(R.layout.item_bottom_nav, viewGroup, false);

            return new MyViewHolder(mView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

            myViewHolder.tv_Count.setText("0" + (i + 1));

            myViewHolder.tv_Name.setText(list.get(i).getChannelName());

            try {
                Glide.with(context).load(list.get(i).getImage()).error(R.drawable.applogo_new).into(myViewHolder.iv_Logo);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (i == posi) {
                myViewHolder.ll_Contain.setBackgroundResource(R.mipmap.box_a);
                if (lastClickLay != null) {
                    lastClickLay.setBackgroundResource(R.mipmap.boxx);
                    lastClickLay = myViewHolder.ll_Contain;
                }
            }

            myViewHolder.ll_Contain.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onClick(View v) {

                    if (lastClickLay == null) {
                        myViewHolder.ll_Contain.setBackgroundResource(R.mipmap.box_a);
                        lastClickLay = myViewHolder.ll_Contain;
                    } else {
                        myViewHolder.ll_Contain.setBackgroundResource(R.mipmap.box_a);
                        lastClickLay.setBackgroundResource(R.mipmap.boxx);
                        lastClickLay = myViewHolder.ll_Contain;
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBottomSheetDialog.dismiss();
                        }
                    }, 200);

                    loadUrl(list.get(i).getChannelUrl(), list.get(i).getChannelName(), list.get(i).getImage(),
                            list.get(i).getChannelNumber(), i);
                    posi = i;
                    s = i;
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView tv_Count, tv_Name;
            public ImageView iv_Logo;
            public LinearLayout ll_Contain;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                tv_Count = itemView.findViewById(R.id.tv_Count);
                tv_Name = itemView.findViewById(R.id.tv_Name);
                ll_Contain = itemView.findViewById(R.id.ll_Contain);
                iv_Logo = itemView.findViewById(R.id.iv_Logo);

            }
        }
    }
private void getChannel_accto_cat(String cat_idd){
        list.clear();
    try {
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://7starcloud.in/tamilcloud/webservice/get_channel", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressDialog.dismiss();
                Log.e("msg", "Channel_Response: " + response);
                try {

                    //Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                    JSONObject object = new JSONObject(response);
                    System.out.println("Login" + object);
                    if (object.getString("status").equals("1")) {
                        Gson gson = new Gson();
                        ChanelResponse successResponse = gson.fromJson(response, ChanelResponse.class);
                        list = successResponse.getResult();
                        //encryptedchannels();
                        navAdapter = new NavAdapter(mContext, list,false);
                        LinearLayoutManager manager = new LinearLayoutManager(mContext);
                        manager.setOrientation(LinearLayoutManager.VERTICAL);
                        rv_Mneu.setLayoutManager(manager);
                        rv_Mneu.setAdapter(navAdapter);

                        rv_Mneu1.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                        rv_Mneu1.setAdapter(navAdapter);

                    } else if (object.getString("status").equals("0")) {
                        //txt_error.setVisibility(View.VISIBLE);
                       // txt_error.setText("No channel exists");
                        navAdapter = new NavAdapter(mContext, list,false);
                        rv_Mneu1.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                        rv_Mneu1.setAdapter(navAdapter);
                        //Toast.makeText(MainActivity.this, "No channel list", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("cat_id",cat_idd);
                return  map;
            }
        };
        rq.add(sr);
    } catch (Exception e) {
    }
}
    private void get_all_request(String catid) {
        try {
            RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
            StringRequest sr = new StringRequest(Request.Method.POST, "http://7starcloud.in/tamilcloud/webservice/get_channel", new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //progressDialog.dismiss();
                    Log.e("msg", "Channel_Response: " + response);
                    try {

                        //Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                        JSONObject object = new JSONObject(response);
                        System.out.println("Login" + object);
                        if (object.getString("status").equals("1")) {
                            Gson gson = new Gson();
                            ChanelResponse successResponse = gson.fromJson(response, ChanelResponse.class);
                            list = successResponse.getResult();
                            for(int y=0;y<list.size();y++){
                                ChannelEntity channelEntity=new ChannelEntity(list.get(y).getId(),
                                        list.get(y).getChannel_Category_id(),list.get(y).getChannelNumber(),list.get(y).getChannelName(),
                                        list.get(y).getChannelUrl(),list.get(y).getUserName(),list.get(y).getImage(),
                                        list.get(y).getSocialId(),list.get(y).getLat(),list.get(y).getLon(),
                                        list.get(y).getRegisterId(),list.get(y).getIosRegisterId(),list.get(y).getStatus(),
                                        list.get(y).getDateTime(),list.get(y).getAbout(),list.get(y).getChannelCategoryName());
                                mchannelDatabase.getChannelDao().insert(channelEntity);
                            }

                            //encryptedchannels();
                            navAdapter = new NavAdapter(mContext, list,false);
                            LinearLayoutManager manager = new LinearLayoutManager(mContext);
                            manager.setOrientation(LinearLayoutManager.VERTICAL);
                            rv_Mneu.setLayoutManager(manager);
                            rv_Mneu.setAdapter(navAdapter);

                           /* LinearLayoutManager manager1 = new LinearLayoutManager(mContext);
                            manager1.setOrientation(LinearLayoutManager.VERTICAL);
                            rv_Mneu1.setLayoutManager(manager1);
                            rv_Mneu1.setAdapter(navAdapter);*/
                            rv_Mneu1.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                           // rv_Mneu1.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL,false));
                            rv_Mneu1.setAdapter(navAdapter);


                            catAdapter=new CatAdapter(mContext,catlist,false);
                            rv_category.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                            rv_category.setAdapter(catAdapter);

                            //loadUrl("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample.mp4"
                            //           , list.get(0).getChannelName(), list.get(0).getImage(), 0);

                            lay_bottam.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    lay_bottam.setVisibility(View.GONE);
                                    //  lay_bottam1.setVisibility(View.GONE);

                                }
                            }, 7000);

                            //if (list.get(0).getSubscription().equals("1")) {
                                tv_standard.setText("");
                                tv_resolution.setText("Loading..");
                                tv_bitrate.setText("Loading..");
                                txt_name.setText(list.get(s).getChannelUrl());
                                txt_chno.setText(list.get(s).getChannelNumber());
                                tv_standard1.setText("");
                                tv_resolution1.setText("Loading..");
                                txt_name1.setText(list.get(s).getChannelUrl());
                                txt_chno1.setText(list.get(s).getChannelNumber());
                                video1.setVisibility(View.GONE);
                                video.setVisibility(View.GONE);
                                iv_channel.setVisibility(View.VISIBLE);
                                iv_channel.setImageResource(R.drawable.paid_channel);
                           // } else {
                                loadUrl(list.get(s).getChannelUrl(),
                                        list.get(s).getChannelName(), list.get(s).getImage(),
                                        list.get(s).getChannelNumber(), 0);
                         //   }
                          //  //resolution of wideo
                                /*MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
                                metaRetriever.setDataSource(list.get(0).getChannelUrl());
                                String height = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                                String width = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                                tv_resolution.setText(width+"*"+height);*/
                        } else {
                            txt_error.setVisibility(View.VISIBLE);
                            txt_error.setText("No channel exists");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map=new HashMap<>();
                    map.put("cat_id",catid);
                    return  map;
                }
            };
            rq.add(sr);
        } catch (Exception e) {
        }


    }

    private void add_user(String imsi, String imei, String countryIso, String networkOperator, String networkOperatorName, String brand, String model, String date, String time, String status) {

        try {
            Call<ResponseBody> call = AppConfig.loadInterface().add_user(imsi, imei, countryIso, networkOperator, networkOperatorName, brand, model, date, time, status);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            System.out.println("Login" + object);
                            if (object.getString("status").equals("1")) {

                            } else {
                                Log.e("response", "" + response.message());
                            }

                        } else {
                            Log.e("response", "" + response.message());
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(mContext, "onResume", Toast.LENGTH_SHORT).show();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        String time = "" + c;

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        ed_signup.putString("channel_position", String.valueOf(s));
        ed_signup.commit();

        add_user(imsi, imei, CountryIso, NetworkOperator, NetworkOperatorName, BRAND, devicename, formattedDate, time, "online");
      //  stopService(new Intent(this, Sservice.class));

    }


    @Override
    public void onPause() {
        super.onPause();
        //   Toast.makeText(mContext, "onPause", Toast.LENGTH_SHORT).show();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        String time = "" + c;

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        ed_signup.putString("channel_position", String.valueOf(s));
        ed_signup.commit();
        loadUrl(list.get(s).getChannelUrl(),
                list.get(s).getChannelName(), list.get(s).getImage(),
                list.get(s).getChannelNumber(), s);
        add_user(imsi, imei, CountryIso, NetworkOperator, NetworkOperatorName, BRAND, devicename, formattedDate, time, "offline");
        drawer.closeDrawer(Gravity.LEFT);
      //  stopService(new Intent(this, Sservice.class));


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  Toast.makeText(mContext, "onDestroy", Toast.LENGTH_SHORT).show();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        String time = "" + c;

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        ed_signup.putString("channel_position", String.valueOf(s));
        ed_signup.commit();
        add_user(imsi, imei, CountryIso, NetworkOperator, NetworkOperatorName, BRAND, devicename, formattedDate, time, "offline");

      //  stopService(new Intent(this, Sservice.class));

    }


    private void add_user2(String imsi, String imei, String countryIso, String networkOperator,
                           String networkOperatorName, String brand, String model, String date, String time,
                           String status) {
        Call<ResponseBody> call = AppConfig.loadInterface().add_user(imsi, imei, countryIso, networkOperator,
                networkOperatorName, brand, model, date, time, status);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Log.e("msg", "ResponseFailure6: "+response.body().toString());

                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("Login" + object);
                        Log.e("msg", "ResponseFailure0: " + object.getString("status"));

                        if (object.getString("status").equals("1")) {
                            finish();
                        } else {
                            Log.e("msg", "ResponseFailure1: " + response.message());
                        }

                    } else {
                        Log.e("msg", "ResponseFailure2: " + response.message());
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Log.e("msg", "ResponseFailure3: " + e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("msg", "ResponseFailure4: " + t.toString());
                t.printStackTrace();
            }
        });
    }


    @SuppressLint("NewApi")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("debug", "we are here");
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:

                if (layout_squeeze.getVisibility() == View.VISIBLE) {
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) video.getLayoutParams();
                    Display display = getWindowManager().getDefaultDisplay();
                    int width1 = display.getWidth();
                    int height1 = display.getHeight();
                    double ratio1 = ((float) (width1)) / 39.0;
                    double ratio2 = ((float) (height1)) / 36.0;
                    int width2 = (int) (ratio1 * 20);
                    int height2 = (int) (ratio2 * 20);
                    params.width = width2;
                    params.height = height2;
                    params.topMargin = 20;
                    params.rightMargin = 20;
                    video.setLayoutParams(params);
                    if (s == 0) {
                        Toast.makeText(mContext, "No previous channel", Toast.LENGTH_SHORT).show();
                    } else {
                        clickupdown = true;
                        s = s - 1;
                        // loadUrl("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample.mp4"
                        //        ,  list.get(i).getChannelName(), list.get(i).getImage(), i);
                        posi = s;
                        rv_Mneu.smoothScrollToPosition(s);
                        rv_Mneu1.smoothScrollToPosition(s);
                        navAdapter.notifyDataSetChanged();

                        try {
                            if (s == posi) {
                                if (navAdapter.lastClickLay == null) {
                                    // Toast.makeText(context, "null"+i, Toast.LENGTH_SHORT).show();

                                    navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                    navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                } else {
                                    // Toast.makeText(context, "not_null"+i, Toast.LENGTH_SHORT).show();
                                    navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                    navAdapter.lastClickLay.setBackgroundResource(R.drawable.channel_back);
                                    navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                }

                            } else {
                                navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.channel_back);
                                //lastClickLay.setBackgroundResource(R.drawable.bg_side_unsel);
                                navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;

                            }

                        } catch (Exception e) {
                            //Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                            video.getLayoutParams();
                    params.width = 3500;
                    params.height = 1200;
                    params.leftMargin = 0;
                    params.topMargin = 0;
                    params.rightMargin = 0;
                    video.setLayoutParams(params);
                    if (layout_squeeze.getVisibility() == View.GONE) {
                        lay_bottam.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lay_bottam.setVisibility(View.GONE);
                                //  lay_bottam1.setVisibility(View.GONE);

                            }
                        }, 7000);
                    }

                    if (s >= list.size() - 1) {
                        s=0;

                       // if (list.get(s).getSubscription().equals("1")) {
                            tv_standard1.setText("");
                            tv_resolution1.setText("Loading..");
                            txt_name1.setText(list.get(s).getChannelName());
                            txt_chno1.setText(list.get(s).getChannelNumber());
                            tv_standard.setText("");
                            tv_resolution.setText("Loading..");
                            tv_bitrate.setText("Loading..");
                            txt_name.setText(list.get(s).getChannelName());
                            txt_chno.setText(list.get(s).getChannelNumber());
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.paid_channel);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);

                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }
                       // } else {
                            loadUrl(list.get(s).getChannelUrl(),
                                    list.get(s).getChannelName(), list.get(s).getImage(),
                                    list.get(s).getChannelNumber(), s);
                      //  }


                        posi = s;
                        rv_Mneu.smoothScrollToPosition(s);
                        rv_Mneu1.smoothScrollToPosition(s);
                        navAdapter.notifyDataSetChanged();
                        try {
                            if (s == posi) {
                                if (navAdapter.lastClickLay == null) {
                                    // Toast.makeText(context, "null"+i, Toast.LENGTH_SHORT).show();

                                    navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                    navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                } else {
                                    // Toast.makeText(context, "not_null"+i, Toast.LENGTH_SHORT).show();
                                    navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                    navAdapter.lastClickLay.setBackgroundResource(R.drawable.channel_back);
                                    navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                }
                            } else {
                                navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.channel_back);
                                //lastClickLay.setBackgroundResource(R.drawable.bg_side_unsel);
                                navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;

                            }

                        } catch (Exception e) {
                            //   Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        //Toast.makeText(mContext, "No next channel", Toast.LENGTH_SHORT).show();
                    } else {
                        s = s + 1;

                      //  if (list.get(s).getSubscription().equals("1")) {
                            tv_standard1.setText("");
                            tv_resolution1.setText("Loading..");
                            txt_name1.setText(list.get(s).getChannelName());
                            txt_chno1.setText(list.get(s).getChannelNumber());
                            tv_standard.setText("");
                            tv_resolution.setText("Loading..");
                            tv_bitrate.setText("Loading..");
                            txt_name.setText(list.get(s).getChannelName());
                            txt_chno.setText(list.get(s).getChannelNumber());
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.paid_channel);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);

                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }
                     //   } else {
                            loadUrl(list.get(s).getChannelUrl(),
                                    list.get(s).getChannelName(), list.get(s).getImage(),
                                    list.get(s).getChannelNumber(), s);
                      //  }


                        posi = s;
                        rv_Mneu.smoothScrollToPosition(s);
                        rv_Mneu1.smoothScrollToPosition(s);
                        navAdapter.notifyDataSetChanged();
                        try {
                            if (s == posi) {
                                if (navAdapter.lastClickLay == null) {
                                    // Toast.makeText(context, "null"+i, Toast.LENGTH_SHORT).show();

                                    navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                    navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                } else {
                                    // Toast.makeText(context, "not_null"+i, Toast.LENGTH_SHORT).show();
                                    navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                    navAdapter.lastClickLay.setBackgroundResource(R.drawable.channel_back);
                                    navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                }
                            } else {
                                navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.channel_back);
                                //lastClickLay.setBackgroundResource(R.drawable.bg_side_unsel);
                                navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;

                            }

                        } catch (Exception e) {
                            //   Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                return true;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (layout_squeeze.getVisibility() == View.VISIBLE) {
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) video.getLayoutParams();
                    Display display = getWindowManager().getDefaultDisplay();
                    int width1 = display.getWidth();
                    int height1 = display.getHeight();
                    double ratio1 = ((float) (width1)) / 39.0;
                    double ratio2 = ((float) (height1)) / 36.0;
                    int width2 = (int) (ratio1 * 20);
                    int height2 = (int) (ratio2 * 20);
                    params.width = width2;
                    params.height = height2;
                    params.topMargin = 20;
                    params.rightMargin = 20;
                    video.setLayoutParams(params);
                    if (s >= list.size() - 1) {

                        clickupdown = true;
                        s=0;

                        posi = s;
                        rv_Mneu.smoothScrollToPosition(s);
                        rv_Mneu1.smoothScrollToPosition(s);
                        navAdapter.notifyDataSetChanged();
                        try {
                            if (s == posi) {
                                if (navAdapter.lastClickLay == null) {
                                    // Toast.makeText(context, "null"+i, Toast.LENGTH_SHORT).show();

                                    navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                    navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                } else {
                                    // Toast.makeText(context, "not_null"+i, Toast.LENGTH_SHORT).show();
                                    navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                    navAdapter.lastClickLay.setBackgroundResource(R.drawable.channel_back);
                                    navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                }
                            } else {
                                navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.channel_back);
                                //lastClickLay.setBackgroundResource(R.drawable.bg_side_unsel);
                                navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;

                            }

                        } catch (Exception e) {
                            //   Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                       // Toast.makeText(mContext, "No next channel", Toast.LENGTH_SHORT).show();
                    } else {
                        clickupdown = true;
                        s = s + 1;

                        posi = s;
                        rv_Mneu.smoothScrollToPosition(s);
                        rv_Mneu1.smoothScrollToPosition(s);
                        navAdapter.notifyDataSetChanged();
                        try {
                            if (s == posi) {
                                if (navAdapter.lastClickLay == null) {
                                    // Toast.makeText(context, "null"+i, Toast.LENGTH_SHORT).show();

                                    navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                    navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                } else {
                                    // Toast.makeText(context, "not_null"+i, Toast.LENGTH_SHORT).show();
                                    navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                    navAdapter.lastClickLay.setBackgroundResource(R.drawable.channel_back);
                                    navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                }
                            } else {
                                navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.channel_back);
                                //lastClickLay.setBackgroundResource(R.drawable.bg_side_unsel);
                                navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;

                            }

                        } catch (Exception e) {
                            //   Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                            video.getLayoutParams();
                    params.width = 3500;
                    params.height = 1200;
                    params.leftMargin = 0;
                    params.topMargin = 0;
                    params.rightMargin = 0;
                    video.setLayoutParams(params);
                    if (layout_squeeze.getVisibility() == View.GONE) {
                        lay_bottam.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lay_bottam.setVisibility(View.GONE);
                                //  lay_bottam1.setVisibility(View.GONE);

                            }
                        }, 7000);
                    }

                    if (s == 0) {
                        Toast.makeText(mContext, "No previous channel", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        s = s - 1;
                      //  if (list.get(s).getSubscription().equals("1")) {
                            tv_standard1.setText("");
                            tv_resolution1.setText("Loading..");
                            tv_bitrate.setText("Loading..");
                            txt_name1.setText(list.get(s).getChannelName());
                            txt_chno1.setText(list.get(s).getChannelNumber());
                            tv_standard.setText("");
                            tv_resolution.setText("Loading..");
                            txt_name.setText(list.get(s).getChannelName());
                            txt_chno.setText(list.get(s).getChannelNumber());
                            video1.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            iv_channel.setVisibility(View.VISIBLE);
                            iv_channel.setImageResource(R.drawable.paid_channel);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            iv_channel1.setVisibility(View.GONE);

                            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.rightMargin = 20;
                                params1.topMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                iv_channel.requestFocus();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.rightMargin = 0;
                                params1.topMargin = 0;
                                iv_channel.setLayoutParams(params1);

                            }
                       // } else {
                            loadUrl(list.get(s).getChannelUrl(),
                                    list.get(s).getChannelName(), list.get(s).getImage(),
                                    list.get(s).getChannelNumber(), s);
                      //  }


                        // loadUrl("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample.mp4"
                        //        ,  list.get(i).getChannelName(), list.get(i).getImage(), i);

                        posi = s;
                        rv_Mneu.smoothScrollToPosition(s);
                        rv_Mneu1.smoothScrollToPosition(s);
                        navAdapter.notifyDataSetChanged();

                        try {
                            if (s == posi) {
                                if (navAdapter.lastClickLay == null) {
                                    // Toast.makeText(context, "null"+i, Toast.LENGTH_SHORT).show();

                                    navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                    navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                } else {
                                    // Toast.makeText(context, "not_null"+i, Toast.LENGTH_SHORT).show();
                                    navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                    navAdapter.lastClickLay.setBackgroundResource(R.drawable.channel_back);
                                    navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                }

                            } else {
                                navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.channel_back);
                                //lastClickLay.setBackgroundResource(R.drawable.bg_side_unsel);
                                navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;

                            }

                        } catch (Exception e) {
                            //Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                return true;
                /* case KeyEvent.KEYCODE_NUMPAD_ENTER:
                Toast.makeText(mContext, "NUMPAD ENTER", Toast.LENGTH_SHORT).show();
                loadUrl(list.get(s).getChannelUrl(), list.get(s).getChannelName(), list.get(s).getImage(), s);

                return true;*/

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Log.d("OnKey", "key pressed!");
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);

                //  Toast.makeText(MainActivity.this, "key right!", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                Log.d("OnKey", "key pressed!");
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                //Toast.makeText(MainActivity.this, "key left!", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Log.d("OnKey", "key pressed!");
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                //Toast.makeText(MainActivity.this, "key left!", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:

                Log.d("OnKey", "key pressed!");
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                //Toast.makeText(MainActivity.this, "key left!", Toast.LENGTH_SHORT).show();
                return true;
          /*  case KeyEvent.KEYCODE_DPAD_CENTER:
                Toast.makeText(mContext, "DPAD ENTER", Toast.LENGTH_SHORT).show();

                if (event.getAction() == KeyEvent.ACTION_UP) {
                    getWindow().getCurrentFocus();
                }
                return true;*/


            case KeyEvent.KEYCODE_BACK:

//                if (doubleBackToExitPressedOnce) {
//                    statusOfflineExit();
//                } else {

                if (layout_squeeze.getVisibility() == View.VISIBLE) {
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);


                    if (iv_channel.getVisibility() == View.VISIBLE) {
                        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                iv_channel.getLayoutParams();
                        params1.width = 3500;
                        params1.height = 1200;
                        params1.leftMargin = 0;
                        iv_channel.setLayoutParams(params1);
                        video.setVisibility(View.GONE);
                        layout_squeeze.setVisibility(View.GONE);
                        video1.setVisibility(View.GONE);
                        iv_channel1.setVisibility(View.GONE);
                        ll_navbar.setVisibility(View.VISIBLE);
                        layout_constraint.setVisibility(View.VISIBLE);
                    } else {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                                video.getLayoutParams();
                        params.width = 3500;
                        params.height = 1200;
                        params.leftMargin = 0;
                        params.topMargin = 0;
                        params.rightMargin = 0;
                        video.setLayoutParams(params);
                        layout_squeeze.setVisibility(View.GONE);
                        video1.setVisibility(View.GONE);
                        iv_channel1.setVisibility(View.GONE);
                        ll_navbar.setVisibility(View.VISIBLE);
                        layout_constraint.setVisibility(View.VISIBLE);
                        video.setVisibility(View.VISIBLE);
                    }
                //    hideKeyboard();


                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

                } else {
                    ed_signup.putString("channel_position", String.valueOf(s));
                    ed_signup.commit();
                    if (doubleBackToExitPressedOnce) {
                        finish();
                    }

                    doubleBackToExitPressedOnce= true;
                    Toast.makeText(MainActivity.this, "Press back button twice to exit.", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    },2000);
                    //statusOfflineExit();
                   /* final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.dialog_back);
                    //  dialog.setCancelable(false);
                    // dialog.setCanceledOnTouchOutside(false);
                    //dialog.getWindow().setWindowAnimations(R.style.DialogTheme);
                    dialog.show();
                    Button btncancel = dialog.findViewById(R.id.btn_cancel);
                    Button btnrecharge = dialog.findViewById(R.id.btn_recharge);
                    final CheckBox btn_checkbox = dialog.findViewById(R.id.btn_checkbox);
                    btncancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            statusOfflineExit();
                        }
                    });

                    btnrecharge.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            if (btn_checkbox.isChecked()) {
                                ed_signup.putString("reboot_state", "yes");
                                ed_signup.commit();
                                try {
                                    //Open the specific App Info page:
                                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    //Open the generic Apps page:
                                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                    startActivity(intent);
                                }


                            } else {
                                Toast.makeText(mContext, "Select Checkbox", Toast.LENGTH_SHORT).show();
                                // dialog.dismiss();
                            }

                        }
                    });*/
                    // doubleBackToExitPressedOnce = true;
                    //Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();


                }

                //  }


              /*  new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 10000);*/

             /*   new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setContentText("Are you sure do you want to exit")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                                Date c = Calendar.getInstance().getTime();
                                System.out.println("Current time => " + c);
                                String time = "" + c;
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                String formattedDate = df.format(c);
                                //add_user2(imsi, imei, CountryIso, NetworkOperator, NetworkOperatorName,BRAND,devicename,
                                //  formattedDate, time, "offline");

                                //* Log.e("msg", "onClickBack1: "+imsi );
                                statusOfflineExit();

                            }
                        })
                        .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();*/
                return true;

            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                return true;

            case KeyEvent.KEYCODE_BUTTON_SELECT:

                return true;

            case KeyEvent.KEYCODE_ENTER:
                return true;
            case KeyEvent.KEYCODE_BUTTON_B:
                return true;
            case KeyEvent.KEYCODE_BUTTON_A:
                return true;


          /*  case KeyEvent.KEYCODE_1:
                try {
                    if (keyCode == KeyEvent.KEYCODE_1 && keyCode == KeyEvent.KEYCODE_0) {
                        doublechannel = true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_1) {
                        doublechannel = true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_2) {
                        doublechannel = true;
                    }

                  *//*  else if(keyCode == KeyEvent.KEYCODE_1){
                        singlechannel=true;
                        doublechannel = true;
                    }
                    else if(keyCode == KeyEvent.KEYCODE_2){
                        singlechannel=true;
                        doublechannel = true;
                    }*//*
                    if (doublechannel) {
                        s = 9;
                        // Toast.makeText(mContext, ""+s, Toast.LENGTH_SHORT).show();
                        posi = s;
                        rv_Mneu.smoothScrollToPosition(s);
                        rv_Mneu1.smoothScrollToPosition(s);

                        loadUrl(list.get(s).getChannelUrl(), list.get(s).getChannelName(), list.get(s).getImage(), s);
                        navAdapter.notifyDataSetChanged();

                        try {
                            if (s == posi) {
                                if (navAdapter.lastClickLay == null) {
                                    // Toast.makeText(context, "null"+i, Toast.LENGTH_SHORT).show();

                                    navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                    navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                } else {
                                    // Toast.makeText(context, "not_null"+i, Toast.LENGTH_SHORT).show();
                                    navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                    navAdapter.lastClickLay.setBackgroundResource(R.drawable.bg_side_unsel);
                                    navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                }
                            } else {
                                navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_unsel);
                                //lastClickLay.setBackgroundResource(R.drawable.bg_side_unsel);
                                navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;

                            }
                        } catch (Exception e) {
                        }
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (doublechannel == false) {
                                    s = 0;
                                    // Toast.makeText(mContext, ""+s, Toast.LENGTH_SHORT).show();
                                    posi = s;
                                    rv_Mneu.smoothScrollToPosition(s);
                                    rv_Mneu1.smoothScrollToPosition(s);

                                    loadUrl(list.get(s).getChannelUrl(), list.get(s).getChannelName(), list.get(s).getImage(), s);
                                    navAdapter.notifyDataSetChanged();

                                    try {
                                        if (s == posi) {
                                            if (navAdapter.lastClickLay == null) {
                                                // Toast.makeText(context, "null"+i, Toast.LENGTH_SHORT).show();

                                                navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                                navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                            } else {
                                                // Toast.makeText(context, "not_null"+i, Toast.LENGTH_SHORT).show();
                                                navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                                                navAdapter.lastClickLay.setBackgroundResource(R.drawable.bg_side_unsel);
                                                navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                                            }
                                        } else {
                                            navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_unsel);
                                            //lastClickLay.setBackgroundResource(R.drawable.bg_side_unsel);
                                            navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;

                                        }
                                    } catch (Exception e) {
                                    }
                                } else {
                                    Log.e("msg", "run: " + doublechannel);
                                }

                            }
                        }, 1000);
                    }

                } catch (Exception e) {
                }


                return true;
*/
        }

        if (KeyEvent.KEYCODE_0 == event.getKeyCode()) {
            video.setVisibility(View.VISIBLE);
            try {
                if ((event.getEventTime() - mUpKeyEventTime) < 5000) {
                    // This is to check if Volume UP key and Power key are pressed at the same time.
                    // Do the Task. Here You can add logic to take screenshot
                    s = 9;
                    channelNumber(s);
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Channel Not Available", Toast.LENGTH_SHORT).show();

            }

            return true;
        } else if (KeyEvent.KEYCODE_1 == event.getKeyCode()) {
            video.setVisibility(View.VISIBLE);
            try {
                if ((event.getEventTime() - mUpKeyEventTime) < 5000) {
                    // This is to check if Volume UP key and Power key are pressed at the same time.
                    // Do the Task. Here You can add logic to take screenshot
                    s = 10;
                    channelNumber(s);
                } else {
                    s = 0;
                    channelNumber(s);
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Channel Not Available", Toast.LENGTH_SHORT).show();

            }


            return true;
        } else if (KeyEvent.KEYCODE_2 == event.getKeyCode()) {
            video.setVisibility(View.VISIBLE);
            try {
                if ((event.getEventTime() - mUpKeyEventTime) < 5000) {
                    // This is to check if Volume UP key and Power key are pressed at the same time.
                    // Do the Task. Here You can add logic to take screenshot
                    s = 11;
                    channelNumber(s);
                } else {
                    s = 1;
                    channelNumber(s);
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Channel Not Available", Toast.LENGTH_SHORT).show();

            }

            return true;
        } else if (KeyEvent.KEYCODE_3 == event.getKeyCode()) {
            video.setVisibility(View.VISIBLE);
            try {
                if ((event.getEventTime() - mUpKeyEventTime) < 5000) {
                    // This is to check if Volume UP key and Power key are pressed at the same time.
                    // Do the Task. Here You can add logic to take screenshot
                    s = 12;
                    channelNumber(s);
                } else {
                    s = 2;
                    channelNumber(s);
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Channel Not Available", Toast.LENGTH_SHORT).show();

            }

            return true;
        } else if (KeyEvent.KEYCODE_4 == event.getKeyCode()) {
            video.setVisibility(View.VISIBLE);
            try {
                if ((event.getEventTime() - mUpKeyEventTime) < 5000) {
                    // This is to check if Volume UP key and Power key are pressed at the same time.
                    // Do the Task. Here You can add logic to take screenshot
                    s = 13;
                    channelNumber(s);
                } else {
                    s = 3;
                    channelNumber(s);
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Channel Not Available", Toast.LENGTH_SHORT).show();
            }

            return true;
        } else if (KeyEvent.KEYCODE_5 == event.getKeyCode()) {
            video.setVisibility(View.VISIBLE);
            try {
                if ((event.getEventTime() - mUpKeyEventTime) < 5000) {
                    // This is to check if Volume UP key and Power key are pressed at the same time.
                    // Do the Task. Here You can add logic to take screenshot
                    s = 14;
                    channelNumber(s);
                } else {
                    s = 4;
                    channelNumber(s);
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Channel Not Available", Toast.LENGTH_SHORT).show();

            }

            return true;
        } else if (KeyEvent.KEYCODE_6 == event.getKeyCode()) {
            video.setVisibility(View.VISIBLE);
            try {
                if ((event.getEventTime() - mUpKeyEventTime) < 5000) {
                    // This is to check if Volume UP key and Power key are pressed at the same time.
                    // Do the Task. Here You can add logic to take screenshot
                    s = 15;
                    channelNumber(s);
                } else {
                    s = 5;
                    channelNumber(s);
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Channel Not Available", Toast.LENGTH_SHORT).show();

            }

            return true;
        } else if (KeyEvent.KEYCODE_7 == event.getKeyCode()) {
            video.setVisibility(View.VISIBLE);
            try {
                if ((event.getEventTime() - mUpKeyEventTime) < 5000) {
                    // This is to check if Volume UP key and Power key are pressed at the same time.
                    // Do the Task. Here You can add logic to take screenshot
                    s = 16;
                    channelNumber(s);
                } else {
                    s = 6;
                    channelNumber(s);
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Channel Not Available", Toast.LENGTH_SHORT).show();

            }

            return true;
        } else if (KeyEvent.KEYCODE_8 == event.getKeyCode()) {
            video.setVisibility(View.VISIBLE);
            try {
                if ((event.getEventTime() - mUpKeyEventTime) < 5000) {
                    // This is to check if Volume UP key and Power key are pressed at the same time.
                    // Do the Task. Here You can add logic to take screenshot
                    s = 17;
                    channelNumber(s);
                } else {
                    s = 7;
                    channelNumber(s);
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Channel Not Available", Toast.LENGTH_SHORT).show();

            }

            return true;
        } else if (KeyEvent.KEYCODE_9 == event.getKeyCode()) {
            video.setVisibility(View.VISIBLE);
            try {
                if ((event.getEventTime() - mUpKeyEventTime) < 5000) {
                    // This is to check if Volume UP key and Power key are pressed at the same time.
                    // Do the Task. Here You can add logic to take screenshot
                    s = 18;
                    channelNumber(s);
                } else {
                    s = 8;
                    channelNumber(s);
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Channel Not Available", Toast.LENGTH_SHORT).show();

            }

            return true;
        }
        return true;
    }


    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_1 == keyCode) {
            video.setVisibility(View.VISIBLE);
            mUpKeyEventTime = event.getEventTime();
        }
        return true;

    }

    private void channelNumber(int s1) {
        posi = s1;
        rv_Mneu.smoothScrollToPosition(s1);
        rv_Mneu1.smoothScrollToPosition(s1);
        if (layout_squeeze.getVisibility() == View.GONE) {
            lay_bottam.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lay_bottam.setVisibility(View.GONE);
                    //  lay_bottam1.setVisibility(View.GONE);

                }
            }, 7000);
        }

      //  if (list.get(s1).getSubscription().equals("1")) {
            tv_standard1.setText("");
            tv_resolution1.setText("Loading..");
            tv_bitrate.setText("Loading..");
            txt_name1.setText(list.get(s1).getChannelName());
            txt_chno1.setText(list.get(s1).getChannelNumber());
            tv_standard.setText("");
            tv_resolution.setText("Loading..");
            txt_name.setText(list.get(s1).getChannelName());
            txt_chno.setText(list.get(s1).getChannelNumber());
            video1.setVisibility(View.GONE);
            video.setVisibility(View.GONE);
            iv_channel.setVisibility(View.VISIBLE);
            iv_channel.setImageResource(R.drawable.paid_channel);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            iv_channel1.setVisibility(View.GONE);

            if (layout_squeeze.getVisibility() == View.VISIBLE) {
                iv_channel.requestFocus();
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                        iv_channel.getLayoutParams();
                Display display = getWindowManager().getDefaultDisplay();
                int width1 = display.getWidth();
                int height1 = display.getHeight();
                double ratio1 = ((float) (width1)) / 39.0;
                double ratio2 = ((float) (height1)) / 36.0;
                int width2 = (int) (ratio1 * 20);
                int height2 = (int) (ratio2 * 20);
                params1.width = width2;
                params1.height = height2;
                params1.rightMargin = 20;
                params1.topMargin = 20;
                iv_channel.setLayoutParams(params1);
            } else {
                iv_channel.requestFocus();
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                        iv_channel.getLayoutParams();
                params1.width = 3500;
                params1.height = 1200;
                params1.rightMargin = 0;
                params1.topMargin = 0;
                iv_channel.setLayoutParams(params1);

            }
       // } else {
            loadUrl(list.get(s1).getChannelUrl(), list.get(s1).getChannelName(),
                    list.get(s1).getImage(), list.get(s1).getChannelNumber(), s1);
       // }

        navAdapter.notifyDataSetChanged();

        try {
            if (s1 == posi) {
                if (navAdapter.lastClickLay == null) {
                    // Toast.makeText(context, "null"+i, Toast.LENGTH_SHORT).show();

                    navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource
                            (R.drawable.bg_side_sel);
                    navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                } else {
                    // Toast.makeText(context, "not_null"+i, Toast.LENGTH_SHORT).show();
                    navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.bg_side_sel);
                    navAdapter.lastClickLay.setBackgroundResource(R.drawable.channel_back);
                    navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;
                }
            } else {
                navAdapter.myViewHolderr.ll_Contain1.setBackgroundResource(R.drawable.channel_back);
                //lastClickLay.setBackgroundResource(R.drawable.bg_side_unsel);
                navAdapter.lastClickLay = navAdapter.myViewHolderr.ll_Contain1;

            }
        } catch (Exception e) {
        }
    }

    private void statusOfflineExit() {
        try {
           /* final ACProgressFlower progressDialog = new ACProgressFlower.Builder(MainActivity.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .text("Loading...")
                    .fadeColor(Color.DKGRAY).build();
            progressDialog.show();*/

            String exit_url = "http://pargmedia.com/channel/webservice/exit_offline?user_id=" +
                    "userid" + "&status='offline'";
            RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
            StringRequest srq = new StringRequest(Request.Method.GET, exit_url, new com.android.volley.Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        if (message.equals("successfully")) {
                            ed_signup.putString("channel_position", String.valueOf(s));
                            ed_signup.commit();
                            finish();

                            //  progressDialog.dismiss();
                        } else {
                            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                            //progressDialog.dismiss();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        //   progressDialog.dismiss();
                        finish();
                        Log.e("msg", "Response: " + e.toString());
                        //Snackbar.make(view,"Something went wrong..",Snackbar.LENGTH_SHORT).show();

                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //  progressDialog.dismiss();
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                    finish();
                    // Snackbar.make(view,"Something went wrong..",Snackbar.LENGTH_SHORT).show();

                }
            });
            rq.add(srq);
        } catch (Exception e) {
        }

    }

    private boolean isNetworkConn() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
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

    public void checkLogin(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // login_method();

            }
        },10000);
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {

            //  loadUrl(list.get(s).getChannelUrl(), list.get(s).getChannelName(), list.get(s).getImage(), s);
            if (event.getAction() == KeyEvent.ACTION_UP) {
                getWindow().getCurrentFocus();
                video.setVisibility(View.VISIBLE);
                video1.setVisibility(View.VISIBLE);
                video.setBackgroundColor(Color.TRANSPARENT);
                video1.setBackgroundColor(Color.TRANSPARENT);

                if (clickupdown == true) {

                   // if (list.get(s).getSubscription().equals("1")) {
                        tv_standard1.setText("");
                        tv_resolution1.setText("Loading..");
                        tv_bitrate.setText("Loading..");
                        txt_name1.setText(list.get(s).getChannelName());
                        txt_chno1.setText(list.get(s).getChannelNumber());
                        tv_standard.setText("");
                        tv_resolution.setText("Loading..");
                        txt_name.setText(list.get(s).getChannelName());
                        txt_chno.setText(list.get(s).getChannelNumber());
                        video1.setVisibility(View.GONE);
                        video.setVisibility(View.GONE);
                        iv_channel.setVisibility(View.VISIBLE);
                        iv_channel.setImageResource(R.drawable.paid_channel);

                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);

                        iv_channel1.setVisibility(View.GONE);

                        if (layout_squeeze.getVisibility() == View.VISIBLE) {
                            iv_channel.requestFocus();
                            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                    iv_channel.getLayoutParams();
                            Display display = getWindowManager().getDefaultDisplay();
                            int width1 = display.getWidth();
                            int height1 = display.getHeight();
                            double ratio1 = ((float) (width1)) / 39.0;
                            double ratio2 = ((float) (height1)) / 36.0;
                            int width2 = (int) (ratio1 * 20);
                            int height2 = (int) (ratio2 * 20);
                            params1.width = width2;
                            params1.height = height2;
                            params1.rightMargin = 20;
                            params1.topMargin = 20;
                            iv_channel.setLayoutParams(params1);
                        } else {
                            iv_channel.requestFocus();
                            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                    iv_channel.getLayoutParams();
                            params1.width = 3500;
                            params1.height = 1200;
                            params1.rightMargin = 0;
                            params1.topMargin = 0;
                            iv_channel.setLayoutParams(params1);

                        }
                   // } else {
                        loadUrl(list.get(s).getChannelUrl(),
                                list.get(s).getChannelName(), list.get(s).getImage(),
                                list.get(s).getChannelNumber(), s);
                   // }

                    clickupdown = false;
                } else {
                    if (doubleclick == false) {
                        try {
                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            if (iv_channel.getVisibility() == View.VISIBLE) {
                                iv_channel1.setVisibility(View.GONE);
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) iv_channel.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params1.width = width2;
                                params1.height = height2;
                                params1.topMargin = 20;
                                params1.rightMargin = 20;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) video.getLayoutParams();
                                Display display = getWindowManager().getDefaultDisplay();
                                int width1 = display.getWidth();
                                int height1 = display.getHeight();
                                double ratio1 = ((float) (width1)) / 39.0;
                                double ratio2 = ((float) (height1)) / 36.0;
                                int width2 = (int) (ratio1 * 20);
                                int height2 = (int) (ratio2 * 20);
                                params.width = width2;
                                params.height = height2;
                                params.topMargin = 20;
                                params.rightMargin = 20;
                                video.setLayoutParams(params);
                            }

                            ll_navbar.setVisibility(View.GONE);
                            lay_bottam.setVisibility(View.GONE);
                            video.setVisibility(View.VISIBLE);
                            // layout_constraint.setVisibility(View.VISIBLE);
                            layout_squeeze.setVisibility(View.VISIBLE);
                            video1.setVisibility(View.VISIBLE);
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            doubleclick = true;
//                    if (!drawer.isDrawerOpen(Gravity.START)) drawer.openDrawer(Gravity.START);
//                    else drawer.closeDrawer(Gravity.END);


                        } catch (Exception e) {
                        }
                    } else if (doubleclick == true) {
                        try {
                            // drawer.closeDrawer(Gravity.LEFT);
                            // Animation slideUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_down);
                            if (iv_channel.getVisibility() == View.VISIBLE) {
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
                                        iv_channel.getLayoutParams();
                                params1.width = 3500;
                                params1.height = 1200;
                                params1.leftMargin = 0;
                                iv_channel.setLayoutParams(params1);
                            } else {
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                                        video.getLayoutParams();
                                params.width = 3500;
                                params.height = 1200;
                                params.leftMargin = 0;
                                params.topMargin = 0;
                                params.rightMargin = 0;
                                video.setLayoutParams(params);
                            }

                            layout_squeeze.setVisibility(View.GONE);
                            video1.setVisibility(View.GONE);
                            iv_channel1.setVisibility(View.GONE);
                            ll_navbar.setVisibility(View.VISIBLE);
                            video.setVisibility(View.VISIBLE);
                            layout_constraint.setVisibility(View.VISIBLE);
                            //  iv_channel.setVisibility(View.VISIBLE);

                            getWindow().addFlags(
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            getWindow().clearFlags(
                                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

                            doubleclick = false;
                        } catch (Exception e) {
                        }
                    }
                }
            }
            return true; // Consume the event (or not, your call)
        }
        return super.dispatchKeyEvent(event);
    }

    /*public void getBitrate() {

        MediaExtractor extractor = new MediaExtractor();
        AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.wathanam_new);
        try {
            extractor.setDataSource("http://7starcloud.com:1935/jawahartv/streamingbox_576p/playlist.m3u8");
            MediaFormat mf = extractor.getTrackFormat(0);
            //  int bitRate = mf.getInteger(MediaFormat.KEY_BIT_RATE);
            //  int samplerate = mf.getInteger(MediaFormat.KEY_SAMPLE_RATE);

            String mime = mf.getString(MediaFormat.KEY_MIME);
            String samplerate = mf.getString(MediaFormat.KEY_SAMPLE_RATE);
            Log.e("msg", "getBitrate1: " + mime);
            Log.e("msg", "getBitrate2: " + samplerate);

            //     int sampleRate = mf.getInteger(MediaFormat.KEY_SAMPLE_RATE);
            // Toast.makeText(mContext, "Bitrate: "+bitRate, Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("msg", "getBitrate3: " + e.toString());


        }


    }*/

    public void login_method(){


        String loginurl="http://pargmedia.com/channel/webservice/user_Login?phone_no="
                +"email"+"&password="+"password"+
                "&macaddress="+mac_address+"&ipaddress="+ip_address+"&devicename="+devicename+
                "&country="+CountryIso+"&status=online&operator="+NetworkOperatorName;

        Log.e("msgg", "login_methodd: "+loginurl);

        RequestQueue rq= Volley.newRequestQueue(MainActivity.this);
        StringRequest srq=new StringRequest(Request.Method.GET, loginurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject =new JSONObject(response);
                    String result=jsonObject.getString("result");
                    String message=jsonObject.getString("message");
                    Log.e("msg", "onResponseLogin: "+response );
                    //     Toast.makeText(LoginActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                    if(message.equals("unsuccessfull,  Contact support @9659857985.")){
                       // Snackbar.make(view,"Please contact support @9659857985",Snackbar.LENGTH_SHORT).show();

                    }
                    else {
                        if(result.equals("unsuccessfull")){
                           // Snackbar.make(view,"Incorrect email or password",Snackbar.LENGTH_SHORT).show();
                        }
                        else {
                            JSONObject jObject=jsonObject.getJSONObject("result");
                            if(jObject.getString("user_status")
                                    .equalsIgnoreCase("deactivated")){
                                ed_signup.clear();
                                ed_signup.apply();
                                ed_signup.commit();
                                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                                intent.putExtra("account_status","deactivated");
                                startActivity(intent);
                                finish();
                            }
                            else  if(jObject.getString("user_status")
                                    .equalsIgnoreCase("deleted")){
                                ed_signup.clear();
                                ed_signup.apply();
                                ed_signup.commit();
                                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                                intent.putExtra("account_status","deleted");
                                startActivity(intent);
                                finish();
                            }
                            else if(jObject.getString("user_status")
                                    .equalsIgnoreCase("activated")) {

                              //  checkLogin();
                                //Toast.makeText(MainActivity.this, "hii", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                 //   Snackbar.make(view,"Something went wrong.."+e.toString(),Snackbar.LENGTH_SHORT).show();

                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Snackbar.make(view,"Something went wrong.."+error.toString(),Snackbar.LENGTH_SHORT).show();


            }
        });
        rq.add(srq);

    }

}
