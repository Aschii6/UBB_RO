import 'package:flutter/material.dart';
import 'package:pdm_inventar/screens/items_screen.dart';
import 'package:pdm_inventar/utils/apis.dart';
import 'package:pdm_inventar/utils/shared_preferences_utils.dart';
import 'package:web_socket_channel/io.dart';

import '../models/item.dart';
import '../models/product.dart';

class ProductsScreen extends StatefulWidget {
  const ProductsScreen({super.key});

  @override
  State<ProductsScreen> createState() => _ProductsScreenState();
}

class _ProductsScreenState extends State<ProductsScreen> {
  bool downloading = false;
  bool downloadedSuccessfully = false;

  List<List<Product>> products = [];
  List<Product> filteredProducts = [];

  List<Item> items = [];

  int downloadedPages = 0;
  int totalPages = 0;

  int _selectedProductCode = -1;
  String _selectedProductName = "";

  TextEditingController searchController = TextEditingController();
  TextEditingController quantityController = TextEditingController();

  late IOWebSocketChannel channel;

  _connectWebSocket() {
    channel = IOWebSocketChannel.connect('ws://172.30.253.59:3000');

    channel.stream.listen((message) async {
      debugPrint('Received message: $message');

      SnackBar snackBar = const SnackBar(
        content: Text('Products changed. Please download again.'),
      );

      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(snackBar);
      }

      while (downloading) {
        await Future.delayed(const Duration(seconds: 1));
      }

      setState(() {
        downloadedSuccessfully = false;
      });
    });
  }

  @override
  void initState() {
    super.initState();

    getItems().then((List<Item> savedItems) {
      setState(() {
        items = savedItems;
      });
    });

    _tryDownload();
    _connectWebSocket();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Products Screen'),
      ),
      floatingActionButton: Stack(
        children: [
          Align(
            alignment: Alignment.bottomRight,
            child: FloatingActionButton(
              onPressed: () {
                Navigator.of(context).push(
                  MaterialPageRoute(
                    builder: (context) => ItemsScreen(items: items),
                  ),
                );
              },
              child: const Text('Items'),
            ),
          ),
          if (!downloading && !downloadedSuccessfully)
            Align(
              alignment: Alignment.bottomLeft,
              child: SizedBox(
                width: 120,
                child: FloatingActionButton(
                  onPressed: _tryDownload,
                  child: const Text('Download'),
                ),
              ),
            )
        ],
      ),
      body: Center(
        child: downloading
            ? Text('Downloading $downloadedPages/$totalPages...', style: const TextStyle(fontSize: 20),)
            : totalPages == 0
                ? const Text('Press the download button to start downloading', style: TextStyle(fontSize: 16),)
                : Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 20.0),
                  child: Column(
                      children: [
                        TextField(
                          controller: searchController,
                          decoration: const InputDecoration(
                            hintText: 'Search',
                          ),
                          onChanged: (value) {
                            if (value.isEmpty) {
                              setState(() {
                                filteredProducts = [];
                              });
                              return;
                            }

                            setState(() {
                              filteredProducts = products
                                  .expand((element) => element)
                                  .where((product) => product.name
                                      .toLowerCase()
                                      .contains(value.toLowerCase()))
                                  .take(5)
                                  .toList();
                            });
                          },
                        ),
                        Expanded(
                          child: ListView.builder(
                            itemCount: filteredProducts.length,
                            itemBuilder: (context, index) {
                              return ListTile(
                                title: Text(filteredProducts[index].name),
                                onTap: () {
                                  setState(() {
                                    _selectedProductCode =
                                        filteredProducts[index].code;
                                    _selectedProductName =
                                        filteredProducts[index].name;
                                  });
                                },
                              );
                            },
                          ),
                        ),
                        const SizedBox(height: 20),
                        if (_selectedProductCode != -1)
                          Column(
                            children: [
                              Text('Selected product: $_selectedProductName'),
                              const SizedBox(height: 10),
                              ElevatedButton(
                                onPressed: () {
                                  setState(() {
                                    _selectedProductCode = -1;
                                    _selectedProductName = "";
                                  });
                                },
                                child: const Text('Clear selection'),
                              ),
                              const SizedBox(height: 10),
                              TextField(
                                controller: quantityController,
                                decoration: const InputDecoration(
                                  hintText: 'Quantity',
                                ),
                              ),
                              const SizedBox(height: 10),
                              ElevatedButton(
                                onPressed: () {
                                  int quantity =
                                      int.tryParse(quantityController.text) ?? 0;
                                  if (quantity <= 0) {
                                    return;
                                  }

                                  quantityController.clear();

                                  Item item = Item(
                                    code: _selectedProductCode,
                                    quantity: quantity,
                                  );

                                  setState(() {
                                    items.add(item);
                                  });

                                  saveItems(items);
                                },
                                child: const Text('Add'),
                              ),
                              const SizedBox(height: 100),
                            ],
                          ),
                      ],
                    ),
                ),
      ),
    );
  }

  Future<void> _tryDownload() async {
    setState(() {
      downloading = true;
    });

    getProductCount().then((int productCount) async {
      setState(() {
        totalPages = (productCount / 10).ceil();
      });
      debugPrint('Total pages: $totalPages');

      while (downloadedPages < totalPages) {
        List<Product> pageProducts = await getProducts(downloadedPages + 1);
        products.add(pageProducts);
        setState(() {
          downloadedPages++;
        });
      }

      setState(() {
        downloadedSuccessfully = true;
        downloading = false;
      });

      if (channel.closeCode != null) {
        _connectWebSocket();
      }
    }).onError((error, stackTrace) {
      debugPrint('Error downloading products: $error');
      setState(() {
        downloadedSuccessfully = false;
        downloading = false;
      });

      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Failed to download all products'),
          ),
        );
      }
    });
  }
}
