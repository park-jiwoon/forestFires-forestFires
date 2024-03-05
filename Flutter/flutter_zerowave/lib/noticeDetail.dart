import 'package:flutter/material.dart';
import 'notice.dart'; // Notice 클래스를 정의한 파일을 임포트합니다. 실제 경로에 맞게 조정해야 합니다.

// 필요한 스타일 파일을 임포트합니다. 실제 경로에 맞게 조정해야 합니다.
import 'package:flutter_zerowave/style/login_style.dart';
import 'package:flutter_zerowave/style/app_styles.dart';
import 'drawer.dart'; // 필요한 경우

class NoticeDetail extends StatelessWidget {
  final Notice notice;

  NoticeDetail({required this.notice});

  @override
  Widget build(BuildContext context) {
    final imageUrl = 'http://192.168.41.202:8081/firealert_images/${notice.imgurl}';
    return Scaffold(
      appBar: AppBar(
        iconTheme: IconThemeData(color: Colors.white),
        backgroundColor: AppColors.darkestBlue,
        title: Text(notice.title, style: appBarTitleTextStyle), // 공지사항의 제목을 앱 바에 표시
        leading: IconButton(
          icon: Icon(Icons.arrow_back, color: appBarIconColor),
          onPressed: () => Navigator.of(context).pop(),
        ),
      ),
      endDrawer: CommonDrawer(), // 필요한 경우
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Text(
              notice.title,
              style: TextStyle(fontSize: 28, fontWeight: FontWeight.bold),
            ),
            SizedBox(height: 10),
            Text(
              "조회수 : ${notice.hit}",
              style: TextStyle(fontSize: 12),
            ),
            SizedBox(height: 10),
            Text(
              "게시일 : ${notice.createDate}",
              style: TextStyle(fontSize: 12),
            ),
            Text(
              "수정된 시간 : ${notice.modifyDate}",
              style: TextStyle(fontSize: 12),
            ),
            SizedBox(height: 20),
            Text(
              notice.post, // 공지사항의 내용 표시
              style: TextStyle(fontSize: 18),
            ),
            SizedBox(height: 20),
            notice.imgurl.isNotEmpty
                ? Image.network(imageUrl, fit: BoxFit.cover)
                : SizedBox(height: 200, child: Placeholder()),
            
          ],
        ),
      ),
    );
  }
}
