import 'package:flutter/material.dart';
import 'package:pdm_inventar/models/item.dart';
import 'package:pdm_inventar/utils/apis.dart';
import 'package:pdm_inventar/utils/shared_preferences_utils.dart';
import 'package:pdm_inventar/widgets/item_card.dart';

class ItemsScreen extends StatefulWidget {
  const ItemsScreen({super.key, required this.items});

  final List<Item> items;

  @override
  State<ItemsScreen> createState() => _ItemsScreenState();
}

class _ItemsScreenState extends State<ItemsScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: const Text('Items'),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () async {
          for (var item in widget.items) {
            if (item.status == "successfullySubmitted") {
              continue;
            }

            setState(() {
              item.status = 'submitting';
            });

            /*saveItem(item).then((Item savedItem) {
              setState(() {
                item.status = 'successfullySubmitted';
              });
            }).catchError((error) {
              setState(() {
                item.status = 'failedToSubmit';
              });
            });*/

            try {
              Item savedItem = await saveItem(item);
              setState(() {
                item.status = 'successfullySubmitted';
              });
            } catch (error) {
              setState(() {
                item.status = 'failedToSubmit';
              });
            }
          }
          saveItems(widget.items);
        },
        child: const Text('Submit'),
      ),
      body: ListView.builder(
        itemCount: widget.items.length,
        itemBuilder: (context, index) {
          return ItemCard(item: widget.items[index]);
        },
      ),
    );
  }
}
