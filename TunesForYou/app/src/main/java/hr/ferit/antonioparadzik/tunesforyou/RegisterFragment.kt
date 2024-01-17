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


class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailRegister: EditText
    private lateinit var passwordRegister: EditText
    private lateinit var registerButton: Button
    private lateinit var loginHere: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        emailRegister= view.findViewById(R.id.editEmailRegister)
        passwordRegister= view.findViewById(R.id.editPasswordRegister)
        registerButton=view.findViewById(R.id.registerButton)
        loginHere=view.findViewById(R.id.loginHereText)

        auth= FirebaseAuth.getInstance()

        registerButton.setOnClickListener {
            createUser()
        }

        loginHere.setOnClickListener {
            val fragmentTransaction : FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.container, LoginFragment())?.commit()
        }


        return view
    }
    private fun createUser(){
        val email=emailRegister.text.toString()
        val password=passwordRegister.text.toString()

        if(TextUtils.isEmpty(email)){
            emailRegister.error="Email cannot be empty!"
            emailRegister.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailRegister.error="Please provide valid email!"
            emailRegister.requestFocus()
            return
        }
        if(TextUtils.isEmpty(password)){
            passwordRegister.error="Password cannot be empty!"
            passwordRegister.requestFocus()
            return
        }
        if(password.length < 6){
            passwordRegister.error="Min password length should be 6 characters!"
            passwordRegister.requestFocus()
            return
        }
        else{
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "User registered successfully", Toast.LENGTH_SHORT).show()
                        val fragmentTransaction : FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
                        fragmentTransaction?.replace(R.id.container, LoginFragment())?.commit()

                    } else {
                        Toast.makeText(activity, "Registration failed", Toast.LENGTH_SHORT).show()
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