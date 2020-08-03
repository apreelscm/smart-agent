package pdfgenerator;

public class PdfCheckBox {
    private static final String UNCHECKED_STATE = "Off";
    private static final String DEFAULT_CHECKED_STATE = "Yes";
    private final String[] states;
    private final String uncheckedState;
    private final String checkedState;

    public static PdfCheckBox create(String[] states) {
        if (states != null) {
            return new PdfCheckBox(states);
        }
        return null;
    }

    private PdfCheckBox(String[] states) {
        this.states = states;
        if (states[0] != null && UNCHECKED_STATE.toLowerCase().equals(states[0].toLowerCase())) {
            uncheckedState = states[0];
            checkedState = states[1];
        } else if (states[1] != null && UNCHECKED_STATE.toLowerCase().equals(states[1].toLowerCase())) {
            uncheckedState = states[1];
            checkedState = states[0];
        } else {
            uncheckedState = UNCHECKED_STATE;
            checkedState = DEFAULT_CHECKED_STATE;
        }
    }

    public String unchecked() {
        return uncheckedState;
    }

    public String checked() {
        return checkedState;
    }
}
