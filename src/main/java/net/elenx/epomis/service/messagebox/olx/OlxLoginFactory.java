package net.elenx.epomis.service.messagebox.olx;

import net.elenx.connection5.DataEntry;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
class OlxLoginFactory {

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String DEFAULT = "Default username";

    List<DataEntry> createLoginData(String login, String password) {
        return Arrays.asList(
                new DataEntry(LOGIN, login),
                new DataEntry(PASSWORD, password)
        );
    }

    String getLoginFromData(List<DataEntry> data) {
        return data.stream()
                .filter(dataEntry -> dataEntry.getKey().equals(LOGIN))
                .findFirst()
                .map(DataEntry::getValue)
                .orElse(DEFAULT);
    }
}
