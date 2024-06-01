package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class InventoryDatabase: RoomDatabase() {

    //Define an abstract method or property that returns an ItemDao instance,
    // and the Room generates the implementation for you.
    abstract fun itemDao(): ItemDao


    //Inside the companion object, declare a private nullable variable Instance for the database and initialize it to null.
    //The Instance variable keeps a reference to the database, when one has been created.
    // This helps maintain a single instance of the database opened at a given time, which is an expensive resource
    // to create and maintain.
    companion object {

        //In multi-threaded environments, changes made to variables by one thread may not be immediately visible to other threads due to caching and
        // optimization mechanisms.
        //@Volatile ensures that changes made to the variable are immediately visible to all threads,
        // preventing potential inconsistencies or stale data.
        @Volatile
        private var Instance: InventoryDatabase? = null
        //The value of a volatile variable is never cached, and all reads and writes are to and from the main memory.
        // These features help ensure the value of Instance is always up to date and is the same for all execution threads.
        // It means that changes made by one thread to Instance are immediately visible to all other threads.

        fun getDatabase(context: Context): InventoryDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {

                // first parameter - This parameter specifies the context in which the database will be created.
                // applicationContext ensures that the database instance is tied to the application's lifecycle and not to
                // any specific activity or fragment context. It helps prevent potential memory leaks.

                //second parameter-When working with Room, which is primarily a Java-based library, Kotlin needs to use Java class objects
                // to represent classes and interfaces that are used by Room, such as RoomDatabase and DAO interfaces.

                //third parameter -This parameter specifies the name of the database file that Room will create or use.
                // In this case, the database file will be named "item_database".
                Room.databaseBuilder(context, InventoryDatabase::class.java, "item_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }

    }
}