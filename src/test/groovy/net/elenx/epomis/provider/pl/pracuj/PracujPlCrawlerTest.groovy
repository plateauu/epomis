package net.elenx.epomis.provider.pl.pracuj

import net.elenx.connection2.ConnectionResponse
import net.elenx.connection2.ConnectionService2
import net.elenx.epomis.entity.JobOffer
import net.elenx.epomis.provider.JobOfferProvider
import org.jsoup.Jsoup
import spock.lang.Specification

public class PracujPlCrawlerTest extends Specification {
    void "extract JobOffers"() {
        given:
        InputStream inputStream = PracujPlCrawlerTest.class.getResourceAsStream("pracujpl.html")

        ConnectionService2 connectionService2 = Mock()
        ConnectionResponse connectionResponse = Mock()
        connectionService2.submit(_) >> connectionResponse
        connectionResponse.getDocument() >> Jsoup.parse(inputStream,
                "UTF-8",
                "https://www.pracuj.pl/praca/java;kw/warszawa;wp")


        JobOfferProvider pracujPlCrawler = new PracujPlCrawler(connectionService2)

        when:
        Set<JobOffer> offers = pracujPlCrawler.offers()

        then:
        offers.size() == 7


    }
}
