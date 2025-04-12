class Item {
  int? id;
  int code;
  int quantity = 0;
  String status;

  Item({this.id, required this.code, required this.quantity, this.status = 'notSubmitted'});

  factory Item.fromJson(Map<String, dynamic> json) {
    return Item(
      id: json['id'],
      code: json['code'],
      quantity: json['quantity'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'code': code,
      'quantity': quantity,
    };
  }

  Map<String, dynamic> toLocalStorageJson() {
    return {
      'id': id,
      'code': code,
      'quantity': quantity,
      'status': status,
    };
  }

  factory Item.fromLocalStorageJson(Map<String, dynamic> json) {
    return Item(
      id: json['id'],
      code: json['code'],
      quantity: json['quantity'],
      status: json['status'],
    );
  }

  @override
  String toString() {
    return 'Item{id: $id, code: $code, quantity: $quantity, status: $status}';
  }
}