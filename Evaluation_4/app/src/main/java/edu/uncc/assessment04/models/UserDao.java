package edu.uncc.assessment04.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE name = :name AND passcode = :passcode LIMIT 1")
    User getUser(String name, String passcode);

    @Query("SELECT * FROM users WHERE name = :name LIMIT 1")
    User getUserByName(String name);
}
