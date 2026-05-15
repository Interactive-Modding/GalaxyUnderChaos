package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.ship.NovadiveEngineLayout;
import server.galaxyunderchaos.ship.CustomizableShip;
import server.galaxyunderchaos.ship.ShipColorSection;
import server.galaxyunderchaos.ship.ShipCustomization;
import server.galaxyunderchaos.sound.ModSounds;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class NovadiveEntity extends Entity implements GeoEntity, CustomizableShip {
    public static final int CONTROL_FORWARD = 1;
    public static final int CONTROL_BACKWARD = 1 << 1;
    public static final int CONTROL_LEFT = 1 << 2;
    public static final int CONTROL_RIGHT = 1 << 3;
    public static final int CONTROL_ASCEND = 1 << 4;
    public static final int CONTROL_DESCEND = 1 << 5;
    public static final int CONTROL_ROLL_LEFT = 1 << 6;
    public static final int CONTROL_ROLL_RIGHT = 1 << 7;
    public static final int CONTROL_BOOST = 1 << 8;

    private static final EntityDataAccessor<Integer> CONTROLS = SynchedEntityData.defineId(NovadiveEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> ENGINE_POWER = SynchedEntityData.defineId(NovadiveEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> RENDER_PITCH = SynchedEntityData.defineId(NovadiveEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> RENDER_ROLL = SynchedEntityData.defineId(NovadiveEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(NovadiveEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> BASE_COLOR = SynchedEntityData.defineId(NovadiveEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PRIMARY_COLOR = SynchedEntityData.defineId(NovadiveEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SECONDARY_COLOR = SynchedEntityData.defineId(NovadiveEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> INTERIOR_COLOR = SynchedEntityData.defineId(NovadiveEntity.class, EntityDataSerializers.INT);

    private static final double SEAT_LOCAL_X = 0.0D;
    private static final double SEAT_LOCAL_Y = 1.4D;
    private static final double SEAT_LOCAL_Z = -0.8D;

    private static final int ENGINE_STARTUP_TICKS_REQUIRED = 100;
    private static final float AIRBORNE_IDLE_ENGINE_POWER = 0.35F;
    private static final double STATIONARY_SPEED_SQR = 0.0036D;

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

    private float cockpitProgress;
    private float cockpitProgressOld;
    private float gearProgress;
    private float gearProgressOld;
    private int idleSoundCooldown;
    private int engineStartupTicks;

    public NovadiveEntity(EntityType<? extends NovadiveEntity> type, Level level) {
        super(type, level);
        this.blocksBuilding = false;
    }

    public NovadiveEntity(Level level, double x, double y, double z) {
        this(galaxyunderchaos.NOVADIVE_ENTITY.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(CONTROLS, 0);
        this.entityData.define(ENGINE_POWER, 0.0F);
        this.entityData.define(RENDER_PITCH, 0.0F);
        this.entityData.define(RENDER_ROLL, 0.0F);
        this.entityData.define(HEALTH, 60.0F);
        this.entityData.define(BASE_COLOR, ShipCustomization.DEFAULT_BASE);
        this.entityData.define(PRIMARY_COLOR, ShipCustomization.DEFAULT_PRIMARY);
        this.entityData.define(SECONDARY_COLOR, ShipCustomization.DEFAULT_SECONDARY);
        this.entityData.define(INTERIOR_COLOR, ShipCustomization.DEFAULT_INTERIOR);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }

        if (!this.level().isClientSide) {
            player.startRiding(this, true);
            this.level().playSound(null, this.blockPosition(), ModSounds.SHIP_ENGINE_START.get(), SoundSource.NEUTRAL, 0.35F, 1.0F);
        }

        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);

        if (!this.level().isClientSide && !this.hasControllingPassenger()) {
            this.setControls(0);
            this.engineStartupTicks = 0;
        }
    }

    @Override
    public void tick() {
        super.tick();

        this.cockpitProgressOld = this.cockpitProgress;
        this.gearProgressOld = this.gearProgress;

        this.tickAnimationTargets();
        this.tickFlight();

        if (this.level().isClientSide) {
//            this.spawnEngineParticles();
            return;
        }

        this.checkInsideBlocks();
    }

    private void tickAnimationTargets() {
        float cockpitTarget = this.shouldOpenCockpit() ? 1.0F : 0.0F;
        float gearTarget = this.shouldRetractLandingGear() ? 1.0F : 0.0F;

        this.cockpitProgress = approach(this.cockpitProgress, cockpitTarget, 0.12F);
        this.gearProgress = approach(this.gearProgress, gearTarget, 0.09F);
    }

    private static float approach(float current, float target, float speed) {
        if (current < target) {
            return Math.min(target, current + speed);
        }

        return Math.max(target, current - speed);
    }

    private boolean shouldOpenCockpit() {
        if (this.hasControllingPassenger()) {
            return false;
        }

        AABB range = this.getBoundingBox().inflate(4.0D, 2.5D, 4.0D);
        List<Player> players = this.level().getEntitiesOfClass(Player.class, range, player -> !player.isSpectator() && player.getVehicle() != this);
        return !players.isEmpty();
    }

    private boolean shouldRetractLandingGear() {
        if (this.onGround()) {
            return false;
        }

        return !this.isLandingSurfaceClose(2.0D);
    }

    private boolean isLandingSurfaceClose(double maxDistance) {
        AABB box = this.getBoundingBox();

        int minX = Mth.floor(box.minX + 0.15D);
        int maxX = Mth.floor(box.maxX - 0.15D);
        int minZ = Mth.floor(box.minZ + 0.15D);
        int maxZ = Mth.floor(box.maxZ - 0.15D);

        int startY = Mth.floor(box.minY);
        int endY = Mth.floor(box.minY - maxDistance);

        for (int y = startY; y >= endY; y--) {
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);

                    if (!this.level().getBlockState(pos).getCollisionShape(this.level(), pos).isEmpty()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void tickFlight() {
        int controls = this.entityData.get(CONTROLS);
        boolean hasPilot = this.hasControllingPassenger();

        if (!hasPilot) {
            controls = 0;
            this.entityData.set(CONTROLS, 0);
        }

        this.tickEngineStartup(controls, hasPilot);

        float targetEngine = this.getTargetEnginePower(controls, hasPilot);
        this.setEnginePower(Mth.lerp(0.08F, this.getEnginePower(), targetEngine));

        if (!hasPilot) {
            this.entityData.set(RENDER_PITCH, Mth.lerp(0.08F, this.getRenderPitch(), 0.0F));
            this.entityData.set(RENDER_ROLL, Mth.lerp(0.08F, this.getRenderRoll(), 0.0F));

            Vec3 drag = this.getDeltaMovement().multiply(0.94D, this.onGround() ? 0.0D : 0.96D, 0.94D);

            if (!this.onGround()) {
                drag = drag.add(0.0D, -0.035D, 0.0D);
            }

            this.setDeltaMovement(drag);
            this.move(MoverType.SELF, this.getDeltaMovement());
            return;
        }

        boolean forward = (controls & CONTROL_FORWARD) != 0;
        boolean backward = (controls & CONTROL_BACKWARD) != 0;
        boolean left = (controls & CONTROL_LEFT) != 0;
        boolean right = (controls & CONTROL_RIGHT) != 0;
        boolean ascend = (controls & CONTROL_ASCEND) != 0;
        boolean descend = (controls & CONTROL_DESCEND) != 0;
        boolean rollLeft = (controls & CONTROL_ROLL_LEFT) != 0;
        boolean rollRight = (controls & CONTROL_ROLL_RIGHT) != 0;
        boolean boost = (controls & CONTROL_BOOST) != 0;

        /*
         * Engine startup rule:
         *
         * Holding controls spins the engines up, but the ship cannot move, lift,
         * turn, or boost until the startup timer reaches 100 ticks.
         */
        if (!this.isEngineStarted()) {
            forward = false;
            backward = false;
            left = false;
            right = false;
            ascend = false;
            descend = false;
            rollLeft = false;
            rollRight = false;
            boost = false;
        }

        float yawTurn = 0.0F;

        if (left) {
            yawTurn += 3.25F;
        }

        if (right) {
            yawTurn -= 3.25F;
        }

        if (boost) {
            yawTurn *= 0.72F;
        }

        this.setYRot(this.getYRot() + yawTurn);
        this.yRotO = this.getYRot();

        Vec3 look = Vec3.directionFromRotation(0.0F, this.getYRot());

        double thrust = 0.0D;

        if (forward) {
            thrust -= boost ? 0.105D : 0.072D;
        }

        if (backward) {
            thrust += 0.035D;
        }

        Vec3 motion = this.getDeltaMovement();
        motion = motion.add(look.x * thrust, 0.0D, look.z * thrust);

        if (ascend) {
            motion = motion.add(0.0D, boost ? 0.115D : 0.082D, 0.0D);

            if (this.onGround() && motion.y < 0.20D) {
                motion = new Vec3(motion.x, boost ? 0.34D : 0.24D, motion.z);
            }
        }

        if (descend) {
            motion = motion.add(0.0D, -0.065D, 0.0D);
        }

        if (!ascend && !descend && !this.onGround()) {
            motion = motion.add(0.0D, -0.008D, 0.0D);
        }

        this.fallDistance = 0.0F;

        double maxHorizontal = boost ? 1.85D : 1.25D;
        double horizontal = Math.sqrt(motion.x * motion.x + motion.z * motion.z);

        if (horizontal > maxHorizontal) {
            double scale = maxHorizontal / horizontal;
            motion = new Vec3(motion.x * scale, motion.y, motion.z * scale);
        }

        motion = new Vec3(
                motion.x * 0.985D,
                Mth.clamp(motion.y, -0.85D, boost ? 1.15D : 0.95D),
                motion.z * 0.985D
        );

        if (this.onGround() && !ascend && thrust == 0.0D) {
            motion = new Vec3(motion.x * 0.75D, 0.0D, motion.z * 0.75D);
        }

        this.setDeltaMovement(motion);
        this.move(MoverType.SELF, this.getDeltaMovement());

        float targetPitch = 0.0F;

        if (forward && ascend) {
            targetPitch = -7.0F;
        } else if (backward) {
            targetPitch = boost ? 14.0F : 8.0F;
        }

        float targetRoll = 0.0F;

        if (left || rollLeft) {
            targetRoll += 18.0F;
        }

        if (right || rollRight) {
            targetRoll -= 18.0F;
        }

        if (rollLeft) {
            this.setYRot(this.getYRot() + 2.0F);
        }

        if (rollRight) {
            this.setYRot(this.getYRot() - 2.0F);
        }

        this.entityData.set(RENDER_PITCH, Mth.lerp(0.12F, this.getRenderPitch(), targetPitch));
        this.entityData.set(RENDER_ROLL, Mth.lerp(0.14F, this.getRenderRoll(), targetRoll));

        if (!this.level().isClientSide && this.idleSoundCooldown-- <= 0 && this.getEnginePower() > 0.25F) {
            this.level().playSound(
                    null,
                    this.blockPosition(),
                    ModSounds.SHIP_ENGINE_LOOP.get(),
                    SoundSource.NEUTRAL,
                    0.18F,
                    0.92F + this.getEnginePower() * 0.16F
            );

            this.idleSoundCooldown = 36;
        }
    }

    private void tickEngineStartup(int controls, boolean hasPilot) {
        if (!hasPilot) {
            this.engineStartupTicks = 0;
            return;
        }

        /*
         * Do not instantly complete startup just because the ship leaves the ground.
         * Startup only advances while the pilot is pressing controls.
         */
        if (controls != 0) {
            this.engineStartupTicks = Math.min(ENGINE_STARTUP_TICKS_REQUIRED, this.engineStartupTicks + 1);
            return;
        }

        if (this.isGroundedAndStationary()) {
            this.engineStartupTicks = 0;
        }
    }

    private boolean isEngineStarted() {
        return this.engineStartupTicks >= ENGINE_STARTUP_TICKS_REQUIRED;
    }

    private float getTargetEnginePower(int controls, boolean hasPilot) {
        /*
         * No pilot = engines wind down, even if the ship is still falling or drifting.
         */
        if (!hasPilot) {
            return 0.0F;
        }

        /*
         * Pressing controls spins engines up during the 5 second startup.
         */
        if (controls != 0) {
            return 1.0F;
        }

        /*
         * With a pilot, engines only fully shut off when grounded and stationary.
         * In air, falling, landing, or sliding = keep idle engine power.
         */
        if (!this.isGroundedAndStationary()) {
            return AIRBORNE_IDLE_ENGINE_POWER;
        }

        return 0.0F;
    }

    private boolean isGroundedAndStationary() {
        return (this.onGround() || this.isLandingSurfaceClose(0.45D))
                && this.getDeltaMovement().lengthSqr() <= STATIONARY_SPEED_SQR;
    }

    private void spawnEngineParticles() {
        float power = this.getEnginePower();

        if (power <= 0.04F || this.random.nextFloat() > power) {
            return;
        }

        Vec3 backward = Vec3.directionFromRotation(0.0F, this.getYRot()).scale(-0.24D - power * 0.24D);

        for (NovadiveEngineLayout.EnginePoint engine : NovadiveEngineLayout.ENGINES) {
            Vec3 pos = this.localToWorld(engine.particleLocal());
            double spread = 0.025D + power * 0.025D;

            this.level().addParticle(
                    net.minecraft.core.particles.ParticleTypes.END_ROD,
                    pos.x,
                    pos.y,
                    pos.z,
                    backward.x + (this.random.nextDouble() - 0.5D) * spread,
                    backward.y + (this.random.nextDouble() - 0.5D) * spread,
                    backward.z + (this.random.nextDouble() - 0.5D) * spread
            );

            if (this.random.nextFloat() < 0.35F) {
                this.level().addParticle(
                        net.minecraft.core.particles.ParticleTypes.FLAME,
                        pos.x,
                        pos.y,
                        pos.z,
                        backward.x * 0.65D,
                        backward.y * 0.65D,
                        backward.z * 0.65D
                );
            }
        }
    }

    public Vec3 localToWorld(Vec3 local) {
        Vec3 rotated = this.rotateLocalByShipRender(local);

        double yaw = Math.toRadians(this.getYRot());
        double cos = Math.cos(yaw);
        double sin = Math.sin(yaw);

        double x = rotated.x * cos + rotated.z * sin;
        double z = rotated.x * sin - rotated.z * cos;

        return new Vec3(this.getX() + x, this.getY() + rotated.y, this.getZ() + z);
    }

    public void setControls(int controls) {
        this.entityData.set(CONTROLS, controls);
    }

    public int getControls() {
        return this.entityData.get(CONTROLS);
    }

    public float getEnginePower() {
        return this.entityData.get(ENGINE_POWER);
    }

    private void setEnginePower(float power) {
        this.entityData.set(ENGINE_POWER, Mth.clamp(power, 0.0F, 1.0F));
    }

    public float getRenderPitch() {
        return this.entityData.get(RENDER_PITCH);
    }

    public float getRenderRoll() {
        return this.entityData.get(RENDER_ROLL);
    }

    public float getCockpitProgress(float partialTick) {
        return Mth.lerp(partialTick, this.cockpitProgressOld, this.cockpitProgress);
    }

    public float getGearProgress(float partialTick) {
        return Mth.lerp(partialTick, this.gearProgressOld, this.gearProgress);
    }

    public float getHealth() {
        return this.entityData.get(HEALTH);
    }

    public void setHealth(float health) {
        this.entityData.set(HEALTH, Mth.clamp(health, 0.0F, 60.0F));
    }

    @Override
    public LivingEntity getControllingPassenger() {
        List<Entity> passengers = this.getPassengers();

        if (!passengers.isEmpty() && passengers.get(0) instanceof LivingEntity living) {
            return living;
        }

        return null;
    }

    public boolean isPilot(Entity entity) {
        return entity != null && entity == this.getControllingPassenger();
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction callback) {
        if (this.hasPassenger(passenger)) {
            Vec3 seat = this.localToWorld(new Vec3(SEAT_LOCAL_X, SEAT_LOCAL_Y, SEAT_LOCAL_Z));
            callback.accept(passenger, seat.x, seat.y, seat.z);
            this.alignPassengerToShip(passenger);
        }
    }

    private void alignPassengerToShip(Entity passenger) {
        float yaw = this.getYRot();

        if (passenger instanceof LivingEntity living) {
            living.setYBodyRot(yaw);
        }

        if (!(passenger instanceof Player)) {
            passenger.setYRot(yaw);
            passenger.yRotO = yaw;

            if (passenger instanceof LivingEntity living) {
                living.setYHeadRot(yaw);
            }
        }
    }

    private Vec3 rotateLocalByShipRender(Vec3 local) {
        Vec3 rolled = rotateAroundZ(local, Math.toRadians(this.getRenderRoll()));
        return rotateAroundX(rolled, Math.toRadians(this.getRenderPitch()));
    }

    private static Vec3 rotateAroundX(Vec3 vec, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double y = vec.y * cos - vec.z * sin;
        double z = vec.y * sin + vec.z * cos;

        return new Vec3(vec.x, y, z);
    }

    private static Vec3 rotateAroundZ(Vec3 vec, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double x = vec.x * cos - vec.y * sin;
        double y = vec.x * sin + vec.y * cos;

        return new Vec3(x, y, vec.z);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.level().isClientSide || this.isRemoved()) {
            return true;
        }

        if (this.isInvulnerableTo(source)) {
            return false;
        }

        this.setHealth(this.getHealth() - amount);
        this.markHurt();

        if (this.getHealth() <= 0.0F) {
            this.destroy(source);
        }

        return true;
    }

    private void destroy(DamageSource source) {
        if (this.level() instanceof ServerLevel serverLevel && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack drop = new ItemStack(galaxyunderchaos.NOVADIVE.get());
            ShipCustomization.saveToStack(this, drop, "novadive");
            this.spawnAtLocation(drop);
        }

        this.discard();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("Health", this.getHealth());
        tag.putFloat("EnginePower", this.getEnginePower());
        tag.putFloat("Pitch", this.getRenderPitch());
        tag.putFloat("Roll", this.getRenderRoll());
        tag.putInt("EngineStartupTicks", this.engineStartupTicks);
        tag.putInt("ShipBaseColor", this.getShipColor(ShipColorSection.BASE));
        tag.putInt("ShipPrimaryColor", this.getShipColor(ShipColorSection.PRIMARY));
        tag.putInt("ShipSecondaryColor", this.getShipColor(ShipColorSection.SECONDARY));
        tag.putInt("ShipInteriorColor", this.getShipColor(ShipColorSection.INTERIOR));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.setHealth(tag.contains("Health") ? tag.getFloat("Health") : 60.0F);
        this.setEnginePower(tag.getFloat("EnginePower"));
        this.entityData.set(RENDER_PITCH, tag.getFloat("Pitch"));
        this.entityData.set(RENDER_ROLL, tag.getFloat("Roll"));
        this.engineStartupTicks = tag.getInt("EngineStartupTicks");
        this.setShipColor(ShipColorSection.BASE, tag.contains("ShipBaseColor") ? tag.getInt("ShipBaseColor") : ShipCustomization.DEFAULT_BASE);
        this.setShipColor(ShipColorSection.PRIMARY, tag.contains("ShipPrimaryColor") ? tag.getInt("ShipPrimaryColor") : ShipCustomization.DEFAULT_PRIMARY);
        this.setShipColor(ShipColorSection.SECONDARY, tag.contains("ShipSecondaryColor") ? tag.getInt("ShipSecondaryColor") : ShipCustomization.DEFAULT_SECONDARY);
        this.setShipColor(ShipColorSection.INTERIOR, tag.contains("ShipInteriorColor") ? tag.getInt("ShipInteriorColor") : ShipCustomization.DEFAULT_INTERIOR);
    }

    @Override
    public int getShipColor(ShipColorSection section) {
        return switch (section) {
            case BASE -> this.entityData.get(BASE_COLOR);
            case PRIMARY -> this.entityData.get(PRIMARY_COLOR);
            case SECONDARY -> this.entityData.get(SECONDARY_COLOR);
            case INTERIOR -> this.entityData.get(INTERIOR_COLOR);
        };
    }

    @Override
    public void setShipColor(ShipColorSection section, int color) {
        int safeColor = ShipCustomization.clampColor(color);
        switch (section) {
            case BASE -> this.entityData.set(BASE_COLOR, safeColor);
            case PRIMARY -> this.entityData.set(PRIMARY_COLOR, safeColor);
            case SECONDARY -> this.entityData.set(SECONDARY_COLOR, safeColor);
            case INTERIOR -> this.entityData.set(INTERIOR_COLOR, safeColor);
        }
    }

    public boolean hasCustomShipColors() {
        return ShipCustomization.isCustomized(this);
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animationCache;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, net.minecraft.world.level.block.state.BlockState state, BlockPos pos) {
    }
}