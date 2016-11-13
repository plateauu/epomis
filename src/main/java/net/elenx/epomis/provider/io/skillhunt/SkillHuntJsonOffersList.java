package net.elenx.epomis.provider.io.skillhunt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class SkillHuntJsonOffersList {

    @JsonProperty
    private List<SkillHuntJsonOffer> data;
}
