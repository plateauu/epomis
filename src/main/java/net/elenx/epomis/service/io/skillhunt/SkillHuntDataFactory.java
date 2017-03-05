package net.elenx.epomis.service.io.skillhunt;

import lombok.Data;
import net.elenx.epomis.entity.JobOffer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Random;

@Data
@Component
public class SkillHuntDataFactory
{
    private static final String FORWARD_SLASH = "/";
    private static final String URL = "https://app.skillhunt.io/jobs/view/582c4703bfe523001f9d316e/product-manager-virtuslab";
    private static final String TYPE = "application";
    private static final String LINKS_OFFER_ID = "57569a82e4b065602cd8b473";
    private static final String APPLICATION_MESSAGE = "Hello, I send you my CV and I would like to apply for this position";
    private static final String APPLICATION_NAME = "Test Name";
    private static final String APPLICATION_RANDOM_MAIL = String.valueOf(new Random().nextInt(10));
    private static final String APPLICATION_EMAIL = "test1" + APPLICATION_RANDOM_MAIL + "@email.address.net";
    private static final String CONTACT_COUNTRY = "Poland";
    private static final String CONTACT_CITY = "Warsaw";
    private static final String CONTACT_PHONE = "223528423";
    
    private final SkillHuntData templateDataEntries;
    private final SkillHuntData.SkillHuntDataLinks templateDataEntriesLinks;
    private final SkillHuntData.SkillHuntDataApplication templateDataEntriesApplication;
    private final SkillHuntData.SkillHuntDataApplication.SkillHuntDataApplicationContact templateDataEntriesContact;
    
    public SkillHuntDataFactory()
    {
        templateDataEntriesLinks = SkillHuntData.SkillHuntDataLinks
            .builder()
            .offer(LINKS_OFFER_ID)
            .build();
        
        templateDataEntriesContact = SkillHuntData.SkillHuntDataApplication.SkillHuntDataApplicationContact
            .builder()
            .phone(CONTACT_PHONE)
            .city(CONTACT_CITY)
            .country(CONTACT_COUNTRY)
            .build();
        
        templateDataEntriesApplication = SkillHuntData.SkillHuntDataApplication
            .builder()
            .workerName(APPLICATION_NAME)
            .workerEmail(APPLICATION_EMAIL)
            .message(APPLICATION_MESSAGE)
            .contact(templateDataEntriesContact)
            .build();
        
        templateDataEntries = SkillHuntData
            .builder()
            .links(templateDataEntriesLinks)
            .application(templateDataEntriesApplication)
            .type(TYPE)
            .build();
    }
    
    public SkillHuntData create(JobOffer jobOffer)
    {
        return SkillHuntData
            .builder()
            .links(SkillHuntData.SkillHuntDataLinks.builder().offer(fetchOfferId(jobOffer)).build())
            .application(templateDataEntriesApplication)
            .type(TYPE)
            .build();
    }
    
    private String fetchOfferId(JobOffer jobOffer)
    {
        return Arrays.asList(jobOffer.getHref().split(FORWARD_SLASH)).get(5);
    }
}
