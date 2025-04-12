class ParkingSpot {
  final int id;
  final String number;
  String takenBy;

  ParkingSpot(this.id, this.number, this.takenBy);

  factory ParkingSpot.fromJson(Map<String, dynamic> json) {
    return ParkingSpot(
      json['id'],
      json['number'],
      json['takenBy'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'number': number,
      'takenBy': takenBy,
    };
  }
}