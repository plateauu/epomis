package net.elenx.epomis.provider.io.skillhunt;

import lombok.Data;
import net.elenx.epomis.entity.JobOffer;
import net.elenx.epomis.model.ProviderType;
import net.elenx.epomis.provider.JobOfferProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@Component
public class SkillHuntCrawlerJson implements JobOfferProvider {

    public static final String SLASH = "/";
    private final RestTemplate restTemplate;
    private final String URL = "https://api.skillhunt.io/api/v1/offers";
    private final String baseURL = "https://app.skillhunt.io/jobs/view/";
    private final Pattern javaPattern = Pattern.compile("\\bJAVA\\b", Pattern.CASE_INSENSITIVE);


    @Override
    public Set<JobOffer> offers() {
        return restTemplate
                .getForObject(URL, SkillHuntJsonOffersList.class)
                .getData()
                .stream()
                .map(this::extractOffer)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }


    private Optional<JobOffer> extractOffer(SkillHuntJsonOffer jobOffer) {
        String jobTitle = jobOffer.getPosition();

        if (!javaPattern.matcher(jobTitle).find() || jobOffer.getSlug() == null) {
            return Optional.empty();
        }

        String company = jobOffer.getCompany();
        String location = jobOffer.getLocation().getCity();
        String href = new StringBuilder(baseURL).append(jobOffer.getId()).append(SLASH).append(jobOffer.getSlug()).toString();

        return JobOffer
                .builder()
                .company(company)
                .providerType(ProviderType.SKILL_HUNT)
                .location(location)
                .href(href)
                .title(jobTitle)
                .buildOptional();

    }
}


