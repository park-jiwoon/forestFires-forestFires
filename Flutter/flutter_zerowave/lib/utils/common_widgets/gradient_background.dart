import 'package:flutter/material.dart';

import '../../values/app_colors.dart';
import '../extensions.dart';

class GradientBackground extends StatelessWidget {
  const GradientBackground({
    required this.children,
    this.colors = AppColors.defaultGradient,
    this.height,
    super.key,
  });

  final List<Color> colors;
  final List<Widget> children;
  final double? height;

  @override
  Widget build(BuildContext context) {
    return DecoratedBox(
      decoration: BoxDecoration(gradient: LinearGradient(colors: colors)),
      child: Padding(
        padding: const EdgeInsets.all(0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            SizedBox(
              height: context.heightFraction(sizeFraction: 0.02),
            ),
            ...children,
          ],
        ),
      ),
    );
  }
}
