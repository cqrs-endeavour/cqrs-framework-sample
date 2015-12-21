/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
import java.util.UUID
import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import write_model.AccountCommands.{CreateAccountCommand, ChangeBalance}
import write_model.AccountEvents.{AccountCreated, BalanceChanged}
import write_model.AccountRepositoryProvider
import akka.pattern.ask

import scala.util.{Failure, Success}

object WriteModelMain extends App {
  val logger = LoggerFactory.getLogger(this.getClass)
  
  if (args.length != 1) {
    logger.info(
      s"""
         |usage: <configuration file suffix>
       """.stripMargin)
  } else {
    val confNumber = args(0).toInt
    val time = System.currentTimeMillis()
    val config = ConfigFactory.load(s"write-model-$confNumber.conf")

    implicit val system = ActorSystem("ClusterSystem", config)

    val cluster = Cluster(system)
    implicit val timeout = Timeout(15, TimeUnit.SECONDS)
    implicit val ec = system.dispatcher

    cluster.registerOnMemberUp {
      val accountRepository = AccountRepositoryProvider.provide

      while(true) {
        logger.info(
          s"""
             |available commands:
             |create
             |<accountId>,<delta>
       """.stripMargin)

        val line = scala.io.StdIn.readLine()

        line match {
          case "create" => {
            (accountRepository ? CreateAccountCommand).mapTo[AccountCreated].onSuccess {
              case AccountCreated(uuid) => {
                logger.info(s"+++Created account $uuid+++")
              }
            }
          }
          case _ => {
            try {
              val accountId = line.split(",")(0)
              val delta = line.split(",")(1).toLong

              (accountRepository ? ChangeBalance(UUID.fromString(accountId), delta)).onComplete {
                case Success(BalanceChanged(_, delta, balance)) => logger.info(s"+++Changed balance of account $accountId by $delta, now has $balance+++")
                case Failure(e) => logger.error(s"+++Could not change balance of $accountId by $delta+++", e)
                case r => logger.error(s"+++Unrecognized response from account $accountId, $r+++")
              }
            } catch {
              case e: Exception => logger.error("+++Could not change account balance+++", e)
            }
          }
        }
      }
    }
  }
}
