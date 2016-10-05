package com.myscheduler.ahscript;

import com4j.*;

/**
 * IActiveHome Interface
 */
@IID("{001000AF-3DEF-0910-10B6-DC5BA692C858}")
public interface IActiveHome extends Com4jObject {
    // Methods:
    /**
     * <p>
     * method SendAction - use to send commands
     * </p>
     * @param bszAction Mandatory java.lang.Object parameter.
     * @param bstrParam Optional parameter. Default value is 0
     * @param vReserved1 Optional parameter. Default value is 0
     * @param vReserved2 Optional parameter. Default value is 0
     * @return  Returns a value of type java.lang.Object
     */

    @DISPID(0) //= 0x0. The runtime will prefer the VTID if present
    @VTID(7)
    @DefaultMethod
    @ReturnValue(type=NativeType.VARIANT)
    Object sendAction(
            @MarshalAs(NativeType.VARIANT) Object bszAction,
            @Optional @DefaultValue("0") @MarshalAs(NativeType.VARIANT) Object bstrParam,
            @Optional @DefaultValue("0") @MarshalAs(NativeType.VARIANT) Object vReserved1,
            @Optional @DefaultValue("0") @MarshalAs(NativeType.VARIANT) Object vReserved2);


    /**
     * <p>
     * Setter method for the COM property "OnRecvAction"
     * </p>
     * @param rhs Mandatory com4j.Com4jObject parameter.
     */

    @DISPID(1) //= 0x1. The runtime will prefer the VTID if present
    @VTID(8)
    void onRecvAction(
            @MarshalAs(NativeType.Dispatch) Com4jObject rhs);


    /**
     * <p>
     * method EnumerateInterfaces
     * </p>
     * @param pCount Mandatory java.lang.Object parameter.
     */

    @DISPID(2) //= 0x2. The runtime will prefer the VTID if present
    @VTID(9)
    void enumerateInterfaces(
            Object pCount);


    /**
     * <p>
     * method GetInterfaceName
     * </p>
     * @param pInterfaceName Mandatory java.lang.Object parameter.
     */

    @DISPID(3) //= 0x3. The runtime will prefer the VTID if present
    @VTID(10)
    void getInterfaceName(
            Object pInterfaceName);


    // Properties:
}
