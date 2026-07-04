package ec.edu.espe.banquito.core.party.client;

import ec.edu.espe.banquito.core.party.grpc.accountlookup.AccountLookupResponse;
import ec.edu.espe.banquito.core.party.grpc.accountlookup.AccountLookupServiceGrpc;
import ec.edu.espe.banquito.core.party.grpc.accountlookup.GetAccountByNumberRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AccountLookupGrpcClient {

    private final AccountLookupServiceGrpc.AccountLookupServiceBlockingStub accountLookupService;

    public AccountLookupGrpcClient(
            @Value("${account-core.grpc.host:localhost}") String accountCoreGrpcHost,
            @Value("${account-core.grpc.port:9091}") int accountCoreGrpcPort) {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(accountCoreGrpcHost, accountCoreGrpcPort)
                .usePlaintext()
                .build();

        this.accountLookupService = AccountLookupServiceGrpc.newBlockingStub(channel);
    }

    public AccountLookupResponse getAccountByNumber(String accountNumber) {
        GetAccountByNumberRequest request = GetAccountByNumberRequest.newBuilder()
                .setAccountNumber(accountNumber)
                .build();

        return this.accountLookupService.getAccountByNumber(request);
    }
}