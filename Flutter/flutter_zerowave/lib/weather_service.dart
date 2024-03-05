// import 'package:flutter/material.dart';
// import 'package:google_maps_flutter/google_maps_flutter.dart';
// import 'package:location/location.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';


class WeatherService {
  static const apiKey = 'f58e858bf87f6ea8a836f2211f9f4f85'; // 실제 API 키로 교체해야 합니다.

  static Future<Map<String, dynamic>> fetchWeather(double lat, double lon) async {
    final url = Uri.parse(
        'https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$apiKey&units=metric');
    final response = await http.get(url);

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Failed to load weather data');
    }
  }
}