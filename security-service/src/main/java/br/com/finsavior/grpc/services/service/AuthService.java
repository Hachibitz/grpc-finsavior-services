package br.com.finsavior.grpc.services.service;

import br.com.finsavior.grpc.security.AuthServiceGrpc;
import br.com.finsavior.grpc.security.SignUpRequest;
import br.com.finsavior.grpc.security.SignUpResponse;
import br.com.finsavior.grpc.services.model.entity.Role;
import br.com.finsavior.grpc.services.model.entity.User;
import br.com.finsavior.grpc.services.model.enums.UserRoleEnum;
import br.com.finsavior.grpc.services.repository.AuthRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@GrpcService
@Slf4j
public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {

    @Autowired
    AuthRepository authRepository;

    @Override
    public void signUp(SignUpRequest request, StreamObserver<SignUpResponse> responseObserver) {
        if(signUpValidations(request) != null) {
            Status status = Status.INVALID_ARGUMENT.withDescription(signUpValidations(request));
            responseObserver.onError(status.asRuntimeException());
            return;
        };

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

    private String signUpValidations(SignUpRequest request) {
        Optional<User> userByEmail = Optional.ofNullable(authRepository.findByEmail(request.getEmail()));
        Optional<User> userByUsername = Optional.ofNullable(authRepository.findByUsername(request.getUsername()));

        StringBuilder resultBuilder = new StringBuilder();

        resultBuilder.append(userByEmail.isPresent() ? "Email já cadastrado. \n" : "");
        resultBuilder.append(userByUsername.isPresent() ? "Usuário já cadastrado. \n" : "");
        resultBuilder.append(!request.getPasswordConfirmation().equals(request.getPassword()) ? "As senhas não coincidem. \n" : "");
        resultBuilder.append(!this.isPasswordValid(request.getPassword()) ? "Critérios da senha não atendidos. \n" : "");
        resultBuilder.append(!this.isEmailValid(request.getEmail()) ? "Email inválido. \n" : "");
        resultBuilder.append(request.getUsername().length() < 4 ? "Usuário precisa ter 4 ou mais caracteres. \n" : "");
        resultBuilder.append(!this.containSymbols(request.getUsername()) ? "Usuário não pode conter símbolos. \n" : "");
        resultBuilder.append(request.getFirstName().length() < 2 ? "Nome precisa ter 2 ou mais caracteres. \n" : "");
        resultBuilder.append(!this.containSymbols(request.getFirstName()) ? "Nome não pode conter símbolos. \n" : "");
        resultBuilder.append(request.getLastName().length() < 2 ? "Sobrenome precisa ter 2 ou mais caracteres. \n" : "");
        resultBuilder.append(!this.containSymbols(request.getLastName()) ? "Sobrenome não pode conter símbolos. \n" : "");

        return resultBuilder.length() > 0 ? resultBuilder.toString().trim() : null;
    }


    public boolean isPasswordValid(String newPassword) {
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$";
        return newPassword.matches(regex);
    }

    public boolean isEmailValid(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(regex);
    }

    public boolean containSymbols(String username) {
        String regex = "^[a-zA-Z0-9_]+$";
        return username.matches(regex);
    }
}
