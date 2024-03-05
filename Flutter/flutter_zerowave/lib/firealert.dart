import 'package:flutter/material.dart';
import 'package:flutter_zerowave/style/login_style.dart';
import 'package:permission_handler/permission_handler.dart';
import './style/app_styles.dart';
import 'camera_screen.dart'; // 카메라 화면 import
import 'map_screen.dart'; // 지도 화면 import
import 'package:location/location.dart' hide PermissionStatus;
import 'dart:async';
// import 'package:google_maps_flutter/google_maps_flutter.dart';

// 240214 jwt 관련 잠시 주석
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart'; // 추가된 임포트
//import 'dart:convert';
import 'ReportInquiry.dart';
import 'drawer.dart';

class Firealert extends StatefulWidget {
  @override
  _FirealertState createState() => _FirealertState();
}

// 권한상태 확인을 위한 위젯
class _FirealertState extends State<Firealert> with WidgetsBindingObserver {
  bool _cameraPermissionGranted = false;
  bool _locationPermissionGranted = false;
  final Location _location = Location();
  String? imagePath; // imagePath 상태 추가
  String? hpToUse;

  // 위젯 초기화 시 호출되며, 앱 생명주기 이벤트를 수신합니다.
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
    initializeHp();
    // 240214 jwt 관련 잠시 주석
    // requestPermission().then((_) => fetchContactInfoIfNeeded());
    
  }


  void initializeHp() async {
    final prefs = await SharedPreferences.getInstance();
    String? jwtToken = prefs.getString('jwt_token');
    String? userHp = prefs.getString('userHp');
    String? guestHp = prefs.getString('guestHp');
    setState(() {
      hpToUse = jwtToken != null ? userHp : guestHp; // 초기화
    });
  }

  @override
  void dispose() {
    WidgetsBinding.instance.removeObserver(this);
    super.dispose();
  }

  // 위젯이 메모리에서 제거될 때 호출되며, 앱 생명주기 이벤트 수신을 중지합니다.
  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    if (state == AppLifecycleState.resumed) {
      // 앱이 다시 활성화될 때 권한 상태 확인
      checkPermission();
    }
  }

  // 사용자에게 카메라 및 위치 접근 권한을 요청하는 함수
  Future<void> requestPermission() async {
    final Map<Permission, PermissionStatus> statuses = await [
      Permission.camera,
      Permission.location,
    ].request();

    setState(() {
      _cameraPermissionGranted =
          statuses[Permission.camera]?.isGranted ?? false;
      _locationPermissionGranted =
          statuses[Permission.location]?.isGranted ?? false;
    });

    // 권한이 거부된 경우, 안내 메시지를 표시
    if (!_cameraPermissionGranted || !_locationPermissionGranted) {
      showPermissionErrorDialog();
    }
  }

  // 현재 권한 상태를 확인하고 필요시 안내 메시지를 표시합니다.
  void checkPermission() async {
    final cameraStatus = await Permission.camera.status;
    final locationStatus = await Permission.location.status;

    setState(() {
      _cameraPermissionGranted = cameraStatus.isGranted;
      _locationPermissionGranted = locationStatus.isGranted;
    });

    // 권한이 거부된 경우, 안내 메시지를 표시
    if (!_cameraPermissionGranted || !_locationPermissionGranted) {
      showPermissionErrorDialog();
    }
  }

  // 권한 거부 시 안내 메시지를 표시하는 함수입니다.
  void showPermissionErrorDialog() {
    showDialog(
      context: context,
      builder: (BuildContext context) => AlertDialog(
        title: Text('Permissions error'),
        content: Text(
            'Camera and location permissions are required for this app to function properly.'),
        actions: <Widget>[
          TextButton(
            child: Text('Ok'),
            onPressed: () => Navigator.of(context).pop(),
          ),
        ],
      ),
    );
  }

  void _uploadReport(String? imagePath) async {
    // 현재 사용자의 JWT 토큰을 가져오는 로직이 필요합니다.
    // 예를 들어, SharedPreferences를 사용해 저장된 JWT 토큰을 가져옵니다.
    final prefs = await SharedPreferences.getInstance();
    String? jwtToken = prefs.getString('jwt_token');
    String? guestToken = prefs.getString('guest_token');
    // 사용할 토큰을 결정합니다. 회원 토큰이 있으면 그것을, 없으면 비회원 토큰을 사용합니다.
    String? tokenToUse = jwtToken ?? guestToken;

    // 사용자의 전화번호를 결정합니다. 회원이면 회원 전화번호, 비회원이면 비회원 전화번호를 사용합니다.
    String? userHp = prefs.getString('userHp');
    String? guestHp = prefs.getString('guestHp');
    String? hpToUse = jwtToken != null ? userHp : guestHp;
    //final String? userHp = prefs.getString('hp');
    if (tokenToUse == null || hpToUse == null) {
      print('No token or hp available');
      return; // 토큰이나 전화번호가 없으면 함수를 종료합니다.
    }

    // MultipartRequest를 생성하여 파일과 필요한 정보를 함께 보냅니다.
    //var request = http.MultipartRequest('POST', Uri.parse('http://192.168.41.180:8081/api/upload'));
    var request = http.MultipartRequest('POST',
        Uri.parse('http://192.168.41.202:8081/api/fireReception/upload'));
    request.headers.addAll({
      'Authorization': 'Bearer $tokenToUse', // JWT 토큰을 요청 헤더에 추가합니다.
    });

    // 이미지 파일이 있다면 첨부합니다.
    if (imagePath != null) {
      request.files.add(await http.MultipartFile.fromPath('image', imagePath));
    }

    // GPS 정보와 사용자의 hp를 요청에 추가합니다.
    // 이 예시에서는 이미지 파일과 함께 서버로 전송합니다.
    final currentLocation = await _location.getLocation();
    request.fields['gps'] =
        '${currentLocation.latitude},${currentLocation.longitude}';
    request.fields['hp'] = hpToUse; // 사용자의 hp는 서버 측에서 JWT 토큰을 검증하여 얻습니다.

    // 서버로 요청을 보냅니다.
    var response = await request.send();

    if (response.statusCode == 200) {
      print('Upload successful');
    } else {
      print('Upload failed');
    }
  }

  void navigateToReportInquiry(ReportData reportData) async {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => ReportInquiry(),
        settings: RouteSettings(
          arguments: reportData,
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    List<Widget> permissionStatusWidgets = [];

    // 카메라 권한 상태를 확인하여 위젯 리스트에 추가합니다.
    if (_cameraPermissionGranted) {
      permissionStatusWidgets.add(Text('Camera permission granted.'));
    } else {
      permissionStatusWidgets.add(Text('Camera permission not granted.'));
    }

    // 위치 권한 상태를 확인하여 위젯 리스트에 추가합니다.
    if (_locationPermissionGranted) {
      permissionStatusWidgets.add(Text('Location permission granted.'));
    } else {
      permissionStatusWidgets.add(Text('Location permission not granted.'));
    }
    print(permissionStatusWidgets);

    // 현재 페이지 정보.전달받은 데이터.데이터가 문자열임
    final args = ModalRoute.of(context)?.settings.arguments as String?;

    return Scaffold(
      appBar: AppBar(
        iconTheme: IconThemeData(color: Colors.white),
        backgroundColor: AppColors.darkestBlue,
        title: Text('산불신고', style: appBarTitleTextStyle),
        leading: IconButton(
          icon: Icon(Icons.arrow_back), // 뒤로가기 아이콘 지정
          color: appBarIconColor, // 아이콘 색상을 하얀색으로 변경
          onPressed: () => Navigator.of(context).pop(), // 뒤로가기 기능 유지
        ),
      ),
      endDrawer: CommonDrawer(),
      // 240214 수정
      body: SingleChildScrollView(
        child: Column(
          mainAxisSize: MainAxisSize.min, // 필요한 만큼의 크기만 사용하도록 설정
          children: [
            Container(
              height: MediaQuery.of(context).size.height *
                  0.5, // 지도 화면 높이를 전체 높이의 절반으로 설정
              child: MapScreen(),
            ),
            Container(
              height: MediaQuery.of(context).size.height *
                  0.3, // 카메라 화면 높이를 전체 높이의 0.3배로 설정
              child: CameraScreen(imagePath: args),
            ),
            Container(
              width: double.infinity, // 컨테이너를 화면 너비에 맞춤
              margin: EdgeInsets.symmetric(horizontal: 00), // 좌우 여백 추가
              child: ElevatedButton(
                onPressed: () async {
                  final currentLocation =
                      await _location.getLocation(); // 현재 위치
                  String? capturedImagePath = args; // 캡처한 이미지 경로

                  print(
                      '현재 위치: ${currentLocation.latitude}, ${currentLocation.longitude}');
                  print('캡처한 이미지 경로: $capturedImagePath');
                  // 신고하기 버튼을 누르면 _uploadReport 함수를 호출합니다.
                  _uploadReport(args);
                  // Navigator.push(context, MaterialPageRoute(builder: (context) => ReportInquiry()));

                  navigateToReportInquiry(ReportData(
                    hp: hpToUse ?? "", // 실제 앱에 맞게 수정
                    gps:
                        "${currentLocation.latitude},${currentLocation.longitude}",
                    img: imagePath ?? "${args}",
                  ));
                },
                style: ElevatedButton.styleFrom(
                  onPrimary: Colors.black, // 텍스트 색상
                  backgroundColor: const Color.fromRGBO(186, 225, 98, 1),
                  minimumSize: Size(double.infinity, 50),
                ),
                child: Text('신고하기'),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class ReportData {
  final String hp;
  final String gps;
  final String img;

  ReportData({required this.hp, required this.gps, required this.img});
}
