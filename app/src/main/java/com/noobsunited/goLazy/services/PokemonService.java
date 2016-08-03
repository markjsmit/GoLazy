package com.noobsunited.goLazy.services;


import android.os.AsyncTask;

import com.noobsunited.goLazy.interfaces.IFail;
import com.noobsunited.goLazy.interfaces.IGetPokemonSuccess;
import com.noobsunited.goLazy.interfaces.IPokemonTransferred;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import java.util.List;

import okhttp3.OkHttpClient;

/**
 * Created by markj on 7/25/2016.
 */
public class PokemonService {


    OkHttpClient httpClient = new OkHttpClient();

    GoService go;

    public PokemonService(GoService go) {
        this.go = go;
    }


    //get pokemon
    public void getPokemon(IGetPokemonSuccess success, IFail fail) {
        new getPokemonTask(success, fail).execute();
    }

    class getPokemonTask extends AsyncTask<Void, Void, Void> {

        private final IFail failEvent;
        private final IGetPokemonSuccess successEvent;
        private boolean success;
        private List<Pokemon> pokemons;

        public getPokemonTask(IGetPokemonSuccess success, IFail fail) {
            this.successEvent = success;
            this.failEvent = fail;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                this.pokemons = go.getClient().getInventories().getPokebank().getPokemons();
                success = true;
            } catch (Exception ex) {
                success = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (success) {
                this.successEvent.handleSuccess(pokemons);
            } else {
                this.failEvent.handleFail();
            }
        }

    }

    public void transferPokemon(List<Pokemon> pokemons, IPokemonTransferred success, IFail fail) {
        new transferPokemon(pokemons, success, fail).execute();
    }

    class transferPokemon extends AsyncTask<Void, Void, Void> {


        private final IFail failEvent;
        private final IPokemonTransferred successEvent;
        private final List<Pokemon> pokemons;
        private boolean success = false;

        public transferPokemon(List<Pokemon> pokemons, IPokemonTransferred success, IFail fail) {
            this.pokemons = pokemons;
            this.successEvent = success;
            this.failEvent = fail;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (Pokemon pokemon : pokemons) {
                    pokemon.transferPokemon();
                }
                success = true;
            } catch (LoginFailedException e) {
                success = false;
            } catch (RemoteServerException e) {
                success = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (success) {
                this.successEvent.handleSuccess();
            } else {
                this.failEvent.handleFail();
            }
        }

    }


}
