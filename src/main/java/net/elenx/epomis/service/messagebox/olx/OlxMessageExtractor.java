package net.elenx.epomis.service.messagebox.olx;

import lombok.Data;
import net.elenx.epomis.service.messagebox.olx.model.OlxMessage;
import net.elenx.epomis.service.messagebox.olx.util.TimeStampConverter;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Data
@Component
class OlxMessageExtractor {

    private static final String A_TAG = "a";
    private static final String ID = "id";
    private static final String BODY_CLASS = "cloud";
    private static final String P_TAG = "p";
    private static final String TIME_CLASS = "time";
    private static final String LINK_CLASS = "link";
    private static final int FIRST = 0;
    private static final String LI_WITH_ROOT_CLASS = "li.root";

    Optional<OlxMessage> extractMessage(Element element, String login) {
        Optional<String> messageId = extractId(element);
        if (messageId.isPresent()) {
            OlxMessage message = OlxMessage.builder()
                    .messageId(messageId.get())
                    .messageRecipient(extractRecipients(element, login))
                    .messageTimestamp(extractTimeStamp(element))
                    .messageBody(extractBody(element))
                    .build();
            return Optional.ofNullable(message);
        }
        return Optional.empty();
    }

    private Optional<String> extractId(Element element) {
        final Element tag = element.getElementsByTag(A_TAG).first();
        if (tag == null || StringUtil.isBlank(tag.attr(ID))) {
            return Optional.empty();
        }
        return Optional.ofNullable(tag.attr(ID));
    }

    private String extractBody(Element element) {
        return element.getElementsByClass(BODY_CLASS).get(FIRST).getElementsByTag(P_TAG).text();
    }

    private String extractTimeStamp(Element element) {
        return TimeStampConverter.convert(element.getElementsByClass(TIME_CLASS).get(FIRST).text());
    }

    private String extractRecipients(Element element, String login) {
        Elements linkElements = element.getElementsByClass(LINK_CLASS);
        if (linkElements.isEmpty()) {
            return login;
        }
        return linkElements.last().text();
    }


    boolean isMessage(Element parsedTag) {
        return !parsedTag.select(LI_WITH_ROOT_CLASS).isEmpty();

    }
}