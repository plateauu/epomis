package net.elenx.epomis.provider.io.skillhunt

import net.elenx.connection2.ConnectionResponse
import net.elenx.connection2.ConnectionService2
import net.elenx.epomis.entity.JobOffer
import net.elenx.epomis.provider.JobOfferProvider
import org.jsoup.Jsoup
import spock.lang.Specification

public class SkillHuntCrawlerTest extends Specification {
    void "skillHunt.io extract offers"()

    {
        given:
        InputStream inputStream = SkillHuntCrawlerTest.class.getResourceAsStream("skillhunt.html")

        ConnectionService2 connectionService2 = Mock();
        ConnectionResponse connectionResponse = Mock();
        connectionService2.submit(_) >> connectionResponse;
        connectionResponse.getDocument() >> Jsoup.parse(inputStream, "UTF-8", "https://app.skillhunt.io/jobs")

        JobOfferProvider skillHuntCrawler = new SkillHuntCrawler(connectionService2)


        when:
        Set<JobOffer> offers = skillHuntCrawler.offers()
        println offers

        then:
        offers.size() == 8


    }

}