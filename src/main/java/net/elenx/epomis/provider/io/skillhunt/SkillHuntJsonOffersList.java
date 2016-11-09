package net.elenx.epomis.provider.io.skillhunt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SkillHuntJsonOffersList {

    @JsonProperty("data")
    private List<SkillHuntJsonOffer> data;
}
