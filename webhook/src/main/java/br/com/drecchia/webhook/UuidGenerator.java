package br.com.drecchia.webhook;

import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class UuidGenerator implements Processor {

	@Override
	public void process(Exchange ex) throws Exception {
		ex.getIn().setHeader("uuid", UUID.randomUUID().toString());
	}

}
