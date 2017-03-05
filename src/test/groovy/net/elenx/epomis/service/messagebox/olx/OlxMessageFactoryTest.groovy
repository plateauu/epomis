package net.elenx.epomis.service.messagebox.olx

import org.jsoup.Jsoup
import spock.lang.Shared
import spock.lang.Specification

class OlxMessageFactoryTest extends Specification {

    @Shared
    def is = OlxMessageFactoryTest.class.getResourceAsStream("conversation_body.html")

    @Shared
    def doc = Jsoup.parse(is, 'UTF-8', 'https://www.olx.pl/mojolx/odpowiedz-na-ogloszenie/2522151735/?ref%5B0%5D%5Baction%5D=myaccount&ref%5B0%5D%5Bmethod%5D=answers#last')
            .getElementById("answersContainer").getElementsByClass("saying")


    def "should create new messages"() {
        given:
        def input = doc

        def extractor = new OlxMessageExtractor()
        def factory = new OlxMessageFactory(extractor)

        when:
        def result = factory.create(input, 'login')

        then:
        result != null
        result.size() == 2
        result.first().messageId == '2522151735'

    }
}
