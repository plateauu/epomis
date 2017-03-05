package net.elenx.epomis.service.messagebox.olx.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OlxConversation {

    private String id;

    private String title;

    private String lastMessageTimestamp;

    private String url;

    private List<OlxMessage> messages;

    public void addMessages(List<OlxMessage> olxMessages) {
        olxMessages.forEach(msg -> msg.setConversationId(this.getId()));
        this.messages = olxMessages;
    }
}