package com.noobsunited.goLazy.activities.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.noobsunited.goLazy.GoLazyApp;
import com.noobsunited.goLazy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mark on 3-8-16.
 */
public class HomeFragment extends NavFragment {
    private Unbinder unbinder;

    @BindView(R.id.webView)
    WebView mainPage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GoLazyApp) getActivity().getApplication()).getPokemonComponent().inject(this);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        unbinder= ButterKnife.bind(this,view);

        mainPage.loadUrl("http://golazy.noobsunited.com/mainscreen.html");

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
