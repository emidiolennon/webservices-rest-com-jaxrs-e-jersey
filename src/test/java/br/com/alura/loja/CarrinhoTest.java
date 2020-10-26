package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

public class CarrinhoTest {

	private HttpServer server;
	private WebTarget target;
	private Client client;

	@Before
	public void startServidor() {
		server = Servidor.inicializaServidor();
	}

	@Test
	public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperado() {

		ClientConfig config = new ClientConfig();
		config.register(new LoggingFilter());	
		this.client = ClientBuilder.newClient(config);
		target = client.target("http://localhost:8080");
		//String conteudo = target.path("/carrinhos/1").request().get(String.class);
		Carrinho carrinho = target.path("/carrinhos/1").request().get(Carrinho.class);

		System.out.println(carrinho);

		//Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);

		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}

	@Test
	public void testaQueSuportaNovosCarrinhos() {

		this.client = ClientBuilder.newClient();
		target = client.target("http://localhost:8080");

		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
		carrinho.setRua("Rua Vergueiro");
		carrinho.setCidade("Sao Paulo");
		//String xml = carrinho.toXML();
		//Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);
		Entity<Carrinho> entity = Entity.entity(carrinho, MediaType.APPLICATION_XML);

        Response response = target.path("/carrinhos").request().post(entity);
        Assert.assertEquals(201, response.getStatus());
        
        String location = response.getHeaderString("location");
        //String conteudo = client.target(location).request().get(String.class);
        Carrinho carrinhoCarregado = client.target(location).request().get(Carrinho.class);
        
        Assert.assertEquals("Tablet", carrinhoCarregado.getProdutos().get(0).getNome());
        //Assert.assertTrue(conteudo.contains("Tablet"));
        //Assert.assertEquals("<status>sucesso</status>", response.readEntity(String.class));
	}

	@After
	public void stopServidor() {
		server.stop();
	}
}
