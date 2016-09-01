/**
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.hawkbit.mgmt.rest.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.hawkbit.mgmt.json.model.targetfilter.MgmtTargetFilterQuery;
import org.eclipse.hawkbit.mgmt.json.model.targetfilter.MgmtTargetFilterQueryRequestBody;
import org.eclipse.hawkbit.mgmt.rest.api.MgmtTargetFilterQueryRestApi;
import org.eclipse.hawkbit.repository.EntityFactory;
import org.eclipse.hawkbit.repository.model.DistributionSet;
import org.eclipse.hawkbit.repository.model.TargetFilterQuery;

/**
 * A mapper which maps repository model to RESTful model representation and
 * back.
 *
 */
public final class MgmtTargetFilterQueryMapper {

    private MgmtTargetFilterQueryMapper() {
        // Utility class
    }

    /**
     * Create a response for targets.
     *
     * @param filters
     *            list of targets
     * @return the response
     */
    public static List<MgmtTargetFilterQuery> toResponse(final Iterable<TargetFilterQuery> filters) {
        final List<MgmtTargetFilterQuery> mappedList = new ArrayList<>();
        if (filters != null) {
            for (final TargetFilterQuery filter : filters) {
                final MgmtTargetFilterQuery response = toResponse(filter);
                mappedList.add(response);
            }
        }
        return mappedList;
    }

    /**
     * Create a response for target.
     *
     * @param filter
     *            the target
     * @return the response
     */
    public static MgmtTargetFilterQuery toResponse(final TargetFilterQuery filter) {
        if (filter == null) {
            return null;
        }
        final MgmtTargetFilterQuery targetRest = new MgmtTargetFilterQuery();
        targetRest.setFilterId(filter.getId());
        targetRest.setName(filter.getName());
        targetRest.setQuery(filter.getQuery());

        targetRest.setCreatedBy(filter.getCreatedBy());
        targetRest.setLastModifiedBy(filter.getLastModifiedBy());

        targetRest.setCreatedAt(filter.getCreatedAt());
        targetRest.setLastModifiedAt(filter.getLastModifiedAt());

        DistributionSet distributionSet = filter.getAutoAssignDistributionSet();
        if (distributionSet != null) {
            targetRest.setAutoAssignDistributionSet(distributionSet.getId());
        }

        targetRest.add(linkTo(methodOn(MgmtTargetFilterQueryRestApi.class).getFilter(filter.getId())).withRel("self"));
        targetRest.add(linkTo(methodOn(MgmtTargetFilterQueryRestApi.class).postAssignedDistributionSet(filter.getId(),null)).withRel("autoAssignDS"));

        return targetRest;
    }

    static TargetFilterQuery fromRequest(final EntityFactory entityFactory,
            final MgmtTargetFilterQueryRequestBody filterRest) {
        final TargetFilterQuery filter = entityFactory.generateTargetFilterQuery();
        filter.setName(filterRest.getName());
        filter.setQuery(filterRest.getQuery());

        return filter;
    }

}
