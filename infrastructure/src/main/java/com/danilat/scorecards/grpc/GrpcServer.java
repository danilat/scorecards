package com.danilat.scorecards.grpc;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.Event;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.usecases.fights.RetrieveAFight;
import com.danilat.scorecards.grpc.fight.RetrieveAFightGrpc;
import com.danilat.scorecards.repositories.InMemoryFightRepository;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.time.LocalDate;

public class GrpcServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    FightRepository fightRepository = new InMemoryFightRepository();
    BoxerId gago = new BoxerId("Andoni Gago");
    BoxerId kiko = new BoxerId("Kiko Martingez");
    Event event = new Event(LocalDate.now(), "Bilbao Arena");
    Fight fight = new Fight(new FightId("SOME_ID"), gago, kiko, event);
    fightRepository.save(fight);

    RetrieveAFight retrieveAFight = new RetrieveAFight(fightRepository);

    Server server = ServerBuilder
        .forPort(8080)
        .addService(new HelloServiceImpl())
        .addService(new RetrieveAFightGrpc(retrieveAFight))
        .build();

    server.start();
    server.awaitTermination();
  }
}
