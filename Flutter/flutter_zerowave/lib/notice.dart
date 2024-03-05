import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'drawer.dart';
import 'noticeDetail.dart';


class Notice {
  final int num;
  final String title;
  final String post;
  final int hit;
  final String imgurl;
  final String createDate;
  final String modifyDate;

  Notice({
    required this.num,
    required this.title,
    required this.post,
    required this.hit,
    required this.imgurl,
    required this.createDate,
    required this.modifyDate,
  });

  factory Notice.fromJson(Map<String, dynamic> json) {
    return Notice(
      num: json['num'],
      title: json['title'],
      post: json['post'],
      hit: json['hit'],
      imgurl: json['imgurl'] ?? '',
      createDate: json['createDate'],
      modifyDate: json['modifyDate'] ?? '',
    );
  }
}

Future<List<Notice>> fetchNotices() async {
  final response = await http.get(Uri.parse('http://192.168.41.202:8081/api/notice'));

  if (response.statusCode == 200) {
    List<dynamic> jsonResponse = json.decode(response.body);
    return jsonResponse.map((notice) => Notice.fromJson(notice)).toList();
  } else {
    throw Exception('Failed to load notices');
  }
}

class NoticesPage extends StatefulWidget {
  @override
  _NoticesPageState createState() => _NoticesPageState();
}

class _NoticesPageState extends State<NoticesPage> {
  late Future<List<Notice>> futureNotices;

  @override
  void initState() {
    super.initState();
    futureNotices = fetchNotices();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
          title: Text("공지사항",
          style: TextStyle(color: Colors.white),),
          iconTheme: IconThemeData(color: Colors.white),
          backgroundColor: Color(0xff0C1C2E),
        ),
        endDrawer: CommonDrawer(),
      body: FutureBuilder<List<Notice>>(
        future: futureNotices,
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            return ListView.separated(
              itemCount: snapshot.data!.length,
              itemBuilder: (context, index) {
                Notice notice = snapshot.data![index];
                return ListTile(
                  contentPadding: EdgeInsets.symmetric(horizontal: 20.0, vertical: 0.0),
                  title: Text("제목 : ${notice.title}", style: TextStyle(fontSize: 20)),
                  subtitle: Text('게시일: ${notice.createDate}\n'),
                  trailing: Text("조회수:${notice.hit}\n글번호:${notice.num}"),
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => NoticeDetail(notice: notice),
                      ),
                    );
                  },
                );
              },
              separatorBuilder: (context, index) => Divider(), // 여기에 Divider 추가
            );
          } else if (snapshot.hasError) {
            return Text("${snapshot.error}");
          }
          return CircularProgressIndicator();
        },
      ),
    );
  }
}