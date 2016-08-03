package com.noobsunited.goLazy.helpers;

import com.pokegoapi.api.inventory.Item;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import POGOProtos.Inventory.Item.ItemIdOuterClass;

/**
 * Created by markj on 8/2/2016.
 */
public class ItemHelper {
    public static List<Map.Entry<ItemIdOuterClass.ItemId,Integer>> findToRemoveItems(List<Item> items, int balls, int potions, int revives, int berries) {

        List<Map.Entry<ItemIdOuterClass.ItemId,Integer>> result=new ArrayList<Map.Entry<ItemIdOuterClass.ItemId,Integer>>();

        int pokeBallCount=0;
        int greatBallCuunt=0;
        int ultraBallCount=0;
        int masterBallCount=0;

        int potionCount=0;
        int superPotionCount=0;
        int hyperPotionCount=0;
        int maxPotionCount=0;

        for (Item item : items){
            if(item.getItemId().equals(ItemIdOuterClass.ItemId.ITEM_RAZZ_BERRY)) {
                int amount =getToRemove(item.getCount(),berries);
                if(amount>0) {
                    result.add(new AbstractMap.SimpleEntry<ItemIdOuterClass.ItemId, Integer>(item.getItemId(), amount));
                }
            }
            if(item.getItemId().equals(ItemIdOuterClass.ItemId.ITEM_REVIVE)) {
                int amount =getToRemove(item.getCount(),revives);
                if(amount>0) {
                    result.add(new AbstractMap.SimpleEntry<ItemIdOuterClass.ItemId, Integer>(item.getItemId(), amount));
                }
            }

            if(item.getItemId().equals(ItemIdOuterClass.ItemId.ITEM_POKE_BALL)) {
                pokeBallCount=item.getCount();
            }
            if(item.getItemId().equals(ItemIdOuterClass.ItemId.ITEM_GREAT_BALL)) {
                greatBallCuunt=item.getCount();
            }
            if(item.getItemId().equals(ItemIdOuterClass.ItemId.ITEM_ULTRA_BALL)) {
                ultraBallCount=item.getCount();
            }

            if(item.getItemId().equals(ItemIdOuterClass.ItemId.ITEM_MASTER_BALL)) {
                masterBallCount=item.getCount();
            }

            if(item.getItemId().equals(ItemIdOuterClass.ItemId.ITEM_POTION)) {
                potionCount=item.getCount();
            }
            if(item.getItemId().equals(ItemIdOuterClass.ItemId.ITEM_SUPER_POTION)) {
                superPotionCount=item.getCount();
            }

            if(item.getItemId().equals(ItemIdOuterClass.ItemId.ITEM_SUPER_POTION)) {
                hyperPotionCount=item.getCount();
            }
            if(item.getItemId().equals(ItemIdOuterClass.ItemId.ITEM_MAX_POTION)) {
                maxPotionCount=item.getCount();
            }
        }

        List<Map.Entry<ItemIdOuterClass.ItemId,Integer>> ballsToRemove=handleBalls(pokeBallCount,greatBallCuunt,ultraBallCount,masterBallCount,balls);
        List<Map.Entry<ItemIdOuterClass.ItemId,Integer>> itemsToRemove=handlePotions(potionCount,superPotionCount,hyperPotionCount,maxPotionCount,potions);

        for(Map.Entry<ItemIdOuterClass.ItemId,Integer> entry: ballsToRemove)
        {
            result.add(entry);
        }

        for(Map.Entry<ItemIdOuterClass.ItemId,Integer> entry: itemsToRemove)
        {
            result.add(entry);
        }

        return result;
    }

    private static List<Map.Entry<ItemIdOuterClass.ItemId,Integer>> handlePotions(int potionCount, int superPotionCount, int hyperPotionCount, int maxPotionCount,int max) {
        ArrayList<Map.Entry<ItemIdOuterClass.ItemId, Integer>> result = new ArrayList<Map.Entry<ItemIdOuterClass.ItemId, Integer>>();

        int total = potionCount+superPotionCount+hyperPotionCount+maxPotionCount;
        int amount=0;

        if(total>max){
            amount = getToRemove(potionCount,max);
            total-=amount;
            result.add(new AbstractMap.SimpleEntry<ItemIdOuterClass.ItemId, Integer>(ItemIdOuterClass.ItemId.ITEM_POTION,amount));
        }

        if(total>max){
            amount = getToRemove(superPotionCount,max);
            total-=amount;
            result.add(new AbstractMap.SimpleEntry<ItemIdOuterClass.ItemId, Integer>(ItemIdOuterClass.ItemId.ITEM_SUPER_POTION,amount));
        }

        if(total>max){
            amount = getToRemove(hyperPotionCount,max);
            total-=amount;
            result.add(new AbstractMap.SimpleEntry<ItemIdOuterClass.ItemId, Integer>(ItemIdOuterClass.ItemId.ITEM_HYPER_POTION,amount));
        }


        if(total>max){
            amount = getToRemove(maxPotionCount,max);
            total-=amount;
            result.add(new AbstractMap.SimpleEntry<ItemIdOuterClass.ItemId, Integer>(ItemIdOuterClass.ItemId.ITEM_MAX_POTION,amount));
        }


        return result;
    }

    private static List<Map.Entry<ItemIdOuterClass.ItemId, Integer>> handleBalls(int pokeBallCount, int greatBallCount, int ultraBallCount, int masterBallCount,int max) {
        ArrayList<Map.Entry<ItemIdOuterClass.ItemId, Integer>> result = new ArrayList<Map.Entry<ItemIdOuterClass.ItemId, Integer>>();

        int total = pokeBallCount+greatBallCount+ultraBallCount+masterBallCount;
        int amount=0;

        if(total>max){
            amount = getToRemove(pokeBallCount,total,max);
            total-=amount;
            result.add(new AbstractMap.SimpleEntry<ItemIdOuterClass.ItemId, Integer>(ItemIdOuterClass.ItemId.ITEM_POKE_BALL,amount));
        }

        if(total>max){
            amount = getToRemove(greatBallCount,total,max);
            total-=amount;
            result.add(new AbstractMap.SimpleEntry<ItemIdOuterClass.ItemId, Integer>(ItemIdOuterClass.ItemId.ITEM_GREAT_BALL,amount));
        }

        if(total>max){
            amount = getToRemove(ultraBallCount,total,max);
            total-=amount;
            result.add(new AbstractMap.SimpleEntry<ItemIdOuterClass.ItemId, Integer>(ItemIdOuterClass.ItemId.ITEM_ULTRA_BALL,amount));
        }


        if(total>max){
            amount = getToRemove(masterBallCount,total,max);
            total-=amount;
            result.add(new AbstractMap.SimpleEntry<ItemIdOuterClass.ItemId, Integer>(ItemIdOuterClass.ItemId.ITEM_MASTER_BALL,amount));
        }


        return result;
    }


    private static Integer getToRemove(int count,int max) {
        return getToRemove(count,count,max);
    }

    private static Integer getToRemove(int count, int totalCount, int max) {
        if(totalCount>max){
            int result= Math.abs(max-totalCount);
            if(result>count){
                result=count;
            }
            return result;
        }
        return 0;
    }

}
