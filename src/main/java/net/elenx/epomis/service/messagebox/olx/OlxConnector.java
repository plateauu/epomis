package net.elenx.epomis.service.messagebox.olx;

import lombok.Data;
import net.elenx.connection5.ConnectionRequest5;
import net.elenx.connection5.ConnectionService5;
import net.elenx.connection5.DataEntry;
import net.elenx.epomis.service.messagebox.olx.model.OlxConversation;
import net.elenx.exceptions.ParseException;
import org.jsoup.Connection;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Service
class OlxConnector {

    private static final String BASE_URL = "https://www.olx.pl/";
    private static final String CONVERSATION_URL = "https://www.olx.pl/mojolx/odpowiedzi/";
    private static final String CONVERSATIONS = "headerRow";
    private static final String MESSAGE_BOX = "answersContainer";
    private static final String SESSION = "PHPSESSID";
    private static final String SAYING = "saying";
    public static final String PROVIDER_NAME = "OLX";

    private final ConnectionService5 connectionService;
    private final OlxConversationExtractor extractor;

    String fetchSessionId() {
        ConnectionRequest5 request = ConnectionRequest5
                .builder()
                .url(BASE_URL)
                .method(Connection.Method.GET.name())
                .build();

        return connectionService
                .submit(request)
                .getCookies()
                .get(SESSION);
    }

    Set<OlxConversation> fetchConversations(List<DataEntry> data, String sessionId) {
        ConnectionRequest5 request = ConnectionRequest5.builder()
                .url(CONVERSATION_URL)
                .method(Connection.Method.GET.name())
                .data(data)
                .cookie(SESSION, sessionId)
                .build();

        return connectionService.submit(request)
                .getResponseAsHtml()
                .orElseThrow(() -> ParseException.noData(PROVIDER_NAME, this.getClass().getName()))
                .getElementsByClass(CONVERSATIONS)
                .stream()
                .map(extractor::extractConversation)
                .collect(Collectors.toSet());
    }

    Elements fetchMessagePage(String conversationUrl, List<DataEntry> data, String sessionId) {
        ConnectionRequest5 request = ConnectionRequest5.builder()
                .url(conversationUrl)
                .method(Connection.Method.GET.name())
                .data(data)
                .cookie(SESSION, sessionId)
                .build();

        return connectionService.submit(request)
                .getResponseAsHtml()
                .orElseThrow(() -> ParseException.noData(PROVIDER_NAME, this.getClass().getName()))
                .getElementById(MESSAGE_BOX)
                .getElementsByClass(SAYING);
    }
}
