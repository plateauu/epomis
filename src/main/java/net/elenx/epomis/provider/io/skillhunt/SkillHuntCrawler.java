package net.elenx.epomis.provider.io.skillhunt;

import lombok.Data;
import lombok.extern.java.Log;
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
@Log
public class SkillHuntCrawler implements JobOfferProvider
{

    private static int counter = 0;
    private final Pattern pattern = Pattern.compile("\\b[Jj][Aa][Vv][Aa]\\b");
    private final ConnectionService2 connectionService2;

    @Override
    public Set<JobOffer> offers()
    {
        log.info("Start offers");

        ConnectionRequest connectionRequest = ConnectionRequest
            .builder()
            .url("https://app.skillhunt.io/jobs")
            .method(Connection.Method.GET)
            .build();


        log.info("koniec testow");

        return connectionService2
            .submit(connectionRequest)
            .getDocument()
            .select("div[class='offer-header padding-horizontal text-align-center cursor-pointer']")
            .stream()
            .map(this::extractOffer)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());
    }
    
    private Optional<JobOffer> extractOffer(Element jobOffer)
    {


        String jobTitle = jobOffer.getElementsByTag("header").attr("title");

        if(!pattern.matcher(jobTitle).find())
        {
            return Optional.empty();
        }

        log.info("oferta numer " + counter++);

        String[] companyWithLocation = jobOffer
            .getElementsByTag("span")
            .get(0)
            .html()
            .split("<br>");
        
        return JobOffer
            .builder()
            .providerType(ProviderType.SKILL_HUNT)
            .title(jobTitle)
            .company(companyWithLocation[0])
            .location(Arrays.asList(companyWithLocation[1].split(",")).get(0))
            .href(jobOffer.attr("abs:href"))
            .buildOptional();
    }
}
