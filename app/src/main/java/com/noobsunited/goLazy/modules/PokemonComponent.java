package com.noobsunited.goLazy.modules;

import com.noobsunited.goLazy.activities.Auth.GoogleAuth;
import com.noobsunited.goLazy.activities.Auth.PtcAuth;
import com.noobsunited.goLazy.activities.Fragments.CleanItemsFragment;
import com.noobsunited.goLazy.activities.Fragments.HomeFragment;
import com.noobsunited.goLazy.activities.Fragments.TransferFragment;
import com.noobsunited.goLazy.activities.MainActivity;
import com.noobsunited.goLazy.activities.mainMenuActivity;
import com.noobsunited.goLazy.adapters.PokemonAdapter;

import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by markj on 7/25/2016.
 */
@Singleton
@Component(modules={AppModule.class, PokemonModule.class,SettingsModule.class})
public interface PokemonComponent {
    void inject(MainActivity activity);

    void inject(PtcAuth googleAuth);

    void inject(GoogleAuth googleAuth);

    void inject(TransferFragment transferFragment);

    void inject(CleanItemsFragment cleanItemsFragment);

    void inject(mainMenuActivity mainMenuActivity);

    void inject(HomeFragment homeFragment);

    void inject(PokemonAdapter pokemonAdapter);
    // void inject(MyFragment fragment);
    // void inject(MyService service);
}