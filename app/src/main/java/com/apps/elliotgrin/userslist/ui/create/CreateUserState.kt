package com.apps.elliotgrin.userslist.ui.create

import com.apps.elliotgrin.userslist.data.model.User

sealed class CreateUserState {
    data class StateUserIsNotNull(val user: User) : CreateUserState()
}