package application.jobcompare.screens;

public interface Screen {
    // show() is not needed as this is done via onCreate() within Activity Screens.
//    void show();

    void cancel();

    void save();

    // reset() is not needed as this is handled natively.
//    void reset();

}
