package com.example.task

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    // creating variables for our edittext,
    // button, textview and progressbar.
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var number: EditText
    private lateinit var refCode: EditText
    private lateinit var button: Button
    private lateinit var loadingPB: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initializing our views
        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        number = findViewById(R.id.number)
        refCode = findViewById(R.id.refCode)
        button = findViewById(R.id.button)

        // adding on click listener to our button.
        button.setOnClickListener {
            if (name.text.toString().isEmpty() && email.text.toString().isEmpty() && password.text.toString().isEmpty() number.text.toString().isEmpty() || refCode.text.toString().isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Please enter the values",
                    Toast.LENGTH_SHORT
                ).show()
            }
            postData(name.getText().toString(), email.getText().toString(), password.getText().toString()
                    number.getText().toString(), refCode.getText().toString())
        }
    }

    private fun postData(name: String, email: String, password: String, number: String, refCode: String) {

        // below line is for displaying our progress bar.
        loadingPB!!.visibility = View.VISIBLE

        // on below line we are creating a retrofit
        // builder and passing our base url
        val retrofit = Retrofit.Builder()
            .baseUrl("https://coincred.qanvustech.com/api/GetRequest/register/") // as we are sending data in json format so
            // we have to add Gson converter factory
            .addConverterFactory(GsonConverterFactory.create()) // at last we are building our retrofit builder.
            .build()
        // below line is to create an instance for our retrofit api class.
        val retrofitAPI = retrofit.create(RetrofitAPI::class.java)

        // passing data from our text fields to our modal class.
        val modal = DataModal(name, email, password, number, refCode)

        // calling a method to create a post and passing our modal class.
        val call = retrofitAPI.createPost(modal)

        // on below line we are executing our method.
        call!!.enqueue(object : Callback<DataModal?> {
            override fun onResponse(call: Call<DataModal?>, response: Response<DataModal?>) {
                // this method is called when we get response from our api.
                Toast.makeText(this@MainActivity, "Data added to API", Toast.LENGTH_SHORT).show()

                // below line is for hiding our progress bar.
                loadingPB!!.visibility = View.GONE

                // on below line we are setting empty text
                // to our both edit text.
                jobEdt!!.setText("")
                nameEdt!!.setText("")

                // we are getting response from our body
                // and passing it to our modal class.
                val responseFromAPI = response.body()

                // on below line we are getting our data from modal class and adding it to our string.
                val responseString = """
                    Response Code : ${response.code()}
                    Name : ${responseFromAPI!!.name}
                    Job : ${responseFromAPI.getJob()}
                    """.trimIndent()

                // below line we are setting our
                // string to our text view.
                responseTV!!.text = responseString
            }

            override fun onFailure(call: Call<DataModal?>, t: Throwable) {
                // setting text to our text view when
                // we get error response from API.
                responseTV!!.text = "Error found is : " + t.message
            }
        })
    }
}
