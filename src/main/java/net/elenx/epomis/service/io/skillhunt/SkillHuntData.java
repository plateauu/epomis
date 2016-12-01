package net.elenx.epomis.service.io.skillhunt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
class SkillHuntData {

    private SkillHuntDataLinks links;
    private SkillHuntDataApplication application;
    private String type;

    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class SkillHuntDataLinks {
        private String offer;
    }

    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class SkillHuntDataApplication {
        private String workerEmail;
        private String workerName;
        private String message;
        private SkillHuntDataApplicationContact contact;

        @Data
        @Builder
        @JsonIgnoreProperties(ignoreUnknown = true)
        static class SkillHuntDataApplicationContact {
            private String phone;
            private String country;
            private String city;
        }
    }


}
