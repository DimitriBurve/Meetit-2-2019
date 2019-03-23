package com.iutbm.example.iutbm.couchot.meetit_1;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CharacterDAO {
    @Query("SELECT * FROM characters")
    List<Character> getAllCharaters();

    @Insert
    void insertCharacters(Character... characters);

}