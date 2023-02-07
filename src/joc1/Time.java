package joc1;

final class Time {
    static double targetFrameTime = 1 / 200.0;
    static double fixedDeltaTime = 1 / 200.0;
    private static double fps = 0;

    static boolean callingFromFixedUpdate = false;
    static double timeScale = 1;
    private static double unscaledDeltaTime = 0;
    private static double unscaledTime = 0;

    private static double deltaTime = 0;
    private static double time = 0;
    private static double unsimulatedTime = 0;


    static double targetFrameTime() {
        return targetFrameTime;
    }

    static double fixedDeltaTime() {
        return fixedDeltaTime;
    }

    static double fps() {
        return fps;
    }

    static double timeScale() {
        return timeScale;
    }
    // Les tres funcions de dalt només estan per poder utilitzar el mateix format que amb la resta de variables

    static double unscaledDeltaTime() {
        return unscaledDeltaTime;
    }

    static double unscaledTime() {
        return unscaledTime;
    }

    static double deltaTime() {
        if (callingFromFixedUpdate) {
            return fixedDeltaTime;
        }
        return deltaTime;
    }

    static double time() {
        return time;
    }

    static double unsimulatedTime() {
        return unsimulatedTime;
    }

    static void physicsStep() {
        unsimulatedTime -= fixedDeltaTime;
    }

    static void updateTimes(double realDeltaTime) {
        unscaledDeltaTime = realDeltaTime;
        deltaTime = unscaledDeltaTime * timeScale;
        unscaledTime += unscaledDeltaTime;
        time += deltaTime;
        unsimulatedTime += deltaTime;
        fps = 1/unscaledDeltaTime;
    }

    static String getFormattedTime(double time) {
        int mins = (int) (time/60);
        String minuteString = mins < 10 ? "0" + mins : Integer.toString(mins);
        int secs = (int) (time % 60);
        String secondsString = secs < 10 ? "0" + secs : Integer.toString(secs);
        return minuteString + ":" + secondsString;
    }
}
