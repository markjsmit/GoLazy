package com.noobsunited.goLazy;

import android.app.Application;

import com.noobsunited.goLazy.modules.AppModule;
import com.noobsunited.goLazy.modules.DaggerPokemonComponent;
import com.noobsunited.goLazy.modules.PokemonComponent;
import com.noobsunited.goLazy.modules.PokemonModule;

/**
 * Created by markj on 7/25/2016.
 */
public class GoLazyApp extends Application {

    private PokemonComponent mPokemonComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger%COMPONENT_NAME%
        mPokemonComponent = DaggerPokemonComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(new AppModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .pokemonModule(new PokemonModule())
                .build();

        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead:
        //  mNetComponent = com.codepath.dagger.components.DaggerNetComponent.create();
    }

    public PokemonComponent getPokemonComponent() {
        return mPokemonComponent;
    }
}
