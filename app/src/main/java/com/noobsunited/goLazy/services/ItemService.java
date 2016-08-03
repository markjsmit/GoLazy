package com.noobsunited.goLazy.services;

import android.os.AsyncTask;

import com.noobsunited.goLazy.interfaces.IFail;
import com.noobsunited.goLazy.interfaces.IGetItemsSuccess;
import com.noobsunited.goLazy.interfaces.IItemsCleanedSuccess;
import com.pokegoapi.api.inventory.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import POGOProtos.Inventory.Item.ItemIdOuterClass;

/**
 * Created by markj on 8/1/2016.
 */
public class ItemService {

    GoService go;
    public ItemService(GoService go) {
     this.go=go;
    }

    public void getItems(IGetItemsSuccess success, IFail fail){
        new getItemsTask(success, fail).execute();
    }

    class getItemsTask extends AsyncTask<Void, Void, Void> {

        private final IFail failEvent;
        private final IGetItemsSuccess successEvent;
        private boolean success;
        private List<Item> items;


        public getItemsTask(IGetItemsSuccess success, IFail fail) {
            this.successEvent = success;
            this.failEvent = fail;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                this.items = new ArrayList<Item>(go.getClient().getInventories().getItemBag().getItems());
                success = true;
            } catch (Exception ex) {
                success = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (success) {
                this.successEvent.handleSuccess(items);
            } else {
                this.failEvent.handleFail();
            }
        }

    }

    public void removeItems(List<Map.Entry<ItemIdOuterClass.ItemId, Integer>> toRemove, IItemsCleanedSuccess success, IFail  fail) {
        new removeItems(toRemove,success, fail).execute();
    }

    class removeItems extends AsyncTask<Void, Void, Void> {

        private final IFail failEvent;
        private final IItemsCleanedSuccess successEvent;
        private final List<Map.Entry<ItemIdOuterClass.ItemId, Integer>> toRemove;
        private boolean success;


        public removeItems(List<Map.Entry<ItemIdOuterClass.ItemId, Integer>> toRemove, IItemsCleanedSuccess success, IFail fail) {
            this.successEvent = success;
            this.failEvent = fail;
            this.toRemove=toRemove;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for(Map.Entry<ItemIdOuterClass.ItemId, Integer> entry: toRemove){
                    go.getClient().getInventories().getItemBag().removeItem(entry.getKey(),entry.getValue());
                }
                success = true;
            } catch (Exception ex) {
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
