package com.nott

import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.REST
import com.flickr4java.flickr.photos.Photo
import com.flickr4java.flickr.photos.PhotoList
import com.flickr4java.flickr.photos.SearchParameters
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.nott.extension.subDirectoryName
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*

class ImageAutoCollecting : CliktCommand() {

    private val configPath: String
            by option(
                "-cp",
                "--configPath",
                help = "Defaults to './config.properties'. Specify the path to config.properties."
            ).default(value = "./config.properties")

    private val outputPath: String
            by option(
                "-op",
                "--outputPath",
                help = "Default to './output'. Specify the path to output the images."
            ).default(value = "./output")

    private val tagsParam: Array<String>?
            by option(
                "-tg",
                "--tags",
                help = "A comma-delimited list of tags. Photos with one or more of the tags listed will be returned. You can exclude results that match a term by prepending it with a - character."
            ).convert("") {
                it.split(",").toTypedArray()
            }

    private val tagModeParam: String
            by option(
                "-tm",
                "--tagMode",
                help = "Defaults to 'any'. The possible values are: 'any' and 'all'"
            ).default("any")

    private val textParam: String?
            by option(
                "-tx",
                "--text",
                help = "A free text search. Photos who's title, description or tags contain the text will be returned. You can exclude results that match a term by prepending it with a - character."
            )

    private val sortParam: Int
            by option(
                "-st",
                "--sort",
                help = "Defaults to 6 (relevance). The possible values are: 0 (date-posted-desc), 1 (date-posted-asc), 2 (date-taken-desc) 3 (date-taken-asc), 4 (interestingness-desc), 5 (interestingness-asc), and 6 (relevance)"
            ).int().default(SearchParameters.RELEVANCE)

    private val mediaParam: String
            by option(
                "-md",
                "--media",
                help = "Defaults to 'all'. The possible values are 'all', 'photos' and 'videos'"
            ).default("all")

    private val extrasParam: Set<String>?
            by option(
                "-ex",
                "--extras",
                help = "The possible values are: description, license, date_upload, date_taken, owner_name, icon_server, original_format, last_update, geo, tags, machine_tags, o_dims, views, media, path_alias, url_sq, url_t, url_s, url_q, url_m, url_n, url_z, url_c, url_l, url_o"
            ).convert("") {
                it.split(",").toSet()
            }

    private val searchParameters: SearchParameters by lazy {
        SearchParameters().apply {
            tags = tagsParam
            tagMode = tagModeParam
            extras = extrasParam
            text = textParam
            sort = sortParam
            media = mediaParam
        }
    }

    private val perPageParam: Int
            by option(
                "-pp",
                "--perPage",
                help = "Defaults to 100. The maximum allowed value is 500."
            ).int().default(100)

    private val pageParam: Int
            by option(
                "-pg",
                "--page",
                help = "Defaults to 1. The page of results to return."
            ).int().default(1)


    override fun run() {
        val flickr = setupFlickrClient()
        val photos: PhotoList<Photo> = flickr.photosInterface.search(searchParameters, perPageParam, pageParam)
        photos.map {
            storePhoto(outputPath, it)
        }
    }

    private fun setupFlickrClient(): Flickr {
        Flickr.debugRequest = true
        Flickr.debugStream = true
        val configProperties = importProperties(configPath)
        val apiKey = configProperties.getProperty("API_KEY")
        val secret = configProperties.getProperty("SECRET")

        return Flickr(apiKey, secret, REST())
    }

    private fun importProperties(configPath: String): Properties {
        val content = FileUtils.openInputStream(File(configPath))
        return Properties().apply {
            load(content)
        }
    }

    private fun storePhoto(outputPath: String, photo: Photo) {
        // extraによるurl指定が無かった場合
        if (photo.sizes.filterNotNull().isEmpty()) {
            val outputFilePath =
                outputPath + "/" + "square" + "/" + photo.id + ".jpg"
            storeFile(photo.squareLargeUrl, outputFilePath)
            return
        }

        photo.sizes.filterNotNull().map {
            val outputFilePath =
                outputPath + "/" + it.subDirectoryName + "/" + photo.id + "_h" + it.height + "_w" + it.width + ".jpg"
            storeFile(it.source, outputFilePath)
        }
    }

    private fun storeFile(url: String, outputFilePath: String) {
        try {
            FileUtils.copyURLToFile(
                URL(url),
                File(outputFilePath)
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}

fun main(args: Array<String>) = ImageAutoCollecting().main(args)
