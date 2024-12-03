package urlshotner.lambda.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import urlshotner.lambda.entity.Urls;
import urlshotner.lambda.repository.UrlsRepository;

import java.net.URI;

@Path("/{token: [0-9A-Fa-f]{20}}")
public class RedirectController {

    @Inject
    private UrlsRepository urlsRepository;

    @GET
    public Response redirect(@PathParam("token") String token) {
        Urls urls = urlsRepository.findByUniqueToken(token);
        if (urls != null) {
            try {
                URI uri = new URI(urls.getUrlOriginal());
                return Response.seeOther(uri).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new ErrorResponse("Invalid URL format."))
                        .build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Token not found."))
                    .build();
        }
    }

    // Classe interna para respostas de erro
    public static class ErrorResponse {
        private String message;

        public ErrorResponse() {}

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
