/**
 * Copyright (C) 2012-2018 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.gda;

import java.util.Set;

import org.n52.sos.coding.json.JSONConstants;
import org.n52.sos.encode.json.AbstractSosResponseEncoder;
import org.n52.sos.gda.GetDataAvailabilityResponse.DataAvailability;
import org.n52.sos.gda.GetDataAvailabilityResponse.ObservationFormatDescriptor;
import org.n52.sos.gda.GetDataAvailabilityResponse.ProcedureDescriptionFormatDescriptor;
import org.n52.sos.ogc.ows.OwsExceptionReport;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 * 
 * @author Christian Autermann <c.autermann@52north.org>
 * 
 * @since 4.0.0
 */
public class GetDataAvailabilityJsonEncoder extends AbstractSosResponseEncoder<GetDataAvailabilityResponse> {
    public GetDataAvailabilityJsonEncoder() {
        super(GetDataAvailabilityResponse.class, GetDataAvailabilityConstants.OPERATION_NAME);
    }

    @Override
    protected void encodeResponse(ObjectNode json, GetDataAvailabilityResponse t) throws OwsExceptionReport {
        ArrayNode a = json.putArray(GetDataAvailabilityConstants.DATA_AVAILABILITY);
        for (DataAvailability da : t.getDataAvailabilities()) {
            ObjectNode objectNode = a.addObject();
            objectNode.put(JSONConstants.FEATURE_OF_INTEREST, da.getFeatureOfInterest().getHref())
                        .put(JSONConstants.PROCEDURE, da.getProcedure().getHref())
                        .put(JSONConstants.OBSERVED_PROPERTY, da.getObservedProperty().getHref())
                        .put(JSONConstants.PHENOMENON_TIME, encodeObjectToJson(da.getPhenomenonTime()));
            if (t.isSetResponseFormat() && GetDataAvailabilityConstants.NS_GDA_20.equals(t.getResponseFormat())) {
                if (da.isSetOffering()) {
                    objectNode.put(JSONConstants.OFFERING, da.getOffering().getHref());
                }
                if (da.isSetFormatDescriptors()) {
                    ObjectNode fdNode = objectNode.putObject(GetDataAvailabilityConstants.FORMAT_DESCRIPTOR);
                    encodeProcedureFormatDescriptor(da.getFormatDescriptor().getProcedureDescriptionFormatDescriptor(), fdNode);
                    encodeObservationFormatDescriptor(da.getFormatDescriptor().getObservationFormatDescriptors(), fdNode);
                }
            }
            if (da.isSetCount()) {
                objectNode.put(JSONConstants.COUNT, da.getCount());
            }
        }
    }

    private void encodeProcedureFormatDescriptor(
            ProcedureDescriptionFormatDescriptor procedureDescriptionFormatDescriptor, ObjectNode fdNode) {
        ObjectNode pfdNode = fdNode.putObject(GetDataAvailabilityConstants.PROCEDURE_FORMAT_DESCRIPTOR);
        pfdNode.put(GetDataAvailabilityConstants.PROCEDURE_DESCRIPTION_FORMAT, procedureDescriptionFormatDescriptor.getProcedureDescriptionFormat());
    }

    private void encodeObservationFormatDescriptor(Set<ObservationFormatDescriptor> observationFormatDescriptors,
            ObjectNode fdNode) {
        ArrayNode ofdArray = fdNode.putArray(GetDataAvailabilityConstants.OBSERVATION_FORMAT_DESCRIPTOR);
        for (ObservationFormatDescriptor ofd : observationFormatDescriptors) {
            ObjectNode ofdNode = ofdArray.addObject();
            ofdNode.put(GetDataAvailabilityConstants.RESPONSE_FORMAT, ofd.getResponseFormat());
            ArrayNode otArray = ofdNode.putArray(GetDataAvailabilityConstants.OBSERVATION_TYPE);
            for (String obsType : ofd.getObservationTypes()) {
                otArray.add(obsType);
            }
        }
    }
}