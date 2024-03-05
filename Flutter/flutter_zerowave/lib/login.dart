import 'package:flutter/material.dart';
import 'package:flutter_zerowave/signup.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import '../values/app_theme.dart';
import '../utils/common_widgets/gradient_background.dart';
import 'drawer.dart';
// import '../utils/helpers/navigation_helper.dart';
// import '../components/app_text_form_field.dart';

class Login extends StatelessWidget {
  // 240213 수정 _usernameController -> _userhpController 변경
  final TextEditingController _userhpController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  final ValueNotifier<bool> passwordNotifier = ValueNotifier<bool>(true);
  final ValueNotifier<bool> fieldValidNotifier = ValueNotifier<bool>(true);

  Future<void> _login(BuildContext context) async {
    if (_userhpController.text.isEmpty) {
      // 240213 수정
      _showError(context, '연락처는 필수 입력 값입니다.');
      return;
    }
    if (_passwordController.text.isEmpty) {
      _showError(context, '비밀번호는 필수 입력 값입니다.');
      return;
    }
    try {
      final response = await http.post(
        //Uri.parse('http://192.168.41.180:8081/api/login'),
        Uri.parse('http://192.168.41.202:8081/api/login'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'hp': _userhpController.text,
          'password': _passwordController.text,
        }),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        await _saveToken(data['token']);
        Navigator.pushReplacementNamed(context, '/');
      } else {
        // var body = utf8.decode(response.bodyBytes);
        final responseBody = jsonDecode(response.body);
        final String errorMessage =
            responseBody['error'] ?? '로그인에 실패했습니다. 아이디나 비밀번호를 확인해주세요.';
        _showError(context, errorMessage);
      }
    } catch (e) {
      _showError(context, '서버와의 통신 중 문제가 발생했습니다.');
    }
  }

  Future<void> _saveToken(String token) async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.setString('jwt_token', token);
    //await prefs.setString('userHp', _userhpController.text);
    String userHp = _userhpController.text; // 사용자가 입력한 전화번호
    await prefs.setString('userHp', userHp); // 'userHp'로 저장
  }

  void _showError(BuildContext context, String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(message)),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text("로그인 페이지",
          style: TextStyle(color: Colors.white),),
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
                    "   로그인",
                    style: AppTheme.titleLarge,
                  ),
                  SizedBox(height: 3),
                  Text("       아이디와 비밀번호를 입력해 주세요", style: AppTheme.bodySmall),
                  Padding(padding: const EdgeInsets.all(20)),
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
                        controller: _userhpController,
                        decoration: InputDecoration(
                          labelText: "Hp",
                        ),
                        textInputAction: TextInputAction.next,
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            // 240213 수정
                            return "전화번호를 입력해주세요!";
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
                                onPressed: () {
                                  passwordNotifier.value =
                                      !passwordNotifier.value;
                                },
                                icon: Icon(
                                  passwordObscure
                                      ? Icons.visibility_off
                                      : Icons.visibility,
                                ),
                              ),
                            ),
                            textInputAction: TextInputAction.done,
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
                      Center(
                        child: Column(
                          children: [
                            ValueListenableBuilder<bool>(
                              valueListenable: fieldValidNotifier,
                              builder: (context, isValid, __) {
                                // 로그인 버튼
                                return ElevatedButton(
                                  onPressed:
                                      isValid ? () => _login(context) : null,
                                  style: ElevatedButton.styleFrom(
                                    onPrimary: Colors.black, // 텍스트 색상
                                    backgroundColor:
                                        const Color.fromRGBO(186, 225, 98, 1),
                                    minimumSize: Size(
                                        double.infinity, 50), // 버튼의 최소 크기 설정
                                  ),
                                  child: Text('로그인'),
                                );
                              },
                            ),
                            SizedBox(height: 10),
                            Container(
                              width: double.infinity, // 컨테이너를 화면 너비에 맞춤
                              margin: EdgeInsets.symmetric(
                                  horizontal: 00), // 좌우 여백 추가
                              child: ElevatedButton(
                                onPressed: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                        builder: (context) => SignUp()),
                                  );
                                },
                                style: ElevatedButton.styleFrom(
                                  onPrimary: Colors.black, // 텍스트 색상
                                  backgroundColor:
                                      const Color.fromRGBO(186, 225, 98, 1),
                                  minimumSize:
                                      Size(double.infinity, 50), // 버튼의 최소 크기 설정
                                ),
                                child: Text('회원가입'),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ));
  }
}
