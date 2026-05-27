package server.galaxyunderchaos.entity.forceuser;

public enum ForceUserRole {
    JEDI(ForceUserSide.LIGHT, false, false, false, false, 1.0F, 2),
    SITH(ForceUserSide.DARK, false, false, false, false, 1.0F, 2),
    NEUTRAL_FORCE_USER(ForceUserSide.NEUTRAL, false, false, false, false, 1.0F, 2),
    SITH_GHOST(ForceUserSide.DARK, true, false, false, false, 1.0F, 2),
    SITH_LORD_GHOST(ForceUserSide.DARK, true, true, false, false, 1.0F, 3),
    SITH_LORD(ForceUserSide.DARK, false, true, false, false, 1.0F, 3),
    JEDI_MASTER(ForceUserSide.LIGHT, false, true, false, false, 1.0F, 3),
    NEUTRAL_MASTER(ForceUserSide.NEUTRAL, false, true, false, false, 1.0F, 3),
    SITH_APPRENTICE(ForceUserSide.DARK, false, false, true, false, 0.78F, 1),
    JEDI_PADAWAN(ForceUserSide.LIGHT, false, false, false, true, 0.78F, 1),
    NEUTRAL_PADAWAN(ForceUserSide.NEUTRAL, false, false, false, true, 0.78F, 1);

    private final ForceUserSide side;
    private final boolean ghost;
    private final boolean boss;
    private final boolean apprentice;
    private final boolean padawan;
    private final float renderScale;
    private final int maxPowerTier;

    ForceUserRole(ForceUserSide side, boolean ghost, boolean boss, boolean apprentice, boolean padawan, float renderScale, int maxPowerTier) {
        this.side = side;
        this.ghost = ghost;
        this.boss = boss;
        this.apprentice = apprentice;
        this.padawan = padawan;
        this.renderScale = renderScale;
        this.maxPowerTier = maxPowerTier;
    }

    public ForceUserSide side() { return side; }
    public boolean isGhost() { return ghost; }
    public boolean isBoss() { return boss; }
    public boolean isStudent() { return apprentice || padawan; }
    public boolean isApprentice() { return apprentice; }
    public boolean isPadawan() { return padawan; }
    public boolean isNeutralTradition() { return side.isNeutral(); }
    public float renderScale() { return renderScale; }
    public int maxPowerTier() { return maxPowerTier; }
}
