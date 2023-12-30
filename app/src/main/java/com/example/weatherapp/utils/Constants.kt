package com.example.weatherapp.utils

import java.text.DecimalFormat

object Constants {
     var decimalFormat = DecimalFormat("###.####")
     var decimalFormatDegree = DecimalFormat("##.#")
     var apiKey = "f621b5d8ff6724370bd43c45e7939fca"
     val unitsValue = "metric"
     val BASE_URL = "https://api.openweathermap.org/data/2.5/"

}