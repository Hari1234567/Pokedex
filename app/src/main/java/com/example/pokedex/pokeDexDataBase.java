package com.example.pokedex;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = PokemonRoomData.class,version = 1)
public abstract class pokeDexDataBase extends RoomDatabase {
    public abstract roomInterface myroomInterface();

}
