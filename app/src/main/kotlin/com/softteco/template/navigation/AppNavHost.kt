package com.softteco.template.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.softteco.template.navigation.AppNavHost.DEEP_LINK_URI
import com.softteco.template.navigation.AppNavHost.RESET_PASSWORD_PATH
import com.softteco.template.navigation.AppNavHost.RESET_TOKEN_ARG
import com.softteco.template.ui.feature.forgotPassword.ForgotPasswordScreen
import com.softteco.template.ui.feature.home.HomeScreen
import com.softteco.template.ui.feature.login.LoginScreen
import com.softteco.template.ui.feature.openSourceLicenses.OpenSourceLicensesScreen
import com.softteco.template.ui.feature.profile.ProfileScreen
import com.softteco.template.ui.feature.resetPassword.ResetPasswordScreen
import com.softteco.template.ui.feature.settings.SettingsScreen
import com.softteco.template.ui.feature.signUp.SignUpScreen

object AppNavHost {
    const val DEEP_LINK_URI = "https://template.softteco.com.deep_link"
    const val RESET_PASSWORD_PATH = "reset_password"
    const val RESET_TOKEN_ARG = "token"
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        bottomBarGraph(navController, Modifier.padding(paddingValues = paddingValues))
        loginGraph(navController)
        settingsGraph(navController)
    }
}

fun NavGraphBuilder.bottomBarGraph(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    navigation(
        startDestination = Screen.Home.route,
        route = Graph.BottomBar.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(modifier = modifier)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                modifier = modifier,
                onBackClicked = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Graph.Login.route) {
                        popUpTo(Graph.BottomBar.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                modifier = modifier,
                onBackClicked = { navController.navigateUp() },
                onLicensesClicked = {
                    navController.navigate(Screen.OpenSourceLicenses.route)
                }
            )
        }
    }
}

fun NavGraphBuilder.loginGraph(navController: NavController) {
    navigation(
        startDestination = Screen.Login.route,
        route = Graph.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onBackClicked = { navController.navigateUp() },
                onSuccess = {
                    navController.navigate(Graph.BottomBar.route) {
                        popUpTo(Graph.BottomBar.route) { inclusive = true }
                    }
                },
                onSignUpClicked = { navController.navigate(Screen.SignUp.route) },
                onForgotPasswordClicked = { navController.navigate(Screen.ForgotPassword.route) }
            )
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onBackClicked = { navController.navigateUp() },
                onSuccess = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(
            route = Screen.ResetPassword.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$DEEP_LINK_URI/$RESET_PASSWORD_PATH/{$RESET_TOKEN_ARG}"
                }
            )
        ) {
            ResetPasswordScreen(
                onSuccess = { navController.navigate(Screen.Login.route) },
            )
        }
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClicked = { navController.navigateUp() },
                onSuccess = { navController.navigate(Screen.Login.route) }
            )
        }
    }
}

fun NavGraphBuilder.settingsGraph(navController: NavController) {
    navigation(
        startDestination = Screen.Settings.route,
        route = Graph.Settings.route,
    ) {
        composable(Screen.OpenSourceLicenses.route) {
            OpenSourceLicensesScreen(onBackClicked = { navController.navigateUp() })
        }
    }
}
