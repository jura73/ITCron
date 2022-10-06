package com.example.itcron.ui.user_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itcron.data.service.RetrofitClient
import com.example.itcron.data.UserService
import com.example.itcron.data.model.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

internal class UserDetailsViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    var isLoading: LiveData<Boolean> = _isLoading
    private val user = MutableLiveData<UserModel>()
    private var disposable: Disposable? = null

    fun setLogin(login: String) {
        loadUserDetails(login)
    }

    fun getUser(): LiveData<UserModel> {
        return user
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    private fun loadUserDetails(login: String) {
        _isLoading.value = true
        val userService = RetrofitClient.instance.create(UserService::class.java)
        disposable = userService.getUser(login)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _isLoading.value = false
                user.value = it
            }, {
                _isLoading.value = false
            })
    }

}