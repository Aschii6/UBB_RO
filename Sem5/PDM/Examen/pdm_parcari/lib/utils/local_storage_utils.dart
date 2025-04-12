import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:pdm_parcari/models/parking_spot.dart';
import 'package:shared_preferences/shared_preferences.dart';

void saveUsername(String username) async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  prefs.setString('username', username);
  debugPrint('Username saved: $username');
}

Future<String> getUsername() async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  debugPrint('Username retrieved: ${prefs.getString('username')}');
  return prefs.getString('username') ?? '';
}

void removeUsername() async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  prefs.remove('username');
  debugPrint('Username removed');
}

void saveLocalParkingSpots(List<ParkingSpot> parkingSpots) async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  List<String> parkingSpotsList = parkingSpots.map((spot) => jsonEncode(spot.toJson())).toList();
  prefs.setStringList('parkingSpots', parkingSpotsList);
  debugPrint('Parking spots saved');
}

Future<List<ParkingSpot>> getLocalParkingSpots() async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  final List<String>? parkingSpotsList = prefs.getStringList('parkingSpots');

  debugPrint('Parking spots retrieved: $parkingSpotsList');

  if (parkingSpotsList != null && parkingSpotsList.isNotEmpty) {
    try {
      return parkingSpotsList.map((spot) => ParkingSpot.fromJson(jsonDecode(spot))).toList();
    } catch (e) {
      debugPrint('Error decoding parking spots: $e');
    }
  }
  return [];
}

void removeLocalParkingSpots() async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  prefs.remove('parkingSpots');
  debugPrint('Parking spots removed');
}