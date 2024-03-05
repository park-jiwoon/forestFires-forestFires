import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';

import 'package:flutter_zerowave/style/login_style.dart';
import 'package:flutter_zerowave/style/app_styles.dart';

import 'drawer.dart';
import 'ReportDetailPage.dart';

class FireReport {
  final int num;
  final String hp;
  final String imgurl;
  final String gps;
  final String adate;
  final String progress;

  FireReport({
    required this.num,
    required this.hp,
    required this.imgurl,
    required this.gps,
    required this.adate,
    required this.progress,
  });

  factory FireReport.fromJson(Map<String, dynamic> json) {
    return FireReport(
      num: json['num'],
      hp: json['hp'],
      imgurl: json['imgurl'],
      gps: json['gps'],
      adate: json['adate'],
      progress: json['progress'] ?? '',
    );
  }
}

class Report extends StatefulWidget {
  @override
  _ReportState createState() => _ReportState();
}

class _ReportState extends State<Report> {
  late Future<List<FireReport>> futureReports;

  @override
  void initState() {
    super.initState();
    futureReports = fetchReports();
  }

  Future<List<FireReport>> fetchReports() async {
    final prefs = await SharedPreferences.getInstance();
    String? jwtToken = prefs.getString('jwt_token');

    final response = await http.get(
      Uri.parse('http://192.168.41.202:8081/api/fireReception/user'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $jwtToken',
      },
    );

    if (response.statusCode == 200) {
      List jsonResponse = json.decode(response.body);
      return jsonResponse.map((report) => FireReport.fromJson(report)).toList();
    } else {
      throw Exception(
          'Failed to load reports from server. Status code: ${response.statusCode}');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        iconTheme: IconThemeData(color: Colors.white),
        backgroundColor: AppColors.darkestBlue,
        title: Text('산불신고', style: appBarTitleTextStyle),
        leading: IconButton(
          icon: Icon(Icons.arrow_back, color: appBarIconColor),
          onPressed: () => Navigator.of(context).pop(),
        ),
      ),
      endDrawer: CommonDrawer(),
      body: Center(
        child: FutureBuilder<List<FireReport>>(
          future: futureReports,
          builder: (context, snapshot) {
            if (snapshot.hasData) {
              List<FireReport> reports = snapshot.data!;
              return ListView.builder(
                itemCount: reports.length,
                itemBuilder: (context, index) {
                  var report = snapshot.data![index];

                  return cardCustom(report, context);
                },
              );
            } else if (snapshot.hasError) {
              return Text("${snapshot.error}");
            }
            return CircularProgressIndicator();
          },
        ),
      ),
    );
  }

  Widget cardCustom(FireReport report, BuildContext context) {
  return Card(
    color: Colors.white,
    shape: RoundedRectangleBorder(
      borderRadius: BorderRadius.circular(8),
    ),
    elevation: 10,
    child: Padding(
      padding: EdgeInsets.all(8.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text("신고 글 번호 : ${report.num}", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
          SizedBox(height: 8),
          Text("신고일 : ${report.adate}", style: TextStyle(fontSize: 14)),
          SizedBox(height: 8),
          // 이미지를 맨 아래에 배치
          Align(
            alignment: Alignment.center, // 이미지를 중앙에 위치시킴
            child: report.imgurl.isNotEmpty
                ? Image.network(
                    'http://192.168.41.202:8081/api/fireReception/images/${Uri.encodeComponent(report.imgurl)}',
                    width: double.infinity, // 컨테이너 너비에 맞춤
                    fit: BoxFit.cover,
                  )
                : Icon(Icons.image_not_supported, size: 80),
          ),
          // 탭 이벤트 추가
          GestureDetector(
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => ReportDetailPage(report: report),
                ),
              );
            },
            child: Container(
              alignment: Alignment.center,
              padding: EdgeInsets.symmetric(vertical: 10),
              child: Text('자세히 보기', style: TextStyle(color: Colors.blue)),
            ),
          ),
        ],
      ),
    ),
  );
}

}