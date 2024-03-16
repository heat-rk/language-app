package ru.heatrk.languageapp.common.utils

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

val NavController.currentRoute
    get() = currentBackStackEntry?.destination?.route

val NavController.previousRoute
    get() = previousBackStackEntry?.destination?.route

fun NavBackStackEntry.requireLongArg(key: String) = requireNotNull(arguments?.getLong(key))

fun NavBackStackEntry.requireStringArg(key: String) = requireNotNull(arguments?.getString(key))
