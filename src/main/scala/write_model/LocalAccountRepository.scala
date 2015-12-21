/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package write_model

import akka.actor.Props
import cqrs.repository.LocalRepository
import write_model.AccountCommands.{ModifyAccountCommand, CreateAccountCommand}

object LocalAccountRepository {
  def props: Props = Props(classOf[LocalAccountRepository])
}

class LocalAccountRepository extends LocalRepository[CreateAccountCommand.type, ModifyAccountCommand] {
  override def aggregateRootProps: Props = AccountActor.props
}
