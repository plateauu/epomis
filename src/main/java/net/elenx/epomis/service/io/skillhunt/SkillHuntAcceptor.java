package net.elenx.epomis.service.io.skillhunt;


import lombok.Data;
import net.elenx.epomis.entity.JobOffer;
import net.elenx.epomis.service.JobOfferService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Data
public class SkillHuntAcceptor implements JobOfferService {


    private static final String BASE_URL = "https://api.skillhunt.io/api/v1/job-applications";
    private final SkillHuntDataFactory skillHuntDataFactory;
    private final RestTemplate restTemplate;


    @Override
    public void accept(JobOffer jobOffer) {

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SkillHuntData> entity = new HttpEntity<>(skillHuntDataFactory.create(jobOffer), requestHeaders);

        restTemplate.exchange(BASE_URL, HttpMethod.POST, entity, Object.class);

    }
}
