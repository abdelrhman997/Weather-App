package com.example.weatherapp.view

import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import android.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.example.weatherapp.viewmodel.MainActivityViewModel
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var weatherViewModel : MainActivityViewModel

    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var sharedPreferencesEditor : SharedPreferences.Editor

    private var decimalFormat = DecimalFormat("###.####")
    private var decimalFormatDegree = DecimalFormat("##.#")

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sharedPreferences = this.getSharedPreferences(packageName, MODE_PRIVATE)
        sharedPreferencesEditor = sharedPreferences.edit()



        if (internet_connection()) {

            weatherViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

            val cName = sharedPreferences.getString("cityName","cairo")
            binding.cityNameEditText.setText(cName)

            weatherViewModel.refreshData(cName!!)

            obserLiveData()

            binding.swipeRefreshLayout.setOnRefreshListener {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.dataViewLinearLayout.visibility = View.GONE
                binding.errorMessageTextView.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                val cityName = sharedPreferences.getString("cityName",cName)
                binding.cityNameEditText.setText(cityName)
                weatherViewModel.refreshData(cityName!!)
                obserLiveData()
            }

            binding.citySearchImageView.setOnClickListener {
                val inputMethod : InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethod.hideSoftInputFromWindow(currentFocus?.windowToken,0)
                val cityName = binding.cityNameEditText.text.toString()
                sharedPreferencesEditor.putString("cityName",cityName)
                sharedPreferencesEditor.commit()
                weatherViewModel.refreshData(cityName)
                obserLiveData()

            }

        } else {
            val snackbar = Snackbar.make(
                findViewById(R.id.content),
                "No internet connection.",
                Snackbar.LENGTH_SHORT
            )
            snackbar.setActionTextColor(
                ContextCompat.getColor(applicationContext, R.color.black)
            )
                .show()
        }
    }

    private fun obserLiveData(){
        weatherViewModel.weather.observe(this, Observer {data->
            data?.let {
                binding.dataViewLinearLayout.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.degreeTextView.text = "${decimalFormatDegree.format(it.main.temp)}°C"
                binding.countryCodeTextView.text = it.sys.country
                binding.cityNameTextView.text = it.name
                binding.humidityTextView.text = "%${it.main.humidity}"
                binding.speedTextView.text = "${it.wind.speed} km/h"
                binding.latTextView.text = decimalFormat.format(it.coord.lat)
                binding.lonTextView.text = decimalFormat.format(it.coord.lon)
                val imageView : ImageView = binding.weatherIconImageView
                val icon = it.weather.get(0).icon
                val imageUrl = "http://openweathermap.org/img/wn/$icon@2x.png"
                Glide.with(this)
                    .load(imageUrl)
                    .into(imageView)

            }

        })

        weatherViewModel.weatherErrorMessage.observe(this, Observer {
            it?.let {
                if(it){
                    binding.errorMessageTextView.visibility = View.VISIBLE
                    binding.dataViewLinearLayout.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                }
                else{
                    binding.errorMessageTextView.visibility = View.GONE
                }
            }
        })

        weatherViewModel.weatherProgressBar.observe(this, Observer {
            if(it){
                binding.errorMessageTextView.visibility = View.GONE
                binding.dataViewLinearLayout.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }
            else{
                binding.progressBar.visibility = View.GONE
            }

        })

    }
    private fun internet_connection(): Boolean {
        //Check if connected to internet, output accordingly
        val cm =
            this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
    }
}