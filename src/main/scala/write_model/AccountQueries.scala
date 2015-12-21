/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package write_model

import java.util.UUID

import cqrs.query.DomainQuery

object AccountQueries {

  sealed trait AccountQuery extends DomainQuery

  case object BankMoney extends AccountQuery

  case object NumberOfClients extends AccountQuery

  case class BankMoneyResponse(bankMoney: Long)

  case class NumberOfClientsResponse(numberOfClients: Long)

  case object AccountNames extends AccountQuery

  case class AccountNamesResponse(accounts: Set[UUID])

}
