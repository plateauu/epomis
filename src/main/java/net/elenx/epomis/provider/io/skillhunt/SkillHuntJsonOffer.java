package net.elenx.epomis.provider.io.skillhunt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class SkillHuntJsonOffer {


    private String company;
    private String position;
    private String id;
    private SkillHuntJsonOfferLocation location;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    class SkillHuntJsonOfferLocation {

            private String city;

    }


}