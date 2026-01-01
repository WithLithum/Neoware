package x.withlithum.neoware.resource

import io.github.oshai.kotlinlogging.KotlinLogging
import x.withlithum.neoware.util.ResourceRef
import x.withlithum.neoware.util.io.NPath
import java.io.BufferedReader
import java.io.InputStreamReader

object ResourceUtil {
    fun getResourceList(path: String, classLoader: ClassLoader): List<ResourceRef> {
        val stream = classLoader.getResourceAsStream(path)
        val refs = ArrayList<ResourceRef>()

        if (stream == null) {
            throw IllegalArgumentException("Resource not found: $path")
        }

        BufferedReader(InputStreamReader(stream)).useLines {
            for (name in it) {
                val url = classLoader.getResource(name) ?: continue

                refs.add(ResourceRef(NPath.removeExtension(name), url))
            }
        }

        return refs
    }
}