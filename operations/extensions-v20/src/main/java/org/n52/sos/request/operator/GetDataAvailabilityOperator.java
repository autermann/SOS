/*
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
package org.n52.sos.request.operator;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.n52.shetland.ogc.ows.exception.CodedOwsException;
import org.n52.shetland.ogc.ows.exception.CompositeOwsException;
import org.n52.shetland.ogc.ows.exception.InvalidParameterValueException;
import org.n52.shetland.ogc.ows.exception.MissingParameterValueException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants.GetDataAvailabilityParams;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityRequest;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse;
import org.n52.sos.ds.AbstractGetDataAvailabilityHandler;
import org.n52.sos.wsdl.WSDLOperation;

/**
 * {@code IRequestOperator} to handle {@link GetDataAvailabilityRequest}s.
 *
 * @author Christian Autermann
 *
 * @since 4.0.0
 */
public class GetDataAvailabilityOperator
        extends
        AbstractRequestOperator<AbstractGetDataAvailabilityHandler, GetDataAvailabilityRequest, GetDataAvailabilityResponse>
        implements WSDLAwareRequestOperator {

    private static final Set<String> CONFORMANCE_CLASSES = Collections
            .singleton(GetDataAvailabilityConstants.CONFORMANCE_CLASS);

    /**
     * Constructs a new {@code GetDataAvailabilityOperator}.
     */
    public GetDataAvailabilityOperator() {
        super(SosConstants.SOS, Sos2Constants.SERVICEVERSION, GetDataAvailabilityConstants.OPERATION_NAME,
                GetDataAvailabilityRequest.class);
    }

    @Override
    public Set<String> getConformanceClasses(String service, String version) {
        if(SosConstants.SOS.equals(service) && Sos2Constants.SERVICEVERSION.equals(version)) {
            return Collections.unmodifiableSet(CONFORMANCE_CLASSES);
        }
        return Collections.emptySet();
    }

    @Override
    public GetDataAvailabilityResponse receive(GetDataAvailabilityRequest sosRequest) throws OwsExceptionReport {
        return getOperationHandler().getDataAvailability(sosRequest);
    }

    @Override
    protected void checkParameters(GetDataAvailabilityRequest request) throws OwsExceptionReport {
        CompositeOwsException exceptions = new CompositeOwsException();

        try {
            checkServiceParameter(request.getService());
        } catch (OwsExceptionReport owse) {
            exceptions.add(owse);
        }
        try {
            checkSingleVersionParameter(request);
        } catch (OwsExceptionReport owse) {
            exceptions.add(owse);
        }

        try {
            checkObservedProperties(request.getObservedProperties(),
                    GetDataAvailabilityParams.observedProperty.name(), false);
        } catch (OwsExceptionReport owse) {
            exceptions.add(owse);
        }
        try {
            checkQueryableProcedures(request.getProcedures(), GetDataAvailabilityParams.procedure.name());
            // add instance and child procedures to request
            if (request.isSetProcedure()) {
                request.setProcedures(addChildProcedures(addInstanceProcedures(request.getProcedures())));
            }
        } catch (OwsExceptionReport owse) {
            exceptions.add(owse);
        }
        try {
            checkFeatureOfInterestIdentifiers(request.getFeaturesOfInterest(),
                    GetDataAvailabilityParams.featureOfInterest.name());
            if (request.isSetFeaturesOfInterest()) {
                request.setFeatureOfInterest(addChildFeatures(request.getFeaturesOfInterest()));
            }
        } catch (OwsExceptionReport owse) {
            exceptions.add(owse);
        }
        try {
            checkOfferings(request.getOfferings(), GetDataAvailabilityParams.offering);
            // add child offerings to request
            if (request.isSetOfferings()) {
                request.setOfferings(addChildOfferings(request.getOfferings()));
            }
        } catch (OwsExceptionReport owse) {
            exceptions.add(owse);
        }
        try {
            if (request.isSetResponseFormat()) {
                checkResponseFormat(request.getResponseFormat());
            }
        } catch (OwsExceptionReport owse) {
            exceptions.add(owse);
        }
        try {
            checkResultFilterExtension(request);
        } catch (OwsExceptionReport owse) {
            exceptions.add(owse);
        }
        try {
            checkSpatialFilterExtension(request);
        } catch (OwsExceptionReport owse) {
            exceptions.add(owse);
        }

        exceptions.throwIfNotEmpty();
    }

    private void checkResponseFormat(String responseFormat) throws CodedOwsException {
        if (responseFormat == null || responseFormat.isEmpty()) {
            throw new MissingParameterValueException("responseFormat");
        }
        if (!(GetDataAvailabilityConstants.NS_GDA.equals(responseFormat)
                || GetDataAvailabilityConstants.NS_GDA_20.equals(responseFormat))) {
            throw new InvalidParameterValueException("responseFormat", responseFormat);
        }
    }

    @Override
    public WSDLOperation getSosOperationDefinition() {
//       TODO no schema available
//        return GetDataAvailabilityConstants.WSDL_OPERATION;
        return null;
    }

    @Override
    public Map<String, String> getAdditionalSchemaImports() {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, String> getAdditionalPrefixes() {
        return Collections.emptyMap();
    }
}
