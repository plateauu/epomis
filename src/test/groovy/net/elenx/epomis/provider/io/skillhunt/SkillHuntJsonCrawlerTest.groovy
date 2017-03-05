package net.elenx.epomis.provider.io.skillhunt

import net.elenx.epomis.entity.JobOffer
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.test.web.client.response.MockRestResponseCreators
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method

class SkillHuntJsonCrawlerTest extends Specification {

    private final String URL = "https://api.skillhunt.io/api/v1/offers"
    private final String FILEPATH = "offers.txt"
    private final String UTF8 = "UTF8"


    void "skillHunt.io extract offers"() {
        given:

        RestTemplate restTemplate = new RestTemplate()
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build()
        SkillHuntCrawlerJson crawler = new SkillHuntCrawlerJson(restTemplate)
        URL url = SkillHuntCrawlerJson.class.getResource(FILEPATH)
        Path resPath = Paths.get(url.toURI())
        String json = new String(Files.readAllBytes(resPath), UTF8)

        mockServer.expect(MockRestRequestMatchers.requestTo(URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(json, MediaType.APPLICATION_JSON))

        when:
        Set<JobOffer> offers = crawler.offers()

        then:
        offers.size() == 8


    }

}
