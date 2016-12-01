package net.elenx.epomis.service.io.skillhunt

import net.elenx.epomis.entity.JobOffer
import net.elenx.epomis.model.ProviderType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class SkillHuntAcceptorTest extends Specification {

    def "perform correct method"() {

        given:
        def RestTemplate restTemplate = Mock()
        def SkillHuntDataFactory skillHuntDataFactory = Mock()
        def JobOffer jobOffer = Mock()

        SkillHuntAcceptor acceptor = new SkillHuntAcceptor(skillHuntDataFactory, restTemplate)

        when:
        acceptor.accept(jobOffer)

        then:
        1 * restTemplate.exchange(_, _, _, _)

    }

}
