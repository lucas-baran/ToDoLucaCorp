package com.lucacorp.todolucas.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucacorp.todolucas.network.TasksRepository
import com.lucacorp.todolucas.network.UserInfo
import com.lucacorp.todolucas.network.UserRepository
import com.lucacorp.todolucas.tasklist.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserInfoViewModel: ViewModel() {
    private val repository = UserRepository()

    private val _userInfo = MutableStateFlow<UserInfo>(value = UserInfo(email = "lucas.baran@ensiie.fr", firstName = "Lucas", lastName = "Baran"))
    public val userInfo: StateFlow<UserInfo> = _userInfo

    fun refresh() {
        viewModelScope.launch {
            val info = repository.getUserInfo()
            if (info != null) {
                _userInfo.value = info;
            }
        }
    }

    fun getUserInfo() {
        viewModelScope.launch {
            repository.getUserInfo()
            refresh()
        }
    }

    fun updateUserAvatar(avatar: MultipartBody.Part) {
        viewModelScope.launch {
            repository.updateUserAvatar(avatar)
            refresh()
        }
    }

    fun updateUserInfo(user: UserInfo) {
        viewModelScope.launch {
            repository.updateUserInfo(user)
            refresh()
        }
    }
}