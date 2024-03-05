import 'dart:io';
import 'package:flutter/material.dart';

class GalleryImageScreen extends StatelessWidget {
  final String imagePath;
  final VoidCallback onRetry; // '다시 시도' 콜백 추가

  const GalleryImageScreen({
    Key? key,
    required this.imagePath,
    required this.onRetry, // 생성자를 통해 onRetry를 전달받습니다.
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('선택한 사진 확인')),
      body: Column(
        children: [
          Expanded(
            child: Image.file(File(imagePath)),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              ElevatedButton(
                onPressed: onRetry, // '다시 시도' 버튼의 onPressed에 onRetry 콜백 연결
                child: Text('다시 시도'),
              ),
              ElevatedButton(
                onPressed: () {
                  // '확인'을 선택했을 때의 로직을 구현합니다.
                  // '/firealert' 라우트로 이미지 경로를 전달합니다.
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
