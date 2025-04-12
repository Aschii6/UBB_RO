import 'dart:convert';

import 'package:pdm_parcari/models/parking_spot.dart';
import 'package:http/http.dart' as http;

const String baseUrl = 'http://10.152.0.180:3000/space';

Future<List<ParkingSpot>> getParkingSpots() async {
  final response =
      await http.get(Uri.parse(baseUrl)).timeout(const Duration(seconds: 8));
  if (response.statusCode == 200) {
    final List<dynamic> data = jsonDecode(response.body);
    return data.map((item) => ParkingSpot.fromJson(item)).toList();
  } else {
    throw Exception('Failed to load parking spots');
  }
}

Future<bool> updateParkingSpotStatus(int id, String status, String username) async {
  final url = Uri.parse('$baseUrl/$id');

  final response = await http.put(
    url,
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      'id': id,
      'status': status,
      'takenBy': status == 'taken' ? username : '',
    }),
  );

  if (response.statusCode == 200) {
    return true;
  } else {
    return false;
  }
}
