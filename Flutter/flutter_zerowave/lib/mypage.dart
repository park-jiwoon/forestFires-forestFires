import 'package:flutter/material.dart';
import 'package:flutter_zerowave/home.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:intl/intl.dart';
// 가정: drawer.dart는 사용자 정의 Drawer 위젯을 포함하는 파일입니다.
import 'drawer.dart';
import 'login.dart';

class MyPage extends StatefulWidget {
  @override
  _MyPageState createState() => _MyPageState();
}

class _MyPageState extends State<MyPage> {
  String userName = '';
  String userHp = '';
  String createTimeFormatted = '';
  TextEditingController _currentPasswordController = TextEditingController();
  TextEditingController _newPasswordController = TextEditingController();
  final _formKey = GlobalKey<FormState>();

  @override
  void initState() {
    super.initState();
    fetchUserInfo();
  }

  Future<void> fetchUserInfo() async {
    final jwtToken = await getJwtToken();
    if (jwtToken == null) return;

    final response = await http.get(
      Uri.parse('http://192.168.41.202:8081/api/mypage/info'),
      headers: {'Authorization': 'Bearer $jwtToken'},
    );

    if (response.statusCode == 200) {
      final data = json.decode(response.body);
      setState(() {
        userName = data['name'];
        userHp = data['hp'];
        DateTime createDate = DateTime.parse(data['CREATE_DATE']);
        createTimeFormatted =
            DateFormat('yyyy-MM-dd – kk:mm').format(createDate);
      });
    } else {
      print("Failed to load user info");
    }
  }

  Future<void> changePassword() async {
    if (!_formKey.currentState!.validate()) return; // 폼 유효성 검사
    final jwtToken = await getJwtToken();
    if (jwtToken == null) return;

    final response = await http.put(
      Uri.parse('http://192.168.41.202:8081/api/mypage/update_pw'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $jwtToken',
      },
      body: jsonEncode({
        'currentPassword': _currentPasswordController.text,
        'newPassword': _newPasswordController.text,
      }),
    );

    if (response.statusCode == 200) {
      _showDialog("Success", "비밀번호가 변경되었습니다.");
    } else {
      _showDialog("Error", "Failed to change password.");
    }
  }

  Future<void> deleteUserAccount() async {
    final jwtToken = await getJwtToken();
    if (jwtToken == null) return;

    final response = await http.delete(
      Uri.parse('http://192.168.41.202:8081/api/mypage/delete_account'),
      headers: {'Authorization': 'Bearer $jwtToken'},
    );

    if (response.statusCode == 200) {
      
      // 여기에 로그아웃 처리 로직을 추가하세요
      // JWT 토큰 삭제
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('jwt_token');
    

    // 로그인 페이지로 이동
    Navigator.of(context).pushReplacement(
      MaterialPageRoute(builder: (context) => Login()), // LoginPage는 로그인 페이지 위젯
    );
    _showDialog("Success", "회원탈퇴 완료!");
    } else {
      _showDialog("Error", "Failed to delete account.");
    }
  }

