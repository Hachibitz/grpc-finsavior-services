package br.com.finsavior.grpc.services.service;

import br.com.finsavior.grpc.maintable.*;
import br.com.finsavior.grpc.services.entity.MainTable;
import br.com.finsavior.grpc.services.repository.MainTableRepository;
import io.grpc.netty.shaded.io.netty.handler.codec.http.HttpResponseStatus;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@GrpcService
public class MainTableDataService extends TableDataServiceGrpc.TableDataServiceImplBase {

    Logger logger = LoggerFactory.getLogger(TableDataServiceGrpc.TableDataServiceImplBase.class);

    @Autowired
    MainTableRepository repository;
    @Autowired
    MainTableRepository mainTableRepository;

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

    @Override
    public void loadMainTableData(MainTableDataRequest request, StreamObserver<MainTableDataResponse> responseObserver) {
        List<MainTable> mainTableList = mainTableRepository.findAllByUserId(request.getUserId());
        MainTableDataResponse.Builder responseBuilder = MainTableDataResponse.newBuilder();

        mainTableList.forEach((data) -> {
            MainTableData mainTableData = MainTableData.newBuilder()
                    .setBillName(data.getBillName())
                    .setBillValue(data.getBillValue())
                    .setBillType(data.getBillType())
                    .setBillDescription(data.getBillDescription())
                    .setBillDate(data.getBillDate())
                    .build();
            responseBuilder.addMainTableData(mainTableData);
        });

        MainTableDataResponse response = responseBuilder.build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void loadCardTableData(CardTableDataRequest request, StreamObserver<CardTableDataResponse> responseObserver) {
        super.loadCardTableData(request, responseObserver);
    }
}
