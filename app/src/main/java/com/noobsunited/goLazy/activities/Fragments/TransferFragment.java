package com.noobsunited.goLazy.activities.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.noobsunited.goLazy.GoLazyApp;
import com.noobsunited.goLazy.R;
import com.noobsunited.goLazy.adapters.PokemonAdapter;
import com.noobsunited.goLazy.interfaces.IFail;
import com.noobsunited.goLazy.interfaces.IGetPokemonSuccess;
import com.noobsunited.goLazy.interfaces.IPokemonTransferred;
import com.noobsunited.goLazy.services.PokemonService;
import com.pokegoapi.api.pokemon.Pokemon;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class TransferFragment extends NavFragment {

    private Unbinder unbinder;



    @BindView(R.id.pokemonGrid)
    public GridView pokemonGrid;

    @BindView(R.id.transferLayout)
    public RelativeLayout pageBody;

    @BindView(R.id.loadBar)
    public FrameLayout loadBar;

    @BindView(R.id.sortButton)
    public Button sortButton;

    public PokemonAdapter pokemonGridAdapter;


    @Inject
    public PokemonService pokemonService;

    public TransferFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GoLazyApp) getActivity().getApplication()).getPokemonComponent().inject(this);
        pokemonService.getPokemon(new handleFoundPokemon(),new handleFail());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_transfer, container, false);
        unbinder=ButterKnife.bind(this,view);


            setEnabled(pageBody,false);
             loadBar.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    private class handleFoundPokemon implements IGetPokemonSuccess {
        @Override
        public void handleSuccess(List<Pokemon> pokemons) {
                pokemonGridAdapter = new PokemonAdapter(getContext(),pokemons);
                pokemonGrid.setAdapter(pokemonGridAdapter);

                pokemonGrid.setOnItemClickListener( new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView , View view , int position ,long arg3)
                    {
                        pokemonGridAdapter.toggleItem(position,view);
                    }
                });
                 setEnabled(pageBody,true);
                sortButton.setText("Sort ("+pokemonGridAdapter.getSortType()+")");
            loadBar.setVisibility(View.INVISIBLE);

        }
    }

    @OnClick(R.id.sortButton)
    public void sortButtonClick(View view){
        pokemonGridAdapter.toggleSortType();
        sortButton.setText("Sort ("+pokemonGridAdapter.getSortType()+")");
    }

    private class handleFail implements IFail {
        @Override
        public void handleFail() {
            Toast.makeText(getContext(),"Transfer failed",Toast.LENGTH_SHORT).show();
            setEnabled(pageBody,false);
            loadBar.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.transferButton)
    public void transferPokemon(View view) {
        List<Pokemon> pokemons= pokemonGridAdapter.getSelectedItems();
        pokemonService.transferPokemon(pokemons,new handleSuccess(),new handleFail());
        loadBar.setVisibility(View.VISIBLE);

    }

    private class handleSuccess implements IPokemonTransferred{

        @Override
        public void handleSuccess() {
            Toast.makeText(getContext(),"Transfer succesful",Toast.LENGTH_SHORT).show();
            pokemonService.getPokemon(new handleFoundPokemon(),new handleFail());
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
