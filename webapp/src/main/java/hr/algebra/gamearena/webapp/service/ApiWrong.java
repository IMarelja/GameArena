package hr.algebra.gamearena.webapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamearena.client.model.ApiError;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;

public record ApiWrong(String message) {

    private static final String FALLBACK_MESSAGE = "Request failed";

    public static List<ApiWrong> fromListApiErrors(List<ApiError> errors) {
        List<ApiWrong> list = new ArrayList<>();

        if(errors == null || errors.isEmpty())
            return new ArrayList<>();

        for (ApiError error : errors) {
            list.add(new ApiWrong(error.getMessage()));
        }
        return list;
    }

    public static List<String> fromListApiErrorToListString(List<ApiError> errors) {
        List<ApiWrong> wrongs = fromListApiErrors(errors);
        return wrongs.isEmpty()
                ? List.of(FALLBACK_MESSAGE)
                : wrongs.stream().map(ApiWrong::message).toList();
    }

    public static List<String> fromRestClientResponseExceptionToListString(RestClientResponseException ex) {
        try {
            JsonNode root = new ObjectMapper().readTree(ex.getResponseBodyAsByteArray());
            JsonNode errorsNode = root.path("errors");

            if (!errorsNode.isArray() || errorsNode.isEmpty()) {
                return List.of(FALLBACK_MESSAGE);
            }

            List<String> messages = new ArrayList<>();
            errorsNode.forEach(errorNode -> messages.add(errorNode.path("message").asText(FALLBACK_MESSAGE)));
            return messages;
        } catch (Exception e) {
            return List.of(FALLBACK_MESSAGE);
        }
    }
}
