package vvsantos.fake.oauth2.server.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import vvsantos.fake.oauth2.server.service.JwtService;

@Controller("/oauth/token")
public class TokenController {

    private final JwtService jwtService;

    public TokenController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Get(produces = MediaType.TEXT_PLAIN) //
    public String index() {
        return jwtService.getFakeToken(); //
    }

}
