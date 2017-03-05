package net.elenx.epomis.service.messagebox.olx;


import lombok.Data;
import net.elenx.epomis.service.messagebox.olx.model.OlxMessage;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
class OlxMessageFactory {

    private final OlxMessageExtractor extractor;

    List<OlxMessage> create(Elements elements, String login) {
        return elements
                .stream()
                .filter(extractor::isMessage)
                .map(message -> extractor.extractMessage(message, login))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing(OlxMessage::getMessageTimestamp))
                .collect(Collectors.toList());
    }

}
