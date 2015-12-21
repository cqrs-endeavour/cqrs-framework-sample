/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import cqrs.query.{CQRSViewEventBus, EventDuplicatesFilter}
import org.slf4j.LoggerFactory
import read_model.AccountView
import write_model.AccountQueries._

import scala.concurrent.Await

object ReadModelMain extends App {
  val logger = LoggerFactory.getLogger(this.getClass)
  
  val time = System.currentTimeMillis()
  // unique client name so it is possible to create many read model instances on single machine
  val config = ConfigFactory.parseString(s"cqrs.kafka-pub-sub.node-name=local-$time")
    .withFallback(ConfigFactory.load())

  val system = ActorSystem(s"read-model-$time", config)
  val filter = new EventDuplicatesFilter()
  val view = system.actorOf(AccountView.props(filter), "account-view")
  system.actorOf(CQRSViewEventBus(view, subscribeFromScratch = true), "cqrs-view-event-bus")
  implicit val timeout = Timeout(15, TimeUnit.SECONDS)
  implicit val ec = system.dispatcher

  while (true) {
    logger.info(
      s"""
         |1 - query bank money
         |2 - query number of clients
         |3 - list of all accounts
       """.stripMargin)
    val line = scala.io.StdIn.readLine()

    line match {
      case "1" => {
        val result = Await.result((view ? BankMoney).mapTo[BankMoneyResponse], timeout.duration)
        logger.info(s"+++Bank currently has ${result.bankMoney} money+++")
      }
      case "2" => {
        
        val result = Await.result((view ? NumberOfClients).mapTo[NumberOfClientsResponse], timeout.duration) 
          logger.info(s"+++Bank currently has ${result.numberOfClients} clients")
      }
      case "3" => {
        val result = Await.result((view ? AccountNames).mapTo[AccountNamesResponse], timeout.duration)
        logger.info(s"Accounts: ${result.accounts}")
      }
      case _ => {
        logger.warn("+++Unrecognized query+++")
      }
    }
  }
}
