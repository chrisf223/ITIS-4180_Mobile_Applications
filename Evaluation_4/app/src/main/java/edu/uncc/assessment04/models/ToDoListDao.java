package edu.uncc.assessment04.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ToDoListDao {
    @Query("SELECT * FROM todo_lists WHERE userId = :userId")
    List<ToDoList> getAllListsForUser(int userId);

    @Insert
    void insert(ToDoList toDoList);

    @Delete
    void delete(ToDoList toDoList);
}
