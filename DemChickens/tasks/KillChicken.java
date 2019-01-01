package scripts.demchickens.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.*;
import scripts.demchickens.DemChickens;

public class KillChicken extends Generic {
    private int[] chickenIds = {2692, 2693}; // more chicken ids will be added later
    private static int chickensKilled = 0;
    private static int preChickensKilled = 0;
    private static int killsBeforeLoot = 0; //Loot after first chicken
    private static int maxRandomKillB4Loot = 0;

    public KillChicken(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return playerIsIdle() && !ctx.inventory.isFull() && !PickupLoot.getStartLoot();
    }

    @Override
    public void execute() {
        DemChickens.setStatus("Killing chicken.");
        int hitpointsXp = ctx.skills.experience(Constants.SKILLS_HITPOINTS);
        if (ctx.movement.energyLevel() > Random.nextInt(0, 55) && Random.nextDouble() > 0.95) {
            ctx.movement.running(true);
            Condition.sleep(Random.nextInt(250, 1895));
        }
        if (Random.nextDouble() > 0.82) {
            ctx.camera.pitch(true);
        }

        final Npc chicken = ctx.npcs.select().id(chickenIds).select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return npc.interacting().equals(ctx.players.local()) || (!npc.interacting().valid() && npc.healthPercent() > 0);
            }
        }).nearest().poll();

        if (!isReachable(chicken)) {
            openGate(chicken);
            if (!isReachable(chicken)) {
                return;
            }
        }
        if (!chicken.inViewport()) {
            ctx.movement.step(chicken);
            ctx.camera.turnTo(chicken);
            Condition.wait(chicken::inViewport, 200, 20);
        }
        chicken.interact("Attack", "Chicken");
        Condition.wait(() -> chicken.healthPercent() == 0
                || playerIsIdle(), 250, 150);
        Condition.sleep(Random.nextInt(500, 1500));
        System.out.println("randomsleep over.");

        if (hitpointsXp != ctx.skills.experience(Constants.SKILLS_HITPOINTS)) {
            chickensKilled++;
            if (chickensKilled - preChickensKilled == killsBeforeLoot) {
                preChickensKilled = chickensKilled;
                PickupLoot.setStartLoot(true);
            }
            return;
        }
    }

    /*private boolean chickensAround()
    {
        if(ctx.npcs.select().id(chickenIds).select(new Filter<Npc>()
        {
            @Override
            public boolean accept(Npc npc)
            {
                return !npc.interacting().valid() && npc.healthPercent() > 0;
            }
        }).isEmpty())
        {
            return false;
        }
        return true;
    }*/

    public static int getChickensKilled() {
        return chickensKilled;
    }

    public static void setKillsBeforeLoot(int kills) {
        killsBeforeLoot = kills;
    }

    public static void setMaxRandomKillB4Loot(int maxKills) {
        maxRandomKillB4Loot = maxKills;
    }

    public static int getMaxRandomKillB4Loot() {
        return maxRandomKillB4Loot;
    }

    public static int getKillsBeforeLoot() {
        return KillChicken.killsBeforeLoot;
    }


}
