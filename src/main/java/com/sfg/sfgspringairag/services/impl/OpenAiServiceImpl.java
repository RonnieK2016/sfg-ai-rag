package com.sfg.sfgspringairag.services.impl;

import com.sfg.sfgspringairag.config.PromptsConfig;
import com.sfg.sfgspringairag.model.MoviesRequest;
import com.sfg.sfgspringairag.model.MoviesResponse;
import com.sfg.sfgspringairag.services.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiServiceImpl implements OpenAiService {

    private final ChatModel chatModel;
    private final PromptsConfig promptsConfig;
    private final SimpleVectorStore vectorStore;

    @Override
    public MoviesResponse getMoviesRecommendation(MoviesRequest moviesRequest) {
        List<Document> documentList = vectorStore.similaritySearch(SearchRequest.builder()
                .query(moviesRequest.question()).topK(5).build());
        List<String> contentList = documentList.stream().map(Document::getText).toList();

        PromptTemplate promptTemplate = new PromptTemplate(promptsConfig.getMovieExpertPrompt());
        Prompt prompt = promptTemplate.create(Map.of("input", moviesRequest.question(), "document",
                String.join("\n", contentList)));
        ChatResponse chatResponse = chatModel.call(prompt);
        return new MoviesResponse(chatResponse.getResult().getOutput().getText());
    }
}
