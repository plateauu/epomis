package net.elenx.epomis.service.messagebox.olx

import org.jsoup.Jsoup
import spock.lang.Shared
import spock.lang.Specification

class OlxMessageExtractorTest extends Specification {

    @Shared
    def is = OlxMessageExtractorTest.getResourceAsStream("conversation_body.html")
    @Shared
    def doc = Jsoup.parse(is, 'UTF-8', 'https://www.olx.pl/mojolx/odpowiedz-na-ogloszenie/2522151735/?ref%5B0%5D%5Baction%5D=myaccount&ref%5B0%5D%5Bmethod%5D=answers#last')
            .getElementById("answersContainer")
            .getElementsByClass("saying")

    def "should parse proper attributes"() {
        given:
        def document = doc.get(1)

        def extractor = new OlxMessageExtractor()

        when:
        def result = extractor.extractMessage(document, 'UserNameLogin')

        then:
        result != null
        result.isPresent()
        result.get().messageId == '2522151735'
        result.get().messageTimestamp == '2017-10-17 14:40'
        result.get().messageBody == 'Witam, jaki jest rozmiar ramy?'
        result.get().messageRecipient == 'UserNameLogin'

    }

    def "should parse correct recipient when recipient is available"() {
        given:
        def document = doc.get(2)

        def extractor = new OlxMessageExtractor()

        when:
        def result = extractor.extractMessage(document, 'UserNameLogin')

        then:
        result != null
        result.isPresent()
        result.get().messageRecipient == 'tgk05'
    }

}
