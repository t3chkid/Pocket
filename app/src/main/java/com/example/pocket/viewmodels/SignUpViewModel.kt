package com.example.pocket.viewmodels

import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pocket.auth.AuthServiceInvalidEmailException
import com.example.pocket.auth.AuthServiceInvalidPasswordException
import com.example.pocket.auth.AuthenticationResult
import com.example.pocket.auth.AuthenticationService
import com.example.pocket.di.IoCoroutineDispatcher
import com.example.pocket.utils.containsDigit
import com.example.pocket.utils.containsLowercase
import com.example.pocket.utils.containsUppercase
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SignUpViewModel {
    val accountCreationResult: LiveData<AuthenticationResult>
    fun createNewAccount(
        name: String,
        email: String,
        password: String,
        profilePhotoUri: Uri? = null
    )
}

@HiltViewModel
class SignUpViewModelImpl @Inject constructor(
    private val authenticationService: AuthenticationService,
    @IoCoroutineDispatcher private val mDefaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel(), SignUpViewModel {

    private val _accountCreationResult = MutableLiveData<AuthenticationResult>()
    override val accountCreationResult = _accountCreationResult as LiveData<AuthenticationResult>

    /**
     * The method is used to check whether the [email] is valid .An email is valid
     * if, and only if, it is not blank(ie. is not empty and doesn't contain whitespace characters)
     * and matches the [Patterns.EMAIL_ADDRESS] regex.
     */
    private fun isValidEmail(email: String) =
        email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    /**
     * The method is used to check whether the [password] is valid.A password is valid if, and only if,
     * it is of length 8 , contains atleast one uppercase and lowercase letter and contains atleast one digit.
     */
    private fun isValidPassword(
        password: String
    ) =
        password.length >= 8 && password.containsUppercase() && password.containsLowercase() && password.containsDigit()

    override fun createNewAccount(
        name: String,
        email: String,
        password: String,
        profilePhotoUri: Uri?
    ) {
        if (!isValidEmail(email)) {
            val exception = AuthServiceInvalidEmailException("Invalid email")
            _accountCreationResult.postValue(AuthenticationResult.Failure(exception))
        }else if (!isValidPassword(password)) {
            val exceptionMessage = "The password must be of length 8, and must contain atleast one uppercase and lowercase letter and atleast one digit."
            val exception = AuthServiceInvalidPasswordException(exceptionMessage)
            _accountCreationResult.postValue(AuthenticationResult.Failure(exception))
        }else{
            CoroutineScope(mDefaultDispatcher).launch {
                val authenticationResult = authenticationService.createAccount(name, email.trim(), password, profilePhotoUri)
                _accountCreationResult.postValue(authenticationResult)
            }
        }
    }
}