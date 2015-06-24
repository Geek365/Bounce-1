package com.gishump.bounce;

public interface RequestHandler {
    void levelCompleteDialog(final DialogResult result, final int attempts);
    void noPurchasesAvailable(final DialogResult result);
    void levelSelector(final DialogResult result);
    void paymentPrompt(final DialogResult result);
    int showMenu();
    boolean arePurchasesAvailable();
    int getLevelsPaidFor();
    void userPayment();
}
