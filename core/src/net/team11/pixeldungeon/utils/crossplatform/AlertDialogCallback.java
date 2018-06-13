package net.team11.pixeldungeon.utils.crossplatform;

public abstract class AlertDialogCallback {
    public abstract void positiveButtonPressed();
    public void negativeButtonPressed(){} // This will not be required
    public void cancelled(){} // This will not be required

}
