package com.mycorp.examples.hello.ds.configurer;

import java.io.IOException;
import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;

import com.mycorp.examples.hello.IHello;
import com.mycorp.examples.hello.model.HelloMessage;

@Component(
	enabled = true
	,immediate = true
	,property = {
		"service.exported.interfaces=*"
		,"service.exported.configs=ecf.jaxrs.jersey.server"
		,"ecf.jaxrs.jersey.server.urlContext=http://localhost:8080"
        , "ecf.jaxrs.jersey.server.alias=/helloconf"
		,"service.pid=com.mycorp.examples.hello.ds.host.HelloComponent"
	}
)
public class HelloConfigurable implements IHello, ManagedService {
	private String id;
    private String database;
    private String user;
    private String password;
    private String create;

	public HelloConfigurable() {
	}


    /**
     * <pre>HTTP Request  (URL = urlContext + alias + "/" + interfacename + "/" + methodname)
     * GET http://localhost:8080/hello/ihello/hello
     *
     *HTTP Response
     * Text Body
     *      Hello service host says 'Hi' back to WWWWWWWW
     * </pre>
     */
	@Override
    public String hello() {
		return "Hello service host says 'Hi' back to WWWWWWWW";
	}


    /**
     * <pre>HTTP Request
     * GET http://localhost:8080/hello/ihello/hello2
     *
     *HTTP Response
     * JSON Body
     * {
     *   "from": "RRR",
     *   "message": "EEE",
     *   "array": [
     *      1,
     *      1,
     *      2,
     *      3,
     *      5,
     *      8,
     *      13,
     *      21
     *  ]
     * }
     * </pre>
     */
    @Override
    public HelloMessage hello2() {
        return new HelloMessage("RRR", "EEE");
    }


    /**
     * <pre>HTTP Request
     * POST http://localhost:8080/hello/ihello/hello3
     * Headers
     *      Content-Type: application/json
     * JSON Body
     * {
     *   "from": "DDD",
     *   "message": "WWS",
     *   "array": [
     *      1,
     *      21
     *  ]
     * }
     *
     *HTTP Response
     * JSON Body
     * {
     *   "from": "RRR",
     *   "message": "EEE",
     *   "array": [
     *      1,
     *      1,
     *      2,
     *      3,
     *      5,
     *      8,
     *      13,
     *      21
     *  ]
     * }
     * </pre>
     */
    @Override
    public HelloMessage hello3(String from) {
        return new HelloMessage("RRR", "EEE");
    }

    /**
     * <pre>HTTP Request
     * POST http://localhost:8080/hello/ihello/hello4
     * Headers
     *      Content-Type: application/json
     * JSON Body
     * {
     *   "from": "DDD",
     *   "message": "WWS",
     *   "array": [
     *      1,
     *      21
     *  ]
     * }
     *
     *HTTP Response
     * Headers
     *      Content-Type: application/json
     * JSON Body
     * {
     *   "from": "DDD",
     *   "message": "WWS",
     *   "array": [
     *      1,
     *      21
     *  ]
     * }
     * </pre>
     */
    @Override
    public HelloMessage hello4(HelloMessage message) {
        return message;
    }

	@Activate
	public void activate(ComponentContext context) throws IOException {
		Dictionary<String, Object> properties = context.getProperties();
		//properties.put("database.id", "wewe");
        id = (String) properties.get("database.id");
        database = (String) properties.get("database");
        user = (String) properties.get("user");
        password = (String) properties.get("password");
        create = (String)properties.get("create");
		System.out.println("Hello service started");
	}
	@Deactivate
	public void deactivate(ComponentContext context) {
		System.out.println("Hello service stopped");
	}
	@Modified
	public void modify() {
		System.out.println("Hello service modified");
	}

	@Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		System.out.println(properties);
	}

//    @Override
//    public String getHello5(String urlToken, String urlLol, String text, String headerIfMatch, String queryPageSize) {
//        // TODO Auto-generated method stub
//        return null;
//    }
}
