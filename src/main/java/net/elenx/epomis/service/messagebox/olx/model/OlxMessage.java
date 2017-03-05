package net.elenx.epomis.service.messagebox.olx.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OlxMessage {

    private String messageId;

    private String messageBody;

    private String messageRecipient;

    private String messageTimestamp;

    private String conversationId;
}
