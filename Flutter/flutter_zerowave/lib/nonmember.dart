import 'package:flutter/material.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

import 'firealert.dart';
import '../values/app_theme.dart';
import '../utils/common_widgets/gradient_background.dart';
import 'drawer.dart';
class NoMember extends StatefulWidget {
  @override
  _NoMemberState createState() => _NoMemberState();
}

class _NoMemberState extends State<NoMember> {
  final TextEditingController _hpController = TextEditingController();
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  Future<void> _loginAsGuest() async {
    final String hp = _hpController.text; // 검증
    if (hp.isEmpty) {
      _showMessage('전화번호를 입력해주세요.');
      return;
    }

    try {
      // 이 부분은 서버에 비회원 로그인 정보를 보내는 예시입니다.
      // 실제 URL과 요청 파라미터는 서버 API 사양에 맞게 조정해야 합니다.
      final response = await http.post(
        Uri.parse('http://192.168.41.202:8081/api/guest/login'),
        //Uri.parse('https://192.168.41.180/api/guest/login'),
        body: json.encode({'hp': hp}),
        headers: {'Content-Type': 'application/json'},
      );
      

      if (response.statusCode == 200 && mounted) {
        final data = json.decode(response.body);
        final String token = data['token']; // 서버에서 받은 토큰
        final prefs = await SharedPreferences.getInstance();

        // 토큰과 전화번호를 로컬 저장소에 저장
        await prefs.setString('guest_token', token); 
        await prefs.setString('guestHp', hp); // 전화번호 저장

        Navigator.pushReplacement(
          context,
          MaterialPageRoute(builder: (context) => Firealert()), // 성공시 신고페이지로 이동
        ); // pushReplacement 쓰는 이유는 뒤로가기 했을 시 로그인페이지로 이동시키지 않기 위해 뒤로가기 스택을 지우기 위함임
      } else if (mounted) { // mounted 검사 추가
        _showMessage('로그인에 실패했습니다.');
      }
    } catch (e) {
      _showMessage('로그인 중 오류가 발생했습니다.');
    }
  }

  void _showMessage(String message) {
    if (mounted) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(message)),
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
            child: ListView(padding: EdgeInsets.zero, children: [
              const GradientBackground(
                colors: [Color(0xff0C1C2E), Color(0xff0C1C2E)],
                children: [
                  Text(
                    "  비회원 로그인",
                    style: AppTheme.titleLarge,
                  ),
                  SizedBox(height: 3),
                  Text("      전화번호를 입력해 주세요", style: AppTheme.bodySmall),
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
                        controller: _hpController,
                        decoration: InputDecoration(labelText: "전화번호"),
                        keyboardType: TextInputType.phone,
                        textInputAction: TextInputAction.next,
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return "번호를 입력해주세요!";
                          }
                          return null;
                        },
                      ),
                      SizedBox(height: 30),
                      Container(
                        width: double.infinity, // 컨테이너를 화면 너비에 맞춤
                        margin: EdgeInsets.symmetric(horizontal: 0), // 좌우 여백 추가
                        child: ElevatedButton(
                          onPressed:
                              _loginAsGuest, // 여기서 수정이 필요했습니다. ()를 제거하고 함수 참조만 남깁니다.
                          child: Text('비회원으로 로그인'), // '비회원으로 로그인' 텍스트를 버튼에 표시
                          style: ElevatedButton.styleFrom(
                            onPrimary: Colors.black, // 텍스트 색상
                            backgroundColor:
                                const Color.fromRGBO(186, 225, 98, 1),
                            minimumSize:
                                Size(double.infinity, 50), // 버튼의 최소 크기 설정
                          ),
                        ),
                      )
                    ],
                  ),
                ),
              ),
            ])));
  }
}
