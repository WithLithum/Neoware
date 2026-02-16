/*
 * SPDX-FileCopyrightText: 2025-2026 WithLithum & contributors
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package x.withlithum.neoware.util.messages

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import x.withlithum.neoware.server.security.BanInfo
import x.withlithum.neoware.util.messages.extensions.lc.lc
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.time.Instant
import kotlin.time.toJavaInstant

@Deprecated("Use BannedMessage instead.")
object BanMessage {
    private val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        .withZone(ZoneOffset.UTC)
    private val line1 = lc("login.banned.line_1")
        .fallback("You have been banned from this server!")
        .decorate(TextDecoration.BOLD)
        .color(NamedTextColor.RED)
    private val line2NoReason = lc("login.banned.line_2_no_reason")
        .fallback("No explanation was given for this ban.")
        .decorate(TextDecoration.ITALIC)
    private val line5 = lc("login.banned.line_5")
        .fallback("Please contact administrator if you have any doubts or questions as to this decision.")
        .color(NamedTextColor.AQUA)

    private fun line2(reason: String): Component {
        return lc("login.banned.line_2", Component.text()
            .content(reason)
            .decorate(TextDecoration.ITALIC)
            .color(NamedTextColor.WHITE).build())
            .fallback("Reason: \"%s\"")
    }

    private fun line3(banInfo: BanInfo): Component {
        val startDate = Component.text()
            .content(formatter.format(banInfo.from.toJavaInstant()))
            .color(NamedTextColor.WHITE).build();

        return if (banInfo.to != null) {
            lc("login.banned.line_3_no_expiry", startDate)
                .fallback("This ban was given to you at %s UTC.")
                .color(NamedTextColor.YELLOW)
        } else {
            lc("login.banned.line_3", startDate)
                .fallback("This ban was given to you at %s UTC. It will not expire.")
                .color(NamedTextColor.YELLOW)
        }
    }

    private fun line4(until: Instant): Component {
        val endDate = Component.text()
            .content(formatter.format(until.toJavaInstant()))
            .color(NamedTextColor.WHITE).build();

        return lc("login.banned.line_4", endDate)
            .fallback("It will expire at %s UTC.")
    }

    fun create(banInfo: BanInfo): Component {
        val builder = Component.text()
            .append(line1)
            .appendNewline()
            .append(if (banInfo.reason != null) {
                line2(banInfo.reason)
            } else {
                line2NoReason
            })
            .appendNewline()
            .append(line3(banInfo))

        if (banInfo.to != null) {
            builder.append(line4(banInfo.to))
        }

        return builder.appendNewline()
            .append(line5)
            .build()
    }
}