// CameraModalContent.dart

import 'package:flutter/material.dart';
import 'package:camera/camera.dart';
import './style/app_styles.dart';
import 'CameraScreen.dart';
import 'GalleryImageScreen.dart';
import 'package:image_picker/image_picker.dart';

class CameraModalContent extends StatelessWidget {
  CameraModalContent({Key? key}) : super(key: key);

  // 사진 선택 기능
  Future<void> _pickImageFromGallery(BuildContext context) async {
    final ImagePicker picker = ImagePicker();
    final XFile? image = await picker.pickImage(source: ImageSource.gallery);

    if (image != null) {
      Navigator.push(
        context,
        MaterialPageRoute(
          builder: (context) => GalleryImageScreen(
            imagePath: image.path,
            onRetry: () {
              _pickImageFromGallery(context);
            },
          ),
        ),
      );
    }
  }

  // 사진 촬영 기능
  Future<void> _takePicture(BuildContext context) async {
    // 사용 가능한 카메라 목록을 얻습니다.
    final cameras = await availableCameras();
    // 특정 카메라를 선택합니다. 예: 후면 카메라
    final firstCamera = cameras.first;

    Navigator.of(context).push(
      MaterialPageRoute(
        builder: (_) => CameraScreen(camera: firstCamera),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: Colors.white,
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children: <Widget>[
          // 사진 촬영 버튼
          _buildActionColumn(
            context,
            Icons.photo_camera,
            '사진 촬영',
            Colors.blue,
            () => _takePicture(context),
          ),
          // 사진 선택 버튼
          _buildActionColumn(
            context,
            Icons.photo_library,
            '사진 선택',
            Colors.red,
            () => _pickImageFromGallery(context),
          ),
        ],
      ),
    );
  }

  Widget _buildActionColumn(BuildContext context, IconData icon, String label,
      Color color, VoidCallback onTap) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: <Widget>[
        CircleAvatar(
          radius: 30,
          backgroundColor: color,
          child: IconButton(
            icon: Icon(icon, color: Colors.white, size: 30),
            onPressed: onTap,
          ),
        ),
        Padding(
          padding: const EdgeInsets.only(top: 6.0),
          child: Text(label, style: CameraModalContentText),
        ),
      ],
    );
  }
}

