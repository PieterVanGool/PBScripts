package scripts.demchickens;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import scripts.demchickens.tasks.Antiban;
import scripts.demchickens.tasks.BuryBones;
import scripts.demchickens.tasks.KillChicken;
import scripts.demchickens.tasks.PickupLoot;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(
        name = "dem chickens", properties = "author=TheUndaed; topic=999; client=4;",
        description = "Kills chickens,loot feathers and bury bones.")

public class DemChickens extends PollingScript<ClientContext> implements PaintListener {
    private List<Task> taskList = new ArrayList<Task>();
    private static String status = "starting script";

    public void start() {
        final DemChickensGUI gui = new DemChickensGUI();
        gui.setVisible(true);
        Condition.wait(gui::done, 1000, 60); //waiting Gui to be done
        KillChicken.setKillsBeforeLoot(Random.nextInt(1, KillChicken.getMaxRandomKillB4Loot()));

        //adding all tasks to tasklist
        taskList.add(new PickupLoot(ctx));
        taskList.add(new BuryBones(ctx));
        taskList.add(new KillChicken(ctx));
        taskList.add(new Antiban(ctx));
    }

    public void poll() {
        for (Task task : taskList) {
            if (ctx.controller.isStopping()) {
                break;
            }
            if (task.activate()) {
                System.out.println("active task: " + task.toString()); // just for debugging
                task.execute();
            }
        }

    }

    public void repaint(Graphics graphics) {
        final Graphics2D g = (Graphics2D) graphics;

        int chickensKilled = KillChicken.getChickensKilled();
        int bonesBuried = BuryBones.getBonesBuried();
        int feathersLooted = PickupLoot.getFeatherCount();
        int chickensHr = (int) ((chickensKilled * 3600000D) / getRuntime());
        int feathersHr = (int) ((feathersLooted * 3600000D) / getRuntime());
        int bonesBuriedHr = (int) ((bonesBuried * 3600000D) / getRuntime());
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        g.setColor(Color.BLACK);
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g.setComposite(alphaComposite);
        g.fillRect(5, 25, 200, 120);
        g.setColor(Color.WHITE);

        g.drawString(String.format("Chickens Killed: %d (%,d /Hr)", chickensKilled, chickensHr), 10, 40);
        g.drawString(String.format("Feathers Looted: %d (%,d /Hr)", feathersLooted, feathersHr), 10, 60);
        g.drawString(String.format("Bones Buried: %d (%,d /Hr)", bonesBuried, bonesBuriedHr), 10, 80);
        g.drawString(String.format("Kills till loot %d ", KillChicken.getKillsBeforeLoot()), 10, 100);
        g.drawString(String.format("status:  %s ", status), 10, 120);
        g.drawString("Running: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 20, 140);


    }

    public static void setStatus(String status) {
        DemChickens.status = status;
    }
}