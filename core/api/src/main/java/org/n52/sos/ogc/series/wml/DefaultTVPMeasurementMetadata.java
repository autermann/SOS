/**
 * Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.ogc.series.wml;

import org.n52.sos.ogc.series.wml.WaterMLConstants.InterpolationType;

/**
 * This class implements the OGC WaterML 2.0 element 
 * <code>MeasurementTimeseries > defaultPointMetadata > DefaultTVPMeasurementMetadata</code>.
 * 
 * See <code>/req/xsd-measurement-timeseries-tvp/interpolation-type</code>.
 * 
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @since 4.4.0
 * @see http://www.opengeospatial.org/standards/waterml
 */
public class DefaultTVPMeasurementMetadata {

    private InterpolationType interpolationtype;

    public boolean isSetInterpolationType() {
        return interpolationtype != null;
    }

    public InterpolationType getInterpolationtype() {
        return interpolationtype;
    }

    public DefaultTVPMeasurementMetadata setInterpolationtype(InterpolationType interpolationtype) {
        this.interpolationtype = interpolationtype;
        return this;
    }

}