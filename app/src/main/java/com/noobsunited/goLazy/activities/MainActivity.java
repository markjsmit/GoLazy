package com.noobsunited.goLazy.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.noobsunited.goLazy.GoLazyApp;
import com.noobsunited.goLazy.R;
import com.noobsunited.goLazy.activities.Auth.GoogleAuth;
import com.noobsunited.goLazy.activities.Auth.PtcAuth;
import com.noobsunited.goLazy.helpers.RequestCodes;
import com.noobsunited.goLazy.interfaces.IAuthSuccess;
import com.noobsunited.goLazy.interfaces.IFail;
import com.noobsunited.goLazy.services.AuthService;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends  AppCompatActivity {

    @Inject
    public AuthService authService;

    @BindView(R.id.mainScreen)
    RelativeLayout baseView;

    @BindView(R.id.topText)
    public TextView topText;

    @BindView(R.id.loadBar)
    public FrameLayout loadBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GoLazyApp) getApplication()).getPokemonComponent().inject(this);
    try {

        setContentView(R.layout.activity_main);
    }catch(Exception ex){
            Log.e(" meh"," meh");

        }


        ButterKnife.bind(this);

        setTitle(R.string.app_name);

        if(authService.shouldRestore()){
            setEnabled(baseView,false);
            authService.restore( new authSuccess(),new authFail());
            topText.setText("Trying to log in to the server");
            loadBar.setVisibility(View.VISIBLE);
        }else{
            loadBar.setVisibility(View.INVISIBLE);
        }

    }

    @OnClick(R.id.googleLoginButton)
    public void OpenGoogleLogin(View view) {
        Intent intent = new Intent(this,GoogleAuth.class);
        startActivityForResult(intent, RequestCodes.AUTH);
    }

    @OnClick(R.id.PtcLoginButton)
    public void OpenPtcLogin(View view) {
        Intent intent = new Intent(this,PtcAuth.class);
        startActivityForResult(intent, RequestCodes.AUTH);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RequestCodes.AUTH) {
            if (resultCode == RESULT_OK) {
               Intent intent=new Intent(this,mainMenuActivity.class);
               startActivity(intent);
                finish();
            }else{
                Toast.makeText(this,"Login failed",Toast.LENGTH_SHORT).show();
            }
        }
    }


    class authFail implements IFail {

        @Override
        public void handleFail() {
            setEnabled(baseView,true);
            Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_SHORT).show();
            topText.setText("Choose a login method");
            loadBar.setVisibility(View.INVISIBLE);
        }
    }


    class authSuccess implements IAuthSuccess{

        @Override
        public void handleSuccess() {
            Intent intent=new Intent(getApplicationContext(),mainMenuActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private  void setEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setEnabled(child, enabled);
            }
        }
        topText.setEnabled(true);
    }
}
