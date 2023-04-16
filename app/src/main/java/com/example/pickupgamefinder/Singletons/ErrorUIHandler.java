package com.example.pickupgamefinder.Singletons;

import com.example.pickupgamefinder.ui.Fragments.PopupNotificationFragment;

public class ErrorUIHandler {
    private static volatile ErrorUIHandler INSTANCE = null;

        private ErrorUIHandler() {}

        public static ErrorUIHandler getInstance() {
            if(INSTANCE == null) {
                synchronized (ErrorUIHandler.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new ErrorUIHandler();
                    }
                }
            }
            return INSTANCE;
        }

        private PopupNotificationFragment popupFragment;

        public void setPopupFragment(PopupNotificationFragment popupFragment) {
            this.popupFragment = popupFragment;
        }

        public void showError(String errorMessage)
        {
            popupFragment.showPopup(errorMessage);
        }
}