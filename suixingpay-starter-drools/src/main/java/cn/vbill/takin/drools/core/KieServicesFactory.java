package cn.vbill.takin.drools.core;

import org.kie.api.KieServices;

/**
 * 
 * @author renjinhao
 *
 */
public class KieServicesFactory {
	private static KieServices INSTANCE;

    static {
        try {
            INSTANCE = ( KieServices ) Class.forName( "cn.vbill.takin.drools.core.LocalKieServicesImpl" ).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to instance KieServices", e);
        }
    }

    /**
     * Returns a reference to the KieServices singleton
     */
    public static KieServices get() {
        return INSTANCE;
    }
}
