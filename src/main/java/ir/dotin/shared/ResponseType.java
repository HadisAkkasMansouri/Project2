package ir.dotin.shared;


public enum ResponseType {

    SUCCESS("Transaction completed successfully!"),
    INADEQUATE_AMOUNT("Not enough balance!"),
    UPPER_BOUND("Upper bound balance restriction violated!"),
    INVALID_TRANSACTION("Invalid transaction type!"),
    UNDEFINED_DEPOSIT("Undefined deposit!");

    private String description;

    ResponseType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
