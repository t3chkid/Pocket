package com.example.pocket.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navArgument
import com.example.pocket.auth.AuthServiceInvalidEmailException
import com.example.pocket.auth.AuthServiceInvalidPasswordException
import com.example.pocket.auth.AuthServiceUserCollisionException
import com.example.pocket.auth.AuthenticationResult
import com.example.pocket.di.AppContainer
import com.example.pocket.di.SignUpContainer
import com.example.pocket.ui.navigation.NavigationDestinations
import com.example.pocket.ui.screens.components.CircularLoadingProgressOverlay
import com.example.pocket.viewmodels.SignUpViewModelImpl

@Composable
fun SignUpScreen(
    appContainer: AppContainer,
    navController: NavController
) {

    val viewmodel = remember {
        //start sign-up flow
        appContainer.signUpContainer = SignUpContainer()
        appContainer.signUpContainer!!.signUpViewModelFactory.create(SignUpViewModelImpl::class.java)
    }
    val result = viewmodel.accountCreationResult.observeAsState()
    var isErrorMessageVisible by remember {
        mutableStateOf(false)
    }
    var errorMessage by remember {
        mutableStateOf("")
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    var emailAddressText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var firstNameText by remember { mutableStateOf("") }
    var lastNameText by remember { mutableStateOf("") }
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    val termsAndConditionText = buildAnnotatedString {
        append("By clicking below you agree to our ")

        pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
        append("Terms of Use ")
        pop()

        append("and consent to our ")

        pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
        append("Privacy Policy.")
        pop()

    }

    BackHandler {
        //end login flow
        appContainer.signUpContainer = null
        navController.navigateUp()
    }

    DisposableEffect(result.value) {
        isLoading = false
        when (val authResult = result.value) {
            is AuthenticationResult.Success -> {
                isErrorMessageVisible = false
                //end sign-up flow
                appContainer.signUpContainer = null
                navController.navigate(NavigationDestinations.HOME_SCREEN.navigationString) {
                    popUpTo(NavigationDestinations.WELCOME_SCREEN.navigationString) {
                        inclusive = true
                    }
                }
            }
            is AuthenticationResult.Failure -> {
                errorMessage = when (authResult.authServiceException) {
                    is AuthServiceInvalidEmailException -> "Please enter a valid email."
                    is AuthServiceInvalidPasswordException -> "The password must be of length 8, and must contain atleast one uppercase and lowercase letter and atleast one digit."
                    is AuthServiceUserCollisionException -> "A user with the same email already exists."
                    else -> "Please enter a valid email and password"
                }
                isErrorMessageVisible = true
            }
        }

        onDispose {
            isErrorMessageVisible = false
            isLoading = true
        }
    }

    CircularLoadingProgressOverlay(isOverlayVisible = isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier
                    .paddingFromBaseline(top = 184.dp),
                text = "Sign up for a new account",
                style = MaterialTheme.typography.h1
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {

                OutlinedTextField(
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth(0.5f),
                    value = firstNameText,
                    onValueChange = { firstNameText = it },
                    placeholder = { Text(text = "First Name") },
                    textStyle = MaterialTheme.typography.body1,
                    singleLine = true
                )

                Spacer(modifier = Modifier.padding(8.dp))

                OutlinedTextField(
                    modifier = Modifier.height(56.dp),
                    value = lastNameText,
                    onValueChange = { lastNameText = it },
                    placeholder = { Text(text = "Last Name") },
                    textStyle = MaterialTheme.typography.body1,
                    singleLine = true
                )

            }

            Spacer(modifier = Modifier.padding(8.dp))

            OutlinedTextField(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                value = emailAddressText,
                onValueChange = { emailAddressText = it },
                placeholder = { Text(text = "Email Address") },
                textStyle = MaterialTheme.typography.body1,
                singleLine = true
            )

            Spacer(modifier = Modifier.padding(8.dp))

            OutlinedTextField(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                value = passwordText,
                onValueChange = { passwordText = it },
                placeholder = { Text(text = "Password") },
                textStyle = MaterialTheme.typography.body1,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable { isPasswordVisible = !isPasswordVisible },
                        imageVector = if (isPasswordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff,
                        contentDescription = ""
                    )
                },
                singleLine = true
            )

            if (isErrorMessageVisible) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colors.error
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 24.dp),
                text = termsAndConditionText,
                style = MaterialTheme.typography.body2
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Button(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                onClick = {
                    isLoading = true
                    viewmodel.createNewAccount(
                        "$firstNameText $lastNameText",
                        emailAddressText,
                        passwordText
                    )
                },
                shape = MaterialTheme.shapes.medium,
                content = { Text(text = "Sign Up", fontWeight = FontWeight.Bold) }
            )

        }
    }
}