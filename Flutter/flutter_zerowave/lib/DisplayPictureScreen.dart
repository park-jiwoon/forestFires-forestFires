import 'dart:io';
import 'package:flutter/material.dart';

class DisplayPictureScreen extends StatelessWidget {
  final String imagePath;

  const DisplayPictureScreen({Key? key, required this.imagePath}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('촬영한 사진 확인')),
      body: Column(
        children: [
          Expanded(
            child: Image.file(File(imagePath)),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              ElevatedButton(
                onPressed: () {
                  // 사용자가 '다시 시도'를 선택하면 카메라 화면으로 돌아갑니다.
                  Navigator.pop(context);
                },
                child: Text('다시 시도'),
              ),
              ElevatedButton(
                onPressed: () {
                  // '확인'을 선택했을 때의 로직을 구현합니다.
                  // firealert.dart로 촬영한 이미지의 경로를 전달합니다.
                  Navigator.pushNamed(context, '/firealert', arguments: imagePath);
                },
                child: Text('확인'),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
