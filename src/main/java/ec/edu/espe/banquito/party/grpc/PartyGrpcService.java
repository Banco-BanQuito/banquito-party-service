package ec.edu.espe.banquito.party.grpc;

import ec.edu.espe.banquito.party.client.AccountLookupGrpcClient;
import ec.edu.espe.banquito.party.grpc.accountlookup.AccountLookupResponse;
import ec.edu.espe.banquito.party.grpc.party.AccountHolderResponse;
import ec.edu.espe.banquito.party.grpc.party.CustomerResponse;
import ec.edu.espe.banquito.party.grpc.party.GetCustomerByAccountRequest;
import ec.edu.espe.banquito.party.grpc.party.GetCustomerRequest;
import ec.edu.espe.banquito.party.grpc.party.PartyServiceGrpc;
import ec.edu.espe.banquito.party.model.Customer;
import ec.edu.espe.banquito.party.repository.CustomerRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyGrpcService extends PartyServiceGrpc.PartyServiceImplBase {

    private final CustomerRepository customerRepository;
    private final AccountLookupGrpcClient accountLookupGrpcClient;

    @Override
    public void getCustomer(GetCustomerRequest request, StreamObserver<CustomerResponse> responseObserver) {
        Integer customerId = Math.toIntExact(request.getCustomerId());

        Customer customer = this.customerRepository.findById(customerId).orElse(null);

        if (customer == null) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("Cliente no encontrado con id: " + customerId)
                            .asRuntimeException()
            );
            return;
        }

        CustomerResponse response = CustomerResponse.newBuilder()
                .setCustomerId(customer.getId())
                .setCustomerType(customer.getCustomerType() != null ? customer.getCustomerType().getValue() : "")
                .setIdentification(customer.getIdentification() != null ? customer.getIdentification() : "")
                .setFirstName(customer.getFirstName() != null ? customer.getFirstName() : "")
                .setLastName(customer.getLastName() != null ? customer.getLastName() : "")
                .setEmail(customer.getEmail() != null ? customer.getEmail() : "")
                .setStatus(customer.getStatus() != null ? customer.getStatus().getValue() : "")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getCustomerByAccount(GetCustomerByAccountRequest request, StreamObserver<AccountHolderResponse> responseObserver) {
        try {
            AccountLookupResponse accountResponse = this.accountLookupGrpcClient.getAccountByNumber(request.getAccountNumber());

            Integer customerId = Math.toIntExact(accountResponse.getCustomerId());

            Customer customer = this.customerRepository.findById(customerId).orElse(null);

            if (customer == null) {
                responseObserver.onError(
                        Status.NOT_FOUND
                                .withDescription("Cliente no encontrado con id: " + customerId)
                                .asRuntimeException()
                );
                return;
            }

            AccountHolderResponse response = AccountHolderResponse.newBuilder()
                    .setAccountId(accountResponse.getAccountId())
                    .setAccountNumber(accountResponse.getAccountNumber())
                    .setHolderName(this.buildFullName(customer))
                    .setHolderIdentification(customer.getIdentification() != null ? customer.getIdentification() : "")
                    .setAccountType(customer.getCustomerType() != null ? customer.getCustomerType().getValue() : "")
                    .setStatus(accountResponse.getStatus() != null ? accountResponse.getStatus() : "")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception exception) {
            responseObserver.onError(
                    Status.UNAVAILABLE
                            .withDescription("No se pudo consultar la cuenta en account-core-service")
                            .withCause(exception)
                            .asRuntimeException()
            );
        }
    }

    private String buildFullName(Customer customer) {
        if (customer.getCustomerType() != null && "JURIDICO".equals(customer.getCustomerType().getValue())) {
            return customer.getLegalName() != null ? customer.getLegalName() : "";
        }

        return ((customer.getFirstName() != null ? customer.getFirstName() : "") + " " +
                (customer.getLastName() != null ? customer.getLastName() : "")).trim();
    }
}