package com.example.itcron.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itcron.data.UserService
import com.example.itcron.data.model.Event
import com.example.itcron.data.model.UserModel
import com.example.itcron.data.service.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

internal class MainViewModel : ViewModel() {

    private companion object{
        const val PER_PAGE = 10
    }

    private var users = mutableListOf<UserModel>()
    private val usersList = MutableLiveData<List<UserModel>>()

    private val _isLoading = MutableLiveData(false)
    var isLoading: LiveData<Boolean> = _isLoading

    private var page = 0
    private var disposable : Disposable? = null

    private val _eventOpenUser = MutableLiveData<Event<UserModel>>()
    var eventOpenUser: LiveData<Event<UserModel>> = _eventOpenUser

    init {
        nextPage()
    }

    fun getUsers(): LiveData<List<UserModel>> {
        return usersList
    }

    fun nextPage(){
        if(isLoading.value == false){
            page += PER_PAGE
            loadUsers()
        }
    }

    fun onRefresh() {
        disposable?.dispose()
        nextPage()
    }

    fun onUserSelected(userModel: UserModel){
        _eventOpenUser.value = Event(userModel)
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    private fun loadUsers() {
        _isLoading.value = true
        val userService = RetrofitClient.instance.create(UserService::class.java)
        disposable = userService.getAllUsers(page * PER_PAGE, PER_PAGE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _isLoading.value = false
                users.addAll(it)
                usersList.value = users
            }, {
                _isLoading.value = false
            })
    }

}