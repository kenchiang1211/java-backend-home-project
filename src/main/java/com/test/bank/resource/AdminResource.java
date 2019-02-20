package com.test.bank.resource;


import com.test.bank.model.AdminUser;
import com.test.bank.service.AdminService;
import com.test.bank.tool.config.EnvConfigManager;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class AdminResource extends BaseResource {

    private AdminService adminService;

    @Inject
    public AdminResource(EnvConfigManager envConfigManager,
                         AdminService adminService) {
        super(envConfigManager);
        this.adminService = adminService;
    }


    @POST
    @Path("/login")
    public Response doLogin(@NotNull @Valid AdminUser adminUser) {
        String token = adminService.login(adminUser.getAccount(), adminUser.getPassword());
        if (token == null) {
            throw new BadRequestException();
        }
        return Response.ok().entity(token).build();
    }
}
