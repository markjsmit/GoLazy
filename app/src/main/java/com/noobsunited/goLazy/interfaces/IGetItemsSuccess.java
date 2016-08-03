package com.noobsunited.goLazy.interfaces;

import com.pokegoapi.api.inventory.Item;

import java.util.List;

/**
 * Created by markj on 8/1/2016.
 */
public interface IGetItemsSuccess {

    void handleSuccess(List<Item> items);
}
