package org.scrum.psd.battleship.controller.dto;

public enum Letter {
    A("A"), B("B"), C("C"), D("D"), E("E"), F("F"), G("G"), H("H");

    private final String value;
    Letter(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
