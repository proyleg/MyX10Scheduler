package com.myscheduler.ahscript;

import com4j.*;

/**
 * _IActiveHomeEvents Interface
 */
@IID("{001000AF-3DEF-0912-10B6-DC5BA692C858}")
public interface _IActiveHomeEvents extends Com4jObject {
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

    @VTID(3)
    @DefaultMethod
    void recvAction(
            @MarshalAs(NativeType.VARIANT) Object bszAction,
            @MarshalAs(NativeType.VARIANT) Object bszParm1,
            @MarshalAs(NativeType.VARIANT) Object bszParm2,
            @MarshalAs(NativeType.VARIANT) Object bszParm3,
            @MarshalAs(NativeType.VARIANT) Object bszParm4,
            @MarshalAs(NativeType.VARIANT) Object bszParm5,
            @MarshalAs(NativeType.VARIANT) Object bszReserved);


    // Properties:
}
