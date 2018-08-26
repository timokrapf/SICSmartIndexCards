package com.example.timokrapf.sic_smartindexcards;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;

public class CardsConverter {

    @TypeConverter
    public static SmartIndexCards toSmartIndexCards(ArrayList<String> strings) {
        if(strings == null) {
            return null;
        } else {
            return new SmartIndexCards(strings.get(0), strings.get(1), strings.get(2));
        }
    }

    @TypeConverter
    public static ArrayList<String> toStringList(SmartIndexCards card) {
        if(card == null) {
            return null;
        } else {
            ArrayList<String> list = new ArrayList<>();
            list.add(card.getSubject());
            list.add(card.getQuestion());
            list.add(card.getAnswer());
            return list;
        }
    }
  /*
    @TypeConverter
    public static String toString(ArrayList<String>)
    */
}
