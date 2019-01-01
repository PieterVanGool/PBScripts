package scripts.demchickens.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Player;
import scripts.demchickens.DemChickens;

import java.awt.*;

public class Antiban extends Generic {


    public Antiban(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return Random.nextDouble() > 0.97 && playerIsIdle();
    }

    @Override
    public void execute() {
        doAntibanAction(Random.nextInt(1, 10));
        DemChickens.setStatus("Anti-ban action");
        Condition.sleep(Random.nextInt(500, 1800));
    }

    public void doAntibanAction(int antibanAction) {
        if (antibanAction == 1) {
            hoverOverSkill(); // hover over prayer skill
        } else if (antibanAction == 2) {
            int minDistance = Random.nextInt(50, 100);      //
            int maxDistance = Random.nextInt(250, 300);     // random mouse movement
            randomMouseMovement(minDistance, maxDistance);  //
        } else if (antibanAction == 3) {
            hoverOverRandomPlayer();    // hover over random player
        } else if (antibanAction == 4) {
            randomCameraTurn();
        } else if (antibanAction == 5) {
            moveMouseOffScreen();
        } else if (antibanAction == 6) {
            standIdle();
        } else if (antibanAction == 7) {
            setRunning();
        } else if (antibanAction == 8) {
            examineObject();
        } else if (antibanAction == 9) {
            activePrayer();
        } else if (antibanAction == 10) {
            xpTabel();
        }
    }

    public void hoverOverSkill() {
        if (!ctx.game.tab(Game.Tab.STATS)) {
            ctx.game.tab(Game.Tab.STATS);
        }
        if (ctx.widgets.component(320, 5).valid()) {
            ctx.widgets.component(320, 5).hover();
            Condition.sleep(Random.nextInt(750, 2500));
        }
    }

    /**
     * Author - Enfilade Moves the mouse a random distance between minDistance
     * and maxDistance from the current position of the mouse by generating
     * random vector and then multiplying it by a random number between
     * minDistance and maxDistance. The maximum distance is cut short if the
     * mouse would go off screen in the direction it chose.
     *
     * @param minDistance The minimum distance the cursor will move
     * @param maxDistance The maximum distance the cursor will move (exclusive)
     */
    public void randomMouseMovement(int minDistance, int maxDistance) {
        double xvec = Math.random();
        if (Random.nextInt(0, 2) == 1) {
            xvec = -xvec;
        }
        double yvec = Math.sqrt(1 - xvec * xvec);
        if (Random.nextInt(0, 2) == 1) {
            yvec = -yvec;
        }
        double distance = maxDistance;
        Point p = ctx.input.getLocation();
        int maxX = (int) Math.round(xvec * distance + p.x);
        distance -= Math.abs((maxX - Math.max(0,
                Math.min(ctx.game.dimensions().getWidth(), maxX)))
                / xvec);
        int maxY = (int) Math.round(yvec * distance + p.y);
        distance -= Math.abs((maxY - Math.max(0,
                Math.min(ctx.game.dimensions().getHeight(), maxY)))
                / yvec);
        if (distance < minDistance) {
            return;
        }
        distance = Random.nextInt(minDistance, (int) distance);
        ctx.input.move((int) (xvec * distance) + p.x, (int) (yvec * distance) + p.y);
    }

    public void hoverOverRandomPlayer() {
        Player playerToHover = ctx.players.select().nearest().poll();
        if (!playerToHover.inViewport()) {
            ctx.camera.turnTo(playerToHover);
        }
        if (playerToHover.valid()) {
            playerToHover.hover();
            Condition.sleep(Random.nextInt(1500, 4000));
        }
    }

    /**
     * Author: Ryukis215
     */
    public void randomCameraTurn() {
        int randNum = Random.nextInt(1, 2);
        if (randNum == 1) {//light camera turn
            ctx.camera.angle(ctx.camera.yaw() + Random.nextInt(-100, 100));
        }
        if (randNum == 2) {//aggressive camera turn
            randNum = Random.nextInt(2, 6);
            for (int i = 0; i < randNum; i++) {
                ctx.camera.angle(ctx.camera.yaw() + Random.nextInt(-100, 100));
                Condition.sleep(Random.nextInt(250, 500));
            }
        }
    }

    public void moveMouseOffScreen() {
        int x, y;
        if (Random.nextDouble() > 0.50) {
            x = Random.nextInt(-1000, -100);
            y = Random.nextInt(-1000, -100);
        } else {
            x = Random.nextInt(1000, 1500);
            y = Random.nextInt(1000, 1500);
        }
        ctx.input.move(x, y);
        ctx.input.defocus();
        Condition.sleep(Random.nextInt(7500, 15000));
        ctx.input.focus();
        x = Random.nextInt(10, 510);
        y = Random.nextInt(10, 330);
        ctx.input.move(x, y);
        Condition.sleep(Random.nextInt(2500, 5000));
    }

    public void standIdle() {
        Condition.sleep(Random.nextInt(2125, 6015));
    }

    public void examineObject() {
        GameObject ex = ctx.objects.select(10).nearest().poll();
        ex.interact("Examine", ex.name());
    }

    public void setRunning() {
        if (ctx.movement.energyLevel() > Random.nextInt(0, 63) && ctx.movement.running(true)) {
            ctx.movement.running(false);
            Condition.sleep(Random.nextInt(250, 1895));
        }
        if (ctx.movement.energyLevel() > Random.nextInt(0, 45) && ctx.movement.running(false)) {
            ctx.movement.running(true);
            Condition.sleep(Random.nextInt(250, 1895));
        }
    }

    public void activePrayer() {
        ctx.widgets.component(160, 16).click();
        Condition.sleep(Random.nextInt(350, 1250));
        ctx.widgets.component(160, 16).click();
        Condition.sleep(Random.nextInt(350, 1250));
    }

    public void xpTabel() {
        ctx.widgets.component(160, 1).click();
        Condition.sleep(Random.nextInt(350, 1250));
    }
}
