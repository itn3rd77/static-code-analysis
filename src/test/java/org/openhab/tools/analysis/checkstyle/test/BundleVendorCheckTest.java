/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.tools.analysis.checkstyle.test;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.TreeWalker;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openhab.tools.analysis.checkstyle.BundleVendorCheck;
import org.openhab.tools.analysis.checkstyle.api.AbstractStaticCheckTest;

/**
 * Tests for {@link BundleVendorCheck}
 *
 * @author Martin van Wingerden
 */
public class BundleVendorCheckTest extends AbstractStaticCheckTest {
    private static final String TEST_DIRECTORY = "correctOpenHABSpellingInManifestTest/";
    private static final DefaultConfiguration checkConfig = createCheckConfig(BundleVendorCheck.class);

    @BeforeClass
    public static void setUpClass() {
        checkConfig.addAttribute("allowedValues", "openHAB");
    }

    @Override
    protected DefaultConfiguration createCheckerConfig(Configuration config) {
        DefaultConfiguration configParent = createCheckConfig(TreeWalker.class);
        configParent.addChild(config);
        return configParent;
    }

    @Test
    public void testEmptyFile() throws Exception {
        verify("emptyManifest.MF",
                generateExpectedMessages()
        );

        // we ignore this, its the responsibility of another check
    }

    @Test
    public void testCompliantFile() throws Exception {
        verify("compliantSample.MF",
                generateExpectedMessages()
        );
    }

    @Test
    public void testMissingBundleVersion() throws Exception {
        verify("missingBundleVendorSample.MF",
                generateExpectedMessages(
                        0,
                        "\"Bundle-Vendor\" is missing"
                )
        );
    }

    @Test
    public void testDoubleBundleVersion() throws Exception {
        verify("doubleBundleVendorSample.MF",
                generateExpectedMessages(
                        4, "Only 1 \"Bundle-Vendor\" was expected.",
                        8, "Only 1 \"Bundle-Vendor\" was expected."
                )
        );
    }

    @Test
    public void testWrongLabelForBundleVersion() throws Exception {
        verify("wrongKeySample.MF",
                generateExpectedMessages(
                        5,
                        "Expect eg. \"Bundle-Vendor: openHAB\" got \"Bundle-vendor:openHAB\""
                )
        );
    }

    @Test
    public void testWrongValueForBundleVersion() throws Exception {
        verify("wrongValueSample.MF",
                generateExpectedMessages(
                        5,
                        "Unexpected \"Openhab\", only allowed options: [openHAB]"
                )
        );
    }

    private void verify(String fileName, String[] expectedMessages) throws Exception {
        verify(checkConfig,
                getPath(TEST_DIRECTORY + fileName),
                expectedMessages
        );
    }
}
