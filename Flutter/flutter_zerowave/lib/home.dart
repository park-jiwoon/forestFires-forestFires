import 'package:flutter/material.dart';
//import 'package:flutter_zerowave/signup.dart';
import './style/app_styles.dart'; // 스타일을 정의한 파일 임포트
import 'package:shared_preferences/shared_preferences.dart'; // 추가된 임포트
import './values/app_colors.dart';
import 'drawer.dart';
import 'map_screen.dart';
import 'weather_service.dart';
import 'package:http/http.dart' as http;
//import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'dart:convert';
import 'package:location/location.dart';
// import 'nonmember.dart';
import 'notice.dart';
import 'noticeDetail.dart';

class Home extends StatefulWidget {
  @override
  _HomeState createState() => _HomeState();
}

Future<List<Notice>> fetchNotices() async {
  final response =
      await http.get(Uri.parse('http://192.168.41.202:8081/api/notice'));

  if (response.statusCode == 200) {
    List<dynamic> jsonResponse = json.decode(response.body);
    return jsonResponse.map((notice) => Notice.fromJson(notice)).toList();
  } else {
    throw Exception('Failed to load notices');
  }
}

class _HomeState extends State<Home> {
  bool isLoggedIn = false;
  String _weatherInfo = "Loading weather...";
  LocationData? _currentLocation;
  late Future<List<Notice>> futureNotices;

  @override
  void initState() {
    super.initState();
    _checkLoginStatus();
    _getCurrentLocationAndWeather();
    futureNotices = fetchNotices();
  }

  Future<void> _getCurrentLocationAndWeather() async {
    Location location = Location();
    bool _serviceEnabled;
    PermissionStatus _permissionGranted;
    //LocationData _locationData;

    _serviceEnabled = await location.serviceEnabled();
    if (!_serviceEnabled) {
      _serviceEnabled = await location.requestService();
      if (!_serviceEnabled) return;
    }

    _permissionGranted = await location.hasPermission();
    if (_permissionGranted == PermissionStatus.denied) {
      _permissionGranted = await location.requestPermission();
      if (_permissionGranted != PermissionStatus.granted) return;
    }

    final _locationData = await location.getLocation();
    setState(() {
      _currentLocation = _locationData;
    });

    WeatherService.fetchWeather(
            _locationData.latitude!, _locationData.longitude!)
        .then((data) {
      setState(() {
        _weatherInfo = "온도: ${data['main']['temp']}°C\n"
            "습도: ${data['main']['humidity']}%\n"
            "강수량: ${data.containsKey('rain') ? data['rain']['1h'].toString() + ' mm' : 'No rain'}\n"
            "풍속: ${data['wind']['speed']} m/s"; // 풍속 추가
      });
    }).catchError((error) {
      setState(() {
        _weatherInfo = "Failed to load weather: $error";
      });
    });
  }

  void _checkLoginStatus() async {
    // async 키워드 추가
    final prefs = await SharedPreferences.getInstance();
    setState(() {
      isLoggedIn = prefs.getString('jwt_token') != null;
    });
  }

  Future<void> _logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('jwt_token');
    setState(() {
      isLoggedIn = false;
    });
    Navigator.pushReplacementNamed(context, '/'); // 사용자를 홈 화면으로 리다이렉트
  }

  Future<List<Notice>> fetchNotices() async {
    final response =
        await http.get(Uri.parse('http://192.168.41.202:8081/api/notice'));
    if (response.statusCode == 200) {
      List jsonResponse = json.decode(response.body);
      return jsonResponse.map((data) => Notice.fromJson(data)).toList();
    } else {
      throw Exception('Failed to load notices from server');
    }
  }

  Widget _buildNoticeSummarySection() {
    return FutureBuilder<List<Notice>>(
      future: futureNotices,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return CircularProgressIndicator();
        } else if (snapshot.hasError) {
          return Text("Error: ${snapshot.error}");
        } else if (snapshot.hasData) {
          List<Notice> notices = snapshot.data!.take(3).toList();
          return Column(
            children: notices
                .map((notice) => ListTile(
                      title: Text(notice.title),
                      subtitle: Text('게시일: ${notice.createDate}'),
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => NoticeDetail(notice: notice),
                          ),
                        );
                      },
                    ))
                .toList(),
          );
        } else {
          return Text("No notices found");
        }
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        iconTheme: IconThemeData(color: Colors.white),
        backgroundColor: AppColors.darkestBlue,
        title: Text('Home', style: appBarTitleTextStyle),
        centerTitle: true,
      ),
      endDrawer: CommonDrawer(),
      body: SingleChildScrollView(
        child: Column(
          children: <Widget>[
            Container(
              color: bannerBackgroundColor,
              alignment: Alignment.center,
              child: Text(
                '희망과 미래를 태우는 산불! 우리 힘으로 막아요!',
                style: bannerTextStyle,
                textAlign: TextAlign.center,
              ),
            ),
            SizedBox(height: 20),
            // 날씨 정보 표시 부분에 스타일 적용
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                _weatherInfo,
                style: TextStyle(fontSize: 20),
                textAlign: TextAlign.center,
              ),
            ),
            SizedBox(height: 20),
            _currentLocation == null
                ? CircularProgressIndicator()
                : Container(
                    height: 300, // 구글 맵 표시 영역 높이 설정
                    child: MapScreen(location: _currentLocation),
                  ),
            SizedBox(height: 20),
            _buildNoticeSummarySection(),
          ],
        ),
      ),
      bottomNavigationBar: Padding(
        padding: const EdgeInsets.fromLTRB(15, 10, 15, 20),
        child: ElevatedButton(
          style: ElevatedButton.styleFrom(
            primary: AppColors.primaryColor,
            minimumSize: Size(double.infinity, bottomButtonHeight),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(bottomButtonBorderRadius),
            ),
          ),
          onPressed: () {
            if (isLoggedIn) {
              // 로그인 한 회원은 '산불 신고하기' 페이지로 진행
              Navigator.pushNamed(context, '/firealert');
            } else {
              // 로그인하지 않은 사용자는 'nomember' 페이지로 이동
              Navigator.pushNamed(context, '/nomember');
              //Navigator.pushNamed(context, '/firealert');
            }
          },
          child: Text('산불 신고하기', style: bottomButtonTextStyle),
        ),
      ),
    );
  }
}
