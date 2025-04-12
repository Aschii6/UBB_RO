import 'package:flutter/material.dart';

import '../utils/local_storage_utils.dart';
import 'home_screen.dart';

class StartScreen extends StatefulWidget {
  const StartScreen({super.key});

  @override
  State<StartScreen> createState() => _StartScreenState();
}

class _StartScreenState extends State<StartScreen> {
  TextEditingController tableNumberController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 40.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text('Enter table number'),
            const SizedBox(height: 20),
            TextField(
              controller: tableNumberController,
              keyboardType: TextInputType.number,
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                int tableNumber = int.parse(tableNumberController.text);
                debugPrint('Table number: $tableNumber');
                saveTable(tableNumber);

                Navigator.of(context).pushReplacement(
                  MaterialPageRoute(
                      builder: (context) =>
                          HomeScreen(tableNumber: tableNumber)),
                );
              },
              child: const Text('Set table'),
            ),
          ],
        ),
      ),
    );
  }

  @override
  void initState() {
    super.initState();
    getTable().then((int? tableNumber) {
      if (tableNumber != null && tableNumber > 0) {
        Navigator.of(context).push(
          MaterialPageRoute(
              builder: (context) => HomeScreen(tableNumber: tableNumber)),
        );
      }
    });
  }
}
