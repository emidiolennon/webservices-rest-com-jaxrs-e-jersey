package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClienteTest {

	private HttpServer server;
	private WebTarget target;
	private Client client;
	
	@Before
	public void startServidor() {
		server = Servidor.inicializaServidor();
	}
	
	@Test
	public void testaQueAConexaoComOServidorFunciona() {
		
		ClientConfig config = new ClientConfig();
		config.register(new LoggingFilter());		
		this.client = ClientBuilder.newClient(config);
		this.target = client.target("https://mocky.io");
		String conteudo = target.path("/v2/52aaf5deee7ba8c70329fb7d").request().get(String.class);
		
		System.out.println(conteudo);
		
		Assert.assertTrue(conteudo.contains("<rua>Rua Vergueiro 3185"));		
	}
	
	@After
	public void stopServidor() {
		server.stop();
	}
}
