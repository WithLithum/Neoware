package x.withlithum.neoware.util.io

object NPath {
    fun removeExtension(path: String): String {
        return path.substring(0, path.lastIndexOf('.'))
    }
}