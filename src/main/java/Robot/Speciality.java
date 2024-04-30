package Robot;

import java.util.Random;

public enum Speciality {
    STRENGTH,
    STEALTH,
    SNIPER,
    SHIELD,
    STATUE,
    SPEED,
    SKIT;

    public static Speciality randomSpeciality() {
        Random random = new Random();
        Speciality[] specialities = Speciality.values();
        return specialities[random.nextInt(specialities.length)];
    }
}