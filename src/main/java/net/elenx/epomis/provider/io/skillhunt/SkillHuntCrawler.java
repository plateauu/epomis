package net.elenx.epomis.provider.io.skillhunt;

import lombok.Data;
import net.elenx.connection2.ConnectionRequest;
import net.elenx.connection2.ConnectionService2;
import net.elenx.epomis.entity.JobOffer;
import net.elenx.epomis.model.ProviderType;
import net.elenx.epomis.provider.JobOfferProvider;
import org.jsoup.Connection;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@Component
public class SkillHuntCrawler implements JobOfferProvider
{

    public final String CLASS_PATTERN = "div[class='offer-header padding-horizontal text-align-center cursor-pointer']";
    private final String URL = "https://app.skillhunt.io/jobs";
    public final String HEADER = "header";
    public final String TITLE = "title";
    public final String SPAN = "span";
    public final String ABS_HREF = "abs:href";
    public final String COMMA = ",";
    public final String BR = "<br>";
    private final Pattern pattern = Pattern.compile("\\b[Jj][Aa][Vv][Aa]\\b");

    private final ConnectionService2 connectionService2;

    @Override
    public Set<JobOffer> offers()
    {

        ConnectionRequest connectionRequest = ConnectionRequest
            .builder()
            .url(URL)
            .method(Connection.Method.GET)
            .build();

        return connectionService2
            .submit(connectionRequest)
            .getDocument()
            .select(CLASS_PATTERN)
            .stream()
            .map(this::extractOffer)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());
    }
    
    private Optional<JobOffer> extractOffer(Element jobOffer)
    {
        String jobTitle = jobOffer.getElementsByTag(HEADER).attr(TITLE);

        if (pattern.matcher(jobTitle).find()) {

            String[] companyWithLocation = jobOffer
                    .getElementsByTag(SPAN)
                    .get(0)
                    .html()
                    .split(BR);

            return JobOffer
                    .builder()
                    .providerType(ProviderType.SKILL_HUNT)
                    .title(jobTitle)
                    .company(companyWithLocation[0])
                    .location(Arrays.asList(companyWithLocation[1].split(COMMA)).get(0))
                    .href(jobOffer.attr(ABS_HREF))
                    .buildOptional();
        } else {
            return Optional.empty();
        }
    }
}
