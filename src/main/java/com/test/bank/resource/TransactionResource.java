package com.test.bank.resource;


import com.test.bank.constant.Role;
import com.test.bank.model.admin.AdminUserVo;
import com.test.bank.service.TransactionService;
import com.test.bank.tool.config.EnvConfigManager;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Principal;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource extends BaseResource {

    TransactionService transactionService;

    @Inject
    public TransactionResource(EnvConfigManager envConfigManager,
                               TransactionService transactionService) {
        super(envConfigManager);
        this.transactionService = transactionService;
    }


    @POST
    @Path("/transfer")
    @RolesAllowed(Role.TRANSFER)
    public Response transfer(@Auth Principal adminUser,
                             @QueryParam("fromUserId") int fromUserId,
                             @QueryParam("toUserId") int toUserId,
                             @QueryParam("amount") int amount) {
        int adminId = ((AdminUserVo) adminUser).getId();
        transactionService.transfer(fromUserId, toUserId, amount, adminId);
        return Response.ok().build();
    }

    @GET
    @Path("/log/{userId}")
    @RolesAllowed(Role.VIEW_TRANSACTION_LOG)
    public Response log(@Auth Principal adminUser,
                        @PathParam("userId") int userId) {
        transactionService.getTransactionLog(userId);
        return Response.ok().build();
    }

    @POST
    @Path("/creditDebit")
    @RolesAllowed(Role.CREDIT_AND_DEBIT)
    public Response creditAndDebit(@Auth Principal adminUser,
                                   @QueryParam("userId") int userId,
                                   @QueryParam("amount") int amount) {
        int adminId = ((AdminUserVo) adminUser).getId();
        transactionService.creditAndDebit(userId, amount, adminId);
        return Response.ok().build();
    }
}
