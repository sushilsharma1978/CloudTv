package com.cloud6.pargmedia.Fragment;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.VideoView;

import com.cloud6.pargmedia.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment{

    private View mView;
    private String url="";
    private VideoView videoView;

    public HomeFragment() {
        // Required empty public constructor
    }

    public HomeFragment(String url) {
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        hideSystemUI();
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        finds();



        videoView.setVideoURI(Uri.parse(url));
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                    videoView.setVideoURI(Uri.parse(url));
                    videoView.start();
            }
        });


        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(getContext(), "API123"+"What " + what + " extra " + extra, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return mView;
    }

    private void finds() {

        videoView = mView.findViewById(R.id.view_Video);
    }



    private void hideSystemUI() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

}
