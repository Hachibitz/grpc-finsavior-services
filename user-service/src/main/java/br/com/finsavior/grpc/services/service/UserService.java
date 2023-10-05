package br.com.finsavior.grpc.services.service;

import br.com.finsavior.grpc.services.model.entity.User;
import br.com.finsavior.grpc.services.repository.UserRepository;
import br.com.finsavior.grpc.user.*;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Optional;

@GrpcService
@Slf4j
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public void deleteAccount(DeleteAccountRequest request, StreamObserver<DeleteAccountResponse> responseObserver) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(request.getUsername()));

        if(user.isPresent()) {
            userRepository.deleteUser(user.get().getId());

            DeleteAccountResponse response = DeleteAccountResponse.newBuilder()
                    .setMessage("Account deleted successfully.")
                    .build();

            log.info("Account deleted successfully: "+user.get().getUsername());

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            Throwable throwable = new Throwable("Usuário não encontrado.");
            responseObserver.onError(throwable);
        }
    }
}
