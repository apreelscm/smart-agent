package com.eservice.eumowy.enums.options

enum AcceptorRelation implements RadioOption {

    OWNS_ACCEPTOR,
    CONTROLS_ACCEPTOR,
    HAS_OVER_QUARTER_OF_VOTES;

    @Override
    String getMessageCode() {
        return "acceptor.relation." + name()
    }
}
