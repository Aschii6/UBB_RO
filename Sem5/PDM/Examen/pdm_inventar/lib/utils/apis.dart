import 'package:flutter/cupertino.dart';
import 'package:http/http.dart' as http;
import 'package:pdm_inventar/models/item.dart';
import 'dart:convert';

import 'package:pdm_inventar/models/product.dart';

const String baseUrl = 'http://172.30.253.59:3000';

Future<int> getProductCount() async {
  final response = await http
      .get(Uri.parse('$baseUrl/product?page=1'))
      .timeout(const Duration(seconds: 6));
  if (response.statusCode == 200) {
    final Map<String, dynamic> data = jsonDecode(response.body);
    debugPrint('Data: $data');
    return data['total'];
  } else {
    throw Exception('Failed to load page count');
  }
}

Future<List<Product>> getProducts(int page) async {
  final response = await http
      .get(Uri.parse('$baseUrl/product?page=$page'))
      .timeout(const Duration(seconds: 6));
  if (response.statusCode == 200) {
    final Map<String, dynamic> data = jsonDecode(response.body);
    debugPrint('Data: $data');
    return data['products']
        .map<Product>((item) => Product.fromJson(item))
        .toList();
  } else {
    throw Exception('Failed to load products');
  }
}

Future<Item> saveItem(Item item) async {
  final response = await http
      .post(
        Uri.parse('$baseUrl/item'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(item.toJson()),
      )
      .timeout(const Duration(seconds: 6));
  if (response.statusCode == 200) {
    return Item.fromJson(jsonDecode(response.body));
  } else {
    throw Exception('Failed to save item');
  }
}
