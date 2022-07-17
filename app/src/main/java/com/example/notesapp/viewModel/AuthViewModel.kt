package com.example.notesapp.viewModel

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.model.UserRequest
import com.example.notesapp.model.UserResponse
import com.example.notesapp.repository.UserRepository
import com.example.notesapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) :ViewModel(){

    val userResponseLiveData:LiveData<NetworkResult<UserResponse>>
    get()=userRepository.userResponseLiveData

    fun loginUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }

    }

    fun registerUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    fun validateUser(email:String,password:String,username:String,isLogin:Boolean):Pair<Boolean,String>{
        var result= Pair(false,"")
        if (TextUtils.isEmpty(email)){
            result= Pair(false,"Enter email")
        }else if (TextUtils.isEmpty(password)){
            result=Pair(false,"Enter Password")
        }else if (!isLogin && TextUtils.isEmpty(username)){
            result=Pair(false,"Enter User Name")
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result=Pair(false,"Enter correct email")
        }else if (password.length<=4){
            result=Pair(false,"Password should be minimum 5 character")
        }else{
            result=Pair(true,"")
        }
        return result
    }
}