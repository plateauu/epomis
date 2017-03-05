package net.elenx.epomis.service.messagebox.olx;

import lombok.Data;
import net.elenx.connection5.DataEntry;
import net.elenx.epomis.service.messagebox.olx.model.OlxConversation;
import net.elenx.epomis.service.messagebox.olx.model.OlxMessage;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Data
@Service
class OlxConversationService {

    private final OlxConnector connector;
    private final OlxLoginFactory loginFactory;
    private final OlxMessageFactory messageFactory;

    Set<OlxConversation> init(String username, String password) {
        final String sessionId = connector.fetchSessionId();
        final List<DataEntry> data = loginFactory.createLoginData(username, password);

        final Set<OlxConversation> olxConversations = connector.fetchConversations(data, sessionId);
        olxConversations.forEach(conversation -> fetchAndAddMessages(sessionId, data, conversation));
        return olxConversations;
    }

    private void fetchAndAddMessages(String sessionId, List<DataEntry> data, OlxConversation conversation) {
        final Elements messages = connector.fetchMessagePage(conversation.getUrl(), data, sessionId);
        final String login = loginFactory.getLoginFromData(data);
        final List<OlxMessage> olxMessages = messageFactory.create(messages, login);
        conversation.addMessages(olxMessages);
    }

}
