import 'package:flutter/material.dart';
import 'report.dart'; // FireReport 클래스를 정의한 파일을 임포트합니다. 실제 경로에 맞게 조정해야 합니다.

import 'package:flutter_zerowave/style/login_style.dart';
import 'package:flutter_zerowave/style/app_styles.dart';
import 'drawer.dart';

class ReportDetailPage extends StatelessWidget {
  final FireReport report;

  ReportDetailPage({required this.report});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        iconTheme: IconThemeData(color: Colors.white),
        backgroundColor: AppColors.darkestBlue,
        title: Text('신고 상세내용', style: appBarTitleTextStyle),
        leading: IconButton(
          icon: Icon(Icons.arrow_back, color: appBarIconColor),
          onPressed: () => Navigator.of(context).pop(),
        ),
      ),
      endDrawer: CommonDrawer(),
      body: SingleChildScrollView( // 스크롤 가능하도록 SingleChildScrollView 사용
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Text('신고 글 번호: ${report.num}', style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
            SizedBox(height: 8),
            Text('신고자 번호: ${report.hp}', style: TextStyle(fontSize: 16)),
            SizedBox(height: 8),
            Text('GPS: ${report.gps}', style: TextStyle(fontSize: 16)),
            SizedBox(height: 8),
            Text('신고일: ${report.adate}', style: TextStyle(fontSize: 16)),
            SizedBox(height: 8),
            Text('Progress: ${report.progress}', style: TextStyle(fontSize: 16)),
            SizedBox(height: 20), // 이미지와 텍스트 사이에 조금 더 공간을 추가
            // 이미지를 여기에 배치하여 맨 아래에 위치하게 함
            report.imgurl.isNotEmpty 
                ? Image.network('http://192.168.41.202:8081/api/fireReception/images/${Uri.encodeComponent(report.imgurl)}', fit: BoxFit.cover) // 이미지 URL이 있을 경우 이미지 표시
                : SizedBox(height: 200, child: Placeholder()), // 이미지 URL이 없을 경우 Placeholder 표시
          ],
        ),
      ),
    );
  }
}

