import 'package:flutter/material.dart';
import 'package:pdm_parcari/utils/apis.dart';

import '../models/parking_spot.dart';

class ParkingSpotCard extends StatefulWidget {
  const ParkingSpotCard(
      {super.key, required this.parkingSpot, required this.username});

  final ParkingSpot parkingSpot;
  final String username;

  @override
  State<ParkingSpotCard> createState() => _ParkingSpotCardState();
}

class _ParkingSpotCardState extends State<ParkingSpotCard> {
  bool updateInProgress = false;
  bool updateFailed = false;
  bool isExpanded = false;

  @override
  Widget build(BuildContext context) {
    bool canExpand = widget.parkingSpot.takenBy == widget.username ||
        widget.parkingSpot.takenBy.isEmpty;

    return Card(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(15.0),
        side: const BorderSide(color: Colors.black, width: 1),
      ),
      margin: const EdgeInsets.all(10),
      child: canExpand
          ? Theme(
              data: Theme.of(context).copyWith(
                dividerColor: Colors.transparent,
              ),
              child: ExpansionTile(
                title: Text(widget.parkingSpot.number),
                trailing: _buildTrailingText(),
                initiallyExpanded: isExpanded,
                onExpansionChanged: (expanded) {
                  setState(() {
                    isExpanded = expanded;
                  });
                },
                children: [
                  updateInProgress
                      ? const Padding(
                          padding: EdgeInsets.all(8.0),
                          child: LinearProgressIndicator(),
                        )
                      : ElevatedButton(
                          onPressed: () {
                            setState(() {
                              updateInProgress = true;
                              updateFailed = false;
                            });

                            String status =
                                widget.parkingSpot.takenBy == widget.username
                                    ? 'free'
                                    : 'taken';

                            updateParkingSpotStatus(widget.parkingSpot.id,
                                    status, widget.username)
                                .then(
                              (success) {
                                setState(() {
                                  updateInProgress = false;
                                  updateFailed = !success;

                                  if (success) {
                                    widget.parkingSpot.takenBy =
                                        status == 'taken'
                                            ? widget.username
                                            : '';
                                  }
                                });
                              },
                            ).onError((error, stackTrace) {
                              setState(() {
                                updateInProgress = false;
                                updateFailed = true;
                              });
                            }).timeout(const Duration(seconds: 5),
                                    onTimeout: () {
                              setState(() {
                                updateInProgress = false;
                                updateFailed = true;
                              });
                            }).whenComplete(() {
                              if (updateFailed && mounted) {
                                ScaffoldMessenger.of(context).showSnackBar(
                                  const SnackBar(
                                    content: Text(
                                        'Failed to update parking spot status'),
                                  ),
                                );
                              }
                            });
                          },
                          child: Text(updateFailed
                              ? 'Retry'
                              : widget.parkingSpot.takenBy == widget.username
                                  ? 'Release'
                                  : 'Take'),
                        ),
                ],
              ),
            )
          : ListTile(
              title: Text(widget.parkingSpot.number),
              trailing: _buildTrailingText(),
            ),
    );
  }

  Widget _buildTrailingText() {
    return widget.parkingSpot.takenBy.isEmpty
        ? const Text('Free', style: TextStyle(color: Colors.green))
        : widget.parkingSpot.takenBy == widget.username
            ? const Text('Taken by you', style: TextStyle(color: Colors.yellow))
            : Text('Taken by ${widget.parkingSpot.takenBy}',
                style: const TextStyle(color: Colors.red));
  }
}
