package com.vanessameerkamp.vmworktimer.controller.helper;

import com.vanessameerkamp.vmworktimer.controller.listener_fragment.WorkTimerListener;
import com.vanessameerkamp.vmworktimer.model.Duration;

public class WorkTimer implements Runnable{

    private int duration;
    private boolean pause = false;

    WorkTimerListener workTimerListener;

    public WorkTimer(WorkTimerListener workTimerListener) {
        this.workTimerListener = workTimerListener;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public void run() {
        pause = false;
        duration = 0;

        while (true) {

            if (pause) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            duration++;

            workTimerListener.handler.post(() -> {
                workTimerListener.workTimerFragment.txtvTimer.setText(Duration.convert(duration));
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {

        duration = 0;
    }
}
