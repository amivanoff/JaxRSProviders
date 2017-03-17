package com.mycorp.examples.hello.ds.host;

import java.io.IOException;
import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
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
            , "service.pid=com.mycorp.examples.hello.ds.host.HelloComponent"
            ,"ecf.jaxrs.jersey.server.urlContext=http://localhost:8080"
            , "ecf.jaxrs.jersey.server.alias=/helloo"
            , "ecf.jaxrs.jersey.server.uri=http://localhost:8080/hello"
	}
)
public class HelloComponent
    implements IHello {

	public HelloComponent() {
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

	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		System.out.println(properties);
	}

    @Override
    public String getHello5(/*@PathParam("token")*/ String urltoken, String urllol, String text,
        String headerIf_Match, String queryPageSize) {
        System.err.println("received hello from=" + urltoken); //$NON-NLS-1$
        return "Hello " + urltoken + " " + urllol + " " + text + " " + headerIf_Match; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
}
