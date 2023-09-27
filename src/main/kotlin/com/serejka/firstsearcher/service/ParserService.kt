package com.serejka.firstsearcher.service

import com.serejka.firstsearcher.model.dto.AdResponse
import com.serejka.firstsearcher.repository.ThreadRepository
import com.serejka.firstsearcher.repository.WorkerRepository
import com.serejka.firstsearcher.webhook.NotifyBot
import jakarta.annotation.PostConstruct
import lombok.SneakyThrows
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.redisson.api.RSet
import org.redisson.api.RedissonClient
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import java.util.Objects
import java.util.concurrent.TimeUnit

// TODO rewrite with https://docs.skrape.it/docs/overview/setup
@Component
class ParserService(
    private var notifyBot: NotifyBot,
    private val threadRepository: ThreadRepository,
    private val workerRepository: WorkerRepository,
    private var redissonClient: RedissonClient
) {

    private lateinit var sentUrls: RSet<String>

    @PostConstruct
    private fun init() {
        sentUrls = redissonClient.getSet("sentUrls")
    }

    @Scheduled(fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
    fun processLogic() {
        threadRepository.findAllByActiveTrue()
            .parallelStream().forEach { thread ->
                try {
                    val url = thread.link
                    val doc = Jsoup.connect(url!!)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/115.0")
                        .referrer("https://www.olx.ua/")
                        .get()
                    val adds = getAdds(doc)
                    val addResponses = adds.stream()
                        .map { ad: Element -> getAddResponse(ad) }
                        .toList()
                    println("Poluchil elementov" + addResponses.size)
                    val collect = addResponses.stream()
                        .filter { adResponse -> !Objects.isNull(adResponse.time) }
                        .filter { ad -> !sentUrls.contains(ad.url) }
                        .filter { adResponse -> isNewAd(adResponse) }
                        .toList()
                    val messages = collect.stream()
                        .map { adResponse -> stringifyAdd(adResponse) }
                        .toList()
                    println("Hochu zapablishsit elementov" + messages.size)
                    workerRepository.findById(thread.worker?.id!!)
                        .ifPresent { worker ->
                            listOf(worker.user?.userId.toString())
                                .forEach { chat: String ->
                                    messages.forEach { text -> sendMessage(chat, text) }
                                }
                            sentUrls.addAll(collect.stream().map<String>(AdResponse::url).toList())
                        }
                } catch (e: IOException) {
                    println("An error occurred: $e")
                }
            }
    }

    @SneakyThrows
    private fun sendMessage(chat: String, text: String) {
        val sendMessage = SendMessage()
        sendMessage.chatId = chat
        sendMessage.text = text
        notifyBot.execute(sendMessage)
    }

    private fun stringifyAdd(adResponse: AdResponse): String {
        return ("""${"Назва: " + adResponse.title} 
link: https://www.olx.ua${adResponse.url}""" + " \n" +
                "Цiна: " + adResponse.price) + " \n"
    }

    @SneakyThrows
    private fun isNewAd(adResponse: AdResponse): Boolean {
        val format: DateFormat = SimpleDateFormat("HH:mm")
        val parse = format.parse(adResponse.time)
        val localDateTime = LocalDateTime.ofInstant(Date().toInstant(), ZoneOffset.UTC)
        val parse1 = format.parse(localDateTime.hour.toString() + ":" + localDateTime.minute)
        return parse1.time - parse.time < 300000 * 12
    }

    private fun getAddResponse(ad: Element): AdResponse {
        val adResponse = AdResponse()
        mapWithAdUrl(ad, adResponse)
        mapWithAdTitleAndPriceAndTime(ad, adResponse)
        return adResponse
    }

    private fun mapWithAdUrl(ad: Element, adResponse: AdResponse) {
        ad.getElementsByTag("a").stream()
            .findFirst()
            .ifPresent { element: Element ->
                adResponse.url = element.attributes()["href"]
            }
    }

    private fun mapWithAdTitleAndPriceAndTime(ad: Element, adResponse: AdResponse) {
        ad.getElementsByClass("css-1apmciz").stream()
            .findFirst().ifPresent { element: Element ->
                mapWithTitle(adResponse, element)
                mapWithPrice(adResponse, element)
                mapWithTodayTime(adResponse, element)
            }
    }

    private fun mapWithTitle(adResponse: AdResponse, element: Element) {
        element.getElementsByTag("h6").stream()
            .findFirst().ifPresent { title: Element ->
                adResponse.title = title.text()
            }
    }

    private fun mapWithPrice(adResponse: AdResponse, element: Element) {
        element.getElementsByAttributeValue("data-testid", "ad-price").stream()
            .findFirst().ifPresent { price: Element ->
                adResponse.price = price.text()
            }
    }

    private fun mapWithTodayTime(adResponse: AdResponse, element: Element) {
        element.getElementsByAttributeValue("data-testid", "location-date").stream()
            .findFirst().ifPresent { date: Element ->
                val text = date.text()
                val substring = text.substring(text.length - 5)
                val matches = substring.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$".toRegex())
                if (matches) {
                    adResponse.time = substring
                }
            }
    }

    private fun getAdds(doc: Document): Elements {
        return doc.getElementsByClass("css-rc5s2u")
    }

}
