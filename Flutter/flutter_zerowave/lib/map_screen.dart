import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:location/location.dart' hide PermissionStatus;
import 'dart:async';
import 'package:geocoding/geocoding.dart' hide Location;
import 'package:permission_handler/permission_handler.dart';

// 지도를 표시하는 위젯
class MapScreen extends StatefulWidget {
  final LocationData? location;

  MapScreen({Key? key, this.location}) : super(key: key);
  @override
  _MapScreenState createState() => _MapScreenState();
}

// 지도 위에 사용자의 현재 위치를 표시하는 기능
// 구글맵 API
// 현재 위치는 location으로 받아옴
class _MapScreenState extends State<MapScreen> {
  final Completer<GoogleMapController> _controller = Completer();
  final Location _location = Location();
  bool _cameraPermissionGranted = false;
  bool _locationPermissionGranted = false;
  

// 지도가 켜지면 실행
  @override
  void initState() {
    super.initState();
    _currentLocation();
  }

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

// 지도의 카메라위치를 현재위치로 이동
  void _currentLocation() async {
    //맵 생성 초기화 기능
    final GoogleMapController controller = await _controller.future;
    final currentLocation = await _location.getLocation();

    controller.animateCamera(CameraUpdate.newCameraPosition(
      // 지도의 중심 위치, 줌 레벨, 기울기 각도 및 방향을 변경 가능
      CameraPosition(
        // 지도의 중심점을 현재 위도, 경도로 조정
        target: LatLng(currentLocation.latitude!, currentLocation.longitude!),
        zoom: 18.0,
      ),
    ));
  }

// UI
  @override
  Widget build(BuildContext context) {
    LatLng _initialCameraPosition =
        LatLng(37.50508097213444, 126.95493073306663);

    return Scaffold(
      // 앱을 겹쳐 사용하기 위한 Stack(지도 + 마커 + 버튼)
      body: Stack(
        children: [
          GoogleMap(
            mapType: MapType.normal,
            initialCameraPosition: CameraPosition(
              // 지도 켰을 시 처음으로 표시될 위치
              target: _initialCameraPosition,
              zoom: 18,
            ),
            onMapCreated: (GoogleMapController controller) {
              _controller.complete(controller);
            },
          ),
          Center(
            // `MyMarker`를 지도 중앙에 배치
            child: MyMarker(
              position: _initialCameraPosition,
            ),
          ),
          Positioned(
            right: 7,
            bottom: 100,
            child: FloatingActionButton(
              // 현재위치로 돌아가는 버튼
              mini: true,
              onPressed: _currentLocation,
              child: Icon(Icons.location_searching),
            ),
          ),
        ],
      ),
    );
  }
}

// MyMarker 커스텀 위젯 구현
// 지도에서 특정위치를 탭할 시 해당 위치의 위도, 경도를 가져오는 기능
class MyMarker extends StatelessWidget {
  // 지도상에서 위젯이 표시될 위치 설정
  final LatLng position;

  // 인스턴스 시 position을 받도록
  // Tab 시 위치정보 보낼 수 있도록 상호작용
  const MyMarker({Key? key, required this.position}) : super(key: key);

  // 전달받은 위도와 경도로 마커 위치 정보 조회
  void _getAddressFromLatLng(LatLng position) async {
    try {
      List<Placemark> placemarks = await placemarkFromCoordinates(
        position.latitude,
        position.longitude,
      );

      if (placemarks.isNotEmpty) {
        Placemark place = placemarks.first;
        print(
            "해당 위치는 ${place.locality}, ${place.postalCode}, ${place.country}");
        // 여기서 UI 업데이트나 다이얼로그 표시 등을 수행할 수 있습니다.
      }
    } catch (e) {
      print("Error occurred: $e");
    }
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        // 탭 이벤트 발생 시, 역지오코딩을 수행하여 주소 정보를 가져옵니다.
        _getAddressFromLatLng(position);
      },
      child: Container(
        child: Icon(Icons.location_on, size: 40, color: Colors.red),
      ),
    );
  }
}
