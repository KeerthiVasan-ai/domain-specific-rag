import 'package:flutter/material.dart';
import 'package:ondevice_rag/feature/rag/service/rag_service.dart';

class RagDemo extends StatefulWidget {
  const RagDemo({super.key});

  @override
  State<RagDemo> createState() => _RagDemoState();
}

class _RagDemoState extends State<RagDemo> {
  final TextEditingController _controller = TextEditingController();
  
  // State variables
  String _answer = "";
  List<String> _suggestions = [];
  bool _isLoading = false;
  String? _successMessage;
  bool _hasAsked = false;

  Future<void> _ask() async {
    if (_controller.text.trim().isEmpty) return;
    
    setState(() {
      _isLoading = true;
      _successMessage = null;
    });

    try {
      final res = await NativeRag.ask(_controller.text);
      setState(() {
        _answer = res['answer'];
        _suggestions = List<String>.from(res['suggestions']);
        _hasAsked = true;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _isLoading = false;
      });
      _showSnackBar("Error: $e", isError: true);
    }
  }

  Future<void> _trainWithSuggestion(String suggestion) async {
    setState(() {
      _isLoading = true;
    });

    try {
      final success = await NativeRag.train(
        question: _controller.text,
        positive: suggestion,
        negative: _answer,
      );

      if (success) {
        setState(() {
          _clearResults();
          _successMessage = "✓ Training complete! Ask the same question to see the improved answer.";
          _isLoading = false;
        });
      } else {
        setState(() {
          _isLoading = false;
        });
        _showSnackBar("Training failed", isError: true);
      }
    } catch (e) {
      setState(() {
        _isLoading = false;
      });
      _showSnackBar("Error: $e", isError: true);
    }
  }

  void _acceptAnswer() {
    setState(() {
      _clearResults();
      _successMessage = "✓ Answer accepted!";
    });
  }

  void _clearResults() {
    _answer = "";
    _suggestions = [];
    _hasAsked = false;
  }

