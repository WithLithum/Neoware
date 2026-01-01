package x.withlithum.neoware.game.locale

import net.kyori.adventure.key.Key
import net.kyori.adventure.translation.Translator
import x.withlithum.neoware.util.KeyRoot
import java.text.MessageFormat
import java.util.Locale

class MapTranslator(val map: Map<String, Map<String, MessageFormat>>) : Translator {
    override fun name(): Key {
        return KeyRoot.id("map")
    }

    override fun translate(key: String, locale: Locale): MessageFormat? {
        val langTag = locale.toLanguageTag()

        return map[langTag]?.get(key)
            ?: map[Locale.US.toLanguageTag()]?.get(key)
    }
}