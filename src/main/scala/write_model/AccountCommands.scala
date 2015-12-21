/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package write_model

import java.util.UUID

import cqrs.command.{CreateDomainCommand, ModifyDomainCommand}

object AccountCommands {
  sealed trait ModifyAccountCommand extends ModifyDomainCommand {
    def accountId: UUID

    override def aggregateRootId: UUID = accountId
  }
  
  case class ChangeBalance(accountId: UUID, delta: Long) extends ModifyAccountCommand
  
  case object CreateAccountCommand extends CreateDomainCommand
}
