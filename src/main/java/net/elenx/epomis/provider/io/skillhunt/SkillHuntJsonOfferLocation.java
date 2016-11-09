package net.elenx.epomis.provider.io.skillhunt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkillHuntJsonOfferLocation {

    private String city;


}
