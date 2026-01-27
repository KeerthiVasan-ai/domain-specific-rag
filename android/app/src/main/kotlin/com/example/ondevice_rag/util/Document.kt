package com.example.ondevice_rag.util

object Document {
    val content = """
On-device Retrieval-Augmented Generation (RAG) is a technique that enables language models to retrieve relevant contextual information locally without relying on cloud-based infrastructure. Unlike traditional cloud-based systems, on-device RAG operates entirely on the user’s device, allowing applications to function offline while preserving user privacy.

The fundamental idea behind RAG is to separate knowledge storage from language understanding. Instead of embedding all knowledge directly into a large language model, relevant information is stored externally and retrieved dynamically when a query is made. This approach reduces model size and improves adaptability to new information.

In an on-device RAG system, the text encoder plays a crucial role. The encoder converts both user queries and document content into fixed-length vector embeddings. These embeddings capture semantic meaning, allowing the system to compare queries and documents using similarity metrics such as dot product or cosine similarity.

A vector storage mechanism is used to store embeddings for all knowledge chunks. On mobile devices, this storage is often kept in memory or in a lightweight local database. Each stored vector corresponds to a specific chunk of text extracted from the original knowledge document.

Similarity scoring is the core retrieval operation in RAG. When a user asks a question, the system computes an embedding for the query and compares it against all stored document embeddings. The document chunks with the highest similarity scores are considered the most relevant and are selected as candidate answers.

Due to strict constraints on memory, computation, and power consumption, on-device RAG systems must be carefully optimized. Lightweight embedding models are preferred, and vector dimensions are typically kept small to ensure fast inference on mobile hardware.

Training in on-device RAG systems is commonly performed using contrastive learning techniques. One popular approach is triplet loss, where the model learns to bring a query embedding closer to a relevant document embedding while pushing it away from an irrelevant one. This allows the system to improve retrieval accuracy over time.

A key feature of on-device RAG is incremental learning through user feedback. When a user indicates that an answer is incorrect and selects a better alternative, the system uses this feedback as a training signal. The model updates its internal weights locally without sending any data to external servers.

This human-in-the-loop training approach allows the RAG system to adapt to user preferences, language style, and domain-specific usage patterns. Over time, the system becomes more personalized and accurate for the individual user.

Privacy preservation is one of the strongest advantages of on-device RAG. Since all processing occurs locally, sensitive user queries and personal data never leave the device. This makes on-device RAG suitable for applications in healthcare, education, and personal productivity.

Another important benefit is offline capability. On-device RAG systems can function without an internet connection, making them reliable in low-connectivity environments. This is particularly valuable for users in remote areas or situations where network access is limited.

From an architectural perspective, on-device RAG systems typically consist of three layers: the user interface layer, the application logic layer, and the AI inference layer. The inference layer hosts the embedding model and retrieval logic, often implemented using TensorFlow Lite for efficient execution on mobile devices.

The application logic layer coordinates interactions between the user interface and the AI model. It manages query submission, retrieval results, and user feedback. This layer also handles re-embedding of document chunks after training updates.

When integrating on-device RAG into a mobile application, careful attention must be paid to lifecycle management. Since models are loaded into memory, developers must ensure proper initialization and cleanup to avoid excessive memory usage.

Persistence of learned behavior is another important consideration. While on-device training updates model weights during runtime, these updates must be saved explicitly if the learning is expected to persist across application restarts. This can be achieved by saving updated model parameters or serialized models to local storage.

On-device RAG systems are particularly well-suited for domain-specific assistants. By initializing the system with a curated knowledge document, developers can ensure that the assistant provides accurate and relevant answers within a bounded domain.

Unlike general-purpose chatbots, domain-specific RAG systems avoid hallucination by restricting responses to known content. This makes them more reliable for educational tools, internal documentation assistants, and technical help systems.

In summary, on-device Retrieval-Augmented Generation represents a powerful approach for building intelligent, privacy-preserving, and adaptive mobile applications. By combining local embeddings, efficient retrieval, and user-driven learning, on-device RAG systems deliver practical AI functionality without the need for cloud infrastructure.
""".trimIndent()

    val trainedContent =
        """On-device Retrieval-Augmented Generation (RAG) is a technique that allows
language models to retrieve relevant context locally without relying on
cloud-based infrastructure.

The core components of an on-device RAG system include a text encoder,
a vector storage mechanism, and a similarity scoring function. The encoder
transforms input text into dense embeddings that capture semantic meaning.

Due to strict constraints on memory, computation, and power consumption,
on-device RAG systems typically use lightweight embedding models and
compact vector representations.

Training is commonly performed using contrastive learning techniques such
as triplet loss. A query embedding is encouraged to be closer to a relevant
document embedding than to an irrelevant one.

A key advantage of on-device RAG is privacy preservation. Since all data
processing occurs locally, sensitive user information never leaves the
device, making it suitable for offline-first and privacy-sensitive
applications.""".trimIndent()

}