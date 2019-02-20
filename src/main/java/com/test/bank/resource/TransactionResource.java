package com.test.bank.resource;


import com.test.bank.model.TransferRequest;
import com.test.bank.model.TransferResponse;
import com.test.bank.service.AdminService;
import com.test.bank.service.TransactionService;
import com.test.bank.tool.config.EnvConfigManager;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource extends BaseResource {

    TransactionService transactionService;
    AdminService adminService;

    @Inject
    public TransactionResource(EnvConfigManager envConfigManager,
                               TransactionService transactionService,
                               AdminService adminService) {
        super(envConfigManager);
        this.transactionService = transactionService;
        this.adminService = adminService;
    }


    @POST
    @Path("/transfer")
    public Response transfer(
            @NotNull @HeaderParam("Token") String token,
            @NotNull @Valid TransferRequest transferRequest) {
        boolean auth = adminService.authenticate(token);
        if (!auth) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        TransferResponse transferResponse = transactionService.transfer(transferRequest.getFromUserId(), transferRequest.getToUserId(), transferRequest.getAmount());
        return Response.ok(transferResponse).build();
    }

}
