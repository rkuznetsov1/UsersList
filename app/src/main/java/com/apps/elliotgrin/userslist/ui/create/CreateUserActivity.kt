package com.apps.elliotgrin.userslist.ui.create

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.apps.elliotgrin.userslist.R
import com.apps.elliotgrin.userslist.data.model.User
import com.apps.elliotgrin.userslist.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_create_user.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val ARG_USER = "arg:user"
private const val NEW_USER_ID = -1

class CreateUserActivity : BaseActivity<CreateUserState, CreateUserViewModel>(CreateUserViewModel::class) {

    private val intentUser: User? by lazy { intent?.extras?.getParcelable<User>(ARG_USER) }

    override val viewModel: CreateUserViewModel by viewModel { parametersOf(intentUser) }

    override val layoutId: Int
        get() = R.layout.activity_create_user

    override fun whenState(state: CreateUserState): Unit? = when(state) {
        is CreateUserState.StateUserIsNotNull -> fillUserInputs(state.user)
        is CreateUserState.StateShowError -> showError(state.error)
        is CreateUserState.StateLoading -> showLoading(state.isLoading)
        is CreateUserState.StateUserIsCreated -> finish()
    }

    override fun initViews() {
        createUserButton.text = if (intentUser == null) "Create user" else "Update user"
        createUserButton.setOnClickListener { createUser() }
    }

    private fun fillUserInputs(user: User) {
        firstNameEditText.setText(user.firstName)
        lastNameEditText.setText(user.lastName)
        emailEditText.setText(user.email)
    }

    private fun createUser() {
        val id = if (intentUser == null) NEW_USER_ID else intentUser!!.id
        val user = User(
            id,
            firstNameEditText.text.toString(),
            lastNameEditText.text.toString(),
            emailEditText.text.toString(),
            intentUser?.avatarUrl
        )

        if (id == NEW_USER_ID) viewModel.createUser(user)
        else viewModel.updateUser(user)
    }

    private fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        fun newIntent(context: Context, user: User? = null): Intent {
            val intent = Intent(context, CreateUserActivity::class.java)
            intent.putExtra(ARG_USER, user)
            return intent
        }
    }
}
