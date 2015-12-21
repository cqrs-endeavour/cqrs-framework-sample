/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package write_model

import java.util.UUID

import cqrs.command.{EventBus, AggregateRootFactory}
import write_model.AccountCommands.CreateAccountCommand
import write_model.AccountEvents.AccountCreated

object AccountFactory extends AggregateRootFactory[Account]{
  override def handleCommand(id: UUID, eventBus: EventBus): CommandHandler = {
    case CreateAccountCommand => eventBus.publish(AccountCreated(id))
  }

  override def applyEvent: EventHandler = {
    case AccountCreated(id) => Account(id, 0)
  }
}
