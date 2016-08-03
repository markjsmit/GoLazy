package com.noobsunited.goLazy.services;

import android.os.AsyncTask;
import android.util.Log;

import com.noobsunited.goLazy.interfaces.IAuthSuccess;
import com.noobsunited.goLazy.interfaces.IFail;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.auth.GoogleUserCredentialProvider;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import okhttp3.OkHttpClient;

/**
 * Created by mark on 1-8-16.
 */
public class AuthService {


    GoService go;
    private SettingsService settingsService;

    public AuthService(GoService go, SettingsService settingsService) {
        this.go = go;
        this.settingsService = settingsService;
    }


    OkHttpClient httpClient = new OkHttpClient();

    //google
    public void googleLogin(String token, IAuthSuccess success, IFail fail) {
        new googleLoginTask(token, success, fail).execute();
    }

    class googleLoginTask extends AsyncTask<Void, Void, Void> {


        private final IFail fail;
        private final IAuthSuccess success;
        private final String token;
        boolean successful = false;

        googleLoginTask(String token, IAuthSuccess success, IFail fail) {
            this.success = success;
            this.fail = fail;
            this.token = token;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (successful) {
                this.success.handleSuccess();
            } else {
                this.fail.handleFail();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {


            try {
                GoogleUserCredentialProvider provider = null;
                provider = new GoogleUserCredentialProvider(httpClient);
                provider.login(token);
                go.setClient(new PokemonGo(provider, httpClient));
                this.successful = true;

                settingsService.set("auth_method", "google");
                settingsService.set("auth_token", provider.getRefreshToken());
            } catch (LoginFailedException e) {

            } catch (RemoteServerException e) {

            }
            return null;
        }


    }


    //ptc
    public void ptcLogin(String username, String password, IAuthSuccess success, IFail fail) {
        new ptcLoginTask(username, password, success, fail).execute();
    }

    class ptcLoginTask extends AsyncTask<Void, Void, Void> {
        private final IFail fail;
        private String username;
        private String password;
        private IAuthSuccess success;
        boolean successful = false;

        ptcLoginTask(String username, String password, IAuthSuccess success, IFail fail) {
            this.username = username;
            this.password = password;
            this.success = success;
            this.fail = fail;

        }

        @Override
        protected void onPostExecute(Void result) {
            if (successful) {
                this.success.handleSuccess();
            } else {
                this.fail.handleFail();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {


            try {
                PtcCredentialProvider provider = null;
                provider = new PtcCredentialProvider(httpClient, username, password);
                go.setClient(new PokemonGo(provider, httpClient));
                this.successful = true;

                settingsService.set("auth_method", "ptc");
                settingsService.set("auth_username", username);
                settingsService.set("auth_password", password);
            } catch (LoginFailedException e) {

            } catch (RemoteServerException e) {

            }
            return null;
        }


    }

    //restore
    public boolean shouldRestore() {
        return !settingsService.get("auth_method", "none").equals("none");
    }

    public void logout() {
        settingsService.set("auth_method", "none");
    }

    public void restore(IAuthSuccess success, IFail fail) {
        new restoreTask(success, fail).execute();
    }

    class restoreTask extends AsyncTask<Void, Void, Void> {
        private final IFail fail;
        private final IAuthSuccess success;
        boolean successful = false;


        restoreTask(IAuthSuccess success, IFail fail) {
            this.success = success;
            this.fail = fail;
        }


        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (settingsService.get("auth_method", "none").equals("ptc")) {
                    go.setClient(new PokemonGo(new PtcCredentialProvider(httpClient, settingsService.get("auth_username", ""), settingsService.get("auth_password", "")), httpClient));
                    successful=true;
                } else if (settingsService.get("auth_method", "none").equals("google")) {
                    go.setClient(new PokemonGo(new GoogleUserCredentialProvider(httpClient, settingsService.get("auth_token", "")), httpClient));
                    successful=true;
                }

            } catch (LoginFailedException e) {
                e.printStackTrace();
            } catch (RemoteServerException e) {
                e.printStackTrace();
                Log.e(" bla"," bla");
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            if (successful) {
                this.success.handleSuccess();
            } else {
                this.fail.handleFail();
            }
        }
    }


}
