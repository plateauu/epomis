package net.elenx.epomis.service.messagebox.olx;

import lombok.Data;
import net.elenx.epomis.service.messagebox.olx.model.OlxConversation;
import net.elenx.epomis.service.messagebox.olx.util.TimeStampConverter;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Data
@Service
class OlxConversationExtractor {

    private static final String TR_TAG = "tr";
    private static final String CLASS = "class";
    private static final String START_POINT = "https";
    private static final String END_POINT = "'}";
    private static final String ELEMENT_WITH_TITLE = "div.brkword.title";
    private static final String TITLE = "title";
    private static final String INPUT_TAG = "input";
    private static final String VALUE = "value";
    private static final String ELEMENT_WITH_DATE = "p.margintop2";

    OlxConversation extractConversation(Element element) {
        return OlxConversation.builder()
                .id(extractId(element))
                .lastMessageTimestamp(extractDate(element))
                .title(extractTitle(element))
                .url(extractUrl(element))
                .build();
    }

    private String extractUrl(Element conversation) {
        String trimmedClassAttributeOfTrTag = conversation.getElementsByTag(TR_TAG).attr(CLASS).trim();
        return trimmedClassAttributeOfTrTag.substring(trimmedClassAttributeOfTrTag.indexOf(START_POINT),
                trimmedClassAttributeOfTrTag.lastIndexOf(END_POINT)
        );
    }

    private String extractTitle(Element conversation) {
        return conversation.select(ELEMENT_WITH_TITLE).attr(TITLE);
    }

    private String extractId(Element conversation) {
        return conversation.getElementsByTag(INPUT_TAG).first().attr(VALUE);
    }

    private String extractDate(Element conversation) {
        return TimeStampConverter.convert(conversation.select(ELEMENT_WITH_DATE).text());
    }
}
