package br.com.finsavior.grpc.services.service;

import br.com.finsavior.grpc.services.model.entity.Role;
import br.com.finsavior.grpc.services.model.entity.User;
import br.com.finsavior.grpc.services.model.enums.UserRoleEnum;
import br.com.finsavior.grpc.services.repository.UserRepository;
import br.com.finsavior.grpc.user.*;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Optional;

@GrpcService
@Slf4j
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    UserRepository userRepository;

    @Override
    public void signUp(SignUpRequest request, StreamObserver<SignUpResponse> responseObserver) {
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(request, User.class);
        Role role = new Role(UserRoleEnum.ROLE_USER.id, UserRoleEnum.ROLE_USER);
        user.getRoles().add(role);
        userRepository.save(user);

        SignUpResponse signUpResponse = SignUpResponse.newBuilder()
                .setMessage("Cadastro realizado com sucesso!")
                .build();

        log.info("Cadastro realizado com sucesso: "+user.getUsername());

        responseObserver.onNext(signUpResponse);
        responseObserver.onCompleted();
    }

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
