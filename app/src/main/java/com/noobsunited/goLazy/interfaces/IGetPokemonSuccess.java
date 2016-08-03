package com.noobsunited.goLazy.interfaces;

import com.pokegoapi.api.pokemon.Pokemon;

import java.util.List;

/**
 * Created by markj on 7/31/2016.
 */
public interface IGetPokemonSuccess {
    void handleSuccess(List<Pokemon> pokemons);
}
