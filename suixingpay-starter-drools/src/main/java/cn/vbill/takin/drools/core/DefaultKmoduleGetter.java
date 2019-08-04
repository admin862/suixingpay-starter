package cn.vbill.takin.drools.core;
/**
 * 
 * @author renjinhao
 *
 */
public class DefaultKmoduleGetter implements KmoduleGetter {

	//@formatter:off
	private static final String KMODULE_CONTENT = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
		  + "<kmodule xmlns=\"http://jboss.org/kie/6.0.0/kmodule\">\r\n"
		  + "		<kbase name=\"defaultKbase\" packages=\"*\" default=\"true\">\r\n"
		  + "			<ksession name=\"defaultKsession\" default=\"true\"/>\r\n" 
		  + " 		<ksession name=\"defaultStatelessKsession\" type=\"stateless\" default=\"true\"/>\r\n" 
		  + " 	</kbase>\r\n" 
		  + "</kmodule>";
	//@formatter:on
	
	@Override
	public String get() {
		return KMODULE_CONTENT;
	}

}
