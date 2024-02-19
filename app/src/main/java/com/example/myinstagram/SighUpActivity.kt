package com.example.myinstagram

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myinstagram.Modals.User
import com.example.myinstagram.databinding.ActivitySignUpBinding
import com.example.myinstagram.utils.USER_NODE
import com.example.myinstagram.utils.USER_PROFILE_FOLDER
import com.example.myinstagram.utils.uploadImage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso


val Firebase.firestore: FirebaseFirestore
    get() = FirebaseFirestore.getInstance()



class SighUpActivity : AppCompatActivity() {
    val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    lateinit var user: User
    private val launcher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                uploadImage(uri, USER_PROFILE_FOLDER) {
                    if (it!=null) {
                        user.image = it
                        binding.profileImage.setImageURI(uri)

                    }
                }
            }
        }


                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)
                    setContentView(binding.root)
                    val text =
                        "<font color=#FF000000>Already have an Account </font> <font color=#C13584>Login </font>"
                    binding.loginBtn.setText(Html.fromHtml(text))
                    user = User()
                    if (intent.hasExtra("MODE")) {
                        if (intent.getIntExtra("MODE", -1) == 1){
                            binding.signbtn.text="Uplode Profile"
                            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                                .addOnSuccessListener {

                                    user=it.toObject<User>()!!
                                    if (!user.image.isNullOrEmpty()){
                                        Picasso.get().load(user.image).into(binding.profileImage)
                                    }


                                }
                        }
                    }
                    binding.signbtn.setOnClickListener {
                        if (intent.hasExtra("MODE")){
                            if (intent.getIntExtra("MODE", -1) == 1){
                                Firebase.firestore.collection(USER_NODE)
                                    .document(Firebase.auth.currentUser!!.uid).set(user)
                                    .addOnSuccessListener {
                                        startActivity(Intent(this@SighUpActivity,HomeActivity::class.java))
                                        finish()
                                    }


                            }

                        }
                        else{

                        if (binding.name.editableText?.toString().equals("") or
                            binding.Email.editableText?.toString().equals("") or
                            binding.Password.editableText?.toString().equals("")
                        ) {
                            Toast.makeText(
                                this@SighUpActivity,
                                "Please fill in all information",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                                binding.Email.editableText.toString(),
                                binding.Password.editableText.toString()
                            ).addOnCompleteListener { result ->

                                if (result.isSuccessful) {
                                    user.name = binding.name.editableText?.toString()
                                    user.email = binding.Email.editableText?.toString()
                                    user.password = binding.Password.editableText?.toString()
                                    Firebase.firestore.collection(USER_NODE)
                                        .document(Firebase.auth.currentUser!!.uid).set(user)
                                        .addOnSuccessListener {
                                           startActivity(Intent(this@SighUpActivity,HomeActivity::class.java))
                                            finish()
                                        }


                                } else {
                                    Toast.makeText(
                                        this@SighUpActivity,
                                        result.exception?.localizedMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }

                            }
                        }
                        }

                    }
                    binding.profileImage.setOnClickListener {
                        launcher.launch("image/*")

                    }
                    binding.loginBtn.setOnClickListener {
                        startActivity(Intent(this@SighUpActivity,LoginActivity::class.java))
                        finish()
                    }

                }
        }







