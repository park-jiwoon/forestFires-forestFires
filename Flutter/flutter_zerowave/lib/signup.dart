import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'login.dart'; // 이 부분은 실제 경로에 따라 다를 수 있습니다.
import '../values/app_theme.dart';
import '../utils/common_widgets/gradient_background.dart';
import 'drawer.dart';

class SignUp extends StatefulWidget {
  @override
  _SignUpState createState() => _SignUpState();
}

class _SignUpState extends State<SignUp> {

  // 240213 수정
  // final TextEditingController _userIdController = TextEditingController();
  final TextEditingController _hpController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final TextEditingController _confirmPasswordController =TextEditingController(); // 추가
  final TextEditingController _userNameController = TextEditingController();

  final ValueNotifier<bool> passwordNotifier = ValueNotifier(true);
  final _formKey = GlobalKey<FormState>();

  @override
  void dispose() {
    //_userIdController.dispose();
    _passwordController.dispose();
    _confirmPasswordController.dispose(); // 추가
    _userNameController.dispose();
    _hpController.dispose();
    super.dispose();
  }

  Future<void> signUp() async {
    final userData = {
      // 240213 수정
      // 'userId': _userIdController.text,
      'hp': _hpController.text,
      'password': _passwordController.text,
      'userName': _userNameController.text,
    };

    final response = await http.post(
      //Uri.parse('http://192.168.41.180:8081/api/register'),
      Uri.parse('http://192.168.41.202:8081/api/register'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(userData),
    );

    if (response.statusCode == 200) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('회원가입 성공')),
      );
      Navigator.of(context).pushReplacement(
        MaterialPageRoute(builder: (context) => Login()),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('회원가입 실패: ${response.body}')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(""),
        iconTheme: IconThemeData(color: Colors.white),
        backgroundColor: Color(0xff0C1C2E),
      ),
      endDrawer: CommonDrawer(),
      body: GestureDetector(
    onTap: () {
      FocusScope.of(context).unfocus(); // 화면 어느 곳이나 터치하면 키보드 내려감
    },
    child: ListView(
      padding: EdgeInsets.zero,
      children: [
        const GradientBackground(
          colors: [Color(0xff0C1C2E), Color(0xff0C1C2E)],
          children: [
            Text(
              "  회원가입",
              style: AppTheme.titleLarge,
            ),
            SizedBox(height: 3),
            Text("      해당 정보를 입력해 주세요", style: AppTheme.bodySmall),
             Padding(
            padding: const EdgeInsets.all(20)),
          ],
          
        ),
        SizedBox(height: 30),
        Form(
          key: _formKey,
          child: Padding(
            padding: const EdgeInsets.all(20),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                    TextFormField(
                    controller: _hpController,
                    decoration: InputDecoration(labelText: "전화번호"),
                    textInputAction: TextInputAction.next,
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return "번호를 입력해주세요!";
                      }
                      return null;
                    },
                  ),
                  SizedBox(height: 30),
                  ValueListenableBuilder<bool>(
                    valueListenable: passwordNotifier,
                    builder: (context, passwordObscure, child) {
                      return TextFormField(
                        obscureText: passwordObscure,
                        controller: _passwordController,
                        decoration: InputDecoration(
                          labelText: "Password",
                          suffixIcon: IconButton(
                            onPressed: () => passwordNotifier.value =
                                !passwordNotifier.value,
                            icon: Icon(
                              passwordObscure
                                  ? Icons.visibility_off
                                  : Icons.visibility,
                            ),
                          ),
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return "비밀번호를 입력해주세요!";
                          }
                          return null;
                        },
                      );
                    },
                  ),
                  SizedBox(height: 30),
                  TextFormField(
                    controller: _confirmPasswordController,
                    decoration: InputDecoration(labelText: "비밀번호 확인"),
                    obscureText: true,
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return "비밀번호 확인을 입력해주세요!";
                      } else if (_passwordController.text != value) {
                        return "비밀번호가 일치하지 않습니다.";
                      }
                      return null;
                    },
                  ),
                  SizedBox(height: 30),
                  TextFormField(
                    controller: _userNameController,
                    decoration: InputDecoration(labelText: "이름"),
                    textInputAction: TextInputAction.next,
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return "이름을 입력해주세요!";
                      }
                      return null;
                    },
                  ),
                  SizedBox(height: 45),
                
                  // SizedBox(height: 30),
                  Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Container(
                          width: double.infinity,
                          margin: EdgeInsets.symmetric(horizontal: 0),
                          child: ElevatedButton(
                            onPressed: () {
                              if (_formKey.currentState!.validate()) {
                                signUp();
                                
                                Navigator.of(context).pushReplacement(
                                  MaterialPageRoute(
                                      builder: (context) => Login()),
                                );
                              }
                            },
                            style: ElevatedButton.styleFrom(
                              onPrimary: Colors.black, // 텍스트 색상
                              backgroundColor:const Color.fromRGBO(186, 225, 98, 1),
                              minimumSize:
                                  Size(double.infinity, 50), // 버튼의 최소 크기 설정
                            ),
                            child: Text('회원가입'),
                          ),
                        ),
                        SizedBox(height: 10),
                        Container(
                          width: double.infinity, // 컨테이너를 화면 너비에 맞춤
                          margin:
                              EdgeInsets.symmetric(horizontal: 00), // 좌우 여백 추가
                          child: ElevatedButton(
                            onPressed: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (context) => Login()),
                              );
                            },
                            style: ElevatedButton.styleFrom(
                              onPrimary: Colors.black, // 텍스트 색상
                              backgroundColor:const Color.fromRGBO(186, 225, 98, 1),
                              minimumSize:
                                  Size(double.infinity, 50), // 버튼의 최소 크기 설정
                            ),
                            child: Text('로그인'),
                          ),
                        ),
                      ],
                    ),
                  )
                ],
              ),
            ),
          ),
        ],
      ),
    )
    );
  }
}