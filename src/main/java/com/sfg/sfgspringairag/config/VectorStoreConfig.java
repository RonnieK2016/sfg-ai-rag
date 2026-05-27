package com.sfg.sfgspringairag.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;

@Configuration
@Slf4j
public class VectorStoreConfig {

    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel
            , VectorStoreProperties vectorStoreProperties) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File vectorStoreFile = new File(vectorStoreProperties.getVectorStorePath());
        if (vectorStoreFile.exists()) {
            simpleVectorStore.load(vectorStoreFile);
        }
        else {
            log.info("Loading vector store from files: {}", vectorStoreProperties.getVectorStorePath());
            vectorStoreProperties.getDocumentsToLoad().forEach(doc -> {
                log.info("Loading vector document from file: {}", doc);
                TikaDocumentReader tika = new TikaDocumentReader(doc);
                List<Document> docs = tika.get();
                TextSplitter splitter = TokenTextSplitter.builder().build();
                List<Document> splitDocs = splitter.apply(docs);
                simpleVectorStore.add(splitDocs);
            });
            simpleVectorStore.save(vectorStoreFile);
        }
        return simpleVectorStore;
    }
}
