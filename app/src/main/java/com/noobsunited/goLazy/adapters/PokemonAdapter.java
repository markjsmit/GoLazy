package com.noobsunited.goLazy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.noobsunited.goLazy.GoLazyApp;
import com.noobsunited.goLazy.R;
import com.noobsunited.goLazy.helpers.CheckablePokemon;
import com.noobsunited.goLazy.services.SettingsService;
import com.pokegoapi.api.pokemon.Pokemon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by markj on 7/31/2016.
 */
public class PokemonAdapter extends BaseAdapter {
    private final List<CheckablePokemon> pokemonList;
    private Context context;

    private String[] sortTypes=new String[]{"cp","name","number"};
    private int currentSortType=0;

    @Inject
    public SettingsService settingsService;


    public PokemonAdapter(Context context, List<Pokemon> pokemonList) {

        this.context = context;
        this.pokemonList=new ArrayList<>();
        int i=0;
        for(Pokemon pokemon : pokemonList){
            CheckablePokemon checkable=new CheckablePokemon(pokemon);
            this.pokemonList.add(checkable);
            i++;
        }

        ((GoLazyApp) context.getApplicationContext()).getPokemonComponent().inject(this);
        this.currentSortType=Integer.parseInt(settingsService.get("default_sort","0"));
        doSort();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view=convertView;
        CheckablePokemon pokemon = (CheckablePokemon)getItem(position);

        if (convertView == null) {
            view = new View(context);
            view = inflater.inflate(R.layout.view_pokemon, null);
        }




        if(pokemon!=null) {
            TextView cpView = (TextView) view.findViewById(R.id.pokemonCP);
            cpView.setText(pokemon.pokemon.getCp() + context.getString(R.string.info_cp));


            TextView nameView = (TextView) view.findViewById(R.id.pokemonName);
            nameView.setText(pokemon.pokemon.getPokemonId().name());

        }


        pokemon.invalidated=true;
        setSelected(position, view);

        return view;
    }

    @Override
    public int getCount() {
        if(pokemonList!=null) {
            return pokemonList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return pokemonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void toggleItem(int position, View view){
        CheckablePokemon pokemon = (CheckablePokemon)getItem(position);
        pokemon.checked=! pokemon.checked;
        pokemon.invalidated=true;
        setSelected(position,view);
    }


    public List<Pokemon> getSelectedItems(){
        ArrayList<Pokemon>  list = new ArrayList<>();

        for(CheckablePokemon pokemon:pokemonList){
            if(pokemon.checked){
                list.add(pokemon.pokemon);
            }
        }

        return list;
    }

    private void setSelected(int position, View view) {
        CheckablePokemon pokemon = (CheckablePokemon)getItem(position);
        if(  pokemon.invalidated) {
            ImageView imageView = (ImageView) view.findViewById(R.id.pokemonImage);
            TextView nameView = (TextView) view.findViewById(R.id.pokemonName);
            if ( pokemon.checked) {

                int imageId = context.getResources().getIdentifier("gray_" + pokemon.pokemon.getPokemonId().getNumber(), "drawable", context.getPackageName());
                imageView.setImageResource(imageId);


                nameView.setTextColor(context.getResources().getColor(R.color.colorSelected));


            } else {

                int imageId = context.getResources().getIdentifier("color_" + pokemon.pokemon.getPokemonId().getNumber(), "drawable", context.getPackageName());
                imageView.setImageResource(imageId);

                nameView.setTextColor(context.getResources().getColor(R.color.colorUnselected));
            }
        }
    }

    public void toggleSortType(){
        currentSortType++;
        if(currentSortType>=sortTypes.length){
            currentSortType=0;
        }
        settingsService.set("default_sort",""+currentSortType);
        doSort();

    }

    private void doSort() {
       Collections.sort(pokemonList, new Comparator<CheckablePokemon>(){
            public int compare(CheckablePokemon o1, CheckablePokemon o2){
                if(currentSortType==0){
                    if(o1.pokemon.getCp() == o2.pokemon.getCp()){
                        return 0;
                    }else{
                       return  o1.pokemon.getCp() < o2.pokemon.getCp() ? 1 : -1;
                    }
                }


                else if(currentSortType==1){
                    return o1.pokemon.getPokemonId().name().compareTo(o2.pokemon.getPokemonId().name());
                }

                else if (currentSortType==2){
                    if(o1.pokemon.getPokemonId().getNumber() == o2.pokemon.getPokemonId().getNumber()){
                        return 0;
                    }else{
                        return  o1.pokemon.getPokemonId().getNumber() < o2.pokemon.getPokemonId().getNumber() ? -1 : 1;
                    }
                }


                return 0;
            }
        });
        notifyDataSetChanged();
    }

    public String getSortType(){
        return sortTypes[currentSortType];
    }

}