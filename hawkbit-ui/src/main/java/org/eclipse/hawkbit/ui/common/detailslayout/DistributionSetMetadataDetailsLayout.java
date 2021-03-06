/**
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.hawkbit.ui.common.detailslayout;

import org.eclipse.hawkbit.repository.DistributionSetManagement;
import org.eclipse.hawkbit.repository.model.DistributionSet;
import org.eclipse.hawkbit.ui.distributions.dstable.DsMetadataPopupLayout;
import org.eclipse.hawkbit.ui.utils.UIComponentIdProvider;
import org.eclipse.hawkbit.ui.utils.VaadinMessageSource;
import org.springframework.data.domain.PageRequest;

import com.vaadin.ui.UI;

/**
 * DistributionSet Metadata details layout.
 *
 */
public class DistributionSetMetadataDetailsLayout extends AbstractMetadataDetailsLayout {

    private static final long serialVersionUID = 1L;

    private final transient DistributionSetManagement distributionSetManagement;

    private final DsMetadataPopupLayout dsMetadataPopupLayout;

    private Long selectedDistSetId;

    /**
     * Initialize the layout.
     * 
     * @param i18n
     *            the i18n service
     * @param distributionSetManagement
     *            the distribution set management service
     * @param dsMetadataPopupLayout
     *            the distribution set metadata popup layout
     */
    public DistributionSetMetadataDetailsLayout(final VaadinMessageSource i18n,
            final DistributionSetManagement distributionSetManagement,
            final DsMetadataPopupLayout dsMetadataPopupLayout) {
        super(i18n);
        this.distributionSetManagement = distributionSetManagement;
        this.dsMetadataPopupLayout = dsMetadataPopupLayout;

    }

    /**
     * Populate distribution set metadata.
     *
     * @param distributionSet
     */
    public void populateDSMetadata(final DistributionSet distributionSet) {
        removeAllItems();
        if (null == distributionSet) {
            return;
        }
        selectedDistSetId = distributionSet.getId();
        final PageRequest pageRequest = PageRequest.of(0, MAX_METADATA_QUERY);
        distributionSetManagement.findMetaDataByDistributionSetId(pageRequest, selectedDistSetId).getContent()
                .forEach(this::setMetadataProperties);
    }

    @Override
    protected void showMetadataDetails(final String metadataKey) {
        distributionSetManagement.get(selectedDistSetId)
                .ifPresent(distSet -> UI.getCurrent().addWindow(dsMetadataPopupLayout.getWindow(distSet, metadataKey)));
    }

    @Override
    protected String getDetailLinkId(final String name) {
        return new StringBuilder(UIComponentIdProvider.DS_METADATA_DETAIL_LINK).append('.').append(name).toString();
    }

}
