package com.noobsunited.goLazy.helpers;

import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.noobsunited.goLazy.interfaces.IFail;
import com.noobsunited.goLazy.interfaces.IGoogleTokenSuccess;

/**
 * Created by markj on 7/30/2016.
 */
public class AuthWebViewClient extends WebViewClient {


    IFail failEvent=null;
    IGoogleTokenSuccess succesEvent=null;


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    public void onSuccess(IGoogleTokenSuccess event){
        succesEvent=event;
    }

    public void onFail(IFail event){
        failEvent=event;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        String cleaned= String.valueOf(url.toCharArray());
        int index =cleaned.indexOf("approval");
        if(index>-1){
                view.evaluateJavascript("(function(){return document.getElementById('code').value})()",new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {
                        if(html.equals("null")){
                            if(failEvent!=null){
                                failEvent.handleFail();
                            }
                        }else{
                            if(succesEvent!=null){
                                html=html.substring(1, html.length()-1);
                                succesEvent.handleSuccess(html.replace("\"",""));
                            }
                        }

                    }
                });

        }
    }
}

