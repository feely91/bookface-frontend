package com.kainos.training.dropwizard.login.frontends.resources;

import java.net.URI;
import java.net.URISyntaxException;

import io.dropwizard.views.View;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.codahale.metrics.annotation.Timed;
import com.kainos.training.dropwizard.login.frontends.views.Index;
import com.kainos.training.dropwizard.login.frontends.views.LoginFailureView;
import com.kainos.training.dropwizard.login.frontends.views.LoginSuccessView;
import com.kainos.training.jersey.client.BaseClient;

@Path("/")
public class ViewsResource {
	
	private BaseClient baseClient;	
	
	public ViewsResource(BaseClient loginClient) {
		baseClient = loginClient ;
	}

	@GET
	@Timed
	@Path("/login")
	@Produces(MediaType.TEXT_HTML)	
	public View login() {
		return new Index();
	}
	
	@POST
	@Timed
	@Path("login-details")
	@Produces(MediaType.TEXT_HTML)
	public Response loginDetails(@FormParam("username") String username,
			 			     @FormParam("password") String password) throws URISyntaxException{
		
		System.out.println("USERNAME LOGIN-DETAILS = " + username);
		System.out.println("PASSWORD LOGIN-DETAILS = " + password);
		

		Response response = baseClient.getLogin(username, password);
		
		if(response.getStatus() == Status.OK.getStatusCode())
		{
			return Response.seeOther(new URI("/login-success")).build();
		}
		else
		{
			return Response.seeOther(new URI("/login-failure")).build();
		}
	}
	
	@GET
	@Timed
	@Path("/login-success")
	@Produces(MediaType.TEXT_HTML)	
	public View loginSuccess() {
		return new LoginSuccessView();
	}
	
	@GET
	@Timed
	@Path("/login-failure")
	@Produces(MediaType.TEXT_HTML)	
	public View loginFailure() {
		return new LoginFailureView();
	}
}
