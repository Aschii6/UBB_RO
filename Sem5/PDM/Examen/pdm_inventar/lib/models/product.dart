class Product {
  final int code;
  final String name;

  Product(this.code, this.name);

  factory Product.fromJson(Map<String, dynamic> json) {
    return Product(
      json['code'],
      json['name'],
    );
  }
}