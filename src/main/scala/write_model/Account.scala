/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package write_model

import java.util.UUID

import cqrs.command.{AggregateQuery, AggregateRoot, EventBus, IllegalCommandException}
import write_model.AccountCommands.ChangeBalance
import write_model.AccountEvents.BalanceChanged

case class Account(id: UUID, balance: Long) extends AggregateRoot[Account] {
  override def handleCommand(eventBus: EventBus): CommandHandler = {
    case cmd@ChangeBalance(_, delta) => {
      val newBalance = delta + balance
      if (newBalance < 0) {
        throw new IllegalCommandException(cmd, s"Could not withdraw $delta when account $id has only $balance")
      } else {
        eventBus.publish(BalanceChanged(id, delta, newBalance))
      }
    }
  }

  override def applyEvent: EventHandler = {
    case BalanceChanged(_, _, newBalance) => Account(id, newBalance)
  }

  override def handleQuery(query: AggregateQuery): Any = ???
}
