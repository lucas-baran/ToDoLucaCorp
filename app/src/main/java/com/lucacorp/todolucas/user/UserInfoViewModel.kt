package com.lucacorp.todolucas.user

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucacorp.todolucas.login.LoginForm
import com.lucacorp.todolucas.login.LoginResponse
import com.lucacorp.todolucas.login.SignUpForm
import com.lucacorp.todolucas.network.TasksRepository
import com.lucacorp.todolucas.network.UserInfo
import com.lucacorp.todolucas.network.UserRepository
import com.lucacorp.todolucas.tasklist.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserInfoViewModel: ViewModel() {
    private val repository = UserRepository()

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    var userInfo = _userInfo.asStateFlow()

    private val _loginResponse = MutableStateFlow<LoginResponse?>(null)
    var loginResponse = _loginResponse.asStateFlow()

    private val _signupResponse = MutableStateFlow<LoginResponse?>(null)
    var signupResponse = _signupResponse.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            var data = repository.refresh()
            if(data != null) {
                _userInfo.value = data
            }
        }
    }

    fun updateUserAvatar(avatar: MultipartBody.Part) {
        viewModelScope.launch {
            var data = repository.updateAvatar(avatar)
            if(data != null) {
                _userInfo.value = data
            }
        }
    }

    fun updateUserInfo(user: UserInfo) {
        viewModelScope.launch {
            var data = repository.update(user);
            if(data != null) {
                _userInfo.value = data
            }
        }
    }

    fun login(loginDetails: LoginForm) {
        viewModelScope.launch {
            _loginResponse.value = repository.login(loginDetails)
            loginResponse = _loginResponse
        }
    }

    fun signUp(signupDetails: SignUpForm) {
        viewModelScope.launch {
            _signupResponse.value = repository.signUp(signupDetails)
            signupResponse = _signupResponse
        }
    }
}