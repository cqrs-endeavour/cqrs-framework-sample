/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package read_model

import cqrs.query.{EventDuplicatesFilter, EventDuplicatesFilterPersistService}

class InMemoryEventDuplicatesFilterPersistService extends EventDuplicatesFilterPersistService {
  private var _lastSequenceNumbers: Seq[(String, Long)] = List()

  override def fetchFilter: EventDuplicatesFilter = ???

  override def updateFilter(updatedSequenceNumbers: Seq[(String, Long)]): Unit = {
    _lastSequenceNumbers = updatedSequenceNumbers
  }

  def lastSequenceNumbers: Seq[(String, Long)] = {
    _lastSequenceNumbers
  }
}
