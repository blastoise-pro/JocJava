package joc1;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

class Animation {
    List<Sprite> frames = new ArrayList<>();
    private double duration;
    private double startTime = -1;
    boolean looping;

    double getDuration() {
        return duration;
    }

    void setDuration(double d) {
        duration = d;
    }

    Animation(List<BufferedImage> frames, Vec2 scale, double duration, boolean looping) {
        for (BufferedImage img:frames) {
            this.frames.add(new Sprite(img, scale));
        }
        this.duration = duration;
    }

    void play() {
        startTime = Time.time();
    }

    void stop() {
        startTime = -1;
    }

    boolean isPlaying() {
        if (startTime == -1) {
            return false;
        }
        if (Time.time() - startTime < duration) {
            return true;
        }
        return looping;
    }

    int getIndex() {
        if (startTime != -1) {
            double progress = (Time.time() - startTime) / duration;
            if (progress < 1) {
                return (int) (progress * frames.size());
            } else if (looping) {
                return (int) ((progress % 1) * frames.size());
            }
        }
        return -1;
    }

    Sprite getFrame() {
        if (startTime != -1) {
            double progress = (Time.time() - startTime) / duration;
            if (progress < 1) {
                return frames.get((int) (progress * frames.size()));
            } else if (looping) {
                return frames.get((int) ((progress % 1) * frames.size()));
            }
        }
        return null;
    }
}
