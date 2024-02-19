package com.example.myinstagram

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myinstagram.Modals.User
import com.example.myinstagram.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginActivity:AppCompatActivity(){
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.loginBtn.setOnClickListener {
            if (binding.Email.editableText?.toString().equals("") or
                binding.textFieldPass.editText?.toString().equals("")
            ) {
                Toast.makeText(
                    this@LoginActivity,
                    "please fill all the details",
                    Toast.LENGTH_SHORT
                ).show()

            }else{
                var User=User(binding.Email.editableText?.toString(),
                    binding.textFieldPass.editText?.toString()
                )
                Firebase.auth.signInWithEmailAndPassword(User.email!!,User.password!!)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()

                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                it.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                }
            }
        }

    }

}
