import 'package:flutter/material.dart';
import 'package:pdm_comenzi/models/menu_item.dart';
import '../utils/local_storage_utils.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key, required this.tableNumber});

  final int tableNumber;

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  late Future<List<MenuItem>> menu;

  @override
  void initState() {
    super.initState();
    menu = _loadMenu();
  }

  Future<List<MenuItem>> _loadMenu() async {
    List<MenuItem> localMenu = await getMenu();
    return localMenu.isNotEmpty ? localMenu : getMenuFromWebSocket();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Home Screen')),
      body: FutureBuilder<List<MenuItem>>(
        future: menu,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError || snapshot.data == null) {
            return const Center(child: Text('Error loading menu'));
          } else if (snapshot.data!.isEmpty) {
            return const Center(child: Text('No menu items available'));
          }

          return ListView.builder(
            itemCount: snapshot.data!.length,
            itemBuilder: (context, index) {
              final menuItem = snapshot.data![index];
              final int totalPrice = menuItem.quantity * menuItem.price; // Calculate total price

              return ListTile(
                title: Text(menuItem.name),
                subtitle: Text('Price: ${menuItem.price}'),
                trailing: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text('Quantity: ${menuItem.quantity}'),
                    Text('Total: $totalPrice'),
                  ],
                ),
              );
            },
          );
        },
      ),
    );
  }
}
