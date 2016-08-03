package com.noobsunited.goLazy.activities.Auth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.noobsunited.goLazy.GoLazyApp;
import com.noobsunited.goLazy.R;
import com.noobsunited.goLazy.interfaces.IAuthSuccess;
import com.noobsunited.goLazy.interfaces.IFail;
import com.noobsunited.goLazy.services.AuthService;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PtcAuth extends AppCompatActivity {

    @BindView(R.id.usenameText)
    EditText Username;

    @BindView(R.id.passwordText)
    EditText Password;

    @BindView(R.id.ptcLoginScreen)
    RelativeLayout baseView;

    @BindView(R.id.loadBar)
    public FrameLayout loadBar;



    @Inject
    public AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((GoLazyApp) getApplication()).getPokemonComponent().inject(this);
        setContentView(R.layout.activity_ptc_auth);
        ButterKnife.bind(this);
        setTitle("Pokemon trainer club login");
        loadBar.setVisibility(View.INVISIBLE);
    }


    @OnClick(R.id.loginButton)
    protected void onLoginClick(View view){
        Toast.makeText(getApplicationContext(),"Trying to log in", Toast.LENGTH_LONG);
        loadBar.setVisibility(View.VISIBLE);
        authService.ptcLogin(
                Username.getText().toString(),
                Password.getText().toString(),
                new authSuccess(),
                new authFail()
        );

        setEnabled(baseView,false);
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

    class authFail implements IFail {
        @Override
        public void handleFail() {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private static void setEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setEnabled(child, enabled);
            }
        }
    }

}
