package com.senac.mintwallet.ui.signin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.FragmentSigninLoginBinding
import com.senac.mintwallet.model.Repository
import com.senac.mintwallet.ui.component.toast.toast
import com.senac.mintwallet.ui.home.HomeActivity


class Login: Fragment() {
    private var _binding: FragmentSigninLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var facebookCallbackManager: CallbackManager

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private val signInLauncher =
        this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account)
                } catch (e: ApiException) {
                    toast.showToast(this.requireContext(), getString(R.string.unhandlerError), false)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSigninLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.id_web_client))
            .requestEmail()
            .build()

        auth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
        facebookCallbackManager = CallbackManager.Factory.create()
        sharedPreferences = context?.getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE)!!

        requireActivity().window.statusBarColor = resources.getColor(R.color.background);
        requireActivity().window.navigationBarColor = resources.getColor(R.color.background);

        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListeners() {
        binding.registerLink.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
        binding.resetPwd.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_resetPassword)
        }
        binding.googleLoginButton.setOnClickListener {
            loginWithGoogle()
        }
        binding.facebookLoginButton.setOnClickListener {
            loginWithFacebook()
        }
        binding.submitButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            login(email,password)
        }
    }

    private fun saveUserId(userId: String?) {
        with(sharedPreferences.edit()) {
            putString("USER_ID", userId)
            apply()
        }
    }
    private fun login(email: String, password: String) {
        this.auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                saveUserId(auth.currentUser?.uid)
                val intent = Intent(context, HomeActivity::class.java)
                startActivity(intent)
            } else {
                toast.showToast(this.requireContext(), "Usuário ou Senha inválidos", false)
            }
        }
    }

    private fun loginWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (
                    !task.isSuccessful ||
                    auth.currentUser?.email == null ||
                    auth.currentUser?.displayName == null
                ) {
                    toast.showToast(this.requireContext(), getString(R.string.unhandlerError), false)
                    return@addOnCompleteListener
                }

               saveUserId(auth.currentUser?.uid)
               Repository().create(auth.currentUser?.email, auth.currentUser?.displayName)
                val intent = Intent(context, HomeActivity::class.java)
                startActivity(intent)
            }
    }

    private fun loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
        LoginManager.getInstance().registerCallback(facebookCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {}

                override fun onError(error: FacebookException) {
                    toast.showToast(requireContext(), getString(R.string.unhandlerError), false)
                }
            })
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken?) {
        val credential = FacebookAuthProvider.getCredential(token?.token!!)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (
                    !task.isSuccessful ||
                    auth.currentUser?.email == null ||
                    auth.currentUser?.displayName == null
                ) {
                    toast.showToast(this.requireContext(), getString(R.string.unhandlerError), false)
                    return@addOnCompleteListener
                }
                saveUserId(auth.currentUser?.uid)
                Repository().create(auth.currentUser?.email, auth.currentUser?.displayName)
                val intent = Intent(context, HomeActivity::class.java)
                startActivity(intent)
            }
    }
}