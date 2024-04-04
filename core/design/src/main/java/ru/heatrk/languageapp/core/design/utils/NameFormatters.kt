package ru.heatrk.languageapp.core.design.utils

import ru.heatrk.languageapp.common.utils.strRes
import ru.heatrk.languageapp.core.design.R

fun formatFullName(firstName: String?, lastName: String?) =
    when {
        firstName == null && lastName == null ->
            null
        firstName == null ->
            strRes(lastName)
        lastName == null ->
            strRes(firstName)
        else ->
            strRes(R.string.full_name_formatted, firstName, lastName)
    }
