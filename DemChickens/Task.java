package scripts.demchickens;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.ClientContext;

public abstract class Task<C extends ClientContext> extends ClientAccessor<C> {
    public Task(C ctx) {
        super(ctx);
    }

    protected boolean failed = false;

    public abstract boolean activate();

    public abstract void execute();


}
