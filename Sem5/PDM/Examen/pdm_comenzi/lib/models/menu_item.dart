class MenuItem {
  final int code;
  final String name;
  final int price;
  int quantity;

  MenuItem({required this.code, required this.name, required this.price, this.quantity = 0});

  factory MenuItem.fromJson(Map<String, dynamic> json) {
    return MenuItem(
      code: json['code'],
      name: json['name'],
      price: json['price'],
      quantity: json['quantity'] ?? 0,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'code': code,
      'name': name,
      'price': price,
      'quantity': quantity,
    };
  }
}