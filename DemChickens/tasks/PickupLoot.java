package scripts.demchickens.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;
import scripts.demchickens.DemChickens;

public class PickupLoot extends Generic {
    private final int BONES_ID = 526;
    private final int FEATHER_ID = 314;
    private static int featherCount = 0;
    private static boolean bonesActive = true;
    private static boolean featherActive = true;
    private static boolean startLoot = false;
    private int lootProgress = 0;

    public PickupLoot(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return playerIsIdle() && !ctx.inventory.isFull() && startLoot;
    }

    @Override
    public void execute() {
        if (bonesActive && featherActive) {
            pickup(BONES_ID, FEATHER_ID);
        } else if (bonesActive && !featherActive) {
            pickup(BONES_ID);
        } else {
            pickup(FEATHER_ID);
        }
        startLoot = false;
    }

    private void pickup(int... itemIds) {
        DemChickens.setStatus("Picking loot from chickens.");
        while (lootProgress < KillChicken.getKillsBeforeLoot()) {
            for (int itemId : itemIds) {
                final GroundItem groundItem = ctx.groundItems.select().id(itemId).nearest().within(10).poll();

                if (!groundItem.valid()) {
                    continue;
                }
                if (!isReachable(groundItem)) {
                    openGate(groundItem);
                    if (!isReachable(groundItem)) {
                        continue;
                    }
                }
                if (groundItem.interact("Take", groundItem.name()) & groundItem.name().equals("Feather")) {
                    featherCount = featherCount + groundItem.stackSize();
                }
                Condition.wait(() -> !groundItem.valid() || playerIsIdle(), 200, 25);
                Condition.sleep(Random.nextInt(250, 1250));
            }
            lootProgress++;
        }
        if (Random.nextDouble() >= 0.90) {
            BuryBones.setBuryNow(true);
        }
        lootProgress = 0;
        KillChicken.setKillsBeforeLoot(Random.nextInt(1, KillChicken.getMaxRandomKillB4Loot())); //change the kills before loot
        startLoot = false;
    }

    public static void setActive(boolean featherActive, boolean bonesActive) {
        PickupLoot.featherActive = featherActive;
        PickupLoot.bonesActive = bonesActive;
    }

    public static int getFeatherCount() {
        return featherCount;
    }

    public static void setStartLoot(boolean startLoot) {
        PickupLoot.startLoot = startLoot;
    }

    public static boolean getStartLoot() {
        return startLoot;
    }

}
