import 'package:flutter/material.dart';
import 'package:ondevice_rag/feature/rag/presentation/rag_ui.dart';

void main() {
  runApp(OnDeviceRag());
}

class OnDeviceRag extends StatefulWidget {
  const OnDeviceRag({super.key});

  @override
  State<OnDeviceRag> createState() => _OnDeviceRagState();
}

class _OnDeviceRagState extends State<OnDeviceRag> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'On Device Rag',
      home: RagDemo(),
      debugShowCheckedModeBanner: false,
    );
  }
}
