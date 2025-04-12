import 'package:flutter/material.dart';

import '../models/item.dart';

class ItemCard extends StatefulWidget {
  const ItemCard({super.key, required this.item});

  final Item item;

  @override
  State<ItemCard> createState() => _ItemCardState();
}

class _ItemCardState extends State<ItemCard> {
  @override
  Widget build(BuildContext context) {
    return Card(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(15.0),
        side: const BorderSide(color: Colors.black, width: 1),
      ),
      margin: const EdgeInsets.all(16),
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 10.0, vertical: 16.0),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(
              widget.item.code.toString(),
              style: TextStyle(color: _getColor()),
            ),
            if (widget.item.status == "submitting") const Text('submitting') else const SizedBox(),
            Text(
              'quantity: ${widget.item.quantity}',
              style: TextStyle(color: _getColor()),
            ),
          ],
        ),
      ),
    );
  }

  Color _getColor() {
    return widget.item.status == "notSubmitted" || widget.item.status == "submitting"
        ? Colors.black
        : widget.item.status == "successfullySubmitted"
            ? Colors.green
            : Colors.red;
  }
}
