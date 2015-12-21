/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package write_model

import java.util.UUID

import cqrs.command.DomainEvent

object AccountEvents {

  sealed trait AccountEvent extends DomainEvent

  case class AccountCreated(accountId: UUID) extends DomainEvent

  case class BalanceChanged(accountId: UUID, delta: Long, currentBalance: Long) extends AccountEvent

}
