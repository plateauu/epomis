package net.elenx.epomis.provider.pl.pracuj;

import lombok.Data;
import net.elenx.connection2.ConnectionRequest;
import net.elenx.connection2.ConnectionService2;
import net.elenx.epomis.entity.JobOffer;
import net.elenx.epomis.model.ProviderType;
import net.elenx.epomis.provider.JobOfferProvider;
import org.jsoup.Connection;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Component
class PracujPlCrawler implements JobOfferProvider
{
    private static final String AWL = "%s?awl=true";
    private static final String URL = "https://www.pracuj.pl/praca/java;kw/warszawa;wp";
    private static final String ID = "mainOfferList";
    private static final String CLASS = "o-list_item";

    private final ConnectionService2 connectionService;

    @Override
    public Set<JobOffer> offers()
    {
        ConnectionRequest connectionRequest = ConnectionRequest
                .builder()
                .url(URL)
                .method(Connection.Method.GET)
                .build();

        return connectionService.submit(connectionRequest)
                .getDocument()
                .getElementById(ID)
                .getElementsByClass(CLASS)
                .stream()
                .map(this::extractOffer)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }


    private Optional<JobOffer> extractOffer(Element jobOffer)
    {
        Element name = jobOffer.getElementsByClass("o-list_item_link_name").get(0);
        String href = name.attributes().get("href");

        if (href == null  || href.length() == 0)
        {
            return Optional.empty();
        }

        String jobTitle = name.text();

        if (jobTitle.equals(new String("..."))){
            return Optional.empty();
        }

        String company = jobOffer.getElementsByClass("o-list_item_link_emp").get(0).text();



        return Optional.of(JobOffer
                .builder()
                .providerType(ProviderType.PRACUJ_PL)
                .title(jobTitle)
                .href(href)
                .company(company)
                .build());
    }
}
