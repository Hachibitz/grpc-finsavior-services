package br.com.finsavior.grpc.services.service;

import br.com.finsavior.grpc.tables.*;
import br.com.finsavior.grpc.services.model.entity.CreditCardTable;
import br.com.finsavior.grpc.services.model.entity.MainTable;
import br.com.finsavior.grpc.services.model.enums.TableEnum;
import br.com.finsavior.grpc.services.repository.CreditCardTableRepository;
import br.com.finsavior.grpc.services.repository.MainTableRepository;
import io.grpc.netty.shaded.io.netty.handler.codec.http.HttpResponseStatus;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@GrpcService
@Slf4j
public class TableDataService extends TableDataServiceGrpc.TableDataServiceImplBase {

    Logger logger = LoggerFactory.getLogger(TableDataServiceGrpc.TableDataServiceImplBase.class);

    @Autowired
    CreditCardTableRepository cardTableRepository;
    @Autowired
    MainTableRepository mainTableRepository;

    @Override
    public void billRegister(BillRegisterRequest request, StreamObserver<BillRegisterResponse> responseObserver) {
        AtomicBoolean isError = new AtomicBoolean(false);;
        int typeChecker = 0;

        List<TableEnum> tableEnum = List.of(TableEnum.values());
        for (TableEnum tableType: tableEnum) {
            typeChecker = tableType.getValue().equals(request.getBillTable()) ? typeChecker+1 : typeChecker+0;
            isError.set(typeChecker == 0);
        }

        if(request.getBillTable().equals(TableEnum.MAIN.getValue())){
            try {
                ModelMapper modelMapper = new ModelMapper();
                MainTable register = modelMapper.map(request ,MainTable.class);
                register.setId(null);
                mainTableRepository.save(register);
                log.info("Registro salvo: "+register.toString());
            } catch (Exception e) {
                log.error("Falha ao salvar o registro: "+e.getMessage());
                isError.set(true);
                e.printStackTrace();
            }
        }

        if(request.getBillTable().equals(TableEnum.CREDIT_CARD.getValue())){
            try {
                ModelMapper modelMapper = new ModelMapper();
                CreditCardTable register = modelMapper.map(request ,CreditCardTable.class);
                register.setId(null);
                cardTableRepository.save(register);
            } catch (Exception e) {
                log.error("Falha ao salvar o registro: "+e.getMessage());
                isError.set(true);
                e.printStackTrace();
            }
        }

        if(isError.get()) {
            Throwable throwable = new Throwable("Erro ao salvar, favor especificar a tabela do registro");
            responseObserver.onError(throwable);
        }

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
                    .setId(data.getId())
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
        List<CreditCardTable> cardTableList = cardTableRepository.findAllByUserId(request.getUserId());
        CardTableDataResponse.Builder responseBuilder = CardTableDataResponse.newBuilder();

        cardTableList.forEach((data) -> {
            CardTableData cardTableData = CardTableData.newBuilder()
                    .setId(data.getId())
                    .setBillName(data.getBillName())
                    .setBillValue(data.getBillValue())
                    .setBillDescription(data.getBillDescription())
                    .setBillDate(data.getBillDate())
                    .build();
            responseBuilder.addCardTableData(cardTableData);
        });

        CardTableDataResponse response = responseBuilder.build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteItemFromCardTable(DeleteItemFromTableRequest request, StreamObserver<GenericResponse> responseObserver) {
        cardTableRepository.deleteById(request.getId());
        GenericResponse response = GenericResponse.newBuilder().setMessage("Item excluído").build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteItemFromMainTable(DeleteItemFromTableRequest request, StreamObserver<GenericResponse> responseObserver) {
        mainTableRepository.deleteById(request.getId());
        GenericResponse response = GenericResponse.newBuilder().setMessage("Item excluído").build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
