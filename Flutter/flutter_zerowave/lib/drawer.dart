// common_drawer.dart
import 'package:flutter/material.dart';
import 'package:flutter_zerowave/report.dart';
//import 'package:flutter_zerowave/report.dart';
import 'package:flutter_zerowave/signup.dart';
import 'package:shared_preferences/shared_preferences.dart';
//import 'home.dart';
import './login.dart'; // 회원가입 페이지 경로에 맞게 수정해야 합니다.
// import './style/app_styles.dart'; // 스타일을 정의한 파일 경로에 맞게 수정해야 합니다.
// import './values/app_colors.dart'; // 색상 값을 정의한 파일 경로에 맞게 수정해야 합니다.
import 'mypage.dart';
import 'notice.dart';

class CommonDrawer extends StatefulWidget {
  @override
  _CommonDrawerState createState() => _CommonDrawerState();
}

class _CommonDrawerState extends State<CommonDrawer> {
  bool isLoggedIn = false;

  @override
  void initState() {
    super.initState();
    _checkLoginStatus();
  }

  void _checkLoginStatus() async {
  final prefs = await SharedPreferences.getInstance();
  // jwt_token 또는 guest_token이 있는지 확인
  setState(() {
    isLoggedIn = prefs.getString('jwt_token') != null || prefs.getString('guest_token') != null;
  });
}


  Future<void> _logout() async {
  final prefs = await SharedPreferences.getInstance();
  // jwt_token과 guest_token 모두 제거
  await prefs.remove('jwt_token');
  await prefs.remove('guest_token');
  Navigator.of(context).pushReplacementNamed('/'); // 로그아웃 후 홈 화면으로 리다이렉트
}


  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: <Widget>[
          DrawerHeader(
            decoration: BoxDecoration(
              color: const Color.fromRGBO(186, 225, 98, 1),
            ),
            child: Image.asset(
              'assets/images/logo.png', // 로고 이미지의 경로
              fit: BoxFit.contain, // 이미지가 영역에 맞게 조정되도록 설정
            ),
          ),
          
          ListTile(
            leading: Icon(Icons.home),
            title: Text("Home"),
            onTap: () {
              Navigator.pushNamed(context, '/');
            },
          ),
          ListTile(
            leading: Icon(Icons.search),
            title: Text("신고 조회"),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => Report()),
              );
            },
          ),
          ListTile(
            leading: Icon(Icons.description),
            title: Text("공지사항"),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => NoticesPage()),
              );
            },
          ),
          if (!isLoggedIn)
            ListTile(
              leading: Icon(Icons.login),
              title: Text('로그인'),
              onTap: () {
                Navigator.of(context).push(MaterialPageRoute(
                    builder: (context) => Login())); // 로그인 페이지로 이동
              },
            ),
          if (isLoggedIn)
            ListTile(
              leading: Icon(Icons.logout),
              title: Text('로그아웃'),
              onTap: _logout,
            ),
          // 다른 ListTile 추가...
          if (!isLoggedIn)
            ListTile(
              leading: Icon(Icons.account_circle),
              title: Text('회원가입'),
              onTap: () {
                Navigator.of(context).push(MaterialPageRoute(
                    builder: (context) => SignUp())); // 로그인 페이지로 이동
              },
            ),
          if (isLoggedIn)
            ListTile(
              leading: Icon(Icons.manage_accounts),
              title: Text('마이 페이지'),
              onTap: () {
                Navigator.of(context).push(MaterialPageRoute(
                    builder: (context) => MyPage()));
              },
            ),
        ],
      ),
    );
  }
}
