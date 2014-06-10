package enums;

public enum AcceptorLocation {
    COUNTRY("acceptor.location.country"), ABROAD("acceptor.location.abroad");

    String messageCode;

    AcceptorLocation(String messageCode) {
        this.messageCode = messageCode;
    }
}
