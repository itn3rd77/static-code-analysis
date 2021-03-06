/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.tools.analysis.checkstyle;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.openhab.tools.analysis.checkstyle.api.AbstractStaticCheck;
import org.openhab.tools.analysis.checkstyle.api.CheckConstants;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.FileText;

/**
 * Verifies that MANIFEST.MF files do not have lines exceeding the maximum
 * allowed size of bytes
 *
 * @author Velin Yordanov - Initial contribution
 *
 */
public class ManifestLineLengthCheck extends AbstractStaticCheck {
    private static final int MAX_LINE_SIZE = 72;
    private static final String LOG_MESSAGE = "No line may be longer than " + MAX_LINE_SIZE
            + " bytes (not characters), in its UTF8-encoded form. If a value would make the initial line longer than this, it should be continued on extra lines (each starting with a single SPACE).";

    public ManifestLineLengthCheck() {
        this.setFileExtensions(CheckConstants.MANIFEST_EXTENSION);
    }

    @Override
    protected void processFiltered(File file, FileText fileText) throws CheckstyleException {
        if (Arrays.stream(fileText.toLinesArray()).anyMatch(x -> x.getBytes(StandardCharsets.UTF_8).length > MAX_LINE_SIZE)) {
            log(0, LOG_MESSAGE);
        }
    }
}
