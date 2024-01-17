package hr.ferit.antonioparadzik.tunesforyou

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailLogin: EditText
    private lateinit var passwordLogin: EditText
    private lateinit var loginButton: Button
    private lateinit var registerHere: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        emailLogin=view.findViewById(R.id.editEmailLogin)
        passwordLogin=view.findViewById(R.id.editPasswordLogin)
        loginButton=view.findViewById(R.id.loginButton)
        registerHere=view.findViewById(R.id.registerHereText)

        auth= FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            loginUser()
        }

        registerHere.setOnClickListener {
            val fragmentTransaction : FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.container, RegisterFragment())?.commit()
        }
        return view
    }
    private fun loginUser(){
        val email=emailLogin.text.toString()
        val password=passwordLogin.text.toString()

        if(TextUtils.isEmpty(email)){
            emailLogin.error="Email cannot be empty!"
            emailLogin.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailLogin.error="Please provide valid email!"
            emailLogin.requestFocus()
            return
        }
        if(TextUtils.isEmpty(password)){
            passwordLogin.error="Password cannot be empty!"
            passwordLogin.requestFocus()
            return
        }

        if(password.length < 6){
            passwordLogin.error="Min password length should be 6 characters!"
            passwordLogin.requestFocus()
            return
        }
        else{
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val fragmentTransaction : FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
                        fragmentTransaction?.replace(R.id.container, HomepageFragment())?.commit()
                    } else {
                        Toast.makeText(activity, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).hideBottomNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as HomeActivity).showBottomNavigation()
    }

}