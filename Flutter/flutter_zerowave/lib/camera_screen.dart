import 'dart:io';
import 'package:flutter/material.dart';
import 'package:dotted_border/dotted_border.dart';
import './style/app_styles.dart';
import 'CameraModalContent.dart';

class CameraScreen extends StatefulWidget {
  final String? imagePath; // Optional imagePath for conditional rendering

  const CameraScreen({Key? key, this.imagePath}) : super(key: key);

  @override
  _CameraScreenState createState() => _CameraScreenState();
}

class _CameraScreenState extends State<CameraScreen> {
  @override
  Widget build(BuildContext context) {
    print('CameraScreen imagePath: ${widget.imagePath}');
    if (widget.imagePath != null) {
      print('Displaying image from path: ${widget.imagePath}');
    } else {
      print('No imagePath provided, displaying default icon');
    }

    return Center(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Padding(
            padding: const EdgeInsets.fromLTRB(0, 15, 0, 20),
            child: Text(
              '첨부할 이미지를 선택해주세요',
              style: cameraScreenText,
            ),
          ),
          Padding(
            padding: const EdgeInsets.fromLTRB(16, 0, 16, 16),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: <Widget>[
                Expanded(
                  child: InkWell(
                    onTap: () {
                      showModalBottomSheet(
                        context: context,
                        builder: (BuildContext context) {
                          return CameraModalContent();
                        },
                      );
                    },
                    child: DottedBorder(
                      borderType: BorderType.RRect,
                      radius: Radius.circular(12),
                      padding: EdgeInsets.all(6),
                      dashPattern: [8, 4],
                      child: ClipRRect(
                        borderRadius: BorderRadius.all(Radius.circular(12)),
                        child: widget.imagePath != null
                            ? Container(
                                width: double.infinity,
                                height: 120,
                                child: Image.file(
                                  File(widget.imagePath!),
                                  fit: BoxFit.cover,
                                ),
                              )
                            : Container(
                                height: 120,
                                alignment: Alignment.center,
                                decoration: BoxDecoration(
                                  color: Colors.grey[200],
                                ),
                                child: Icon(
                                  Icons.camera_alt,
                                  size: 48,
                                  color: Colors.blue,
                                ),
                              ),
                      ),
                    ),
                  ),
                ),
                // SizedBox(width: 16),
                // // 비디오 아이콘 클릭 로직은 변경 없음
                // Expanded(
                //   child: InkWell(
                //     onTap: () {
                //       print('비디오 클릭됨');
                //     },
                //     child: DottedBorder(
                //       borderType: BorderType.RRect,
                //       radius: Radius.circular(12),
                //       padding: EdgeInsets.all(6),
                //       dashPattern: [8, 4],
                //       child: ClipRRect(
                //         borderRadius: BorderRadius.all(Radius.circular(12)),
                //         child: Container(
                //           height: 120,
                //           alignment: Alignment.center,
                //           decoration: BoxDecoration(
                //             color: Colors.grey[200],
                //           ),
                //           child: Icon(
                //             Icons.videocam,
                //             size: 48,
                //             color: Colors.blue,
                //           ),
                //         ),
                //       ),
                //     ),
                //   ),
                // ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
