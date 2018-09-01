package com.example.timokrapf.sic_smartindexcards;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

/*
https://mobikul.com/saving-complex-objects-using-android-room-library/
 */
public class CardsConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static ArrayList<SmartIndexCards> stringToList(String data) {
        if(data == null) {
            return null;
        } else {
            Type listType = new TypeToken<ArrayList<SmartIndexCards>>(){}.getType();
            return gson.fromJson(data, listType);
        }
    }

    @TypeConverter
    public static String ListToString(ArrayList<SmartIndexCards> cards) {
        if(cards == null) {
            return null;
        } else {
            return gson.toJson(cards);
        }
    }
}


