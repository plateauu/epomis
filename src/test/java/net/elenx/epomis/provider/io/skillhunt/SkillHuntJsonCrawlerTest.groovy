package net.elenx.epomis.provider.io.skillhunt

import net.elenx.connection2.ConnectionResponse
import net.elenx.connection2.ConnectionService2
import net.elenx.epomis.entity.JobOffer
import net.elenx.epomis.provider.JobOfferProvider
import org.jsoup.Jsoup
import spock.lang.Specification

class SkillHuntJsonCrawlerTest extends Specification {
    void "skillHunt.io extract offers"()

    {
        given:
        InputStream inputStream = SkillHuntCrawlerTest.class.getResourceAsStream("offers.txt")

        ConnectionService2 connectionService2 = Mock();
        ConnectionResponse connectionResponse = Mock();
        connectionService2.submit(_) >> connectionResponse;
        connectionResponse.getDocument() >> Jsoup.parse(inputStream, "UTF-8", "https://api.skillhunt.io/api/v1/offers?filters=%7B%22closed%22:%7B%22$eq%22:false%7D%7D")

        JobOfferProvider skillHuntCrawler = new SkillHuntCrawler(connectionService2)


        when:
        Set<JobOffer> offers = skillHuntCrawler.offers()
        println offers

        then:
        offers.size() == 8


    }

}
