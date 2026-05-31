package edu.polytech.filrouge_teamM.navigation;

public interface Notifiable {
    int ACTION_OPEN_DETAIL = 1;
    int ACTION_OPEN_LOCATION_WITH_PHOTO = 3;
    int ACTION_OPEN_DESCRIPTION = 4;
    int ACTION_OPEN_SUMMARY = 6;
    int ACTION_SEND_REPORT = 7;
    int ACTION_OPEN_SENT_DETAIL = 8;
    int ACTION_START_CAMERA = 9;
    int ACTION_OPEN_LOCATION_WITHOUT_PHOTO = 10;

    void onClick(int numFragment);
    void onDataChange(int numFragment, Object object, int actionCode, Object argsAction);
    void onFragmentDisplayed(int fragmentId);
}
