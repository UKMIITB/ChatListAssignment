package com.example.chatlistassignment.repository.room;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.chatlistassignment.model.Contact;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addContact(Contact contact);

    @Delete
    Completable deleteContact(Contact contact);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addListOfContact(List<Contact> contactList);

    @Query("select * from contactdb order by name asc")
    DataSource.Factory<Integer, Contact> getAllContacts();

    @Query("select * from contactdb")
    Single<List<Contact>> getAllContactsList();

    @Query("select * from contactdb where name like :query or number like :query order by name asc")
    DataSource.Factory<Integer, Contact> getQueryContact(String query);
}
