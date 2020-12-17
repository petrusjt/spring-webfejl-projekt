package hu.unideb.entities;

public enum Level {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    int level;
    Level(int level)
    {
        this.level = level;
    }

    public int getOrdinal()
    {
        return level;
    }

    public static Level getLevelFromInt(int level) {
        switch(level)
        {
            case 1:
                return LOW;
            case 2:
                return MEDIUM;
            case 3:
                return HIGH;
            default:
                throw new IllegalArgumentException();
        }
    }
}
