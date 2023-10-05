package br.com.finsavior.grpc.services.service;

import br.com.finsavior.grpc.security.AuthServiceGrpc;
import br.com.finsavior.grpc.security.SignUpRequest;
import br.com.finsavior.grpc.security.SignUpResponse;
import br.com.finsavior.grpc.services.model.entity.Role;
import br.com.finsavior.grpc.services.model.entity.User;
import br.com.finsavior.grpc.services.model.enums.UserRoleEnum;
import br.com.finsavior.grpc.services.repository.AuthRepository;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
@Slf4j
public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {

    @Autowired
    AuthRepository authRepository;

    @Override
    public void signUp(SignUpRequest request, StreamObserver<SignUpResponse> responseObserver) {
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(request, User.class);
        Role role = new Role(UserRoleEnum.ROLE_USER.id, UserRoleEnum.ROLE_USER);
        user.getRoles().add(role);
        authRepository.save(user);

        SignUpResponse signUpResponse = SignUpResponse.newBuilder()
                .setMessage("Cadastro realizado com sucesso!")
                .build();

        log.info("Cadastro realizado com sucesso: "+user.getUsername());

        responseObserver.onNext(signUpResponse);
        responseObserver.onCompleted();
    }
}
