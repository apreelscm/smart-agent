package enums;

public enum AcceptorPESELCountry {
    PESEL("pesel.label"), COUNTRY("country.label");

    String messageCode;

    AcceptorPESELCountry(String messageCode) {
        this.messageCode = messageCode;
    }
}
