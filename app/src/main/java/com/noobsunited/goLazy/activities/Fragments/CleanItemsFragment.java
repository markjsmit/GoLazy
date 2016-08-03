package com.noobsunited.goLazy.activities.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.noobsunited.goLazy.GoLazyApp;
import com.noobsunited.goLazy.R;
import com.noobsunited.goLazy.helpers.ItemHelper;
import com.noobsunited.goLazy.interfaces.IFail;
import com.noobsunited.goLazy.interfaces.IGetItemsSuccess;
import com.noobsunited.goLazy.interfaces.IItemsCleanedSuccess;
import com.noobsunited.goLazy.services.ItemService;
import com.noobsunited.goLazy.services.SettingsService;
import com.pokegoapi.api.inventory.Item;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import POGOProtos.Inventory.Item.ItemIdOuterClass;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CleanItemsFragment extends Fragment {

    @BindView(R.id.ballsText)
    public EditText balls;

    @BindView(R.id.potionsText)
    public EditText potions;

    @BindView(R.id.raspberryText)
    public EditText raspberries;

    @BindView(R.id.revivesText)
    public EditText revives;

    @BindView(R.id.cleanItemsLayout)
    public LinearLayout pageBody;

    @BindView(R.id.loadBar)
    public FrameLayout loadBar;

    @Inject
    public SettingsService settingsService;

    @Inject
    public ItemService itemService;
    protected List<Item> items;

    private Unbinder unbinder;

    public CleanItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ((GoLazyApp) getActivity().getApplication()).getPokemonComponent().inject(this);
        }catch(Exception ex){
            Log.e("muhaha","muhaha");
        }

        itemService.getItems(new handleGetItemsSucces(), new handleFail());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_clean_items, container, false);
        unbinder = ButterKnife.bind(this, view);
        setEnabled(pageBody,false);
        initForm();
        loadBar.setVisibility(View.INVISIBLE);
        return view;
    }

    private void initForm() {
        balls.setText(settingsService.get("clean_balls","75"));
        potions.setText(settingsService.get("clean_potions","50"));
       revives.setText(settingsService.get("clean_revives","40"));
        raspberries.setText(settingsService.get("clean_raspberries","35"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.cleanButton)
    public void startClean(){

        List<Map.Entry<ItemIdOuterClass.ItemId, Integer>> toRemove = ItemHelper.findToRemoveItems(items, Integer.parseInt(balls.getText().toString()), Integer.parseInt(potions.getText().toString()), Integer.parseInt(revives.getText().toString()), Integer.parseInt(raspberries.getText().toString()));
        itemService.removeItems(toRemove,new handleRemoveItemsSuccess(),new handleFail());
    }

    private class handleRemoveItemsSuccess implements IItemsCleanedSuccess{

        @Override
        public void handleSuccess() {
            settingsService.set("clean_balls",balls.getText().toString());
            settingsService.set("clean_potions",potions.getText().toString());
            settingsService.set("clean_revives",revives.getText().toString());
            settingsService.set("clean_raspberries",raspberries.getText().toString());
            setEnabled(pageBody,false);
            loadBar.setVisibility(View.VISIBLE);
            itemService.getItems(new handleGetItemsSucces(), new handleFail());
            Toast.makeText(getContext(),"clean successful",Toast.LENGTH_SHORT).show();
        }
    }


    private class handleGetItemsSucces implements IGetItemsSuccess{



        @Override
        public void handleSuccess(List<Item> itemsResult) {
            setEnabled(pageBody,true);
            loadBar.setVisibility(View.INVISIBLE);
            items=itemsResult;

        }
    }


    private class handleFail implements IFail {
        @Override
        public void handleFail() {
            Toast.makeText(getContext(),"clean failed",Toast.LENGTH_SHORT).show();
            setEnabled(pageBody,false);
            loadBar.setVisibility(View.INVISIBLE);
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