  void _showSnackBar(String message, {bool isError = false}) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(message),
        backgroundColor: isError ? Colors.red.shade700 : Colors.green.shade700,
        behavior: SnackBarBehavior.floating,
      ),
    );
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final colorScheme = Theme.of(context).colorScheme;
    
    return Scaffold(
      backgroundColor: colorScheme.surface,
      appBar: AppBar(
        title: const Text(
          "On-Device RAG",
          style: TextStyle(fontWeight: FontWeight.w600),
        ),
        centerTitle: true,
        elevation: 0,
        backgroundColor: colorScheme.primaryContainer,
        foregroundColor: colorScheme.onPrimaryContainer,
      ),
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              // Question Input Card
              Card(
                elevation: 2,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(16),
                ),
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      TextField(
                        controller: _controller,
                        decoration: InputDecoration(
                          labelText: "Ask a question",
                          hintText: "Type your question here...",
                          prefixIcon: const Icon(Icons.help_outline),
                          border: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(12),
                          ),
                          filled: true,
                          fillColor: Color.alphaBlend(
                            colorScheme.surfaceContainerHighest.withAlpha(77),
                            colorScheme.surface,
                          ),
                        ),
                        maxLines: 2,
                        minLines: 1,
                        textInputAction: TextInputAction.search,
                        onSubmitted: (_) => _ask(),
                      ),
                      const SizedBox(height: 16),
                      FilledButton.icon(
                        onPressed: _isLoading ? null : _ask,
                        icon: _isLoading 
                            ? const SizedBox(
                                width: 20,
                                height: 20,
                                child: CircularProgressIndicator(
                                  strokeWidth: 2,
                                  color: Colors.white,
                                ),
                              )
                            : const Icon(Icons.search),
                        label: Text(_isLoading ? "Processing..." : "Ask"),
                        style: FilledButton.styleFrom(
                          padding: const EdgeInsets.symmetric(vertical: 14),
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(12),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              
              const SizedBox(height: 20),
              
              // Success Message
              if (_successMessage != null)
                Container(
                  padding: const EdgeInsets.all(16),
                  decoration: BoxDecoration(
                    color: Colors.green.shade50,
                    borderRadius: BorderRadius.circular(12),
                    border: Border.all(color: Colors.green.shade200),
                  ),
                  child: Row(
                    children: [
                      Icon(Icons.check_circle, color: Colors.green.shade700),
                      const SizedBox(width: 12),
                      Expanded(
                        child: Text(
                          _successMessage!,
                          style: TextStyle(
                            color: Colors.green.shade800,
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                      ),
                      IconButton(
                        onPressed: () => setState(() => _successMessage = null),
                        icon: Icon(Icons.close, color: Colors.green.shade700),
                        padding: EdgeInsets.zero,
                        constraints: const BoxConstraints(),
                      ),
                    ],
                  ),
                ),
              
              // Results Section
              if (_hasAsked && !_isLoading) ...[
                const SizedBox(height: 20),
                
                // Answer Card
                Expanded(
                  child: SingleChildScrollView(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        Card(
                          elevation: 3,
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(16),
                          ),
                          color: Color.alphaBlend(
                            colorScheme.primaryContainer.withAlpha(77),
                            colorScheme.surface,
                          ),
                          child: Padding(
                            padding: const EdgeInsets.all(20),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Row(
                                  children: [
                                    Icon(
                                      Icons.lightbulb,
                                      color: colorScheme.primary,
                                    ),
                                    const SizedBox(width: 8),
                                    Text(
                                      "Answer",
                                      style: TextStyle(
                                        fontSize: 18,
                                        fontWeight: FontWeight.bold,
                                        color: colorScheme.primary,
                                      ),
                                    ),
                                  ],
                                ),
                                const SizedBox(height: 12),
                                Text(
                                  _answer,
                                  style: const TextStyle(
                                    fontSize: 16,
                                    height: 1.5,
                                  ),
                                ),
                                const SizedBox(height: 16),
                                Row(
                                  children: [
                                    Expanded(
                                      child: FilledButton.icon(
                                        onPressed: _acceptAnswer,
                                        icon: const Icon(Icons.check),
                                        label: const Text("Accept"),
                                        style: FilledButton.styleFrom(
                                          backgroundColor: Colors.green.shade600,
                                          padding: const EdgeInsets.symmetric(vertical: 12),
                                          shape: RoundedRectangleBorder(
                                            borderRadius: BorderRadius.circular(10),
                                          ),
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          ),
                        ),
                        
                        // Suggestions Section
                        if (_suggestions.isNotEmpty) ...[
                          const SizedBox(height: 20),
                          Padding(
                            padding: const EdgeInsets.symmetric(horizontal: 4),
                            child: Row(
                              children: [
                                Icon(
                                  Icons.auto_fix_high,
                                  color: colorScheme.secondary,
                                  size: 20,
                                ),
                                const SizedBox(width: 8),
                                Text(
                                  "Not right? Select a better answer:",
                                  style: TextStyle(
                                    fontSize: 14,
                                    fontWeight: FontWeight.w600,
                                    color: colorScheme.secondary,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 12),
                          ..._suggestions.map(
                            (suggestion) => Padding(
                              padding: const EdgeInsets.only(bottom: 10),
                              child: Card(
                                elevation: 1,
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(12),
                                  side: BorderSide(
                                    color: colorScheme.outline.withAlpha(51),
                                  ),
                                ),
                                child: InkWell(
                                  onTap: () => _trainWithSuggestion(suggestion),
                                  borderRadius: BorderRadius.circular(12),
                                  child: Padding(
                                    padding: const EdgeInsets.all(16),
                                    child: Row(
                                      children: [
                                        Expanded(
                                          child: Text(
                                            suggestion,
                                            style: const TextStyle(
                                              fontSize: 15,
                                              height: 1.4,
                                            ),
                                          ),
                                        ),
                                        const SizedBox(width: 12),
                                        Container(
                                          padding: const EdgeInsets.all(8),
                                          decoration: BoxDecoration(
                                            color: colorScheme.secondaryContainer,
                                            borderRadius: BorderRadius.circular(8),
                                          ),
                                          child: Icon(
                                            Icons.school,
                                            color: colorScheme.onSecondaryContainer,
                                            size: 20,
                                          ),
                                        ),
                                      ],
                                    ),
                                  ),
                                ),
                              ),
                            ),
                          ),
                        ],
                      ],
                    ),
                  ),
                ),
              ],
              
              // Loading indicator when processing but no results yet
              if (_isLoading && !_hasAsked)
                Expanded(
                  child: Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        CircularProgressIndicator(
                          color: colorScheme.primary,
                        ),
                        const SizedBox(height: 16),
                        Text(
                          "Processing your question...",
                          style: TextStyle(
                            color: colorScheme.onSurfaceVariant,
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
            ],
          ),
        ),
      ),
    );
  }
}
