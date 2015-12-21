/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package write_model

import akka.actor.Props
import cqrs.command.{AggregateRootFactory, AggregateRootActor}

object AccountActor {
  def props: Props = Props(classOf[AccountActor])
}

class AccountActor extends AggregateRootActor[Account]{
  override def aggregateRootFactory: AggregateRootFactory[Account] = AccountFactory
}
