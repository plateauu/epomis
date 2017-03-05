package net.elenx.epomis.service.messagebox.olx

import org.jsoup.Jsoup
import spock.lang.Specification

class OlxConversationExtractorTest extends Specification {

    def "should find properties of conversation"() {
        given:
        def is = OlxConversationExtractorTest.class.getResourceAsStream('conversation_list.html')
        def document = Jsoup.parse(is, 'UTF-8', 'https://www.olx.pl/mojolx/odpowiedzi/')

        def extractor = new OlxConversationExtractor()
        when:
        def conversation = extractor.extractConversation(document.getElementsByClass('headerRow').first())

        then:
        conversation != null
        conversation.title == 'Rower męski opony 26 cale'
        conversation.lastMessageTimestamp == '2017-10-17 14:51'
        conversation.id == '2522151735'
        conversation.url == 'https://www.olx.pl/mojolx/odpowiedz-na-ogloszenie/2522151735/?ref%5B0%5D%5Baction%5D=myaccount&ref%5B0%5D%5Bmethod%5D=answers#last'

    }
}