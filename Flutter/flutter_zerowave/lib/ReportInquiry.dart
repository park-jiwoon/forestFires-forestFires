import 'package:flutter/material.dart';
import 'package:flutter_zerowave/home.dart';
import 'dart:io';
import 'firealert.dart';
import 'package:flutter_zerowave/style/login_style.dart';
//import 'package:permission_handler/permission_handler.dart';
import './style/app_styles.dart';
import 'drawer.dart';

class ReportInquiry extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // 전달받은 데이터를 ReportData 객체로 변환
    final ReportData reportData =
        ModalRoute.of(context)!.settings.arguments as ReportData;
    TextStyle infoTextStyle = TextStyle(
      fontSize: 16,
      fontWeight: FontWeight.bold,
      color: Colors.black,
    );

    TextStyle buttonTextStyle = TextStyle(
      fontSize: 18,
      fontWeight: FontWeight.bold,
      color: Colors.black,
    );

    return Scaffold(
      appBar: AppBar(
        iconTheme: IconThemeData(color: Colors.white),
        backgroundColor: AppColors.darkestBlue,
        title: Text('산불신고', style: appBarTitleTextStyle),
      ),
      endDrawer: CommonDrawer(),
      body: SingleChildScrollView(
        // 스크롤 가능하게 만들기 위해 SingleChildScrollView 추가
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center, // 중앙 정렬
            children: <Widget>[
              Padding(
                padding: const EdgeInsets.all(8.0), // 텍스트에 패딩 추가
                child: Text('HP: ${reportData.hp}',
                    style: infoTextStyle), // 텍스트 스타일 지정
              ),
              Padding(
                padding: const EdgeInsets.all(8.0), // 텍스트에 패딩 추가
                child: Text('GPS: ${reportData.gps}',
                    style: infoTextStyle), // 텍스트 스타일 지정
              ),
              // Padding(
              //   padding: const EdgeInsets.all(8.0),
              //   child: Text('신고일: ${reportData.adate}', style: infoTextStyle),
              // ),
              reportData.img.isNotEmpty
                  ? Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Container(
                        decoration: BoxDecoration(
                          border: Border.all(color: Colors.white),
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: ClipRRect(
                          borderRadius: BorderRadius.circular(8),
                          child: Image.file(
                            File(reportData.img),
                            fit: BoxFit.cover,
                            width: MediaQuery.of(context).size.width,
                          ),
                        ),
                      ),
                    )
                  : Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Text('No image selected.', style: infoTextStyle),
                    ),
              ElevatedButton(
                onPressed: () {
                  Navigator.push(context, MaterialPageRoute(builder: (context) => Home()));
                },
                style: ElevatedButton.styleFrom(
                  onPrimary: Colors.black,
                  backgroundColor: const Color.fromRGBO(186, 225, 98, 1),
                  minimumSize: Size(double.infinity, 50),
                ),
                child: Text('확 인', style: buttonTextStyle),
              ),
            ],
          ),
        ),
      ),
    );
  }
}