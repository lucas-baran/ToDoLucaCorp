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

    var loginResponse: LoginResponse? = null

    private val _loginSuccess = MutableStateFlow<Boolean>(false)
    var loginSuccess = _loginSuccess.asStateFlow()

    private val _signupSuccess = MutableStateFlow<Boolean>(false)
    var signupSuccess = _signupSuccess.asStateFlow()

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
            loginResponse = repository.login(loginDetails)
            _loginSuccess.value = loginResponse != null
        }
    }

    fun signUp(signupDetails: SignUpForm) {
        viewModelScope.launch {
            loginResponse = repository.signUp(signupDetails)
            _signupSuccess.value = loginResponse != null
        }
    }
}