package com.example.myinstagram.Post

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myinstagram.databinding.ActivityPostBinding
import com.example.myinstagram.utils.POST_FOLDER
import com.example.myinstagram.utils.uploadImage


class PostActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    var imageUrl:String?=null
    private val launcher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                uploadImage(uri, POST_FOLDER) {
                    url->
                    if (it != null) {
                        binding.selectImage.setImageURI(uri)


                    }
                }
            }
        }
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(binding.root)

                setSupportActionBar(binding.materialToolbar)
                getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
                getSupportActionBar()?.setDisplayShowHomeEnabled(true);
                binding.materialToolbar.setNavigationOnClickListener {
                    finish()
                }
                binding.selectImage.setOnClickListener {
                    launcher.launch("image/*")
                }
                binding.postButton.setOnClickListener {
//                    val post:Post=Post()

                }

            }
        }

