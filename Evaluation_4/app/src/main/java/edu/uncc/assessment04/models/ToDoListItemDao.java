package edu.uncc.assessment04.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ToDoListItemDao {
    @Query("SELECT * FROM todo_items)
    List<ToDoListItem> getAllListsForUser(int userId);

    @Insert
    void insert(ToDoListItem toDoListItem);

    @Delete
    void delete(ToDoListItem toDoListItem);
}
