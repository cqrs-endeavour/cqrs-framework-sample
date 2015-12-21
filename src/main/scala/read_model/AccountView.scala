/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package read_model

import java.util.UUID

import akka.actor.Props
import cqrs.query.CQRSViewEventBus.MostOfTheEventsAlreadyReplayed
import cqrs.query.{DomainQuery, DomainView, EventDuplicatesFilter, EventEnvelope}
import write_model.AccountEvents.{AccountCreated, BalanceChanged}
import write_model.AccountQueries._

object AccountView {
  def props(filterService: EventDuplicatesFilter): Props = Props(classOf[AccountView], filterService)
}

class AccountView(filterService: EventDuplicatesFilter) extends DomainView {

  private var bankMoney = 0L

  private var accounts: Set[UUID] = Set.empty

  override def receiveEvent: PartialFunction[EventEnvelope, Unit] = {
    case envelope@EventEnvelope(_, _, event: AccountCreated) => {
      if (filterService.markAsSeen(envelope)) {
        accounts += event.accountId
      }
    }
    case envelope@EventEnvelope(_, _, BalanceChanged(_, delta, _)) => {
      if (filterService.markAsSeen(envelope)) {
        bankMoney += delta
      }
    }
  }

  override def receiveQuery: PartialFunction[DomainQuery, Any] = {
    case BankMoney => {
      BankMoneyResponse(bankMoney)
    }
    case NumberOfClients => NumberOfClientsResponse(accounts.size)
    case AccountNames => AccountNamesResponse(accounts)
  }

  override def receiveMessage: PartialFunction[Any, Unit] = {
    case MostOfTheEventsAlreadyReplayed => {
      log.info("View has processed most of events from replay")
    }
  }
}
