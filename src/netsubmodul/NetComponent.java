package netsubmodul;

import logicmodule.OpcodeHandler;

/**
 * Created by sandor on 2014.12.13..
 */
public abstract class NetComponent {

    protected OpcodeHandler msgHandler;

    public void setOpcodeHandler (OpcodeHandler handler ) {

        this.msgHandler = handler;

    }

}
