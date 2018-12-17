package com.danilat.scorecards.grpc;

import com.danilat.scorecards.grpc.HelloServiceGrpc.HelloServiceImplBase;
import io.grpc.stub.StreamObserver;

public class HelloServiceImpl extends HelloServiceImplBase {

  @Override
  public void hello(
      HelloRequest request, StreamObserver<HelloResponse> responseObserver) {

    String greeting = new StringBuilder()
        .append("Hello, ")
        .append(request.getFirstName())
        .append(" ")
        .append(request.getLastName())
        .toString();

    HelloResponse response = HelloResponse.newBuilder()
        .setGreeting(greeting)
        .build();
    System.out.println(greeting);

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
