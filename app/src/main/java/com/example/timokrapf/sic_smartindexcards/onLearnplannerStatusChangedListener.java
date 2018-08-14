package com.example.timokrapf.sic_smartindexcards;

public interface onLearnplannerStatusChangedListener {
    public void onUpdateCountdownView(String toUpdate);

    public void onResetCountdownView(int stringID);

    public void onEggTimerFinished();
}
