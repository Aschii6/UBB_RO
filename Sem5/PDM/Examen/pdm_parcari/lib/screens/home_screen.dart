import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:pdm_parcari/utils/local_storage_utils.dart';
import 'package:web_socket_channel/io.dart';

import '../models/parking_spot.dart';
import '../utils/apis.dart';
import '../widgets/parking_spot_card.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key, required this.username});

  final String username;

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  late Future<List<ParkingSpot>> parkingSpots;
  bool failedApiCall = false;

  final TextEditingController searchController = TextEditingController();
  List<ParkingSpot> filteredSpots = [];

  late IOWebSocketChannel channel;

  void _filterParkingSpots(String query) async {
    if (query.isEmpty) {
      setState(() {
        filteredSpots = [];
      });
      return;
    }

    List<ParkingSpot> spots = await parkingSpots;
    List<ParkingSpot> filtered =
        spots.where((spot) => spot.number.contains(query)).toList();

    setState(() {
      filteredSpots = filtered;
    });
  }

  @override
  void initState() {
    super.initState();
    parkingSpots = _loadParkingSpots();
    _connectWebSocket();
  }

  void _connectWebSocket() {
    channel = IOWebSocketChannel.connect('ws://10.152.0.180:3000');

    /*if (channel.closeCode != null) {
      _reconnectWebSocket();
    }*/

    channel.stream.listen((message) {
      final Map<String, dynamic> updatedSpot = jsonDecode(message);
      ParkingSpot spot = ParkingSpot.fromJson(updatedSpot);

      debugPrint('WebSocket message received: $spot');

      setState(() {
        parkingSpots.then((spots) {
          int index = spots.indexWhere((element) => element.id == spot.id);
          spots[index] = spot;

          setState(() {
            parkingSpots = Future.value(spots);
          });

          if (searchController.text.isNotEmpty) {
            _filterParkingSpots(searchController.text);
          }
        });
      });
    }, onDone: () {
      debugPrint('WebSocket channel closed');
      _reconnectWebSocket();
    }, onError: (error) {
      debugPrint('Error with WebSocket channel: $error');
      _reconnectWebSocket();
    });
  }

  void _reconnectWebSocket() {
    Future.delayed(const Duration(seconds: 5), () {
      if (channel.closeCode != null && mounted) {
        _connectWebSocket();
      }
    });
  }

  Future<List<ParkingSpot>> _loadParkingSpots() async {
    try {
      List<ParkingSpot> apiParkingSpots = await getParkingSpots();
      saveLocalParkingSpots(apiParkingSpots);

      setState(() {
        failedApiCall = false;
      });

      return apiParkingSpots;
    } catch (e) {
      setState(() {
        failedApiCall = true;
      });

      SnackBar snackBar = const SnackBar(
        content: Text('Failed to load parking spots from API'),
      );
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(snackBar);
      }

      List<ParkingSpot> localParkingSpots = await getLocalParkingSpots();

      if (localParkingSpots.isNotEmpty) {
        return localParkingSpots;
      } else {
        throw Exception('No parking spots available, even locally');
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Welcome, ${widget.username}')),
      floatingActionButton: failedApiCall
          ? FloatingActionButton(
              onPressed: () {
                setState(() {
                  parkingSpots = _loadParkingSpots();
                });
              },
              child: const Icon(Icons.refresh),
            )
          : null,
      body: FutureBuilder<List<ParkingSpot>>(
        future: parkingSpots,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError || snapshot.data == null) {
            return const Center(child: Text('Error loading parking spots'));
          } else if (snapshot.data!.isEmpty) {
            return const Center(child: Text('No parking spots available'));
          }

          return Column(
            children: [
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: TextField(
                  controller: searchController,
                  decoration: InputDecoration(
                    labelText: 'Search by Spot Number',
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(10.0),
                    ),
                    suffixIcon: IconButton(
                      icon: const Icon(Icons.clear),
                      onPressed: () {
                        searchController.clear();
                        _filterParkingSpots('');
                      },
                    ),
                  ),
                  onChanged: _filterParkingSpots,
                ),
              ),
              Expanded(
                child: ListView.builder(
                  itemCount: filteredSpots.isNotEmpty
                      ? filteredSpots.length
                      : snapshot.data!.length,
                  itemBuilder: (context, index) {
                    return ParkingSpotCard(
                      parkingSpot: filteredSpots.isNotEmpty
                          ? filteredSpots[index]
                          : snapshot.data![index],
                      username: widget.username,
                    );
                  },
                ),
              ),
            ],
          );
        },
      ),
    );
  }
}
