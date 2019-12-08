package com.nott

import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.REST
import com.flickr4java.flickr.photos.Photo
import com.flickr4java.flickr.photos.PhotoList
import com.flickr4java.flickr.photos.SearchParameters
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*

class ImageAutoCollecting : CliktCommand() {

    private lateinit var configProperties: Properties

    private val configPath: String
            by option(
                "-cp",
                "--configPath",
                help = "Specify the path to config.properties. Default configPath is ./config.properties."
            ).default(value = "./config.properties")

    private val outputPath: String
            by option(
                "-op",
                "--outputPath",
                help = "Specify the path to output the images. Default outputPath is ./output/."
            ).default(value = "./output/")

    private val searchQuery: String by argument(name = "<SearchQuery>", help = "ex. 'sea', or 'cat baby'")

    override fun run() {
        configProperties = importProperties(configPath)
        val photos = collectPhotosWithQuery(searchQuery)
        photos.map {
            storePhoto(outputPath, it)
        }
    }

    private fun importProperties(configPath: String): Properties {
        val content = FileUtils.openInputStream(File(configPath))
        return Properties().apply {
            load(content)
        }
    }

    private fun collectPhotosWithQuery(query: String): PhotoList<Photo> {
        val apiKey = configProperties.getProperty("API_KEY")
        val secret = configProperties.getProperty("SECRET")
        val flickr = Flickr(apiKey, secret, REST())

        val sp = SearchParameters().apply {
            media = "photos"
            extras = setOf("url_sq", "tags")
            text = query
            tagMode = "all"
            sort = SearchParameters.RELEVANCE
        }
        return flickr.photosInterface.search(sp, 100, 1)
    }

    private fun storePhoto(outputPath: String, photo: Photo) {
        try {
            FileUtils.copyURLToFile(
                URL(photo.squareLargeUrl),
                File(outputPath + photo.id + ".jpg")
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}

fun main(args: Array<String>) = ImageAutoCollecting().main(args)
