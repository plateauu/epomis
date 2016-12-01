package net.elenx.epomis.service.io.skillhunt

import net.elenx.epomis.entity.JobOffer
import spock.lang.Specification


class SkillHuntDataFactoryTest extends Specification {
    def "fetch propper id"() {

        given:
        JobOffer jobOffer = JobOffer
                .builder()
                .href("https://app.skillhunt.io/jobs/view/582c4703bfe523001f9d316e/product-manager-virtuslab")
                .build()

        SkillHuntDataFactory skillHuntDataFactory = new SkillHuntDataFactory()

        when:
        SkillHuntData data = skillHuntDataFactory.create(jobOffer)

        then:
        data.getLinks().getOffer() == "582c4703bfe523001f9d316e"

    }

}
