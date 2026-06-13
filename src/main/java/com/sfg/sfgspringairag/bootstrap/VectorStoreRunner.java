package com.sfg.sfgspringairag.bootstrap;

import com.sfg.sfgspringairag.config.VectorStoreProperties;
import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.GetCollectionStatisticsResponse;
import io.milvus.param.R;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.collection.GetCollectionStatisticsParam;
import io.milvus.param.collection.HasCollectionParam;
import io.milvus.response.GetCollStatResponseWrapper;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.HasCollectionReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class VectorStoreRunner implements CommandLineRunner {

    @Autowired
    VectorStore vectorStore;

    @Autowired
    private MilvusServiceClient milvusClient;

    @Autowired
    VectorStoreProperties vectorStoreProperties;

    @Override
    public void run(String... args) throws Exception {

        R<GetCollectionStatisticsResponse> responseR = milvusClient.getCollectionStatistics(GetCollectionStatisticsParam.newBuilder().withCollectionName("movies_store").build());
        GetCollStatResponseWrapper wrapper = new GetCollStatResponseWrapper(responseR.getData());

        if (vectorStore instanceof MilvusVectorStore
                && wrapper.getRowCount() == 0) {

            log.debug("Loading documents into vector store");

            vectorStoreProperties.getDocumentsToLoad().forEach(document -> {
                System.out.println("Loading document: " + document.getFilename());

                log.info("Loading vector document from file: {}", document);
                TikaDocumentReader tika = new TikaDocumentReader(document);
                List<Document> docs = tika.get();
                TextSplitter splitter = TokenTextSplitter.builder().build();
                List<Document> splitDocs = splitter.apply(docs);
                vectorStore.add(splitDocs);
            });
        }

        log.info("Vector store loaded");
    }
}
