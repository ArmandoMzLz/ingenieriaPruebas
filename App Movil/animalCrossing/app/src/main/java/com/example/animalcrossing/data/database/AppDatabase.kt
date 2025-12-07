package com.example.animalcrossing.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

//Entity
import com.example.animalcrossing.data.entity.petEntity
import com.example.animalcrossing.data.entity.userEntity
import com.example.animalcrossing.data.entity.walkerEntity

//Dao
import com.example.animalcrossing.data.dao.petDao
import com.example.animalcrossing.data.dao.userDao
import com.example.animalcrossing.data.dao.walkerDao
import com.example.animalcrossing.data.dao.walkerRequestDao
import com.example.animalcrossing.data.entity.walkerRequestEntity

@Database(
    entities = [petEntity::class, userEntity::class, walkerEntity::class, walkerRequestEntity::class],
    version = 3,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): userDao
    abstract fun petDao(): petDao
    abstract fun walkerDao(): walkerDao
    abstract fun walkerRequestDao(): walkerRequestDao
}