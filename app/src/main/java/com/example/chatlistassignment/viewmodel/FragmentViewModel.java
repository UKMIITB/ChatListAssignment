package com.example.chatlistassignment.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.chatlistassignment.R;
import com.example.chatlistassignment.model.Contact;
import com.example.chatlistassignment.model.User;
import com.example.chatlistassignment.repository.LocalRepository;
import com.example.chatlistassignment.utils.SyncNativeContacts;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class FragmentViewModel extends AndroidViewModel {

    public final static String TAG = "TAG";
    public LiveData<PagedList<User>> userList;
    private LocalRepository repository;

    public LiveData<PagedList<User>> queriedUserList;

    public LiveData<PagedList<Contact>> contactList;
    public LiveData<PagedList<Contact>> queryContactList;

    SyncNativeContacts syncNativeContacts;

    private final String SHARED_PREF_CONTACT_SYNC_KEY = "ContactSync";
    private final String SHARED_PREF_FILE_NAME = "SharedPreference";

    @Inject
    public FragmentViewModel(@NonNull Application application, LocalRepository repository) {
        super(application);
        this.repository = repository;
    }

    public void init() {

        PagedList.Config config = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(10).build();
        userList = new LivePagedListBuilder<>(repository.getAllUser(), config).build();
    }

    public void queryInit(String query) {

        PagedList.Config config = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(10).build();
        queriedUserList = new LivePagedListBuilder<>(repository.queryAllUser(query), config).build();
    }

    public void contactInit() {

        PagedList.Config config = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(10).build();

        contactList = new LivePagedListBuilder<>(repository.getAllContacts(), config).build();
    }

    public void queryContactInit(String query) {

        PagedList.Config config = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(10).build();

        queryContactList = new LivePagedListBuilder<>(repository.getQueryContact(query), config).build();
    }

    private Toast toast;

    private static MutableLiveData<String> queryString = new MutableLiveData<>();

    public static void setQueryString(String query) {
        queryString.setValue(query);
    }

    public LiveData<String> getQueryString() {
        return queryString;
    }

    private static MutableLiveData<Boolean> isMultiSelectOn = new MutableLiveData<>();

    public void setIsMultiSelect(boolean isMultiSelect) {
        isMultiSelectOn.postValue(isMultiSelect);
    }

    public static LiveData<Boolean> getIsMultiSelectOn() {
        return isMultiSelectOn;
    }


    public void addUser(User user) {

        repository.addUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        Log.d("TAG", "Inside onSubscribe of addUser in ViewModel");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "Inside onComplete of addUser in ViewModel");
                        successToast("User added successfully");
                        isMultiSelectOn.postValue(false);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("TAG", "Inside onError of addUser in ViewModel." + e.getMessage());
                        failureToast(e.getMessage());
                    }
                });
    }

    public void deleteUser(User user) {

        repository.deleteUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("TAG", "Inside onSubscribe of deleteUser in ViewModel");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "Inside onComplete of deleteUser in ViewModel");
                        successToast("User data removed successfully");
                        isMultiSelectOn.postValue(false);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("TAG", "Inside onError of deleteUser in ViewModel");
                        failureToast(e.getMessage());
                    }
                });
    }

    public void updateUser(User user) {

        repository.updateUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("TAG", "Inside onSubscribe of updateUser in ViewModel");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "Inside onComplete of updateUser in ViewModel");
                        successToast("User Data updated successfully");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("TAG", "Inside onError of updateUser in ViewModel");
                        failureToast(e.getMessage());
                    }
                });
    }


    public void deleteListOfUsers(List<User> userArrayList) {
        Log.e("TAG", "deleteListOfUsers: ---> " + userArrayList.size());
        repository.deleteListOfUsers(userArrayList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        Log.d("TAG", "Inside onSubscribe of deleteListOfUsers in ViewModel");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "Inside onComplete of deleteListOfUsers in ViewModel");
                        successToast("User Data deleted successfully");
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.d("TAG", "Inside onError of deleteListOfUsers in ViewModel");
                    }
                });
    }


    private void successToast(String message) {

        if (toast != null)
            toast.cancel();

        toast = Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT);
        View view = toast.getView();

        view.getBackground().setColorFilter(ContextCompat.getColor(getApplication(), R.color.teal_200), PorterDuff.Mode.SRC_IN);

        toast.show();
    }

    private void failureToast(String message) {

        if (toast != null)
            toast.cancel();

        toast = Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT);
        View view = toast.getView();

        view.getBackground().setColorFilter(ContextCompat.getColor(getApplication(), R.color.red), PorterDuff.Mode.SRC_IN);

        toast.show();
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }

    private static int contactListSize = 0;

    public void setContactListSize(int size) {
        contactListSize = size;
    }

    public int getContactListSize() {
        return contactListSize;
    }

    public void completeContactSync() {
        syncNativeContacts = new SyncNativeContacts(getApplication());
        syncNativeContacts.getContactArrayList().doAfterSuccess(this::addContactListToDB)
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<Contact>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        Log.e(TAG, "onSubscribe: Inside complete sync  ");
                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull List<Contact> contactList) {
                        Log.e(TAG, "onSuccess: Inside complete sync   -->>  " + contactList.size());
                        setContactListSize(contactList.size());
                        setIsFullContactSyncCompleted(true);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.e(TAG, "onError: Inside complete sync error ->> " + e.getMessage());
                    }
                });
    }

    public void deltaContactSync() {
        syncNativeContacts = new SyncNativeContacts(getApplication());

        Single.zip(syncNativeContacts.getContactArrayList(), repository.getAllContactsList(),
                new BiFunction<List<Contact>, List<Contact>, Boolean>() {
                    @io.reactivex.annotations.NonNull
                    @Override
                    public Boolean apply(@io.reactivex.annotations.NonNull List<Contact> nativeContactList, @io.reactivex.annotations.NonNull List<Contact> dbContactList) throws Exception {
                        List<Contact> addList = new ArrayList<>();
                        List<Contact> deleteList = new ArrayList<>();

                        addList.addAll(nativeContactList);
                        addList.removeAll(dbContactList);

                        deleteList.addAll(dbContactList);
                        deleteList.removeAll(nativeContactList);

                        FragmentViewModel.this.addContactListToDB(addList);

                        for (Contact contact : deleteList) {
                            FragmentViewModel.this.deleteContact(contact);
                        }

                        FragmentViewModel.this.setContactListSize(dbContactList.size() + addList.size() - deleteList.size());

                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void deleteContact(Contact contact) {
        repository.deleteContact(contact)
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe of deleteContact: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete of delete Contact: ");
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.d(TAG, "onError of delete contact: ");
                    }
                });
    }


    private void addContactListToDB(List<Contact> contactList) {

        repository.addListOfContact(contactList)
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        Log.d("TAG", "Inside onSubscribe of addContactListDB in SyncNativeContacts");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "Inside onComplete of addContactListDB in SyncNativeContacts");
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.d("TAG", "Inside onError of addContactListDB in SyncNativeContacts.: " + e.getMessage());
                    }
                });
    }

    private void setIsFullContactSyncCompleted(boolean aBoolean) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_PREF_CONTACT_SYNC_KEY, aBoolean);
        editor.apply();
    }

    public boolean getIsFullContactSyncCompleted() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SHARED_PREF_CONTACT_SYNC_KEY, false);
    }
}