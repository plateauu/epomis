package net.elenx.epomis.service.messagebox.olx

import net.elenx.epomis.service.messagebox.olx.util.TimeStampConverter
import spock.lang.Specification

class TimeStampConverterTest extends Specification {

    def "check parse of local date time"() {
        given:
        def input = "17 paź, 14:51"

        when:
        def result = TimeStampConverter.convert(input)

        then:
        result != null
        result == "2017-10-17 14:51"
    }
}
