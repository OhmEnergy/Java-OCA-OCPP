package eu.chargetime.ocpp.model.firmware;
/*
 * ChargeTime.eu - Java-OCA-OCPP
 *
 * MIT License
 *
 * Copyright (C) 2016-2018 Thomas Volden <tv@chargetime.eu>
 * Copyright (C) 2018 Mikhail Kladkevich <kladmv@ecp-share.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import eu.chargetime.ocpp.PropertyConstraintException;
import eu.chargetime.ocpp.model.OcppRequest;
import eu.chargetime.ocpp.utilities.MoreObjects;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/** Sent by the Charge Point to the Central System. */
@XmlRootElement
public class DiagnosticsStatusNotificationRequest extends OcppRequest {

  private DiagnosticsStatus status;

  public DiagnosticsStatusNotificationRequest() {}

  /**
   * Set required fields.
   *
   * @param status Diagnostics status, see {@link #setStatus(DiagnosticsStatus)}.
   */
  public DiagnosticsStatusNotificationRequest(DiagnosticsStatus status) {
    this.status = status;
  }

  @Override
  public boolean validate() {
    return status != null;
  }

  /**
   * This contains the status.
   *
   * @return connector.
   */
  public DiagnosticsStatus getStatus() {
    return status;
  }

  /**
   * Required. This contains the identifier of the status.
   *
   * @param status {@link DiagnosticsStatus}.
   */
  @XmlElement
  public void setStatus(DiagnosticsStatus status) {
    if (status == null) {
      throw new PropertyConstraintException(null, "Diagnostic status must be present");
    }

    this.status = status;
  }

  @Override
  public boolean transactionRelated() {
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DiagnosticsStatusNotificationRequest that = (DiagnosticsStatusNotificationRequest) o;
    return status == that.status;
  }

  @Override
  public int hashCode() {
    return Objects.hash(status);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("status", status)
        .add("isValid", validate())
        .toString();
  }
}
