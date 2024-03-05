// User 모델 정의
class User {
  String userId;
  String password;
  String userName;
  String hp;

  User({
    required this.userId,
    required this.password, 
    required this.userName, 
    required this.hp
  });

  Map<String, dynamic> toJson() {
    return {
      'user_id': userId,
      'password': password,
      'user_name': userName,
      'hp': hp,
    };
  }
}
