package server.galaxyunderchaos.force;

public enum ForceSide {
    LIGHT,
    DARK,
    NEUTRAL,
    UNIVERSAL;

    public boolean isSelectableBranch() {
        return this != UNIVERSAL;
    }
}
