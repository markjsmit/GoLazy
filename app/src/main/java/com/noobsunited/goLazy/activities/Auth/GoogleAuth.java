package com.noobsunited.goLazy.activities.Auth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.noobsunited.goLazy.GoLazyApp;
import com.noobsunited.goLazy.R;
import com.noobsunited.goLazy.helpers.AuthWebViewClient;
import com.noobsunited.goLazy.interfaces.IFail;
import com.noobsunited.goLazy.interfaces.IAuthSuccess;
import com.noobsunited.goLazy.interfaces.IGoogleTokenSuccess;
import com.noobsunited.goLazy.services.AuthService;
import com.pokegoapi.auth.GoogleUserCredentialProvider;
import com.pokegoapi.util.Log;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoogleAuth extends AppCompatActivity {

    @BindView(R.id.googleAuthScreen)
    public WebView authScreen;


    @Inject
    public AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((GoLazyApp) getApplication()).getPokemonComponent().inject(this);
        setContentView(R.layout.activity_google_auth);
        ButterKnife.bind(this);
        setTitle("Google login");
        setupWebView();
        loadBar.setVisibility(View.INVISIBLE);
    }

    @BindView(R.id.loadBar)
    public FrameLayout loadBar;



    public void setupWebView(){
        try {
            AuthWebViewClient webViewClient = new AuthWebViewClient();
            webViewClient.onSuccess(new googleTokenSuccess());
            webViewClient.onFail(new fail());

            authScreen.getSettings().setJavaScriptEnabled(true);
            authScreen.setWebViewClient(webViewClient);
            authScreen.loadUrl(GoogleUserCredentialProvider.LOGIN_URL);
        }catch(Exception ex){

            Log.e("","");
        }

    }

    class googleTokenSuccess implements IGoogleTokenSuccess {
        @Override
        public void handleSuccess(String token) {
            loadBar.setVisibility(View.VISIBLE);
            authScreen.loadData("Please wait.", "text/html", "utf-8");
            authService.googleLogin(token,new authSuccess(), new fail());
            Toast.makeText(getApplicationContext(),"Trying to log in", Toast.LENGTH_LONG);
        }
    }

    class authSuccess implements IAuthSuccess {

        @Override
        public void handleSuccess() {
            try{
              setResult(RESULT_OK);
                finish();
            }catch (Exception ex){
                ex.getMessage();
            }

        }


    }

    class fail implements IFail {
        @Override
        public void handleFail() {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

}
