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

    val newDocument = """
        Retrieval-Augmented Generation (RAG) is a framework that enhances language models by combining information retrieval with natural language generation. Rather than depending entirely on a model’s internal parameters to store and recall knowledge, RAG systems dynamically retrieve relevant information from an external knowledge source at inference time. This retrieved information is then used as contextual grounding for generating responses.

        The fundamental motivation behind RAG is that language models, regardless of size, have limited and static knowledge once trained. Updating or expanding this knowledge through retraining is computationally expensive and impractical for many applications. RAG addresses this limitation by decoupling knowledge storage from language understanding, allowing systems to incorporate new or updated information without modifying the core language model.

        At a high level, a RAG system consists of two main components: a retriever and a generator. The retriever is responsible for locating relevant pieces of information from a document collection, while the generator uses those retrieved pieces to produce a coherent and contextually grounded response. The effectiveness of a RAG system depends on the tight integration between these two components.

        The first stage of a RAG pipeline is document preparation. Raw documents are processed and divided into smaller, manageable units known as chunks. Chunking is necessary because embedding models operate on fixed-length inputs. Each chunk should ideally represent a semantically coherent piece of information, such as a paragraph or a group of related sentences. Poor chunking can degrade retrieval quality, either by fragmenting meaning or by including too much unrelated context.

        Once documents are chunked, each chunk is passed through a text encoder to generate a vector embedding. An embedding is a numerical representation of text that captures its semantic meaning. The goal of the encoder is to map texts with similar meanings to nearby points in a high-dimensional vector space. The same encoder is used for both document chunks and user queries to ensure that comparisons are meaningful.

        These embeddings are stored in a vector store, which serves as the system’s external memory. Each stored vector is linked to its original text chunk. The vector store enables efficient similarity search, allowing the system to quickly identify which document chunks are most relevant to a given query.

        When a user submits a query, the query undergoes the same embedding process as the documents. The resulting query embedding is compared against all stored document embeddings using a similarity function. Common similarity measures include cosine similarity and dot product. These measures quantify how close two vectors are in the embedding space, which correlates with semantic similarity.

        The retrieval step selects the top-k document chunks with the highest similarity scores. These chunks are considered the most relevant to the user’s query. Retrieval quality is critical, as irrelevant or noisy chunks can mislead the generation model and result in incorrect or confusing answers.

        The retrieved chunks are then passed to the generation component. Instead of answering the query in isolation, the language model receives both the query and the retrieved content as input. This augmented context constrains the model’s output, encouraging it to base its response on retrieved information rather than on memorized or inferred knowledge.

        One of the key advantages of RAG is its ability to reduce hallucination. Hallucination occurs when a language model generates information that is factually incorrect or unsupported. By grounding generation in retrieved documents, RAG systems limit the model’s freedom to invent details and improve factual reliability, especially in domain-specific scenarios.

        Training plays an important role in improving RAG performance. While some RAG systems rely on pretrained encoders, others fine-tune the retriever using supervised or contrastive learning techniques. A common approach is triplet loss, where the model is trained using a query, a relevant document chunk, and an irrelevant chunk. The objective is to bring the query embedding closer to the relevant chunk while pushing it away from the irrelevant one.

        Through training, the retriever learns more precise semantic representations, improving retrieval accuracy. High-quality retrieval directly translates into better generation quality, as the generator receives more relevant and informative context.

        Chunk size is a crucial design decision in RAG systems. Smaller chunks tend to improve retrieval precision because they focus on specific ideas, while larger chunks preserve more contextual information. There is no universal optimal chunk size; it depends on the domain, document structure, and query patterns. Many systems adopt overlapping chunks to balance precision and context retention.

        Embedding dimensionality is another important consideration. Higher-dimensional embeddings can capture richer semantic information but require more storage and computation. Lower-dimensional embeddings are more efficient but may lose expressive power. RAG systems must strike a balance between retrieval accuracy and resource efficiency.

        RAG systems are particularly well-suited for domain-specific knowledge applications. By restricting the document collection to a curated set of trusted sources, the system ensures that responses remain within known boundaries. This makes RAG a strong choice for educational tools, internal documentation assistants, technical support systems, and knowledge-based question answering.

        A well-designed RAG system should also handle failure cases gracefully. If retrieval does not return any sufficiently relevant documents, the system should acknowledge that the information is unavailable rather than generating an unsupported answer. This behavior improves user trust and system reliability.

        Another strength of RAG is its adaptability. Because knowledge is stored externally, documents can be added, removed, or updated independently of the language model. This enables rapid iteration and continuous improvement without costly retraining cycles.

        Evaluation of RAG systems differs from traditional language model evaluation. In addition to generation quality, developers must assess retrieval accuracy, relevance of retrieved chunks, and grounding consistency. Metrics such as recall@k, precision@k, and response faithfulness are commonly used to measure performance.

        RAG also supports explainability. Since responses are grounded in retrieved documents, systems can expose the source chunks used to generate an answer. This transparency helps users verify correctness and understand the reasoning behind responses.

        Despite its advantages, RAG introduces additional complexity compared to standalone language models. Developers must manage document preprocessing, embedding generation, vector storage, and retrieval tuning. However, this complexity is often justified by the improvements in accuracy, reliability, and controllability.

        In summary, Retrieval-Augmented Generation is a powerful paradigm that enhances language models by integrating external knowledge retrieval into the generation process. By separating knowledge storage from reasoning, RAG systems enable scalable, updatable, and reliable question answering. When designed carefully, RAG provides a strong foundation for building intelligent systems that deliver accurate, grounded, and trustworthy responses.
       
    """.trimIndent()

    val finalDocument = """
        Retrieval-Augmented Generation (RAG) is a technique in artificial intelligence that combines information retrieval with natural language generation to produce accurate and grounded responses. Instead of relying only on a language model’s internal parameters, a RAG system retrieves relevant information from an external collection of documents and uses that information as context when generating answers. In simple terms, RAG allows a model to search for knowledge first and then generate responses based on real documents rather than memory alone.

        Traditional language models store knowledge inside their trained weights. Once training is complete, this knowledge becomes static and cannot be easily updated. Adding new information or correcting existing knowledge usually requires retraining the entire model, which is computationally expensive and time-consuming. This limitation makes traditional language models difficult to maintain in real-world applications where information changes frequently. Retrieval-Augmented Generation solves this problem by separating knowledge storage from language understanding.

        A RAG system is composed of two main components: a retriever and a generator. The retriever is responsible for searching a document collection and identifying text that is relevant to a user’s query. The generator then takes the original query along with the retrieved text and produces a coherent and context-aware response. The quality of a RAG system depends heavily on how accurately the retriever selects relevant information.

        Before retrieval can take place, documents must undergo preprocessing. Raw text is cleaned and divided into smaller units known as chunks. Chunking is required because embedding models operate on limited-length inputs and cannot process entire documents at once. Each chunk should represent a complete and meaningful unit of information. Poor chunking can lead to fragmented meaning and lower retrieval quality.

        After chunking, each document chunk is converted into a vector embedding. An embedding is a numerical representation of text that captures semantic meaning in a high-dimensional space. Texts with similar meanings are mapped to nearby points in this vector space. The same embedding model is used to encode both document chunks and user queries so that similarity comparisons are meaningful.

        In a RAG system, document embeddings are stored inside a vector store. The vector store acts as the external memory of the system, maintaining a relationship between embeddings and their original text chunks. When a user submits a query, the query text is also converted into an embedding. This query embedding is compared against stored document embeddings using similarity measures such as cosine similarity or dot product. The chunks with the highest similarity scores are retrieved as the most relevant results.

        The retrieval process typically returns the top-k document chunks based on similarity scores. These retrieved chunks are assumed to contain information relevant to the user’s question. Retrieval quality is critical, because irrelevant or noisy chunks can mislead the generation component and result in incorrect answers. A well-designed retriever significantly improves the overall effectiveness of a RAG system.

        The generation component in a RAG system differs from a standalone language model. Instead of generating responses in isolation, the generator receives both the user query and the retrieved document chunks as input. This augmented context constrains the model’s output and encourages it to base responses on retrieved evidence. As a result, RAG systems produce more factual and reliable answers.

        One of the key advantages of Retrieval-Augmented Generation is its ability to reduce hallucination. Hallucination occurs when a language model generates information that is not supported by factual evidence. Because RAG systems ground responses in retrieved documents, the model is less likely to invent details. This makes RAG especially valuable in domain-specific and high-stakes applications.

        Training can further improve retrieval quality in RAG systems. Some implementations fine-tune the retriever using supervised or contrastive learning methods. A common approach is triplet loss training, where the model learns from a query, a relevant document chunk, and an irrelevant chunk. The objective is to bring the query embedding closer to the relevant chunk while pushing it away from the irrelevant one.

        Through training, the retriever gradually learns to align semantic representations with user intent. Improved retrieval accuracy leads directly to better generation quality, since the generator receives more relevant context. Over time, this training enables RAG systems to adapt to specific domains and usage patterns.

        Chunk size is an important design decision in Retrieval-Augmented Generation. Smaller chunks tend to improve retrieval precision because they focus on specific concepts, while larger chunks preserve more contextual information. There is no universal optimal chunk size. Many practical systems use overlapping chunks to balance precision and context retention.

        Embedding dimensionality also plays a significant role in system performance. Higher-dimensional embeddings capture richer semantic information but require more computation and memory. Lower-dimensional embeddings are more efficient and better suited for on-device environments, though they may sacrifice some expressive power. Designers must balance accuracy and resource constraints carefully.

        RAG systems are particularly effective for domain-specific applications such as educational platforms, internal documentation assistants, enterprise knowledge bases, and technical support tools. By restricting the document collection to trusted sources, the system ensures that responses remain grounded within known boundaries.

        Another strength of Retrieval-Augmented Generation is adaptability. Because knowledge is stored externally, documents can be added, removed, or updated without modifying the core language model. This enables rapid iteration and keeps systems up to date without expensive retraining cycles.

        A well-designed RAG system should handle failure cases gracefully. If retrieval does not return sufficiently relevant documents, the system should acknowledge the lack of information rather than generating unsupported answers. This behavior improves user trust and system reliability.

        Evaluating RAG systems requires more than traditional language model metrics. Developers must assess retrieval accuracy, relevance of retrieved chunks, and consistency between responses and source documents. Metrics such as recall@k, precision@k, and faithfulness are commonly used.

        Although Retrieval-Augmented Generation introduces additional system complexity, this complexity is often justified by improvements in accuracy, reliability, and controllability. By integrating retrieval with generation, RAG provides a scalable and trustworthy approach to building intelligent question-answering systems.
    """.trimIndent()
}