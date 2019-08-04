package cn.vbill.takin.drools.core;

import org.drools.compiler.kie.builder.impl.KieServicesImpl;
import org.kie.api.builder.KieRepository;

/**
 * 
 * @author renjinhao
 *
 */
public class LocalKieServicesImpl extends KieServicesImpl {
	
	public KieRepository getRepository() {
        return LocalKieRepositoryImpl.INSTANCE;
    }
}
