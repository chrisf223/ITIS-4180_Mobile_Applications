package edu.uncc.assessment04.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, ToDoList.class, ToDoListItem.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ToDoListDao toDoListDao();
    public abstract ToDoListItem toDoListItemDao();

}
