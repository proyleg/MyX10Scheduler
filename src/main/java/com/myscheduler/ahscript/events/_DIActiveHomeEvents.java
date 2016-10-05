package com.myscheduler.ahscript.events;

import com4j.DISPID;
import com4j.DefaultMethod;
import com4j.IID;

/**
 * _DIActiveHomeEvents Interface
 */
@IID("{001000AF-3DEF-0911-10B6-DC5BA692C858}")
public abstract class _DIActiveHomeEvents {
    // Methods:
    /**
     * <p>
     * method RecvAction - Called when commands have been received
     * </p>
     * @param bszAction Mandatory java.lang.Object parameter.
     * @param bszParm1 Mandatory java.lang.Object parameter.
     * @param bszParm2 Mandatory java.lang.Object parameter.
     * @param bszParm3 Mandatory java.lang.Object parameter.
     * @param bszParm4 Mandatory java.lang.Object parameter.
     * @param bszParm5 Mandatory java.lang.Object parameter.
     * @param bszReserved Mandatory java.lang.Object parameter.
     */

    @DISPID(0)
    @DefaultMethod
    public void recvAction(
            Object bszAction,
            Object bszParm1,
            Object bszParm2,
            Object bszParm3,
            Object bszParm4,
            Object bszParm5,
            Object bszReserved) {
        throw new UnsupportedOperationException();
    }


    // Properties:
}
