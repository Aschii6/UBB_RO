import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:pdm_comenzi/models/menu_item.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:web_socket_channel/web_socket_channel.dart';

void saveTable(int tableNumber) async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  prefs.setInt('tableNumber', tableNumber);
  debugPrint('Table number saved: $tableNumber');
}

Future<int> getTable() async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  debugPrint('Table number retrieved: ${prefs.getInt('tableNumber')}');
  return prefs.getInt('tableNumber') ?? 0;
}

void removeTable() async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  prefs.remove('tableNumber');
  debugPrint('Table number removed');
}

void saveMenu(List<MenuItem> menu) async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  List<String> menuList = menu.map((item) => jsonEncode(item.toJson())).toList();
  prefs.setStringList('menu', menuList);
  debugPrint('Menu saved');
}

Future<List<MenuItem>> getMenu() async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  final List<String>? menuList = prefs.getStringList('menu');

  debugPrint('Menu retrieved: $menuList');

  if (menuList != null && menuList.isNotEmpty) {
    try {
      return menuList.map((item) => MenuItem.fromJson(jsonDecode(item))).toList();
    } catch (e) {
      debugPrint('Error decoding menu: $e');
    }
  }
  return [];
}

void removeMenu() async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  prefs.remove('menu');
  debugPrint('Menu removed');
}

Future<List<MenuItem>> getMenuFromWebSocket() async {
  final Uri wsUrl = Uri.parse('ws://10.152.0.180:3000');
  final WebSocketChannel channel = WebSocketChannel.connect(wsUrl);
  Completer<List<MenuItem>> completer = Completer();

  channel.stream.listen(
        (event) {
      try {
        final List<dynamic> data = jsonDecode(event);
        final List<MenuItem> menuItems = data.map((item) => MenuItem.fromJson(item)).toList();
        if (!completer.isCompleted) {
          completer.complete(menuItems);
        }
      } catch (e) {
        if (!completer.isCompleted) {
          completer.complete([]);
        }
      }
    },
    onError: (error) {
      if (!completer.isCompleted) {
        completer.complete([]);
      }
    },
    onDone: () => channel.sink.close(),
  );

  saveMenu(await completer.future);

  return completer.future;
}