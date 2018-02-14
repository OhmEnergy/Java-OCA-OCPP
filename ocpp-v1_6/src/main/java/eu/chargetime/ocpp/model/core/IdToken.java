package eu.chargetime.ocpp.model.core;

import eu.chargetime.ocpp.model.Validatable;
import eu.chargetime.ocpp.utilities.ModelUtil;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

        /*
         * ChargeTime.eu - Java-OCA-OCPP
         *
         * MIT License
         *
         * Copyright (C) 2016 Daniel Vocke (daniel.vocke@ohme.io)
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

/**
 * Single sampled value in {@link MeterValue}. Each value can be accompanied by optional fields.
 */
@XmlRootElement
@XmlType(propOrder = {"idToken"})
public class IdToken implements Validatable {

    private String idToken;

    public IdToken() {
    }

    public IdToken(String tag) {
        setIdToken(tag);
    }

    @Override
    public boolean validate() {
        return ModelUtil.validate(idToken, 20);
    }

    /**
     * @return the ID value represented by this tag class
     */
    public String getIdToken() {
        return idToken;
    }

    /**
     * assign a new ID value to this tag class
     *
     * @param newTag new value to be assigned
     */
    public void setIdToken(String newTag) {
        this.idToken = newTag;
    }
}
