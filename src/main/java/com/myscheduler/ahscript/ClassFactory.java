package com.myscheduler.ahscript;

import com4j.COM4J;

/**
 * Defines methods to create COM objects
 */
public abstract class ClassFactory {
    private ClassFactory() {} // instanciation is not allowed


    /**
     * ActiveHome Class
     */
    public static IActiveHome createActiveHome() {
        return COM4J.createInstance( IActiveHome.class, "{001000AF-2DEF-0208-10B6-DC5BA692C858}" );
    }
}