  Future<String?> getJwtToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString('jwt_token');
  }

  void _showDialog(String title, String content) {
    showDialog(
      context: context,
      builder: (_) => AlertDialog(
        title: Text(title),
        content: Text(content),
        actions: <Widget>[
          TextButton(
            child: Text('OK'),
            onPressed: () => Navigator.of(context).pop(),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "마이 페이지",
          style: TextStyle(color: Colors.white),
        ),
        iconTheme: IconThemeData(color: Colors.white),
        backgroundColor: Color(0xff0C1C2E),
      ),
      endDrawer: CommonDrawer(),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(20.0),
          child: Form(
            key: _formKey,
            child: Card(
              elevation: 10.0,
              //color: Color.fromARGB(215, 240,255,240),
              color: Color.fromARGB(255, 255,255,255),
              child: Padding(
                padding: const EdgeInsets.all(30.0),
                child: Column(
                  children: [
                    UserInformationCard(
                        userName: userName,
                        userHp: userHp,
                        createTimeFormatted: createTimeFormatted),
                    Divider(),
                    PasswordChangeForm(
                        currentPasswordController: _currentPasswordController,
                        newPasswordController: _newPasswordController,
                        changePassword: changePassword),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: deleteUserAccount,
        child: Icon(Icons.delete_forever),
        backgroundColor: Colors.red,
      ),
      // floatingActionButton: FloatingActionButton(
      //   onPressed: () async {
      //     // onPressed를 비동기 함수로 만듦
      //     await deleteUserAccount(); // 계정 삭제 함수를 비동기로 호출
      //     // 작업 완료 후 페이지 이동
      //     Navigator.of(context).pushReplacement(
      //       // 현재 페이지를 스택에서 제거하고 새 페이지로 이동
      //       MaterialPageRoute(
      //           builder: (context) => Home()), // NewPage는 이동할 새 페이지 위젯
      //     );
      //   },
      //   child: Icon(Icons.delete_forever),
      //   backgroundColor: Colors.red,
      // ),
    );
  }
}

class UserInformationCard extends StatelessWidget {
  final String userName;
  final String userHp;
  final String createTimeFormatted;

  const UserInformationCard(
      {Key? key,
      required this.userName,
      required this.userHp,
      required this.createTimeFormatted})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      alignment: Alignment.centerLeft, // 이는 Container를 감싸는 더 큰 위젯에 적용됩니다.
      child: Column(
        crossAxisAlignment:
            CrossAxisAlignment.start, // Column의 자식들을 왼쪽으로 정렬합니다.
        children: <Widget>[
          Text("이름\n $userName", style: TextStyle(fontSize: 18)),
          SizedBox(height: 8), // Text 위젯 사이의 간격을 위해
          Text("전화번호\n $userHp", style: TextStyle(fontSize: 18)),
          SizedBox(height: 8),
          Text("전화번호\n $createTimeFormatted", style: TextStyle(fontSize: 18)),
        ],
      ),
    );
  }
}
// Column(
//   crossAxisAlignment: CrossAxisAlignment.start, // 자식들을 시작점(왼쪽)으로 정렬
//   children: [
//     Text("이름: $userName", style: TextStyle(fontSize: 18)),
//     SizedBox(height: 8),
//     Text("전화번호: $userHp", style: TextStyle(fontSize: 18)),
//     SizedBox(height: 8),
//     Text("가입일: $createTimeFormatted", style: TextStyle(fontSize: 18)),
//   ],
// );

class PasswordChangeForm extends StatelessWidget {
  final TextEditingController currentPasswordController;
  final TextEditingController newPasswordController;
  final Function changePassword;

  const PasswordChangeForm(
      {Key? key,
      required this.currentPasswordController,
      required this.newPasswordController,
      required this.changePassword})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        const SizedBox(height: 30),
        TextFormField(
          controller: currentPasswordController,
          decoration: InputDecoration(labelText: '현재 비밀번호'),
          obscureText: true,
        ),
        const SizedBox(height: 30),
        TextFormField(
          controller: newPasswordController,
          decoration: InputDecoration(labelText: '새 비밀번호'),
          obscureText: true,
        ),
        const SizedBox(height: 30),
        Container(
          width: double.infinity, // 컨테이너를 화면 너비에 맞춤
          margin: EdgeInsets.symmetric(horizontal: 0), // 좌우 여백 추가
          child: ElevatedButton(
            onPressed: () async {
              // changePassword()가 Future를 반환한다고 가정할 때
              await changePassword(); // 비밀번호 변경 함수를 비동기로 호출
              // 작업 완료 후 페이지 이동
              Navigator.of(context).push(
                MaterialPageRoute(
                    builder: (context) => Home()), // NewPage는 목적지 페이지 위젯
              );
            },
            child: Text('비밀번호 변경'), // 버튼에 표시될 텍스트
            style: ElevatedButton.styleFrom(
              onPrimary: Colors.black, // 버튼 내 텍스트 색상
              backgroundColor: Color.fromRGBO(186, 225, 98, 1), // 버튼 배경 색상
              minimumSize: Size(double.infinity, 50), // 버튼의 최소 크기 설정
            ),
          ),
        ),

        // ElevatedButton(
        //   onPressed: () => changePassword(),

        //   child: Text('비밀번호 변경'),
        // ),
      ],
    );
  }
}
