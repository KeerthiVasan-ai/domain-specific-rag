import 'package:flutter/services.dart';

class NativeRag {
  static const _channel = MethodChannel('agentic_rag/channel');

  static Future<Map> ask(String question) async {
    return await _channel.invokeMethod('ask', {'question': question});
  }

  static Future<bool> train({
    required String question,
    required String positive,
    required String negative,
  }) async {
    final result = await _channel.invokeMethod('train', {
      'question': question,
      'positive': positive,
      'negative': negative,
    });
    return result['success'] == true;
  }
}
