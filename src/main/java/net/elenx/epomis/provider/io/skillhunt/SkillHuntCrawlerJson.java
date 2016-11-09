package net.elenx.epomis.provider.io.skillhunt;

import lombok.Data;
import net.elenx.epomis.entity.JobOffer;
import net.elenx.epomis.model.ProviderType;
import net.elenx.epomis.provider.JobOfferProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Component
public class SkillHuntCrawlerJson implements JobOfferProvider {

    private final RestTemplate restTemplate;
    private final String URL = "https://api.skillhunt" +
            ".io/api/v1/offers?filters=%7B%22closed%22:%7B%22$eq%22:false%7D%7D";


    @Override
    public Set<JobOffer> offers() {
        return restTemplate
                .getForObject(URL, SkillHuntJsonOffersList.class)
                .getData()
                .stream()
                .map(this::extractOffer)
                .collect(Collectors.toSet());
    }

    private JobOffer extractOffer(SkillHuntJsonOffer jobOffer) {
        String jobTitle = jobOffer.getPosition();
        String company = jobOffer.getCompany();
        String location = jobOffer.getLocation().getCity();

        return JobOffer
                .builder()
                .company(company)
                .providerType(ProviderType.SKILL_HUNT)
                .location(location)
                .title(jobTitle)
                .build();
    }

}
