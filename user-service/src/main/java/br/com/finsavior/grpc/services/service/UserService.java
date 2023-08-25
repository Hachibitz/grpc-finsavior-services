package br.com.finsavior.grpc.services.service;

import br.com.finsavior.grpc.services.model.entity.User;
import br.com.finsavior.grpc.services.repository.UserRepository;
import br.com.finsavior.grpc.user.SignUpRequest;
import br.com.finsavior.grpc.user.SignUpResponse;
import br.com.finsavior.grpc.user.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    UserRepository repository;

    Logger logger = LoggerFactory.getLogger(UserServiceGrpc.UserServiceImplBase.class);

    @Override
    public void signUp(SignUpRequest request, StreamObserver<SignUpResponse> responseObserver) {
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(request, User.class);
        repository.save(user);

        SignUpResponse signUpResponse = SignUpResponse.newBuilder()
                .setMessage("Cadastro realizado com sucesso!")
                .build();

        logger.info("Cadastro realizado com sucesso!");

        responseObserver.onNext(signUpResponse);
        responseObserver.onCompleted();
    }
}
