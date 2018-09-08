package com.example.timokrapf.sic_smartindexcards;

import java.util.List;
/*
From https://www.techotopia.com/index.php/An_Android_Room_Database_and_Repository_Tutorial
Code was adapted to fit into App
 */

public interface AsyncResult {
    void asyncFinished(List<SmartIndexCards> cards);
}
