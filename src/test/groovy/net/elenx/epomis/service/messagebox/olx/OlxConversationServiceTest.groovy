package net.elenx.epomis.service.messagebox.olx

import net.elenx.epomis.service.messagebox.olx.model.OlxConversation
import net.elenx.epomis.service.messagebox.olx.model.OlxMessage
import org.jsoup.Jsoup
import spock.lang.Specification

class OlxConversationServiceTest extends Specification {


    def is = OlxMessageFactoryTest.class.getResourceAsStream("conversation_body.html")
    def doc = Jsoup.parse(is, 'UTF-8', 'https://www.olx.pl/mojolx/odpowiedz-na-ogloszenie/2522151735/?ref%5B0%5D%5Baction%5D=myaccount&ref%5B0%5D%5Bmethod%5D=answers#last')
            .getElementById("answersContainer").getElementsByClass("saying")

    def "should add messages to conversation"() {
        given:
        def connector = Mock(OlxConnector)
        def messageFactory = Mock(OlxMessageFactory)
        def loginFactory = Mock(OlxLoginFactory)
        def conversation = createConversation()

        def service = new OlxConversationService(connector, loginFactory, messageFactory)

        when:
        def result = service.init('username', 'password');

        then:
        1 * connector.fetchSessionId() >> 'sessionID'
        1 * loginFactory.createLoginData(_, _) >> [new net.elenx.connection5.DataEntry('login', 'username')]
        1 * connector.fetchConversations(_, _) >> org.assertj.core.util.Sets.newHashSet([conversation])
        1 * connector.fetchMessagePage(_, _, _) >> doc
        1 * loginFactory.getLoginFromData(_) >> 'login'
        1 * messageFactory.create(_, _) >> [createMessage(), createMessage()]

        and:
        result.first().messages.size() == 2
        result.first().messages.first().conversationId == conversation.id
        result.first().messages.get(0).conversationId == conversation.id

    }

    def createMessage() {
        OlxMessage.builder()
                .messageId('ID')
                .messageBody('MESSAGE BODY')
                .messageRecipient('FOO BAR')
                .messageTimestamp('2015-21-01')
                .build()
    }

    def createConversation() {
        OlxConversation.builder().id('12222')
                .lastMessageTimestamp('1221')
                .title('title')
                .build()

    }
}
