/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.io;

public final class PathHelper {
    private PathHelper() {
        throw new AssertionError("No PathHelper instances for you!");
    }

    /**
     * Removes the file extension name from the given path.
     *
     * <p>
     * This method will only remove the last part of the extension name - for example, if the given
     * file name is `archive.tar.gz`, `archive.tar` will be returned. In other words, this method
     * is the equivalent of:
     * <pre>path.substring(0, path.lastIndexOf('.'))</pre>
     * </p>
     *
     * @return The file name with the last extension name removed.
     */
    public static String removeExtension(String path) {
        return path.substring(0, path.lastIndexOf('.'));
    }

    /**
     * Gets the extension name of the specified path.
     * <p>
     * This method will only return the last part of the extension name - for example, if the given
     * file name is {@code archive.tar.gz}, {@code gz} will be returned.
     * <p>
     * File names starting with a dot (indicating a hidden file under Unix environments) are
     * treated as files without an extension.
     *
     * @return The extension name (such as {@code exe}), or an empty string if none can be found.
     */
    public static String getExtension(String path) {
        return path.substring(path.lastIndexOf('.') + 1);
    }
}
