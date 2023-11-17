package br.com.finsavior.grpc.services.service;

import br.com.finsavior.grpc.services.model.entity.User;
import br.com.finsavior.grpc.services.repository.UserRepository;
import br.com.finsavior.grpc.tables.GenericResponse;
import br.com.finsavior.grpc.user.*;
import io.grpc.Status;
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
            log.error("Usuário {} não encontrado", request.getUsername());
            Status status = Status.NOT_FOUND.withDescription("Usuário não encontrado.");
            responseObserver.onError(status.asRuntimeException());
        }
    }

    @Override
    public void changeAccountPassword(ChangePasswordRequest request, StreamObserver<GenericResponse> responseObserver) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(request.getUsername()));

        if(request.getCurrentPassword().equals(request.getNewPassword())) {
            log.error("Erro na alteração de senha. A senha atual não pode ser igual a anterior.");
            Status status = Status.UNAUTHENTICATED.withDescription("A senha atual não pode ser igual a anterior.");
            responseObserver.onError(status.asRuntimeException());
            return;
        }

        if(user.isPresent()) {
            boolean isPasswordValid = user.get().getPassword().equals(request.getCurrentPassword());
            if(isPasswordValid) {
                user.get().setPassword(request.getNewPassword());
                userRepository.save(user.get());
                GenericResponse response = GenericResponse.newBuilder().setMessage("Senha alterada com sucesso!").build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } else {
                log.error("Erro na alteração de senha. Senha atual incorreta.");
                Status status = Status.UNAUTHENTICATED.withDescription("Senha atual incorreta.");
                responseObserver.onError(status.asRuntimeException());
            }
        } else {
            log.error("Erro na alteração de senha, usuário não encontrado.");
            Status status = Status.NOT_FOUND.withDescription("Usuário não encontrado.");
            responseObserver.onError(status.asRuntimeException());
        }
    }
}
