package x.withlithum.neoware.util.io

import okio.Path

/**
 * Provides utility methods for dealing with paths.
 */
object NPath {
    /**
     * Removes the file extension name from the given path.
     *
     * This method will only remove the last part of the extension name - for example, if the given
     * file name is `archive.tar.gz`, `archive.tar` will be returned. In other words, this method
     * is the equivalent of:
     * ```kotlin
     * path.substring(0, path.lastIndexOf('.'))
     * ```
     *
     * @return The file name with the last extension name removed.
     */
    fun removeExtension(path: String): String {
        return path.substring(0, path.lastIndexOf('.'))
    }

    /**
     * Gets the extension name of the specified path.
     *
     * This method will only return the last part of the extension name - for example, if the given
     * file name is `archive.tar.gz`, `gz` will be returned.
     *
     * File names starting with a dot (indicating a hidden file under Unix environments) are
     * treated as files without an extension.
     *
     * @return The extension name (such as `exe`), or an empty string if none can be found.
     */
    fun getExtension(path: Path): String {
        val lastSegment = path.segments.last()
        if (!lastSegment.contains('.') || lastSegment.startsWith('.')) {
            return ""
        }

        return lastSegment.substring(lastSegment.lastIndexOf('.') + 1)
    }
}