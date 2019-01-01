package scripts.demchickens.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Locatable;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import scripts.demchickens.Task;

public abstract class Generic extends Task<ClientContext> {
    private final int CLOSED_GATE_ID = 1558;
    private final int[] BOUNDS = {115, 118, -120, -20, -85, 85};

    public Generic(ClientContext ctx) {
        super(ctx);
    }

    protected boolean playerIsIdle() {
        return ctx.players.local().animation() == -1 && !ctx.players.local().inMotion() && !ctx.players.local().interacting().valid();

    }

    protected void openGate(final Locatable locatable) {
        final GameObject closedGate = ctx.objects.select().id(CLOSED_GATE_ID).nearest(locatable).within(10).poll();
        if (closedGate.valid()) {
            if (!closedGate.inViewport()) {
                ctx.movement.step(closedGate);
                Condition.wait(() -> closedGate.inViewport(), 400, 30);
            }

            closedGate.bounds(BOUNDS);
            closedGate.interact("Open", "Gate");
            Condition.wait(() -> !closedGate.valid() || isReachable(locatable), 350, 30);
        }
    }

    boolean isReachable(Locatable l) {
        Tile t = l.tile();
        ctx.objects.select().at(t);
        //filters to only include gates
        ctx.objects.select(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.type() == GameObject.Type.BOUNDARY && gameObject.id() == (CLOSED_GATE_ID);
            }
        });
        if (ctx.objects.isEmpty() && t.matrix(ctx).reachable()) {
            return true;
        }
        return false;
    }


}
