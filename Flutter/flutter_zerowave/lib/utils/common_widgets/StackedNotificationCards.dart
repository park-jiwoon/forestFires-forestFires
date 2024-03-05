import 'package:flutter/material.dart';



class StackedNotificationCards extends StatefulWidget {
  final List<BoxShadow> shadow;
  final String type;
  final List<Widget> notifications;
  final Color cardColor;
  final double padding;
  final Widget headerTitle;
  final Widget headerShowLess;
  final VoidCallback onTapClearAll;
  final Widget headerClearAllButton;
  final Widget clearAllStacked;
  final Widget clear;
  final Widget view;
  final Function(int) onTapClearCallback;
  final Function(int) onTapViewCallback;

  StackedNotificationCards({
    Key? key,
    required this.shadow,
    required this.type,
    required this.notifications,
    required this.cardColor,
    required this.padding,
    required this.headerTitle,
    required this.headerShowLess,
    required this.onTapClearAll,
    required this.headerClearAllButton,
    required this.clearAllStacked,
    required this.clear,
    required this.view,
    required this.onTapClearCallback,
    required this.onTapViewCallback,
  }) : super(key: key);

  @override
  _StackedNotificationCardsState createState() => _StackedNotificationCardsState();
}

class _StackedNotificationCardsState extends State<StackedNotificationCards> {
  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Padding(
            padding: EdgeInsets.all(widget.padding),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                widget.headerTitle,
                GestureDetector(
                  onTap: widget.onTapClearAll,
                  child: widget.headerClearAllButton,
                ),
              ],
            ),
          ),
          // 알림 목록을 ListView 대신 Column으로 직접 구성합니다.
          // ListView를 사용하면 스크롤 뷰 내 스크롤 뷰를 넣는 것이 되어 문제가 발생할 수 있습니다.
          for (var i = 0; i < widget.notifications.length; i++)
            GestureDetector(
              onTap: () => widget.onTapViewCallback(i),
              child: widget.notifications[i],
            ),
        ],
      ),
    );
  }
}
