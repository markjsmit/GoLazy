package com.noobsunited.goLazy.helpers;

import com.pokegoapi.api.pokemon.Pokemon;

/**
 * Created by markj on 8/3/2016.
 */
public class CheckablePokemon {
    public boolean checked=false;
    public boolean invalidated=true;
    public Pokemon pokemon;

    public CheckablePokemon(Pokemon pokemon) {

        this.pokemon = pokemon;
    }
}
