package com.serejka.firstsearcher.context


class BotContext {
    companion object {
        var userId: ThreadLocal<Long> = ThreadLocal()
        var isAdmin: ThreadLocal<Boolean> = ThreadLocal()
    }
}
