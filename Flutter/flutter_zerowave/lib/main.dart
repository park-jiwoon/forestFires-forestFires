import 'package:flutter/material.dart';
import 'package:flutter_zerowave/nonmember.dart';
import 'home.dart'; // 수정된 임포트
import 'login.dart'; // 수정된 임포트
import 'signup.dart'; // 수정된 임포트
import 'firealert.dart';
import 'utils/helpers/snackbar_helper.dart';
// import 'utils/helpers/navigation_helper.dart';
import 'values/app_theme.dart';


void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'My App',
      theme: AppTheme.themeData,
      initialRoute: '/',
      scaffoldMessengerKey: SnackbarHelper.key,
      routes: {
        '/' :(context) => Home(),
        '/login':(context) => Login(), 
        '/signup':(context) => SignUp(),
        '/firealert':(context) => Firealert(),
        '/nomember':(context) => NoMember(),

      },
    );
  }
}