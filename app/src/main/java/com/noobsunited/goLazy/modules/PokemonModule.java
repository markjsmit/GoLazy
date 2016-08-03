package com.noobsunited.goLazy.modules;

import com.noobsunited.goLazy.services.AuthService;
import com.noobsunited.goLazy.services.GoService;
import com.noobsunited.goLazy.services.ItemService;
import com.noobsunited.goLazy.services.PokemonService;
import com.noobsunited.goLazy.services.SettingsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by markj on 7/25/2016.
 */
@Module
public class PokemonModule {
        // Dagger will only look for methods annotated with @Provides
        @Provides
        @Singleton
        PokemonService providesPokemonService(GoService go) {
            return new PokemonService(go);
        }

        @Provides
        @Singleton
        AuthService providesAuthService(GoService go, SettingsService settingsService) {
                  return new AuthService(go,settingsService);
        }

        @Provides
        @Singleton
        GoService providesGoService() {
                return new GoService();
        }

        @Provides
        @Singleton
        ItemService provideItemService(GoService go) {return new ItemService(go);
        }
}
