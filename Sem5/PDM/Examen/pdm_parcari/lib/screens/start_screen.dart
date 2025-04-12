import 'package:flutter/material.dart';
import 'package:pdm_parcari/utils/local_storage_utils.dart';

import 'home_screen.dart';

class StartScreen extends StatefulWidget {
  const StartScreen({super.key});

  @override
  State<StartScreen> createState() => _StartScreenState();
}

class _StartScreenState extends State<StartScreen> {
  TextEditingController usernameController = TextEditingController();

  @override
  void initState() {
    super.initState();

    getUsername().then((String? username) {
      if (username != null && username.isNotEmpty) {
        if (mounted) {
          Navigator.of(context).pushReplacement(
            MaterialPageRoute(
                builder: (context) => HomeScreen(username: username)),
          );
        }
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Start Screen')),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 30.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text('Enter your username'),
            const SizedBox(height: 20),
            TextField(
              controller: usernameController,
              decoration: const InputDecoration(
                hintText: 'Username',
              ),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                String username = usernameController.text;
                debugPrint('Username: $username');
                saveUsername(username);

                Navigator.of(context).pushReplacement(
                  MaterialPageRoute(
                      builder: (context) => HomeScreen(username: username)),
                );
              },
              child: const Text('Next'),
            ),
          ],
        ),
      ),
    );
  }
}
