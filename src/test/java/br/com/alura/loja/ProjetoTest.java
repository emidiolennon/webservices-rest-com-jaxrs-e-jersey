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

import br.com.alura.loja.modelo.Projeto;

public class ProjetoTest {

	private HttpServer server;

	@Before
	public void startServidor() {
		server = Servidor.inicializaServidor();
	}

	@Test
	public void testaQueBuscarUmProjetoTrazOProjetoEsperado() {

		ClientConfig config = new ClientConfig();
		config.register(new LoggingFilter());
		Client client = ClientBuilder.newClient(config);
		WebTarget target = client.target("http://localhost:8080");
		// String conteudo = target.path("/projetos/1").request().get(String.class);
		Projeto projeto = target.path("/projetos/1").request().get(Projeto.class);

		System.out.println(projeto);

		// Projeto projeto = (Projeto) new XStream().fromXML(conteudo);

		Assert.assertEquals("Minha loja", projeto.getNome());
	}

	@After
	public void stopServidor() {
		server.stop();
	}
}
