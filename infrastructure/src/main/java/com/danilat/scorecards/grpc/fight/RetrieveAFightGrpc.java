package com.danilat.scorecards.grpc.fight;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.usecases.fights.RetrieveAFight;
import com.danilat.scorecards.grpc.fights.RetrieveAFightGrpc.RetrieveAFightImplBase;
import com.danilat.scorecards.grpc.fights.RetrieveAFightRequest;
import com.danilat.scorecards.grpc.fights.RetrieveAFightResponse;
import io.grpc.stub.StreamObserver;
import java.time.ZoneOffset;

public class RetrieveAFightGrpc extends RetrieveAFightImplBase {

  public RetrieveAFight retrieveAFight;

  public RetrieveAFightGrpc(RetrieveAFight retrieveAFight) {
    this.retrieveAFight = retrieveAFight;
  }

  @Override
  public void execute(RetrieveAFightRequest request, StreamObserver<RetrieveAFightResponse> streamObserver) {
    Fight fight = this.retrieveAFight.execute(new FightId(request.getId()));
    long happenedAt = fight.event().happenAt().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();

    RetrieveAFightResponse response = RetrieveAFightResponse.newBuilder()
        .setId(request.getId())
        .setFirstBoxer(fight.firstBoxer().toString())
        .setSecondBoxer(fight.secondBoxer().toString())
        .setHappenAt(happenedAt)
        .setPlace(fight.event().place())
        .build();

    streamObserver.onNext(response);
    streamObserver.onCompleted();
  }
}
