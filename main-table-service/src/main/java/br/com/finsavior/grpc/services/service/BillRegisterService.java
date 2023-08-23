package br.com.finsavior.grpc.services.service;

import br.com.finsavior.grpc.maintable.MainServiceGrpc;
import br.com.finsavior.grpc.maintable.BillRegisterRequest;
import br.com.finsavior.grpc.maintable.BillRegisterResponse;
import br.com.finsavior.grpc.services.entity.MainTable;
import br.com.finsavior.grpc.services.repository.MainTableRepository;
import io.grpc.netty.shaded.io.netty.handler.codec.http.HttpResponseStatus;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class BillRegisterService extends MainServiceGrpc.MainServiceImplBase {

    Logger logger = LoggerFactory.getLogger(MainServiceGrpc.MainServiceImplBase.class);

    @Autowired
    MainTableRepository repository;

    @Override
    public void billRegister(BillRegisterRequest request, StreamObserver<BillRegisterResponse> responseObserver) {
        ModelMapper modelMapper = new ModelMapper();
        MainTable register = modelMapper.map(request ,MainTable.class);
        repository.save(register);

        BillRegisterResponse response = BillRegisterResponse.newBuilder()
                .setStatus(HttpResponseStatus.OK.toString())
                .setMessage(request)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
