package com.noobsunited.goLazy.services;

import com.pokegoapi.api.PokemonGo;

/**
 * Created by mark on 1-8-16.
 */
public class GoService {
    private PokemonGo client;

    public PokemonGo getClient() {
        return client;
    }

    public void setClient(PokemonGo go) {
        client=go;
    }
    
}
