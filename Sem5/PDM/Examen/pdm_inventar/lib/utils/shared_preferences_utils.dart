import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:pdm_inventar/models/item.dart';
import 'package:shared_preferences/shared_preferences.dart';

void saveItems(List<Item> items) async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  List<String> itemsList = items.map((item) => jsonEncode(item.toLocalStorageJson())).toList();
  prefs.setStringList('items', itemsList);
  debugPrint('Items saved');
}

Future<List<Item>> getItems() async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  final List<String>? itemsList = prefs.getStringList('items');

  debugPrint('Items retrieved: $itemsList');

  if (itemsList != null && itemsList.isNotEmpty) {
    try {
      return itemsList.map((item) => Item.fromLocalStorageJson(jsonDecode(item))).toList();
    } catch (e) {
      debugPrint('Error decoding items: $e');
    }
  }
  return [];
}

void removeItems() async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  prefs.remove('items');
  debugPrint('Items removed');
}