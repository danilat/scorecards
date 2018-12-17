package com.danilat.scorecards.grpc;

import com.danilat.scorecards.grpc.fights.RetrieveAFightGrpc;
import com.danilat.scorecards.grpc.fights.RetrieveAFightRequest;
import com.danilat.scorecards.grpc.fights.RetrieveAFightResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
  public static void main(String[] args) {
    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
        .usePlaintext()
        .build();

    HelloServiceGrpc.HelloServiceBlockingStub helloStub
        = HelloServiceGrpc.newBlockingStub(channel);

    HelloResponse helloResponse = helloStub.hello(HelloRequest.newBuilder()
        .setFirstName("Dani")
        .setLastName("Latorre")
        .build());

    System.out.println(helloResponse);

    RetrieveAFightGrpc.RetrieveAFightBlockingStub fightStub
        = RetrieveAFightGrpc.newBlockingStub(channel);

    RetrieveAFightResponse fightResponse = fightStub.execute(RetrieveAFightRequest.newBuilder()
        .setId("SOME_ID")
        .build());

    System.out.println(fightResponse);

    channel.shutdown();
  }
}