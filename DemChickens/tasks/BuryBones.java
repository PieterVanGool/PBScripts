package scripts.demchickens.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Item;
import scripts.demchickens.DemChickens;

public class BuryBones extends Generic {
    private static int bonesBuried = 0;
    private final int BONES_ID = 526;
    private final int FEATHER_ID = 314;
    private static boolean buryNow = false;

    //
    public BuryBones(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return (playerIsIdle() && ctx.inventory.isFull()) || (playerIsIdle() && buryNow);
    }

    @Override
    public void execute() {
        DemChickens.setStatus("Burry bones.");
        if (!ctx.game.tab(Game.Tab.INVENTORY)) {
            ctx.game.tab(Game.Tab.INVENTORY);
        }
        //bury bones in the inventory
        int prayerXp = ctx.skills.experience(Constants.SKILLS_PRAYER);
        while (ctx.inventory.select().id(BONES_ID).count() != 0) {
            ctx.inventory.select().id(BONES_ID).poll().interact("Bury", "Bones");
            Condition.sleep(Random.nextInt(450, 1500));
            if (prayerXp < ctx.skills.experience(Constants.SKILLS_PRAYER)) {
                bonesBuried++;
            }
        }
        //dropping all items in inventory after burry of bones (keeps feathers)
        Item items[] = ctx.inventory.items();
        int[] itemsToDrop = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            if (items[i].id() != FEATHER_ID)
                itemsToDrop[i] = items[i].id();
        }
        while (ctx.inventory.select().id(itemsToDrop).count() != 0) {
            DemChickens.setStatus("Dropping items.");
            ctx.inventory.select().id(itemsToDrop).poll().interact("Drop");
            Condition.sleep(Random.nextInt(800, 1500));
        }
        buryNow = false;
    }

    public static int getBonesBuried() {
        return bonesBuried;
    }

    public static void setBuryNow(boolean buryNow) {
        BuryBones.buryNow = buryNow;
    }
}

