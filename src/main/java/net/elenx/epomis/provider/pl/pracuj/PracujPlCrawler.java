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
    private static final String WHITESPACE_CHAR = "\\s";
    private static final String SPACE_CHAR = " ";
    private static final String JAVA = "JAVA";
    private static final String WARSZAWA = "WARSZAWA";
    private static final int FIRST_INDEX = 0;
    private static final String COMMA = ",";
    private static final String URL = "https://www.pracuj.pl/praca/java;kw/warszawa;wp";
    private static final String ID = "mainOfferList";
    private static final String CLASS_OFFER = "o-list_item";
    private static final String CLASS_OFFER_EMPLOYER = "o-list_item_link_emp";
    private static final String CLASS_MANY_LOCATION = "ul.acc_cnt.o-list--inner";
    private static final String CLASS_OFFER_NAME = "o-list_item_link_name";
    private static final String CLASS_LOCATION_NAME = "span.o-list_item_desc_location_name.latlng";
    private static final String ATTR_HREF = "href";
    private static final String ATTR_TITLE = "title";
    private static final String ATTR_DATA_ADDRESS = "data-address";
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
                                .getElementsByClass(CLASS_OFFER)
                                .stream()
                                .map(this::extractOffer)
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .collect(Collectors.toSet());
    }
    
    private Optional<JobOffer> extractOffer(Element jobOffer)
    {
        
        String jobTitle = jobOffer.getElementsByClass(CLASS_OFFER_NAME).get(FIRST_INDEX).text();
        
        if(isJavaOffer(jobOffer, jobTitle))
        {
            
            String company = jobOffer.getElementsByClass(CLASS_OFFER_EMPLOYER).get(FIRST_INDEX).text();
            Optional<String> href = extractHrefFromOffer(jobOffer);
            Optional<String> location = extractLocationFromOffer(jobOffer);
            
            if(location.isPresent() && href.isPresent())
            {
                
                return JobOffer
                    .builder()
                    .providerType(ProviderType.PRACUJ_PL)
                    .title(jobTitle)
                    .href(href.get())
                    .location(location.get())
                    .company(company)
                    .buildOptional();
            }
            
        }
        
        return Optional.empty();
        
    }
    
    private boolean isJavaOffer(Element jobOffer, String jobTitle)
    {
        return !jobOffer.getElementsByClass(CLASS_OFFER_EMPLOYER).isEmpty()
               && jobTitle.replaceAll(WHITESPACE_CHAR, SPACE_CHAR).toUpperCase().contains(JAVA);
    }
    
    private Optional<String> extractHrefFromOffer(Element jobOffer)
    {
        
        if(isSingleOfferClass(jobOffer))
        {
            return Optional.of(jobOffer.getElementsByClass(CLASS_OFFER_NAME).get(FIRST_INDEX).attr(ATTR_HREF));
        }
        
        return jobOffer.getElementsByClass(CLASS_OFFER_NAME)
                       .stream()
                       .filter(hrefElement -> hrefElement.attr(ATTR_TITLE).toUpperCase().contains(WARSZAWA))
                       .map(hrefElement -> hrefElement.attr(ATTR_HREF))
                       .findFirst();
        
    }
    
    private Optional<String> extractLocationFromOffer(Element jobOffer)
    {
        
        if(isSingleOfferClass(jobOffer))
        {
            Optional<String> location = Optional.of(jobOffer.select(CLASS_LOCATION_NAME)
                                                            .attr(ATTR_DATA_ADDRESS)
                                                            .split(COMMA)[0]);
            
            if(location.isPresent())
            {
                return location.get().toUpperCase().contains(WARSZAWA) ? location : Optional.empty();
            }
        }
        
        return jobOffer.getElementsByClass(CLASS_OFFER_NAME)
                       .stream()
                       .map(locationElement -> locationElement.attr(ATTR_TITLE).split(COMMA)[FIRST_INDEX])
                       .filter(locationString -> locationString.toUpperCase().contains(WARSZAWA))
                       .findFirst();
    }
    
    private boolean isSingleOfferClass(Element jobOffer)
    {
        return jobOffer.select(CLASS_MANY_LOCATION).isEmpty();
    }
    
}




