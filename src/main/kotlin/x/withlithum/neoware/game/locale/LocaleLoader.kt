package x.withlithum.neoware.game.locale

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.wasabithumb.jtoml.JToml
import io.github.wasabithumb.jtoml.except.parse.TomlLocalParseException
import x.withlithum.neoware.util.io.resources.ResourceHelper
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.text.MessageFormat
import java.util.*

object LocaleLoader {
    private val toml = JToml.jToml()
    private val logger = KotlinLogging.logger {}

    @JvmStatic
    fun loadEmbedded(classLoader: ClassLoader): MapTranslator {
        val resources = ResourceHelper.listDirectory("neoware/lang", classLoader)
        val map = HashMap<String, Map<String, MessageFormat>>()
        for (resource in resources) {
            map[resource.name] = loadMap(resource.url, resource.name)
        }

        logger.debug { "${map.size} locales" }
        return MapTranslator(map)
    }

    fun loadMap(url: URL, name: String): Map<String, MessageFormat> {
        try {
            InputStreamReader(url.openStream()).use {
                val table = toml.read(it)
                val map = HashMap<String, MessageFormat>()

                for (key in table.keys()) {
                    val value = table[key] ?: continue
                    if (!value.isPrimitive) {
                        logger.warn { "locale $name($key): not primitive" }
                        continue
                    }

                    val primitive = value.asPrimitive()
                    if (!primitive.isString) {
                        logger.warn { "locale $name($key): not string" }
                        continue
                    }

                    val strValue = value.asPrimitive().asString()

                    try {
                        map[key.toString()] = MessageFormat(strValue)
                    } catch (e: IllegalArgumentException) {
                        logger.warn { "locale $name($key): invalid message format: ${e.message}" }
                    }
                }

                return map
            }
        } catch (e: TomlLocalParseException) {
            logger.error { "locale $name(${e.lineNumber}, ${e.columnNumber}): ${e.rawMessage}" }
            return emptyMap()
        } catch (e: IOException) {
            logger.error(e) { "locale $name: I/O error" }
            return emptyMap()
        }
    }
}